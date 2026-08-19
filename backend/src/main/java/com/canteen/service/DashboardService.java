package com.canteen.service;

import com.canteen.dto.DashboardStatsResponse;
import com.canteen.entity.Order;
import com.canteen.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public DashboardService(
        OrderRepository orderRepository,
        UserRepository userRepository,
        MenuItemRepository menuItemRepository,
        InventoryRepository inventoryRepository,
        OrderItemRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        stats.setTotalOrders(orderRepository.count());
        stats.setTodayOrders(orderRepository.countTodayOrders(startOfDay));
        stats.setTotalRevenue(orderRepository.calculateTotalRevenue());
        stats.setTodayRevenue(orderRepository.calculateTodayRevenue(startOfDay));
        stats.setTotalCustomers(userRepository.countByEnabledTrue());
        stats.setAvailableMenuItems(menuItemRepository.countByAvailableTrue());
        stats.setPendingOrders(orderRepository.countByStatus(Order.OrderStatus.PLACED) +
                               orderRepository.countByStatus(Order.OrderStatus.CONFIRMED) +
                               orderRepository.countByStatus(Order.OrderStatus.PREPARING));
        stats.setLowStockCount(inventoryRepository.findLowStockItems().size());

        // Orders by Status
        Map<String, Long> statusMap = new HashMap<>();
        List<Object[]> statusCounts = orderRepository.countOrdersByStatus();
        for (Object[] row : statusCounts) {
            statusMap.put(row[0].toString(), (Long) row[1]);
        }
        stats.setOrdersByStatus(statusMap);

        // Top 5 selling items
        List<Object[]> topItems = orderItemRepository.findTopSellingItems(PageRequest.of(0, 5));
        List<Map<String, Object>> topItemList = topItems.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", row[0]);
            map.put("quantitySold", row[1]);
            map.put("revenue", row[2]);
            return map;
        }).collect(Collectors.toList());
        stats.setTopSellingItems(topItemList);

        return stats;
    }
}

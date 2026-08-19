package com.canteen.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardStatsResponse {
    private long totalOrders;
    private long todayOrders;
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private long totalCustomers;
    private long availableMenuItems;
    private long pendingOrders;
    private long lowStockCount;
    private Map<String, Long> ordersByStatus;
    private List<Map<String, Object>> topSellingItems;

    public DashboardStatsResponse() {}

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public long getTodayOrders() { return todayOrders; }
    public void setTodayOrders(long todayOrders) { this.todayOrders = todayOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

    public long getAvailableMenuItems() { return availableMenuItems; }
    public void setAvailableMenuItems(long availableMenuItems) { this.availableMenuItems = availableMenuItems; }

    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public Map<String, Long> getOrdersByStatus() { return ordersByStatus; }
    public void setOrdersByStatus(Map<String, Long> ordersByStatus) { this.ordersByStatus = ordersByStatus; }

    public List<Map<String, Object>> getTopSellingItems() { return topSellingItems; }
    public void setTopSellingItems(List<Map<String, Object>> topSellingItems) { this.topSellingItems = topSellingItems; }
}

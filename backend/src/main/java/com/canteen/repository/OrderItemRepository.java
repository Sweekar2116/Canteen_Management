package com.canteen.repository;

import com.canteen.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi.itemName, SUM(oi.quantity), SUM(oi.totalPrice) " +
           "FROM OrderItem oi JOIN oi.order o " +
           "WHERE o.status = 'COMPLETED' " +
           "GROUP BY oi.itemName " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingItems(Pageable pageable);
}

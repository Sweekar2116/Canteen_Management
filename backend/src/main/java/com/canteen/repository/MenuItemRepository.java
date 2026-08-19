package com.canteen.repository;

import com.canteen.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailableTrue();
    List<MenuItem> findByCategoryIdAndAvailableTrue(Long categoryId);

    @Query("SELECT m FROM MenuItem m WHERE " +
           "(:query IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:categoryId IS NULL OR m.category.id = :categoryId) AND " +
           "(:vegetarian IS NULL OR m.vegetarian = :vegetarian) AND " +
           "(:minPrice IS NULL OR m.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR m.price <= :maxPrice) AND " +
           "(:availableOnly = false OR m.available = true)")
    Page<MenuItem> searchMenuItems(
        @Param("query") String query,
        @Param("categoryId") Long categoryId,
        @Param("vegetarian") Boolean vegetarian,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("availableOnly") boolean availableOnly,
        Pageable pageable
    );

    long countByAvailableTrue();
}

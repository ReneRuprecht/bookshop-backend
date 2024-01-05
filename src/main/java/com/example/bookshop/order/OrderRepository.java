package com.example.bookshop.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders o where o.user_id=?1", nativeQuery = true)
    Optional<List<Order>> findAllByUserId(Long userId);
}

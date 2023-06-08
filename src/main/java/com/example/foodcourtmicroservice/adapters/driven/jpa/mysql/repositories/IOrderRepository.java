package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderEntity o WHERE o.idClient = :id")
    boolean findByIdClient(@Param("id") Long id);

    @Query("SELECT o FROM OrderEntity o WHERE o.idRestaurant = :idRestaurant AND o.orderStatusEntity = :orderStatus")
    Page<OrderEntity> findByRestaurantIdAndOrderStatus(Long idRestaurant, OrderStatusEntity orderStatus, Pageable pageable);
}

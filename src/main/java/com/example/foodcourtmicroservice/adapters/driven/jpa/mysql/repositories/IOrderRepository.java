package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.EmailUsersEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderEntity o WHERE o.idClient = :id")
    boolean findByIdClient(@Param("id") Long id);
    @Query("SELECT o FROM OrderEntity o WHERE o.idRestaurant = :idRestaurant AND o.orderStatusEntity = :orderStatus")
    Page<OrderEntity> findByRestaurantIdAndOrderStatus(Long idRestaurant, OrderStatusEntity orderStatus, Pageable pageable);
    @Query(value = "SELECT * FROM orders o WHERE o.id = :id AND o.id_restaurant = :idRestaurant AND o.order_status_entity = :orderStatus", nativeQuery = true)
    OrderEntity findByIdAndIdRestaurantAndOrderStatus(@Param("id") Long id, @Param("idRestaurant")Long idRestaurant, @Param("orderStatus") Integer orderStatusEntity);
    @Query(value = "SELECT * FROM orders WHERE id = :id AND order_status_entity = 1", nativeQuery = true)
    OrderEntity findByIdAndStatusOrder(@Param("id") Long id);

    @Query(value = "SELECT * FROM orders WHERE id = :id AND order_status_entity = 0 AND id_client = :idClient", nativeQuery = true)
    OrderEntity findByIdAndStatusOrderAndIdClient(@Param("id") Long id, @Param("idClient") Long idClient);
    @Query(value = "SELECT * FROM orders WHERE id = :id AND order_status_entity = 2 AND code_verification = :codeVerification", nativeQuery = true)
    OrderEntity findByIdAndStatusOrderAndCodeVerification(@Param("id") Long id, @Param("codeVerification")  Long codeOrderVerification);

//    @Query(value = "SELECT" +
//            "    (SELECT u.mail FROM pragma_users.user u WHERE u.id = o.id_client) AS emailClient," +
//            "    (SELECT u.mail FROM pragma_users.user u WHERE u.id = o.id_employee) AS emailEmployee" +
//            "FROM orders o" +
//            "WHERE o.id = :id;", nativeQuery = true)
//    EmailUsersEntity getEmailUser(@Param("id") Long id);

    @Query(value = "SELECT " +
            "(SELECT u.mail FROM pragma_users.user u WHERE u.id = o.id_client) AS emailClient, " +
            "(SELECT u.mail FROM pragma_users.user u WHERE u.id = o.id_employee) AS emailEmployee " +
            "FROM orders o " +
            "WHERE o.id = :id", nativeQuery = true)
    List<Object[]> getEmailUser(@Param("id") Long id);

}

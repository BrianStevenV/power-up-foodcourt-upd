package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idClient;

    private LocalDate date;

    private OrderStatusEntity orderStatusEntity = OrderStatusEntity.PENDING;

    private Long idEmployee;

    @JoinColumn(name = "id_restaurant")
    private Long idRestaurant;
}

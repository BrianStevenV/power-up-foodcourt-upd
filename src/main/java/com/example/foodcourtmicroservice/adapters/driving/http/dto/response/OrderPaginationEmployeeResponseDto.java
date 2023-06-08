package com.example.foodcourtmicroservice.adapters.driving.http.dto.response;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class OrderPaginationEmployeeResponseDto {
    private Long id;

    private Long idClient;

    private LocalDate date;

    private OrderStatusEntity orderStatusEntity = OrderStatusEntity.PENDING;

    private Long idEmployee;

    private Long idRestaurant;
}

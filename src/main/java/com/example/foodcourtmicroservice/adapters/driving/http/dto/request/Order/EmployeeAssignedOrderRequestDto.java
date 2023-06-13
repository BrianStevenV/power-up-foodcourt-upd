package com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeAssignedOrderRequestDto {
    private Long idRestaurant;
    List<Long> idOrder;
}

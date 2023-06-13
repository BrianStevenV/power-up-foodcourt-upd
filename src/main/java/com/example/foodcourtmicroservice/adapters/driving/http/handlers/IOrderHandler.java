package com.example.foodcourtmicroservice.adapters.driving.http.handlers;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.EmployeeAssignedOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    void createOrder(OrderRequestDto order);
    Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long idRestaurant, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage);
    void employeeAssignedOrder(EmployeeAssignedOrderRequestDto employeeAssignedOrderRequestDto);

    String markOrderReady(Long id);

    void markOrderDelivered(Long id, Long codeOrderVerification);

    void cancelToOrder(Long id);
}

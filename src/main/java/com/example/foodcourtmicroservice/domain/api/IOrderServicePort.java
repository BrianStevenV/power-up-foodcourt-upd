package com.example.foodcourtmicroservice.domain.api;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderServicePort {
    void createOrder(String nameRestaurant, List<PlateOrder> plateOrderList);
    Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long idRestaurant, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage);
}

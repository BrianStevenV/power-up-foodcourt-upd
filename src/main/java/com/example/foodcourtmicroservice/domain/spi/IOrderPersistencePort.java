package com.example.foodcourtmicroservice.domain.spi;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderPersistencePort {
    void createOrder(Order order, List<PlateOrder> plateOrderList);
    boolean clientHasOrder(Long id);

    Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long id, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage);
}

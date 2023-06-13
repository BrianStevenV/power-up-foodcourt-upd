package com.example.foodcourtmicroservice.adapters.driving.http.handlers.imp;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.EmployeeAssignedOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.example.foodcourtmicroservice.adapters.driving.http.mappers.IPlateOrderRequestMapper;
import com.example.foodcourtmicroservice.domain.api.IMessengerTwilioServicePort;
import com.example.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHandlerImp implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IPlateOrderRequestMapper plateOrderMapper;
    private final IMessengerTwilioServicePort messengerTwilioServicePort;
    @Override
    public void createOrder(OrderRequestDto orderRequestDto) {
        List<PlateOrder> plateOrderList = new ArrayList<>();
        orderRequestDto.getPlateOrderRequestDtoList().forEach(plate -> plateOrderList.add(plateOrderMapper.toPlateOrder(plate)));
        orderServicePort.createOrder(orderRequestDto.getNameRestaurant(), plateOrderList);
    }

    @Override
    public Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long idRestaurant, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage) {
        return orderServicePort.getPaginationOrderForEmployee(idRestaurant, orderStatusRequestDto, sizePage);
    }

    @Override
    public void employeeAssignedOrder(EmployeeAssignedOrderRequestDto employeeAssignedOrderRequestDto) {
        orderServicePort.employeeAssignedOrder(employeeAssignedOrderRequestDto);
    }

    @Override
    public String markOrderReady(Long id) {
        return messengerTwilioServicePort.markOrderReady(id);
    }

    @Override
    public void markOrderDelivered(Long id, Long codeOrderVerification) {
        orderServicePort.markOrderDelivered(id, codeOrderVerification);
    }

    @Override
    public void cancelToOrder(Long id) {
        orderServicePort.cancelToOrder(id);
    }
}

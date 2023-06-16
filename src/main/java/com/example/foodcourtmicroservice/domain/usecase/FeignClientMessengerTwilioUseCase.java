package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.IMessengerFeignClient;
import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.ITraceabilityFeignClient;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Logs.LogsOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.api.IMessengerTwilioServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.OrderToReadyNotAvailableException;
import com.example.foodcourtmicroservice.domain.model.Order.EmailUser;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;
import com.example.foodcourtmicroservice.domain.model.Traceability.Traceability;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;

import java.util.List;

public class FeignClientMessengerTwilioUseCase implements IMessengerTwilioServicePort {
    private final IMessengerFeignClient messengerFeignClient;
    private final IOrderPersistencePort orderPersistencePort;
    private final ITraceabilityFeignClient traceabilityFeignClient;
    public FeignClientMessengerTwilioUseCase(IMessengerFeignClient messengerFeignClient, IOrderPersistencePort orderPersistencePort, ITraceabilityFeignClient traceabilityFeignClient){
        this.messengerFeignClient = messengerFeignClient;
        this.orderPersistencePort = orderPersistencePort;
        this.traceabilityFeignClient = traceabilityFeignClient;
    }

    @Override
    public String markOrderReady(Long id) {
        Order order = orderPersistencePort.validateIdAndStatusOrder(id);

        if(order != null){

            String notification = messengerFeignClient.sendNotification();
            String numericString = notification.replaceAll("[^\\d]", "");
            long codeVerification = Long.parseLong(numericString);

            order.setOrderStatusEntity(OrderStatus.READY);
            order.setCodeVerification(codeVerification);

            LogsOrderRequestDto logsOrderRequestDto = createLogsOrderRequestDto(order,OrderStatusRequestDto.IN_PREPARATION, OrderStatusRequestDto.READY);

            traceabilityFeignClient.createLogs(logsOrderRequestDto);
            orderPersistencePort.saveOrder(order);

            return notification;
        }   else{
            throw new OrderToReadyNotAvailableException();
        }

    }

    private LogsOrderRequestDto createLogsOrderRequestDto(Order order, OrderStatusRequestDto statusBefore, OrderStatusRequestDto statusNew) {
        EmailUser emailPair = orderPersistencePort.getUserEmail(order.getId());

        String emailClient = emailPair != null ? emailPair.getEmailClient() : "";
        String emailEmployee = emailPair != null ? emailPair.getEmailEmployee() : "";

        return new LogsOrderRequestDto(
                order.getId(),
                order.getIdClient(),
                emailClient,
                statusBefore,
                statusNew,
                order.getIdEmployee(),
                emailEmployee
        );
    }

}

package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.IMessengerFeignClient;
import com.example.foodcourtmicroservice.domain.api.IMessengerTwilioServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.OrderToReadyNotAvailableException;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;

public class FeignClientMessengerTwilioUseCase implements IMessengerTwilioServicePort {
    private final IMessengerFeignClient messengerFeignClient;
    private final IOrderPersistencePort orderPersistencePort;
    public FeignClientMessengerTwilioUseCase(IMessengerFeignClient messengerFeignClient, IOrderPersistencePort orderPersistencePort){
        this.messengerFeignClient = messengerFeignClient;
        this.orderPersistencePort = orderPersistencePort;
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
            orderPersistencePort.saveOrder(order);

            return notification;
        }   else{
            throw new OrderToReadyNotAvailableException();
        }

    }
}

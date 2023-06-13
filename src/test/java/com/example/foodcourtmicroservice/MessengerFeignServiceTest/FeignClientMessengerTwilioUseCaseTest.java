package com.example.foodcourtmicroservice.MessengerFeignServiceTest;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.IMessengerFeignClient;
import com.example.foodcourtmicroservice.domain.exceptions.OrderToReadyNotAvailableException;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.example.foodcourtmicroservice.domain.usecase.FeignClientMessengerTwilioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringBootTest
public class FeignClientMessengerTwilioUseCaseTest {
    @Mock
    private IMessengerFeignClient messengerFeignClient;
    @Mock
    private IOrderPersistencePort orderPersistencePort;
    private FeignClientMessengerTwilioUseCase messengerTwilioUseCase;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        messengerTwilioUseCase = new FeignClientMessengerTwilioUseCase(messengerFeignClient, orderPersistencePort);
    }

    @Test
    @DisplayName("Test: sendNotification() - Success")
    public void sendNotificationSuccessfulTest() {
        // Arrange

        when(messengerFeignClient.sendNotification()).thenReturn("Notification 123");

        // Act

        String result = messengerFeignClient.sendNotification();

        // Assert

        assertEquals("Notification 123", result);
    }

    @Test
    @DisplayName("Test: validateIdAndStatusOrder() - Success")
    public void validateIdAndStatusOrderSucessfulTest() {
        // Arrange

        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);

        when(orderPersistencePort.validateIdAndStatusOrder(orderId)).thenReturn(expectedOrder);

        // Act

        Order result = orderPersistencePort.validateIdAndStatusOrder(orderId);

        // Assert

        assertEquals(expectedOrder, result);
    }

    @Test
    @DisplayName("Test: saveOrder() - Success")
    public void testSaveOrder() {
        // Arrange

        Order order = new Order();
        OrderEntity orderEntity = new OrderEntity();

        when(orderPersistencePort.saveOrder(order)).thenReturn(orderEntity);

        // Act

        OrderEntity result = orderPersistencePort.saveOrder(order);

        // Assert

        assertEquals(orderEntity, result);
    }

    @Test
    @DisplayName("Test: markOderReady - Success")
    public void markOrderReadySuccessfulTest() {
        // Arrange

        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderPersistencePort.validateIdAndStatusOrder(orderId)).thenReturn(order);
        when(messengerFeignClient.sendNotification()).thenReturn("Notification 123");

        // Act

        String result = messengerTwilioUseCase.markOrderReady(orderId);

        // Assert

        assertEquals("Notification 123", result);
        assertEquals(OrderStatus.READY, order.getOrderStatusEntity());
        verify(orderPersistencePort, times(1)).saveOrder(order);
    }

    @Test
    @DisplayName("Test: markOrderReady - Failure (OrderToReadyNotAvailableException)")
    public void markOrderReadyFailureTest() {
        // Arrange

        Long orderId = 1L;

        when(orderPersistencePort.validateIdAndStatusOrder(orderId)).thenReturn(null);

        // Act & Assert

        assertThrows(OrderToReadyNotAvailableException.class, () -> {
            messengerTwilioUseCase.markOrderReady(orderId);
        });
        verify(orderPersistencePort, never()).saveOrder(any());
    }
}

package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign.ITraceabilityFeignClient;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Logs.LogsOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.EmployeeAssignedOrderRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.CancelToOrderException;
import com.example.foodcourtmicroservice.domain.exceptions.ClientHasOrderException;
import com.example.foodcourtmicroservice.domain.exceptions.IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException;
import com.example.foodcourtmicroservice.domain.exceptions.MarkOrderDeliveredException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateBelongOtherRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateStatusDisabledException;
import com.example.foodcourtmicroservice.domain.model.Order.EmailUser;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;
    private final ITraceabilityFeignClient traceabilityFeignClient;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort,
                        IAuthenticationUserInfoServicePort authenticationUserInfoServicePort, ITraceabilityFeignClient traceabilityFeignClient) {
        this.orderPersistencePort = orderPersistencePort;
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.authenticationUserInfoServicePort = authenticationUserInfoServicePort;
        this.traceabilityFeignClient = traceabilityFeignClient;
    }

    @Override
    public void createOrder(String nameRestaurant, List<PlateOrder> plateOrderList) {
        Order order = buildOrder(nameRestaurant);

        validateClientHasNoOrder(order.getIdClient());

        plateOrderList.forEach(plateOrder -> {
            Long plateId = plateOrder.getIdPlate();
            validatePlateBelongsToRestaurant(plateId, order.getIdRestaurant());
            validatePlateStatus(plateId);
            plateOrder.setIdOrder(order.getId());
            plateOrder.setIdPlate(plateId);
        });

        orderPersistencePort.createOrder(order, plateOrderList);
    }

    @Override
    public Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long idRestaurant, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage) {
        return orderPersistencePort.getPaginationOrderForEmployee(idRestaurant, orderStatusRequestDto, sizePage);
    }

    @Override
    public void employeeAssignedOrder(EmployeeAssignedOrderRequestDto employeeAssignedOrderRequestDto) {
        Long idEmployee = authenticationUserInfoServicePort.getIdUserFromToken();
        employeeAssignedOrderRequestDto.getIdOrder().forEach(idOrder -> {
            Order order = orderPersistencePort.validateIdAndIdRestaurantAndStatusOrder(idOrder, employeeAssignedOrderRequestDto.getIdRestaurant());

            if(order != null){

                order.setIdEmployee(idEmployee);
                order.setOrderStatusEntity(OrderStatus.IN_PREPARATION);
                orderPersistencePort.saveOrder(order);


                LogsOrderRequestDto logsOrderRequestDto = createLogsOrderRequestDto(order,OrderStatusRequestDto.PENDING, OrderStatusRequestDto.IN_PREPARATION);
                traceabilityFeignClient.createLogs(logsOrderRequestDto);

            }   else {
                throw new IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException();
            }

        });
    }

    @Override
    public void markOrderDelivered(Long id, Long codeOrderVerification) {
        Order order = orderPersistencePort.validateIdAndStatusOrderAndCodeVerification(id, codeOrderVerification);
        if(order != null){
            order.setOrderStatusEntity(OrderStatus.DELIVERED);
            orderPersistencePort.saveOrder(order);


            LogsOrderRequestDto logsOrderRequestDto = createLogsOrderRequestDto(order,OrderStatusRequestDto.READY, OrderStatusRequestDto.DELIVERED);
            traceabilityFeignClient.createLogs(logsOrderRequestDto);

        }   else{
            throw new MarkOrderDeliveredException();
        }
    }

    @Override
    public void cancelToOrder(Long id) {
        Long idClient = authenticationUserInfoServicePort.getIdUserFromToken();
        Order order =  orderPersistencePort.ValidateIdAndStatusOrderAndIdClient(id, idClient);

        if(order != null){
            order.setOrderStatusEntity(OrderStatus.CANCELED);
            orderPersistencePort.saveOrder(order);

            LogsOrderRequestDto logsOrderRequestDto = createLogsOrderRequestDto(order,OrderStatusRequestDto.PENDING, OrderStatusRequestDto.CANCELED);
            traceabilityFeignClient.createLogs(logsOrderRequestDto);

        }   else{
            throw new CancelToOrderException();
        }

    }


    private Order buildOrder(String nameRestaurant) {
        Order order = new Order();
        Long idClient = authenticationUserInfoServicePort.getIdUserFromToken();
        Long idRestaurant = restaurantPersistencePort.getByNameRestaurant(nameRestaurant);
        order.setIdClient(idClient);
        order.setDate(LocalDate.now());
        order.setIdRestaurant(idRestaurant);
        return order;
    }

    private void validateClientHasNoOrder(Long idClient) {
        if (orderPersistencePort.clientHasOrder(idClient)) {
            throw new ClientHasOrderException();
        }
    }

    private void validatePlateStatus(Long plateId) {
        if (!platePersistencePort.findByStatus(plateId)) {
            throw new PlateStatusDisabledException();
        }
    }

    private void validatePlateBelongsToRestaurant(Long plateId, Long restaurantId) {
        Plate plate = platePersistencePort.findByIdAndIdRestaurant(plateId, restaurantId);
        if (plate == null) {
            throw new PlateBelongOtherRestaurantException();
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

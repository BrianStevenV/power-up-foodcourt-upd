package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.api.IAuthenticationUserInfoServicePort;
import com.example.foodcourtmicroservice.domain.api.IOrderServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.ClientHasOrderException;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthenticationUserInfoServicePort authenticationUserInfoServicePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort,
                        IAuthenticationUserInfoServicePort authenticationUserInfoServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.authenticationUserInfoServicePort = authenticationUserInfoServicePort;
    }

    @Override
    public void createOrder(String nameRestaurant, List<PlateOrder> plateOrderList) {
        Order order = new Order();
        Long idClient = authenticationUserInfoServicePort.getIdUserFromToken();
        if(Boolean.TRUE.equals(orderPersistencePort.clientHasOrder(idClient))){
            throw new ClientHasOrderException();
        }
        Optional<Long> idOptional = platePersistencePort.findById(plateOrderList.get(0).getIdPlate());
        Long id = idOptional.orElse(null);
        order.setIdClient(idClient);
        order.setDate(LocalDate.now());
        order.setIdRestaurant(restaurantPersistencePort.getByNameRestaurant(nameRestaurant));
        plateOrderList.forEach(plate -> plate.setIdOrder(order.getId()));
        plateOrderList.forEach(plate -> plate.setIdPlate(id));
        orderPersistencePort.createOrder(order, plateOrderList);
    }

    @Override
    public Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long idRestaurant, OrderStatusRequestDto orderStatusRequestDto, Integer sizePage) {
        return orderPersistencePort.getPaginationOrderForEmployee(idRestaurant, orderStatusRequestDto, sizePage);
    }

}

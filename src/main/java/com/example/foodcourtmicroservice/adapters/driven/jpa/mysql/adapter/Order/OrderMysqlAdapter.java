package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.Order;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.EmailUsersEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderPlateEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderPlateIdEmbeddeable;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IEmailUsersEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderPlateEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderStatusEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderPlateRepository;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.domain.model.Order.EmailUser;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import com.example.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderMysqlAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IOrderPlateRepository orderPlateRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderPlateEntityMapper orderPlateEntityMapper;
    private final IOrderStatusEntityMapper orderStatusEntityMapper;
    private final IEmailUsersEntityMapper emailUsersEntityMapper;

    @Override
    public void createOrder(Order order, List<PlateOrder> plateOrderList) {
        OrderEntity orderEntity = orderEntityMapper.toOrderEntity(order);
        orderRepository.save(orderEntity);

        Long idOrder = orderEntity.getId();

        plateOrderList.forEach(plateOrderToEntity -> {
            OrderPlateEntity orderPlateEntity = orderPlateEntityMapper.toOrderPlateEntity(plateOrderToEntity);
            OrderPlateIdEmbeddeable orderPlateIdEmbeddeable = new OrderPlateIdEmbeddeable();
            orderPlateIdEmbeddeable.setOrderId(idOrder);
            orderPlateIdEmbeddeable.setPlateId(plateOrderToEntity.getIdPlate());
            orderPlateEntity.setOrderPlateIdEmbeddeable(orderPlateIdEmbeddeable);
            orderPlateRepository.save(orderPlateEntity);
        });

    }
    @Override
    public boolean clientHasOrder(Long id) {
        return orderRepository.findByIdClient(id);
    }

    @Override
    public Page<OrderPaginationEmployeeResponseDto> getPaginationOrderForEmployee(Long id, OrderStatusRequestDto orderStatus, Integer sizePage) {
        OrderStatusEntity statusEntity = OrderStatusEntity.valueOf(orderStatus.name());

        Pageable pageable = PageRequest.of(0, sizePage);
        Page<OrderEntity> orderEntityPage = orderRepository.findByRestaurantIdAndOrderStatus(id, statusEntity, pageable);

        return orderEntityPage.map(orderEntityMapper::toOrderPaginationEmployeeResponseDto);
    }

    @Override
    public Order validateIdAndIdRestaurantAndStatusOrder(Long id, Long idRestaurant) {
        OrderEntity orderEntity = orderRepository.findByIdAndIdRestaurantAndOrderStatus(id,idRestaurant);
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public Order validateIdAndStatusOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findByIdAndStatusOrder(id);
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public Order validateIdAndStatusOrderAndCodeVerification(Long id, Long codeOrderVerification) {
        OrderEntity orderEntity = orderRepository.findByIdAndStatusOrderAndCodeVerification(id, codeOrderVerification);
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public Order ValidateIdAndStatusOrderAndIdClient(Long id, Long idClient) {
        OrderEntity orderEntity = orderRepository.findByIdAndStatusOrderAndIdClient(id, idClient);
        return orderEntityMapper.toOrder(orderEntity);
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(orderEntityMapper.toOrderEntity(order));
    }

//    @Override
//    public EmailUser getUserEmail(Long id) {
//        EmailUsersEntity emailUsersEntity = orderRepository.getEmailUser(id);
//        return emailUsersEntityMapper.toEmailUser(emailUsersEntity);
//    }

    @Override
    public EmailUser getUserEmail(Long id) {
        List<Object[]> result = orderRepository.getEmailUser(id);

        if (!result.isEmpty()) {
            Object[] row = result.get(0);
            String emailClient = row[0] != null ? row[0].toString() : "";
            String emailEmployee = row[1] != null ? row[1].toString() : "";

            return new EmailUser(emailClient, emailEmployee);
        }

        return null; // o devuelve un valor predeterminado si no hay resultados
    }




}

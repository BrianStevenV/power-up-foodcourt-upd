package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter.Order;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderPlateEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderPlateEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order.IOrderStatusEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderPlateRepository;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
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
    @Override
    public void createOrder(Order order, List<PlateOrder> plateOrderList) {
        OrderEntity orderEntity = orderEntityMapper.toOrderEntity(order);
        orderRepository.save(orderEntity);
        List<OrderPlateEntity> orderPlateEntityList = new ArrayList<>();
        plateOrderList.forEach(plateOrderToEntity -> orderPlateEntityList.add(orderPlateEntityMapper.toOrderPlateEntity(plateOrderToEntity)));
        orderPlateEntityList.forEach(plateOrderEntity -> plateOrderEntity.setIdOrder(orderRepository.findById(orderEntity.getId()).get().getId()));
        orderPlateRepository.saveAll(orderPlateEntityList);
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


}

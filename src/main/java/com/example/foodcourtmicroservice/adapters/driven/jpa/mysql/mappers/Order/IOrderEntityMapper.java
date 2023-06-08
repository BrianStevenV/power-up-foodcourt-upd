package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.OrderPaginationEmployeeResponseDto;
import com.example.foodcourtmicroservice.domain.model.Order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    OrderEntity toOrderEntity(Order order);

    OrderPaginationEmployeeResponseDto toOrderPaginationEmployeeResponseDto(OrderEntity orderEntity);
}

package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderStatusEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderStatusEntityMapper {
    OrderStatusEntity toOrderStatusEntity(OrderStatusRequestDto orderStatusRequestDto);
}

package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.Order;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.OrderPlateEntity;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderPlateEntityMapper {
    OrderPlateEntity toOrderPlateEntity(PlateOrder plateOrder);
}

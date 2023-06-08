package com.example.foodcourtmicroservice.adapters.driving.http.mappers;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.PlateOrderRequestDto;
import com.example.foodcourtmicroservice.domain.model.Order.PlateOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateOrderRequestMapper {
    PlateOrder toPlateOrder(PlateOrderRequestDto plateOrderRequestDto);
}

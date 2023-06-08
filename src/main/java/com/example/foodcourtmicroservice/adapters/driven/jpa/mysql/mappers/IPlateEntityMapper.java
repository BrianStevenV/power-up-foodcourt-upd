package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.model.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateEntityMapper {
    PlateEntity toPlateEntity(Plate plate);
    Plate toPlate(PlateEntity plateEntity);
    PlatePaginationResponseDto toPlatePaginationResponseDto(PlateEntity plateEntity);
}

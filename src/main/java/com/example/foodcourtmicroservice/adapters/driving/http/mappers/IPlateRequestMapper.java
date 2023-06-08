package com.example.foodcourtmicroservice.adapters.driving.http.mappers;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateStatusUpdateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.UpdatePlateRequestDto;
import com.example.foodcourtmicroservice.domain.model.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateRequestMapper {

    Plate toPlate(PlateRequestDto plateRequestDto);
    Plate toUpdatePlate(UpdatePlateRequestDto updatePlateRequestDto);
    Plate toStatusUpdatePlate(PlateStatusUpdateRequestDto plateStatus);
}

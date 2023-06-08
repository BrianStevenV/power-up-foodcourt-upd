package com.example.foodcourtmicroservice.adapters.driving.http.handlers.imp;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.PlateStatusUpdateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.UpdatePlateRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IPlateHandler;
import com.example.foodcourtmicroservice.adapters.driving.http.mappers.IPlateRequestMapper;
import com.example.foodcourtmicroservice.domain.api.IPlateServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlateHandlerImp implements IPlateHandler {
    private final IPlateServicePort plateServicePort;
    private final IPlateRequestMapper plateRequestMapper;

    @Override
    public void savePlate(PlateRequestDto plateRequestDto, Long idRestaurant, String categoryPlate) {
        plateServicePort.savePlate(plateRequestMapper.toPlate(plateRequestDto), idRestaurant, categoryPlate);
    }

    @Override
    public void updatePlate(UpdatePlateRequestDto updatePlateRequestDto) {
        plateServicePort.updatePlate(plateRequestMapper.toUpdatePlate(updatePlateRequestDto));
    }

    @Override
    public void statusEnabledPlate(Boolean enabled, PlateStatusUpdateRequestDto plateStatus) {
        plateServicePort.statusEnabledPlate(enabled, plateRequestMapper.toStatusUpdatePlate(plateStatus));
    }

    @Override
    public Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory) {
        return plateServicePort.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory);
    }

    @Override
    public Long getByNameCategory(String nameCategory) {
        return plateServicePort.getByNameCategory(nameCategory);
    }
}

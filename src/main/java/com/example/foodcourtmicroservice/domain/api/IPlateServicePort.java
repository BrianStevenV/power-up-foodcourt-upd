package com.example.foodcourtmicroservice.domain.api;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.model.Plate;
import org.springframework.data.domain.Page;

public interface IPlateServicePort {
    void savePlate(Plate plate, Long idRestaurant, String categoryPlate);
    void updatePlate(Plate plate);
    void statusEnabledPlate(Boolean enabled, Plate plate);
    Long getByNameCategory(String nameCategory);

    Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory);
}

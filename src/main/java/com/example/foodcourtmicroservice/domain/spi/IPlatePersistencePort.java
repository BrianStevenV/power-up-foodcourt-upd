package com.example.foodcourtmicroservice.domain.spi;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.model.Plate;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPlatePersistencePort {
    void savePlate(Plate plate);
    Plate findByIdPlateEntity(Long id);
    Optional<Long> findById(Long id);

    Plate statusEnabledPlate(Plate plate);

    Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory);
    Page<PlatePaginationResponseDto> getPaginationPlatesWithoutCategory(Long idRestaurant, Integer pageSize, String sortBy);

    Plate findByIdAndIdRestaurant(Long id, Long idRestaurant);

    boolean findByStatus(Long idPlate);
}

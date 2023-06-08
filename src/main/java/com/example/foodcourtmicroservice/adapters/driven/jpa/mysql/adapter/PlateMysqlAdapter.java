package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IPlateEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IPlateRepository;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@RequiredArgsConstructor
public class PlateMysqlAdapter implements IPlatePersistencePort {
    private final IPlateRepository plateRepository;
    private final IPlateEntityMapper plateEntityMapper;
    @Override
    public void savePlate(Plate plate) {
        PlateEntity plateEntity = plateEntityMapper.toPlateEntity(plate);
        plateRepository.save(plateEntity);
    }

    @Override
    public Optional<PlateEntity> findByIdPlateEntity(Long id) {
        Optional<PlateEntity> plateEntity = plateRepository.findById(id);
        return plateEntity;
    }

    @Override
    public Optional<Long> findById(Long id) {
        Optional<PlateEntity> plateEntityOptional = plateRepository.findById(id);
        return plateEntityOptional.map(PlateEntity::getId);
    }


    @Override
    public Plate statusEnabledPlate(Plate plate) {
        PlateEntity plateEntity = plateRepository.findByIdRestaurantAndName(plate.getIdRestaurant(), plate.getName());
        return plateEntityMapper.toPlate(plateEntity);
    }

    @Override
    public Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(sortBy).ascending());
        Page<PlateEntity> platePage;
        platePage = plateRepository.findByRestaurantIdAndCategoryId(idRestaurant, idCategory, pageable);
        return platePage.map(plateEntityMapper::toPlatePaginationResponseDto);
    }

    @Override
    public Page<PlatePaginationResponseDto> getPaginationPlatesWithoutCategory(Long idRestaurant, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(sortBy).ascending());
        Page<PlateEntity> platePage;
        platePage = plateRepository.findByRestaurantId(idRestaurant, pageable);
        return platePage.map(plateEntityMapper::toPlatePaginationResponseDto);
    }


}

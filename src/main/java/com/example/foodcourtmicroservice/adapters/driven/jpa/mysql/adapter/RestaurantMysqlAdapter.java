package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.RestaurantEntityNotFoundException;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantMysqlAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;


    @Override
    public Long getByNameRestaurant(String nameRestaurant) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findByName(nameRestaurant);

        return restaurantEntity.map(RestaurantEntity::getId).orElseThrow(() -> new RestaurantEntityNotFoundException());
    }

    @Override
    public Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer sizePage, String filter) {
        Pageable pageable = PageRequest.of(0, sizePage, Sort.by(filter).ascending());
        return restaurantRepository.findAll(pageable).map(restaurantEntityMapper::toRestaurantPaginationResponseDto);
    }
}

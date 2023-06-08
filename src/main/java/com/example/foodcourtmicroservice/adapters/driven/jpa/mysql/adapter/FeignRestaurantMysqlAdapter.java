package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.DataDuplicateViolationException;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantExternalPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class FeignRestaurantMysqlAdapter implements IRestaurantExternalPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;


    @Override
    public void saveRestaurantPersistenceFeign(Restaurant restaurantRequestDto) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurantRequestDto);
        try{
            restaurantRepository.save(restaurantEntity);
        }   catch (DataIntegrityViolationException e){
            throw new DataDuplicateViolationException();
        }

    }

}

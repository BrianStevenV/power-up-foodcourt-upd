package com.example.foodcourtmicroservice.domain.spi;

import com.example.foodcourtmicroservice.domain.model.Restaurant;

public interface IRestaurantExternalPersistencePort {
    void saveRestaurantPersistenceFeign(Restaurant restaurantRequestDto);
}

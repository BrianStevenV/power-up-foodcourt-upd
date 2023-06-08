package com.example.foodcourtmicroservice.domain.api;


import com.example.foodcourtmicroservice.domain.model.Restaurant;

public interface IRestaurantExternalServicePort {
    void saveRestaurantServiceFeign(Restaurant restaurantRequestDto);
}

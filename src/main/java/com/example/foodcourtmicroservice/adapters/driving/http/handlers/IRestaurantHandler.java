package com.example.foodcourtmicroservice.adapters.driving.http.handlers;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    void saveRestaurantFeign(RestaurantRequestDto restaurantRequestDto);

    Long getByNameRestaurant(String nameRestaurant);

    Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer pageSize, String filter);
}

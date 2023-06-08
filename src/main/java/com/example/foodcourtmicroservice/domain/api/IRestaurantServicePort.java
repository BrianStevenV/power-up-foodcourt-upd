package com.example.foodcourtmicroservice.domain.api;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    Long getByNameRestaurant(String nameRestaurant);

    Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer pageSize, String filter);
}

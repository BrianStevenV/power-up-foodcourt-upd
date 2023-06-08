package com.example.foodcourtmicroservice.adapters.driving.http.handlers.imp;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import com.example.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;

import com.example.foodcourtmicroservice.adapters.driving.http.mappers.IRestaurantRequestMapper;
import com.example.foodcourtmicroservice.domain.api.IRestaurantExternalServicePort;

import com.example.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImp implements IRestaurantHandler {
    private final IRestaurantExternalServicePort restaurantExternalServicePort;

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    @Override
    public void saveRestaurantFeign(RestaurantRequestDto restaurantRequestDto) {
        restaurantExternalServicePort.saveRestaurantServiceFeign(restaurantRequestMapper.toRestaurant(restaurantRequestDto));
    }

    @Override
    public Long getByNameRestaurant(String nameRestaurant) {
        return restaurantServicePort.getByNameRestaurant(nameRestaurant);
    }

    @Override
    public Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer pageSize, String filter) {
        return restaurantServicePort.getPaginationRestaurants(pageSize, filter);
    }
}

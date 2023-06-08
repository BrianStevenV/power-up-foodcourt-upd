package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import com.example.foodcourtmicroservice.domain.api.IRestaurantServicePort;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort){ this.restaurantPersistencePort = restaurantPersistencePort;}
    @Override
    public Long getByNameRestaurant(String nameRestaurant) {
        return restaurantPersistencePort.getByNameRestaurant(nameRestaurant);
    }

    @Override
    public Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer pageSize, String filter) {
        return restaurantPersistencePort.getPaginationRestaurants(pageSize, filter);
    }
}

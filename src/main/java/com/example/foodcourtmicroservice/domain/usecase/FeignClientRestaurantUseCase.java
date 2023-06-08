package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driving.http.controller.RestaurantFeignClient;
import com.example.foodcourtmicroservice.domain.api.IRestaurantExternalServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.NoProviderException;
import com.example.foodcourtmicroservice.domain.model.ConstantsDomain;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import com.example.foodcourtmicroservice.domain.spi.IRestaurantExternalPersistencePort;


public class FeignClientRestaurantUseCase implements IRestaurantExternalServicePort {

    private RestaurantFeignClient restaurantFeignClient;

    private IRestaurantExternalPersistencePort restaurantExternalPersistencePort;

    public FeignClientRestaurantUseCase(RestaurantFeignClient restaurantFeignClient, IRestaurantExternalPersistencePort restaurantExternalPersistencePort){
        this.restaurantFeignClient = restaurantFeignClient;
        this.restaurantExternalPersistencePort = restaurantExternalPersistencePort;
    }

    public FeignClientRestaurantUseCase(){}


    @Override
    public void saveRestaurantServiceFeign(Restaurant restaurantRequest) {
        if(restaurantFeignClient.getUserByDni(restaurantRequest.getIdOwner()).getIdRole().getDescription().equals(ConstantsDomain.PROVIDER_ROLE_DESCRIPTION)){
            restaurantRequest.setIdOwner(restaurantFeignClient.getUserByDni(restaurantRequest.getIdOwner()).getId());
            restaurantExternalPersistencePort.saveRestaurantPersistenceFeign(restaurantRequest);
        }   else {
            throw new NoProviderException();
        }
    }
}

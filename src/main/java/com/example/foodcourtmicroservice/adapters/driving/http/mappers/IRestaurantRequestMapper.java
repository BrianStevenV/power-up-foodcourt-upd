package com.example.foodcourtmicroservice.adapters.driving.http.mappers;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {
    Restaurant toRestaurant(RestaurantRequestDto restaurantRequestDto);
    RestaurantEntity toRestaurantEntity(RestaurantRequestDto restaurantRequestDto);
}


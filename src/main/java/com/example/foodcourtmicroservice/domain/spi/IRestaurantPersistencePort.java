package com.example.foodcourtmicroservice.domain.spi;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantPaginationResponseDto;
import com.example.foodcourtmicroservice.domain.model.Restaurant;
import org.springframework.data.domain.Page;

public interface IRestaurantPersistencePort {
    Long getByNameRestaurant(String nameRestaurant);

    Page<RestaurantPaginationResponseDto> getPaginationRestaurants(Integer sizePage, String filter);

    Restaurant findByIdAndIdOwner(Long id, Long idOwner);

}

package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByName(String name);
    @Query(value = "SELECT * FROM restaurant WHERE id = :id AND id_owner = :idOwner", nativeQuery = true)
    RestaurantEntity findByIdAndIdOwner(@Param("id") Long id, @Param("idOwner") Long idOwner);
}

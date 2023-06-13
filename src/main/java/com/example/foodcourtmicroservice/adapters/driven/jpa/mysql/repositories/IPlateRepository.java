package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {
    @Query(value = "SELECT * FROM plates WHERE id =:id", nativeQuery = true)
    PlateEntity findByIdPlateEntity(@Param("id") Long id);
    @Query(value = "SELECT * FROM plates where id = :id AND enabled = true", nativeQuery = true)
    PlateEntity findByEnabled(@Param("id") Long id);

    @Query(value = "SELECT * FROM plates WHERE name = :name AND id_restaurant = :idRestaurant", nativeQuery = true)
    PlateEntity findByIdRestaurantAndName(@Param("idRestaurant")Long idRestaurant, @Param("name")String name);

    @Query(value = "SELECT * FROM plates WHERE id_restaurant = :idRestaurant AND (:idCategory IS NULL OR id_category = :idCategory)",
            countQuery = "SELECT COUNT(*) FROM plates WHERE id_restaurant = :idRestaurant AND (:idCategory IS NULL OR id_category = :idCategory)",
            nativeQuery = true)
    Page<PlateEntity> findByRestaurantIdAndCategoryId(Long idRestaurant, Long idCategory, Pageable pageable);

    @Query(value = "SELECT * FROM plates WHERE id_restaurant = :idRestaurant",
            countQuery = "SELECT COUNT(*) FROM plates WHERE id_restaurant = :idRestaurant",
            nativeQuery = true)
    Page<PlateEntity> findByRestaurantId(Long idRestaurant, Pageable pageable);

    @Query(value = "SELECT * FROM plates WHERE id = :id AND id_restaurant = :idRestaurant", nativeQuery = true)
    PlateEntity findByIdAndIdRestaurant(@Param("id") Long id, @Param("idRestaurant") Long idRestaurant);
}

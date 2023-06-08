package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {
    Optional<PlateEntity> findById(Long id);

//    @Query("SELECT p FROM PlateEntity p WHERE p.name = :name AND p.idRestaurant = :idRestaurant")
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
}

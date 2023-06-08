package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.CategoryPlateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ICategoryRepository extends JpaRepository<CategoryPlateEntity, Long> {
    Optional<CategoryPlateEntity> findByName(String nameCategory);
}

package com.example.foodcourtmicroservice.domain.spi;



public interface ICategoryPersistencePort {
    Long getCategoryByName(String name);
}

package com.example.foodcourtmicroservice.adapters.driving.http.controller;


import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "restaurant-service", url = "http://localhost:8090")
public interface RestaurantFeignClient {
    @GetMapping("/user/{dniNumber}")
    UserResponseDto getUserByDni(@PathVariable("dniNumber") Long dniNumber);
}

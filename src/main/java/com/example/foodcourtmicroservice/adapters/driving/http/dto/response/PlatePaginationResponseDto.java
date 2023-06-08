package com.example.foodcourtmicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlatePaginationResponseDto {
    private String name;
    private String description;
    private Double price;
}

package com.example.foodcourtmicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class PlateStatusUpdateRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private Long idRestaurant;
}

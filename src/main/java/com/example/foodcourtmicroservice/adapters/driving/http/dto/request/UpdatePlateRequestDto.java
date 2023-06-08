package com.example.foodcourtmicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePlateRequestDto {
    @NotNull
    private Long id;
    @NotNull
    private Double price;
    @NotBlank
    private String description;
}

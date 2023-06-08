package com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlateOrderRequestDto {
    @NotNull
    @Positive
    private Long idPlate;
    @NotNull
    @Positive
    private Integer amountPlate;
}

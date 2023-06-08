package com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderRequestDto {
    @NotNull
    private String nameRestaurant;
    @UniqueElements
    @Valid
    private List<PlateOrderRequestDto> plateOrderRequestDtoList;
}

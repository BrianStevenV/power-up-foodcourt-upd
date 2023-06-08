package com.example.foodcourtmicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RestaurantRequestDto {

    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]+$")
    @NotBlank
    private String name;
    @NotBlank
    private String direction;
    @Pattern(regexp = "^[0-9+]{1,13}$")
    @NotBlank
    private String phone;
    @NotBlank
    private String urlLogotype;
    @Pattern(regexp = "^[0-9]+$")
    @NotBlank
    private String nit;
    @NotNull
    private Long idOwner;

}

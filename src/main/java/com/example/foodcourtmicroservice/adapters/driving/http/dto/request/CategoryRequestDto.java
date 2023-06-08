package com.example.foodcourtmicroservice.adapters.driving.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CategoryRequestDto {
    private Long id;
    private String name;
    private String description;
    public CategoryRequestDto(Long id){
        this.id = id;
    }
}

package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plates")
@Data
public class PlateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String urlImage;
    private Boolean enabled = true;
    @JoinColumn(name = "id_categoryPlate")
    private Long idCategory;
    @JoinColumn(name = "id_restaurant")
    private Long idRestaurant;
}

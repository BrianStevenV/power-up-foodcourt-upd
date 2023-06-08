package com.example.foodcourtmicroservice.domain.model;

public class Plate {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String urlImage;
    private Boolean enabled = true;
    private Long idCategory;
    private Long idRestaurant;

    public Plate(Long id, String name, String description, Double price, String urlImage, Boolean enabled, Long idCategory, Long idRestaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.urlImage = urlImage;
        this.enabled = enabled;
        this.idCategory = idCategory;
        this.idRestaurant = idRestaurant;
    }

    public Plate(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}

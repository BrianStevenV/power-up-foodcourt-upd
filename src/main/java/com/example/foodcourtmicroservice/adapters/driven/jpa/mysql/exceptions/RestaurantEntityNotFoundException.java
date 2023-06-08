package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions;

import java.util.NoSuchElementException;

public class RestaurantEntityNotFoundException extends NoSuchElementException {
    public RestaurantEntityNotFoundException(){ super();}
}

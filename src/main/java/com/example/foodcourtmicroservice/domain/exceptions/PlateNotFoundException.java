package com.example.foodcourtmicroservice.domain.exceptions;

import java.util.NoSuchElementException;

public class PlateNotFoundException extends NoSuchElementException {
    public PlateNotFoundException(){super();}
}

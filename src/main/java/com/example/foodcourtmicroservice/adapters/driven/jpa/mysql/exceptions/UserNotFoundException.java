package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(){ super();}
}

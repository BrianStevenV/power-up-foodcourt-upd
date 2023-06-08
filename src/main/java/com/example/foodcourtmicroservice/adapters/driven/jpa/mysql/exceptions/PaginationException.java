package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions;

import org.webjars.NotFoundException;

public class PaginationException extends NotFoundException {
    public PaginationException(String message){super(message);}
}

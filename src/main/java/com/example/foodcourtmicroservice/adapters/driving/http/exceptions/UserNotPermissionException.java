package com.example.foodcourtmicroservice.adapters.driving.http.exceptions;

import java.nio.file.AccessDeniedException;

public class UserNotPermissionException extends RuntimeException {
    public UserNotPermissionException(String message){
        super(message);
    }
}

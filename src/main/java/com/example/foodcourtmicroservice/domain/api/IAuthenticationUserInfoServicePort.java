package com.example.foodcourtmicroservice.domain.api;

public interface IAuthenticationUserInfoServicePort {
    String getIdentifierUserFromToken();
    Long getIdUserFromToken();
}

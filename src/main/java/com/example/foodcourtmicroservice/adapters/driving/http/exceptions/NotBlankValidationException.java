package com.example.foodcourtmicroservice.adapters.driving.http.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotBlank;

public class NotBlankValidationException implements ConstraintValidator<NotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The field " + context.getDefaultConstraintMessageTemplate() + " is mandatory.");
        }
        return true;
    }

}

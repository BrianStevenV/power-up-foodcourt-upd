package com.example.foodcourtmicroservice.configuration;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.CategoryNotFoundException;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.DataDuplicateViolationException;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.PaginationException;
import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.RestaurantEntityNotFoundException;
import com.example.foodcourtmicroservice.domain.exceptions.ClientHasOrderException;
import com.example.foodcourtmicroservice.domain.exceptions.DifferentRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException;
import com.example.foodcourtmicroservice.domain.exceptions.IdPlateNotFoundException;
import com.example.foodcourtmicroservice.domain.exceptions.NoProviderException;
import com.example.foodcourtmicroservice.domain.exceptions.OrderToReadyNotAvailableException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateBelongOtherRestaurantException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateNotFoundException;
import com.example.foodcourtmicroservice.domain.exceptions.PlateStatusDisabledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.foodcourtmicroservice.configuration.Constants.CATEGORY_EXCEPTION;
import static com.example.foodcourtmicroservice.configuration.Constants.CLIENT_HAS_ORDER_EXCEPTION;
import static com.example.foodcourtmicroservice.configuration.Constants.DATA_DUPLICATE_RESTAURANT_DTO;
import static com.example.foodcourtmicroservice.configuration.Constants.DIFFERENT_RESTAURANT_ERROR;
import static com.example.foodcourtmicroservice.configuration.Constants.ID_AND_ID_RESTAURANT_AND_STATUS_ORDER_NOT_FOUND;
import static com.example.foodcourtmicroservice.configuration.Constants.ID_UPDATE_NOT_FOUND;
import static com.example.foodcourtmicroservice.configuration.Constants.NO_PROVIDER_PERMISSION;
import static com.example.foodcourtmicroservice.configuration.Constants.ORDER_TO_READY_NOT_AVAILABLE_EXCEPTION;
import static com.example.foodcourtmicroservice.configuration.Constants.PAGINATION_ERROR;
import static com.example.foodcourtmicroservice.configuration.Constants.PLATE_BELONG_OTHER_RESTAURANT_ERROR;
import static com.example.foodcourtmicroservice.configuration.Constants.PLATE_NOT_FOUND;
import static com.example.foodcourtmicroservice.configuration.Constants.PLATE_STATUS_DISABLED_EXCEPTION;
import static com.example.foodcourtmicroservice.configuration.Constants.RESPONSE_ERROR_MESSAGE_KEY;
import static com.example.foodcourtmicroservice.configuration.Constants.RESTAURANT_ENTITY_NOT_FOUND;
import static com.example.foodcourtmicroservice.configuration.Constants.WRONG_CREDENTIALS_MESSAGE;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(IdPlateNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleIdPlateNotFouncException(IdPlateNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ID_UPDATE_NOT_FOUND));
    }

    @ExceptionHandler(NoProviderException.class)
    public ResponseEntity<Map<String, String>> handleNoProviderException(NoProviderException noProviderException){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NO_PROVIDER_PERMISSION));
    }

    @ExceptionHandler(DataDuplicateViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataDuplicateViolationException(DataDuplicateViolationException dataDuplicateViolationException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DATA_DUPLICATE_RESTAURANT_DTO));
    }

    @ExceptionHandler(RestaurantEntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantEntityNotFoundException(RestaurantEntityNotFoundException restaurantEntityNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_ENTITY_NOT_FOUND));
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<Map<String, String>> handlePaginationException(PaginationException paginationException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PAGINATION_ERROR));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, CATEGORY_EXCEPTION));
    }


    @ExceptionHandler(PlateNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePlateNotFoundException(PlateNotFoundException plateNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PLATE_NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, WRONG_CREDENTIALS_MESSAGE));
    }

    @ExceptionHandler(ClientHasOrderException.class)
    public ResponseEntity<Map<String, String>> handleClientHasOrderException(ClientHasOrderException clientHasOrderException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, CLIENT_HAS_ORDER_EXCEPTION));
    }

    @ExceptionHandler(IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException.class)
    public ResponseEntity<Map<String, String>> handleIdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException(IdOrderAndIdRestaurantAndOrderStatusPendingIsFalseException
                                                                                                                 idOrderAndIdRestaurantAndOrderStatusPendingIsFalseException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ID_AND_ID_RESTAURANT_AND_STATUS_ORDER_NOT_FOUND));
    }

    @ExceptionHandler(DifferentRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleDifferentRestaurantException(DifferentRestaurantException differentRestaurantException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DIFFERENT_RESTAURANT_ERROR));
    }

    @ExceptionHandler(PlateBelongOtherRestaurantException.class)
    public ResponseEntity<Map<String, String>> handlePlateBelongOtherRestaurantException(PlateBelongOtherRestaurantException plateBelongOtherRestaurantException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PLATE_BELONG_OTHER_RESTAURANT_ERROR));
    }

    @ExceptionHandler(PlateStatusDisabledException.class)
    public ResponseEntity<Map<String, String >> handlePlateStatusDisabledException(PlateStatusDisabledException plateStatusDisabledException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PLATE_STATUS_DISABLED_EXCEPTION));
    }

    @ExceptionHandler(OrderToReadyNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleOrderToReadyNotAvailableException(OrderToReadyNotAvailableException orderToReadyNotAvailableException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_TO_READY_NOT_AVAILABLE_EXCEPTION));
    }
}

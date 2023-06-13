package com.example.foodcourtmicroservice.configuration;

public class Constants {
    private Constants(){ throw new IllegalStateException("Utility class");}
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong credentials or role not allowed";
    public static final String CLIENT_HAS_ORDER_EXCEPTION = "Customer already has an order, it must first be fulfilled in order to assign another.";
    public static final String NO_PROVIDER_PERMISSION = "The Role is not allowed for the action.";
    public static final String DATA_DUPLICATE_RESTAURANT_DTO = "The name or NIT registered is already exist in the database.";
    public static final String RESTAURANT_ENTITY_NOT_FOUND = "The restaurant was not found.";
    public static final String PAGINATION_ERROR = "Error to paginate.";
    public static final String EMPLOYEE_TO_ORDER = "Employee successfully assigned.";
    public static final String PLATE_NOT_FOUND = "Plate not found.";
    public static final String PLATE_STATUS_DISABLED_EXCEPTION = "The plate is not available.";
    public static final String PLATE_BELONG_OTHER_RESTAURANT_ERROR = "Plate belong other restaurant or Plate not found, you can only order food from the same restaurant.";
    public static final String ID_AND_ID_RESTAURANT_AND_STATUS_ORDER_NOT_FOUND = "Order, Restaurant and Status order not found, Please, the order must belong to the same restaurant and pending status.";
    public static final String DIFFERENT_RESTAURANT_ERROR = "The owner belong other restaurant.";
    public static final String UPDATE_PLATE_OK = "Updated information.";
    public static final String UPDATE_STATUS_PLATE = "Status updated plate.";
    public static final String CATEGORY_EXCEPTION = "Category not found.";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String ID_UPDATE_NOT_FOUND = "Id Plate not is found.";
    public static final String MARK_ORDER_READY = "The order is ready.";
    public static final String ORDER_TO_READY_NOT_AVAILABLE_EXCEPTION = "Order to ready not available.";
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE = "Restaurant created successfully.";
    public static final String PLATE_CREATED_MESSAGE = "Plate created successfully.";
    public static final String ORDER_CREATED_MESSAGE = "Order created successfully.";
    public static final String SWAGGER_TITLE_MESSAGE = "User API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "User microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
}

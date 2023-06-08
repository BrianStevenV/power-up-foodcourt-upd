package com.example.foodcourtmicroservice.configuration;

public class Constants {
    private Constants(){ throw new IllegalStateException("Utility class");}
    public static final Long VEGAN_DISHES = 1L;
    public static final Long SEA_DISHES = 2L;
    public static final Long MEAT_DISHES = 3L;
    public static final String NAME_VEGAN_DISHES = "Vegan Category.";
    public static final String NAME_SEA_DISHES = "Sea Category.";
    public static final String NAME_MEAT_DISHES = "Meat Category";

    public static final String DESCRIPTION_VEGAN_DISHES = "Plant-based food without animal products or by-products.";
    public static final String DESCRIPTION_SEA_DISHES= "Edible marine creatures.";
    public static final String DESCRIPTION_MEAT_DISHES = "Animal flesh for consumption.";

    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong credentials or role not allowed";
    public static final String CLIENT_HAS_ORDER_EXCEPTION = "Customer already has an order, it must first be fulfilled in order to assign another.";
    public static final String NO_PROVIDER_PERMISSION = "The Role is not allowed for the action.";
    public static final String DATA_DUPLICATE_RESTAURANT_DTO = "The name or NIT registered is already exist in the database.";

    public static final String RESTAURANT_ENTITY_NOT_FOUND = "The restaurant was not found.";
    public static final String PAGINATION_ERROR = "Error to paginate.";

    public static final String PLATE_NOT_FOUND = "Plate not found.";

    public static final String UPDATE_PLATE_OK = "Updated information.";
    public static final String UPDATE_STATUS_PLATE = "Status updated plate.";
    public static final String CATEGORY_EXCEPTION = "Category not found.";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String ID_UPDATE_NOT_FOUND = "Id Plate not is found.";

    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE = "Restaurant created successfully.";
    public static final String PLATE_CREATED_MESSAGE = "Plate created successfully.";
    public static final String ORDER_CREATED_MESSAGE = "Order created successfully.";
    public static final Long PROVIDER_ROLE_ID = 3L;
    public static final String PROVIDER_DESCRPTION = "PROVIDER_ROLE";
    public static final String USER_PERMISSION_DENIED = "User has not permission appropiate.";
    public static final String SWAGGER_TITLE_MESSAGE = "User API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "User microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
}

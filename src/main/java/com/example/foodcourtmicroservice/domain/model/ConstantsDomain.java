package com.example.foodcourtmicroservice.domain.model;

public class ConstantsDomain {
    private ConstantsDomain(){ throw new IllegalStateException("Utility class"); }

    public static final String PROVIDER_ROLE_DESCRIPTION = "PROVIDER_ROLE";
    public static final Integer ORDER_STATUS_PENDING_INTEGER = 0;
    public static final Integer ORDER_STATUS_IN_PREPARATION_INTEGER = 1;
    public static final Integer ORDER_STATUS_READY_INTEGER = 2;
    public static final Integer ORDER_STATUS_DELIVERED_INTEGER = 3;
    public static final Integer ORDER_STATUS_CANCELED = 4;
}

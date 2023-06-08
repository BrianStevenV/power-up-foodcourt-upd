package com.example.foodcourtmicroservice.domain.model.Order;


import java.time.LocalDate;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDate date;
    private OrderStatus orderStatusEntity = OrderStatus.PENDING;
    private Long idEmployee;
    private Long idRestaurant;

    public Order(Long id, Long idClient, LocalDate date, OrderStatus orderStatusEntity, Long idEmployee, Long idRestaurant) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.orderStatusEntity = orderStatusEntity;
        this.idEmployee = idEmployee;
        this.idRestaurant = idRestaurant;
    }
    public Order(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public OrderStatus getOrderStatusEntity() {
        return orderStatusEntity;
    }

    public void setOrderStatusEntity(OrderStatus orderStatusEntity) {
        this.orderStatusEntity = orderStatusEntity;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}

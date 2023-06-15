package com.example.foodcourtmicroservice.domain.model.Traceability;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import com.example.foodcourtmicroservice.domain.model.Order.OrderStatus;

public class Traceability {
    private Long idOrder;
    private Long idClient;
    private String emailClient;
    private OrderStatus statusBefore;
    private OrderStatus statusNew;
    private Long idEmployee;
    private String emailEmployee;

    public Traceability(Long idOrder, Long idClient, String emailClient, OrderStatus statusBefore, OrderStatus statusNew, Long idEmployee, String emailEmployee) {
        this.idOrder = idOrder;
        this.idClient = idClient;
        this.emailClient = emailClient;
        this.statusBefore = statusBefore;
        this.statusNew = statusNew;
        this.idEmployee = idEmployee;
        this.emailEmployee = emailEmployee;
    }

    public Traceability() {
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public OrderStatus getStatusBefore() {
        return statusBefore;
    }

    public void setStatusBefore(OrderStatus statusBefore) {
        this.statusBefore = statusBefore;
    }

    public OrderStatus getStatusNew() {
        return statusNew;
    }

    public void setStatusNew(OrderStatus statusNew) {
        this.statusNew = statusNew;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getEmailEmployee() {
        return emailEmployee;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }
}

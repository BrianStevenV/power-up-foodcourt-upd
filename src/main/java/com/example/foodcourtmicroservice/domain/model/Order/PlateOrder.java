package com.example.foodcourtmicroservice.domain.model.Order;

public class PlateOrder {
    private Long idOrder;
    private Long idPlate;
    private Integer amountPlate;

    public PlateOrder(Long idOrder, Long idPlate, Integer amountPlate) {
        this.idOrder = idOrder;
        this.idPlate = idPlate;
        this.amountPlate = amountPlate;
    }
    public PlateOrder(){}

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdPlate() {
        return idPlate;
    }

    public void setIdPlate(Long idPlate) {
        this.idPlate = idPlate;
    }

    public Integer getAmountPlate() {
        return amountPlate;
    }

    public void setAmountPlate(Integer amountPlate) {
        this.amountPlate = amountPlate;
    }
}

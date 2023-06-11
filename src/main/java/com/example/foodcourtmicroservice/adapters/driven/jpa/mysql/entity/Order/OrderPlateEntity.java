package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name = "order_plates")
public class OrderPlateEntity {
    @EmbeddedId
    private OrderPlateIdEmbeddeable orderPlateIdEmbeddeable;
    @Column(name = "amountPlate")
    private Integer amountPlate;
}

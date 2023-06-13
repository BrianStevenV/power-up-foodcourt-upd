package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
@Embeddable
@Data
public class OrderPlateIdEmbeddeable implements Serializable {
    @Column(name = "id_orders")
    private Long orderId;

    @Column(name = "id_plates")
    private Long plateId;
}

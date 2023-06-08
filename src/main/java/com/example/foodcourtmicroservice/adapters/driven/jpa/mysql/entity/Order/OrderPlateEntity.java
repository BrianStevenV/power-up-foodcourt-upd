package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name = "order_plates")
public class OrderPlateEntity {
    @Id
    @JoinColumn(name = "id_orders")
    private Long idOrder;
    @JoinColumn(name = "id_plates")
    private Long idPlate;
    @Column(name = "amountPlate")
    private Integer amountPlate;
}

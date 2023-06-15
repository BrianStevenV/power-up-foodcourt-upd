package com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Logs;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Order.OrderStatusRequestDto;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LogsOrderRequestDto {
    private Long idOrder;
    private Long idClient;
    @Email
    private String emailClient;
    private OrderStatusRequestDto stateBefore;
    private OrderStatusRequestDto stateNew;
    private Long idEmployee;
    @Email
    private String emailEmployee;
}

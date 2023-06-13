package com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "twilio-service", url = "http://localhost:8094")
public interface IMessengerFeignClient {
    @GetMapping("/twilio/notification/")
    String sendNotification();
}

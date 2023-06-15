package com.example.foodcourtmicroservice.adapters.driving.http.controller.Feign;

import com.example.foodcourtmicroservice.adapters.driving.http.dto.request.Logs.LogsOrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "traceability-service", url = "http://localhost:8093")
public interface ITraceabilityFeignClient {
    @PostMapping("/traceability/logs/")
    void createLogs(LogsOrderRequestDto logsOrderRequestDto);
}

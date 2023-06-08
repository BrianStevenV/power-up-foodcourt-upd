package com.example.foodcourtmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class FoodCourtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCourtApplication.class, args);
	}

}

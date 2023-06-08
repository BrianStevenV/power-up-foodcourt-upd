package com.example.foodcourtmicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class UserResponseDto {
    private Long id;
    private String dniNumber;
    private String name;
    private String surname;
    private String mail;
    private String phone;
    private LocalDate birthdate;
    private String password;
    private RoleResponseDto idRole;

}

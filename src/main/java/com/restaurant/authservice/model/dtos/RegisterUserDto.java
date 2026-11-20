package com.restaurant.authservice.model.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String name;
    private String username;
    private String password;
}

package com.restaurant.authservice.model.dtos;

import lombok.Data;

@Data
public class LoginUserDto {
    private String username;
    private String password;
}

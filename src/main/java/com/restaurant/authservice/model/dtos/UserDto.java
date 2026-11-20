package com.restaurant.authservice.model.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto {

    private String name;
    private String username;

}

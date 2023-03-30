package com.jakut.shop.dto;

import com.jakut.shop.model.Role;
import lombok.Data;

@Data
public class UserLoginResponseDto {
    private Long id;

    private String name;

    private String username;

    private Role role;

    public String token;
}
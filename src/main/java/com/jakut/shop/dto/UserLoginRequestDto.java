package com.jakut.shop.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String username;

    private String password;
}
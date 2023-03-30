package com.jakut.shop.util;

mport com.jakut.shop.model.User;
import com.jakut.shop.model.UserLoginResponseDto;

public class AppUtils {

    public static UserLoginResponseDto toUserLoginResponse(User user) {
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();
        userLoginResponseDto.setId(user.getId());
        userLoginResponseDto.setName(user.getName());
        userLoginResponseDto.setRole(user.getRole());
        userLoginResponseDto.setUsername(user.getUsername());
        userLoginResponseDto.setToken(user.getToken());
        return userLoginResponseDto;
    }
}

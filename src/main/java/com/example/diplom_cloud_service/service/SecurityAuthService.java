package com.example.diplom_cloud_service.service;


import com.example.diplom_cloud_service.dto.UserDto;
import com.example.diplom_cloud_service.entity.User;
import org.springframework.http.HttpStatus;

public interface SecurityAuthService {
	String geJwtTokenByLoginAndPassword(UserDto userDto);
	HttpStatus deleteTokenAndLogout(String token);
	boolean validateUser(UserDto userDto, User user);
}
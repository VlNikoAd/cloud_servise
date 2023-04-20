package com.example.diplom_cloud_service.controller;


import com.example.diplom_cloud_service.dto.TokenDto;
import com.example.diplom_cloud_service.dto.UserDto;
import com.example.diplom_cloud_service.service.SecurityAuthService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class AuthUserController {
	SecurityAuthService service;

	public AuthUserController(SecurityAuthService service) {
		this.service = service;
	}

	@PostMapping("/login")
	public TokenDto login(@RequestBody @NonNull UserDto userDto) {
		String token = service.geJwtTokenByLoginAndPassword(userDto);
		return new TokenDto(token);
	}

	@PostMapping("/logout")
	public HttpStatus logout(@RequestHeader("auth-token") @NonNull String token) {
		service.deleteTokenAndLogout(token);
		return HttpStatus.OK;
	}
}

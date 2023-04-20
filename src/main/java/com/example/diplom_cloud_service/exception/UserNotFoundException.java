package com.example.diplom_cloud_service.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {

	public UserNotFoundException(String message) {
		super(message);
	}
}
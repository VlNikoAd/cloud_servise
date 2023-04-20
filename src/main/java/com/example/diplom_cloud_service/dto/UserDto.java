package com.example.diplom_cloud_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class UserDto {

	@JsonProperty("login")
	private String login;

	@JsonProperty("password")
	private String password;

	public UserDto(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Override
	public String toString() {
		return "AuthRequest{" +
		       "login='" + login + '\'' +
		       ", password='" + password + '\'' +
		       '}';
	}
}

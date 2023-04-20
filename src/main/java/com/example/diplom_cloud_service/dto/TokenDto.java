package com.example.diplom_cloud_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
	@JsonProperty("auth-token")
	private String token;

	@Override
	public String toString() {
		return "token='" + token + '\'' +
		       '}';
	}
}

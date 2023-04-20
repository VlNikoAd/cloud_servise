package com.example.diplom_cloud_service.dto;

import lombok.Data;

@Data
public class Error {

	private String message;
	private int id;

	public Error(String message, int id) {
		this.message = message;
		this.id = id;
	}
}

package com.example.diplom_cloud_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileDto {
	@JsonProperty("filename")
	String fileName;
	@JsonProperty("size")
	long size;

	public FileDto(String fileName, long size) {
		this.fileName = fileName;
		this.size = size;
	}
}

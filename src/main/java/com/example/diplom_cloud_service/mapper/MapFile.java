package com.example.diplom_cloud_service.mapper;


import com.example.diplom_cloud_service.dto.FileDto;
import com.example.diplom_cloud_service.entity.File;
import org.springframework.stereotype.Component;

@Component
public class MapFile {
	public FileDto mapFileToFileDto(File file) {
		return new FileDto(file.getFileName(), file.getSize());
	}

}

package com.example.diplom_cloud_service.controller;


import com.example.diplom_cloud_service.dto.FileDto;
import com.example.diplom_cloud_service.service.FileService;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
class FileControllerTest {
	@Mock
	FileService fileService;
	@Mock
	Logger log;
	@InjectMocks
	FileController fileController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetFileList(){
		when(fileService.showSavedFile(anyInt())).thenReturn(List.of(new FileDto("fileName", 0L)));

		List<FileDto> result = fileController.getFileList(0);
		Assertions.assertEquals(List.of(new FileDto("fileName", 0L)), result);
	}

	@Test
	void testSaveFile() throws IOException {
		when(fileService.uploadFile(anyString(), any())).thenReturn(HttpStatus.OK);

		HttpStatus result = fileController.saveFile("filename", null);
		Assertions.assertEquals(HttpStatus.OK, result);
	}

	@Test
	void testDeleteFile(){
		when(fileService.deleteFile(anyString())).thenReturn(HttpStatus.CONTINUE);

		HttpStatus result = fileController.deleteFile("fileName");
		Assertions.assertEquals(HttpStatus.CONTINUE, result);
	}

	@Test
	void testGetFile(){
		when(fileService.getFileByName(anyString())).thenReturn(new byte[]{(byte) 0});

		byte[] result = fileController.getFile("fileName");
		Assertions.assertArrayEquals(new byte[]{(byte) 0}, result);
	}

	@Test
	void testEditFile(){
		when(fileService.editFileByName("fileName","newFileName")).thenReturn(HttpStatus.OK);

		HttpStatus result = fileController.editFile("fileName", Map.of("filename","newFileName"));
		Assertions.assertEquals(HttpStatus.OK, result);
	}
}

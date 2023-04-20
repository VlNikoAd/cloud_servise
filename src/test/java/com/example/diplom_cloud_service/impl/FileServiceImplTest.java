package com.example.diplom_cloud_service.impl;


import com.example.diplom_cloud_service.dto.FileDto;
import com.example.diplom_cloud_service.entity.File;
import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.mapper.MapFile;
import com.example.diplom_cloud_service.repository.FileRepository;
import com.example.diplom_cloud_service.service.impl.FileServiceImpl;
import com.example.diplom_cloud_service.service.impl.SecurityAuthServiceImpl;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.apache.coyote.http11.Constants.a;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;

class FileServiceImplTest {
	@Mock
	FileRepository fileRepository;
	@Mock
	SecurityAuthServiceImpl securityAuthService;
	@Mock
	MapFile map;
	@InjectMocks
	FileServiceImpl fileServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private void setUpSecurityContext(String username) {
		SecurityContext securityContext = mock(SecurityContext.class);
		Authentication authentication = mock(Authentication.class);

		// Set up the behavior of the authentication object
		when(authentication.getName()).thenReturn(username);

		// Set up the behavior of the SecurityContext object
		when(securityContext.getAuthentication()).thenReturn(authentication);

		// Set the SecurityContext to return the mocked SecurityContext object
		SecurityContextHolder.setContext(securityContext);
	}



	@Test
	void testUploadFile() throws IOException {

		setUpSecurityContext("login");
		// Use given() and willReturn() to mock the behavior of the SecurityContextHolder
		when(securityAuthService.findUserByLogin(anyString()))
				.thenReturn(new User("login", "password"));

		MultipartFile file = new MockMultipartFile("filename","filename".getBytes());

		HttpStatus result = fileServiceImpl.uploadFile("filename", file);

		Assertions.assertEquals(HttpStatus.OK, result);
	}

	@Test
	void testEditFileByName() {
		setUpSecurityContext("login");

		File file = new File();
		file.setFileName("fileName");

		when(fileRepository.findFileByFileNameAndUserLogin("fileName", "login"))
				.thenReturn(Optional.of(file));
		HttpStatus result = fileServiceImpl.editFileByName("fileName", "newFileName");
		Assertions.assertEquals(HttpStatus.OK, result);

	}

	@Test
	void testGetFileByName() {

		setUpSecurityContext("login");

		File file = new File();
		file.setFileName("fileName");
		byte [] f = "Test file".getBytes();
		file.setFile(f);

		when(fileRepository.findFileByFileNameAndUserLogin("fileName", "login"))
				.thenReturn(Optional.of(file));

		byte[] result = fileServiceImpl.getFileByName("fileName");
		Assertions.assertArrayEquals(f, result);
	}

	@Test
	void testDeleteFile() {
		setUpSecurityContext("login");

		File file = new File();
		when(fileRepository.findFileByFileNameAndUserLogin("fileName", "login")).thenReturn(Optional.of(file));

		HttpStatus result = fileServiceImpl.deleteFile("fileName");
		Assertions.assertEquals(HttpStatus.OK, result);
	}

	@Test
	void testShowSavedFile() {
		setUpSecurityContext("login");
		when(fileRepository.findAllFilesByLogin(anyString()))
				.thenReturn(List.of(new File(new User("login", "password"), "fileName", new byte[]{(byte) 0}, Long.valueOf(1))));

		List<FileDto> result = fileServiceImpl.showSavedFile(1);
		Assertions.assertEquals(List.of(new FileDto("fileName", 1L)), result);
	}

	@Test
	void testFindFileByFileNameAndLogin() {
		setUpSecurityContext("login");

		File file = new File();
		file.setFileName("fileName");

		when(fileRepository.findFileByFileNameAndUserLogin("fileName", "login"))
				.thenReturn(Optional.of(file));

		File result = fileServiceImpl.findFileByFileNameAndLogin("fileName", "login");
		Assertions.assertEquals("fileName", result.getFileName());
	}
}

package com.example.diplom_cloud_service.impl;


import com.example.diplom_cloud_service.auth.JwtTokenService;
import com.example.diplom_cloud_service.dto.UserDto;
import com.example.diplom_cloud_service.entity.Token;
import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.repository.UserRepository;
import com.example.diplom_cloud_service.service.impl.SecurityAuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


class SecurityAuthServiceImplTest {
	@Mock
	JwtTokenService jwtTokenService;
	@Mock
	UserRepository userRepository;

	@InjectMocks
	SecurityAuthServiceImpl securityAuthServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGeJwtTokenByLoginAndPassword(){

		given(jwtTokenService.generateToken("login")).willReturn("TokenForUser");

		given(jwtTokenService.saveTokenToDataBase(any())).willReturn("TokenForUser");

		given(jwtTokenService.mapUserAndJwtTokenToToken(any(), anyString())).willReturn(new Token());

		given(userRepository.findByLoginAndPassword("login", "password"))
				.willReturn(Optional.of(new User("login", "password")));

		String result = securityAuthServiceImpl.geJwtTokenByLoginAndPassword(new UserDto("login", "password"));
		Assertions.assertEquals("TokenForUser", result);
	}

	@Test
	void testDeleteTokenAndLogout(){
		HttpStatus result = securityAuthServiceImpl.deleteTokenAndLogout("9879879879878979879879");
		Assertions.assertEquals(HttpStatus.OK, result);
	}

	@Test
	void testValidateUser(){

		boolean result = securityAuthServiceImpl.validateUser(
				new UserDto("login", "password"),
				new User("login", "password"));

		Assertions.assertEquals(true, result);
	}

	@Test
	void testFindUserByLoginAndPassword(){
		given(userRepository.findByLoginAndPassword("login", "password"))
				.willReturn(Optional.of(new User("login", "password")));

		User result = securityAuthServiceImpl.findUserByLoginAndPassword("login", "password");
		assertThat(String.valueOf(result),true);
	}

	@Test
	void testFindUserByLogin(){
		given(userRepository.findByLogin("login")).willReturn(
				Optional.of(new User("login", "password")));

		User result = securityAuthServiceImpl.findUserByLogin("login");

		assertThat(String.valueOf(result),true);
		assertThat(String.valueOf(result.getPassword().equals("password")),true);

	}
}

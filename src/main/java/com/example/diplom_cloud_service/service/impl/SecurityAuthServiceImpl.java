package com.example.diplom_cloud_service.service.impl;


import com.example.diplom_cloud_service.auth.JwtTokenService;
import com.example.diplom_cloud_service.dto.UserDto;
import com.example.diplom_cloud_service.entity.Token;
import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.exception.UserNotFoundException;
import com.example.diplom_cloud_service.repository.UserRepository;
import com.example.diplom_cloud_service.service.SecurityAuthService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
@Log4j2
@Service
public class SecurityAuthServiceImpl implements SecurityAuthService {

	private final JwtTokenService jwtTokenService;
	private final UserRepository userRepository;


	@Autowired
	public SecurityAuthServiceImpl(JwtTokenService jwtTokenService, UserRepository userRepository) {
		this.jwtTokenService = jwtTokenService;
		this.userRepository = userRepository;
	}


	@Override
	public String geJwtTokenByLoginAndPassword(UserDto userDto) {
		User user = findUserByLoginAndPassword(userDto.getLogin(), userDto.getPassword());
		if (user != null) {
			if (user.getToken() != null) {
				return user.getToken().getToken();
			}
			if (validateUser(userDto, user)) {
				String token = jwtTokenService.generateToken(user.getLogin());
				log.info("Token generate for user:" + user.getLogin() + " " + new Date(System.currentTimeMillis()));

				Token userToken = jwtTokenService.mapUserAndJwtTokenToToken(user, token);
				return jwtTokenService.saveTokenToDataBase(userToken);
			}
		}
		throw new UserNotFoundException("User not found with username: " + userDto.getLogin());
	}

	@Override
	public HttpStatus deleteTokenAndLogout(String token) {

		jwtTokenService.removeTokenAndLogout(token.substring(7, token.length()));
		return HttpStatus.OK;
	}

	@Override
	public boolean validateUser(UserDto userDto, User user) {
		return userDto.getLogin().equals(user.getLogin())
		       || userDto.getPassword().equals(user.getPassword());
	}

	@Transactional
	public User findUserByLoginAndPassword(String login, String password) {
		return userRepository.findByLoginAndPassword(login, password).orElseThrow(() ->
				                                                                          new UserNotFoundException("User not found with username: " + login + " and password"));
	}

	@Transactional
	public User findUserByLogin(String login){
		return userRepository.findByLogin(login)
		                     .orElseThrow(() -> new UserNotFoundException("User not found with username: " + login));
	}

}

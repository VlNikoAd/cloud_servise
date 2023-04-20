package com.example.diplom_cloud_service.auth;


import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.service.impl.SecurityAuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {


	private final SecurityAuthServiceImpl service;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = service.findUserByLogin(username);
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
		return new CustomUserDetails(user.getLogin(), user.getPassword(), authorities);

	}
}

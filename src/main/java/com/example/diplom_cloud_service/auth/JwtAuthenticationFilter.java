package com.example.diplom_cloud_service.auth;


import com.example.diplom_cloud_service.entity.Token;
import com.example.diplom_cloud_service.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	UserDetailsService userDetailsService;
	JwtTokenService jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {

		String token = request.getHeader("auth-token");

		if ((token != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
			try {
				Token userToken = jwtTokenProvider.findToken(token.substring(7, token.length()));
				UserDetails userDetails = userDetailsService.loadUserByUsername(userToken.getUser().getLogin());

				if (userDetails != null && jwtTokenProvider.validateToken(token.substring(7, token.length()), userDetails)) {

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (InvalidJwtException e) {
				log.warn("JwtAuthenticationFilter: " + e.getMessage()+ ": " + token );
			}
		}
		filterChain.doFilter(request, response);
	}

}

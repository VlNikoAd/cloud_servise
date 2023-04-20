package com.example.diplom_cloud_service.auth;


import com.example.diplom_cloud_service.entity.Token;
import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.exception.InvalidJwtException;
import com.example.diplom_cloud_service.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Log4j2
@Service
public class JwtTokenService {
	@Value("${jwt.secret.key}")
	String secret;
	private final TokenRepository tokenRepository;

	public JwtTokenService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()));
	}


	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);

	}

	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
		           .setClaims(claims)
		           .setSubject(userName)
		           .setIssuedAt(new Date(System.currentTimeMillis()))
		           .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	@Transactional
	public Token findToken(String token) {
		return tokenRepository.findByToken(token).orElseThrow(() ->
				                                                      new InvalidJwtException("Invalid Token"));

	}

	@Transactional
	public String saveTokenToDataBase(Token token) {
		tokenRepository.save(token);
		return token.getToken();

	}

	@Transactional
	public void removeTokenAndLogout(String token) {
		Token userToken = findToken(token);
		if (userToken != null) {
			tokenRepository.delete(userToken);
			log.info("The token is removed");
		}
	}

	public Token mapUserAndJwtTokenToToken(User user, String token) {
		Token userToken = new Token();
		userToken.setToken(token);
		userToken.setUser(user);
		return userToken;
	}
}

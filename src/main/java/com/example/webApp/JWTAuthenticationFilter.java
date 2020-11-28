package com.example.webApp;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		//setFilterProcessUrl("/api/services/controller/user/login");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
		try {
			User creds = new ObjectMapper()
					.readValue(req.getInputStream(), User.class);
			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						creds.getUsername(),
						creds.getPassword(),
						new ArrayList<>()
					)
			);
			
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public void successfulAuthentication(HttpServletRequest req, 
										HttpServletResponse res,
										FilterChain chain,
										Authentication auth) throws IOException{
		String token = JWT.create()
				.withSubject(((User)auth.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET.getBytes()));
		
		String body = ((User)auth.getPrincipal()).getUsername() + " " + token;
		
		res.getWriter().write(body);
		res.getWriter().flush();
	}
	
}

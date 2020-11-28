package com.example.webApp;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	public void doFilterInternal(HttpServletRequest req,
								HttpServletResponse res,
								FilterChain chain) throws IOException, ServletException{
		String header = req.getHeader(HEADER_STRING);
		
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthenticaion(req);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
			
	}

	//Reads the JWT from the Authorization header, and then uses JWT to validate the token
	private UsernamePasswordAuthenticationToken getAuthenticaion(HttpServletRequest req) {
		String token = req.getHeader(HEADER_STRING);
		
		if(token != null) {
			String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
					.build()
					.verify(token.replace(TOKEN_PREFIX, ""))
					.getSubject();
			
			if(user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}

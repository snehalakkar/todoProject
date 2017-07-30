package com.bridgeit.TodoApp.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.TokenService;

@Component
public class TokenManupulation extends Tokens {

	Tokens tokens = new Tokens();

	@Autowired
	TokenService tokenService;

	// Generate Access and Refresh token after successfull login
	public Tokens generateTokens() {
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(accessToken+"***************");
		String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
		tokens.setAccessToken(accessToken);
		tokens.setRefreshToken(refreshToken);
		tokens.setCreatedOn(new Date());
		return tokens;
	}

	// Access token generate method when access token is expired but refresh
	// token is valid
	public Tokens generateNewaccessToken() {
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		tokens.setAccessToken(accessToken);
		tokens.setCreatedOn(new Date());
		return tokens;
	}

	public User accesstokenValidation(String accToken) {
		User user = null;
		Tokens tokens = tokenService.getTokenbyAccessToken(accToken);
		System.out.println("token 11111:" + tokens);

		long createdOn = tokens.getCreatedOn().getTime();// in milisec
		System.out.println("createdOn+ " + createdOn);
		long date = new Date().getTime();
		System.out.println("date " + date);
		long diff = date - createdOn;
		long difference = TimeUnit.MILLISECONDS.toMinutes(diff);

		if (difference < 10) {
			user = tokens.getGetUser();
			
			return user;
		}
		
		return user;
	}

	public boolean refreshtokenValidation(String refreshToken) {
		Tokens tokens = tokenService.getTokenbyRefreshToken(refreshToken);
		System.out.println("token 11111:" + tokens);
		
		long createdOn = tokens.getCreatedOn().getTime();// in milisec
		System.out.println("createdOn+ " + createdOn);
		long date = new Date().getTime();
		System.out.println("date " + date);
		long diff = date - createdOn;
		long difference = TimeUnit.MILLISECONDS.toMinutes(diff);

		if (difference < 10) {
			
			return true;
		}
		return false;
	}
}

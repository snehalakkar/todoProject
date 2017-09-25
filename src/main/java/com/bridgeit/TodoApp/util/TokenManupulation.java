package com.bridgeit.TodoApp.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.RedisService;
import com.bridgeit.TodoApp.Service.TokenService;

@Component
public class TokenManupulation extends Tokens {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Tokens tokens = new Tokens();

	@Autowired
	TokenService tokenService;
	
	@Autowired
	RedisService redisService;

	// Generate Access and Refresh token after successfull login
	public Tokens generateTokens() {
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(accessToken + "***************");
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
		System.out.println("inside accesstokenValidation ......" + accToken);
		User user = null;
		/*Tokens tokens = tokenService.getTokenbyAccessToken(accToken);*/
		Tokens redistoken = redisService.findToken(accToken);
		System.out.println("redistoken "+ redistoken);
		long createdOn = redistoken.getCreatedOn().getTime();// in milisec

		long date = new Date().getTime();

		long diff = date - createdOn;
		long difference = TimeUnit.MILLISECONDS.toMinutes(diff);

		if (difference < 30) {
			user = redistoken.getGetUser();

			return user;
		}
		return user;
	}

	public boolean refreshtokenValidation(String refreshToken) {
		Tokens tokens = tokenService.getTokenbyRefreshToken(refreshToken);

		long createdOn = tokens.getCreatedOn().getTime();// in milisec

		long date = new Date().getTime();

		long diff = date - createdOn;
		long difference = TimeUnit.MILLISECONDS.toMinutes(diff);

		if (difference < 60) {
			return true;
		}
		return false;
	}
}

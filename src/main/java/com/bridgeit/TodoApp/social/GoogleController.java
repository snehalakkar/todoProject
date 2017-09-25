package com.bridgeit.TodoApp.social;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.GooglePojo;
import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.RedisService;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.social.GoogleConnection;
import com.bridgeit.TodoApp.util.TokenManupulation;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class GoogleController {
	@Autowired
	GoogleConnection googleConnection;

	@Autowired
	Tokens tokens;

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@Autowired
	TokenManupulation tokenManupulation;
	
	@Autowired
	RedisService redisService;

	@RequestMapping(value = "loginWithGoogle")
	public void googleConnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(" in googleLoginURL  ");
		String unid = UUID.randomUUID().toString();
		request.getSession().setAttribute("STATE", unid);
		String googleLoginURL = googleConnection.getGoogleAuthURL(unid);
		System.out.println("googleLoginURL  " + googleLoginURL);
		response.sendRedirect(googleLoginURL);
	}

	@RequestMapping(value = "connectgoogle")
	public void redirectFromGoogle(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionState = (String) request.getSession().getAttribute("STATE");
		String googlestate = request.getParameter("state");

		System.out.println("in connect google");

		if (sessionState == null || !sessionState.equals(googlestate)) {
			response.sendRedirect("loginWithGoogle");
		}

		String error = request.getParameter("error");
		if (error != null && error.trim().isEmpty()) {
			response.sendRedirect("userlogin");
		}

		String authCode = request.getParameter("code");
		String googleaccessToken = googleConnection.getAccessToken(authCode);
		System.out.println("accessToken " + googleaccessToken);

		JsonNode profile = googleConnection.getUserProfile(googleaccessToken);
		System.out.println("google profile :"+profile);
		System.out.println("google profile :" + profile.get("displayName"));
		System.out.println("user email in google login :" + profile.get("emails").get(0).get("value").asText()); //asText() is use to remove outer text.
		System.out.println("google profile :" + profile.get("image").get("url"));
		User user = userService.getUserByEmail(profile.get("emails").get(0).get("value").asText());

		// get user profile
		if (user == null) {
			System.out.println(" user is new to our db");
			user = new User();
			user.setFullName(profile.get("displayName").asText());
			user.setEmail(profile.get("emails").get(0).get("value").asText());
			user.setPassword("");
			user.setProfile(profile.get("image").get("url").asText());
			userService.registerUser(user);
		}

		System.out.println(" user is not new to our db ,it is there in our db");
		tokens = tokenManupulation.generateTokens();
		user.setProfile(profile.get("image").get("url").asText());
		userService.updateUserProfile(user);
		
		tokens.setGetUser(user);
		tokenService.saveToken(tokens);
		redisService.saveTokens(tokens);
		
		Cookie acccookie = new Cookie("socialaccessToken", tokens.getAccessToken());
		Cookie refreshcookie = new Cookie("socialrefreshToken", tokens.getRefreshToken());
		response.addCookie(acccookie);
		response.addCookie(refreshcookie);
		response.sendRedirect("http://localhost:8080/TodoApp/#!/home");
	}
}

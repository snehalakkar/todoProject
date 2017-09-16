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
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.social.GoogleConnection;
import com.bridgeit.TodoApp.util.TokenManupulation;

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

		GooglePojo profile = googleConnection.getUserProfile(googleaccessToken);
		System.out.println("google profile :" + profile);
		System.out.println("user email in google login " + profile.getEmails().get(0).getValue());
		User user = userService.getUserByEmail(profile.getEmails().get(0).getValue());

		// get user profile
		if (user == null) {
			System.out.println(" user is new to our db");
			user = new User();
			user.setFullName(profile.getDisplayName());
			user.setEmail(profile.getEmails().get(0).getValue());
			user.setPassword("");
			user.setProfile(profile.getImage().getUrl());
			userService.registerUser(user);
		}

		System.out.println(" user is not new to our db ,it is there in our db");
		tokens = tokenManupulation.generateTokens();
		tokens.setGetUser(user);
		tokenService.saveToken(tokens);

		Cookie acccookie = new Cookie("googleaccessToken", tokens.getAccessToken());
		Cookie refreshcookie = new Cookie("googlerefreshToken", tokens.getRefreshToken());
		response.addCookie(acccookie);
		response.addCookie(refreshcookie);
		response.sendRedirect("http://localhost:8080/TodoApp/#!/home");
	}
}

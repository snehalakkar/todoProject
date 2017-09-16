package com.bridgeit.TodoApp.social;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.FacebookProfile;
import com.bridgeit.TodoApp.DTO.GooglePojo;
import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.social.FacebookConnection;
import com.bridgeit.TodoApp.util.TokenManupulation;

@RestController
public class FacebookController {

	@Autowired
	FacebookConnection facebookConnection;

	@Autowired
	UserService userService;

	@Autowired
	Tokens tokens;

	@Autowired
	TokenManupulation tokenManupulation;

	@Autowired
	TokenService tokenService;

	@RequestMapping(value = "loginWithFacebook")
	public void googleConnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(" in loginWithFacebook  ");
		String unid = UUID.randomUUID().toString();
		request.getSession().setAttribute("STATE", unid);
		String facebookLoginURL = facebookConnection.getFacebookAuthURL(unid);
		System.out.println("facebookLoginURL  " + facebookLoginURL);
		response.sendRedirect(facebookLoginURL);
	}

	@RequestMapping(value = "connectFB")
	public void redirectFromFacebook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sessionState = (String) request.getSession().getAttribute("STATE");
		String googlestate = request.getParameter("state");

		System.out.println("in connect facebook");

		if (sessionState == null || !sessionState.equals(googlestate)) {
			response.sendRedirect("loginWithFacebook");
		}

		String error = request.getParameter("error");
		if (error != null && error.trim().isEmpty()) {
			response.sendRedirect("userlogin");
		}

		String authCode = request.getParameter("code");
		String fbaccessToken = facebookConnection.getAccessToken(authCode);
		System.out.println("fbaccessToken " + fbaccessToken);

		FacebookProfile profile = facebookConnection.getUserProfile(fbaccessToken);
		System.out.println("fb profile :" + profile);
		System.out.println("user email in fb login " + profile.getEmail());
		User user = userService.getUserByEmail(profile.getEmail());

		// get user profile
		if (user == null) {
			System.out.println(" user is new to our db");
			user = new User();
			user.setFullName(profile.getName());
			user.setEmail(profile.getEmail());
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

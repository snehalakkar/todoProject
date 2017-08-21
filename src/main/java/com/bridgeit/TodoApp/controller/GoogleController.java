package com.bridgeit.TodoApp.controller;

import java.io.IOException;
import java.util.UUID;

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
import com.bridgeit.TodoApp.social.GoogleConnection;

@RestController
public class GoogleController {
	@Autowired
	GoogleConnection googleConnection;

	@Autowired
	Tokens tokens;

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
	public ResponseEntity<Tokens> redirectFromGoogle(HttpServletRequest request, HttpServletResponse response)
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
		String accessToken = googleConnection.getAccessToken(authCode);
		// response.setHeader("accessToken", accessToken);

		System.out.println("accessToken " + accessToken);
		tokens.setAccessToken(accessToken);

		GooglePojo profile = googleConnection.getUserProfile(accessToken);
		System.out.println("profile " + profile);
		/*
		 * User user =
		 * userService.getUserByEmail(profile.getEmails().get(0).getValue());
		 * 
		 * token.setAccesstoken(accessToken); token.setUser(user);
		 * tokenService.tokenSave(token);
		 * 
		 * //get user profile
		 * 
		 * 
		 * if(user==null) { user = new User(); String name =
		 * profile.getDisplayName(); String namesplit[]=name.split(" ");
		 * user.setFirstname(namesplit[0]); user.setLastname(namesplit[1]);
		 * user.setEmail(profile.getEmails().get(0).getValue());
		 * user.setPassword(""); user.setProfile(profile.getImage().getUrl());
		 * userService.signUp(user); } HttpSession session =
		 * request.getSession(); session.setAttribute("UserSession", user);
		 */

		// response.sendRedirect("http://localhost:8080/TodoApp/#!/home");
		return new ResponseEntity<Tokens>(tokens, HttpStatus.OK);
	}
}

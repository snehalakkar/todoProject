package com.bridgeit.TodoApp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.TokenResponse;
import com.bridgeit.TodoApp.json.UserFieldError;
import com.bridgeit.TodoApp.util.TokenManupulation;

@RestController
public class TokenController {

	@Autowired
	TokenManupulation tokenManupulation;

	@Autowired
	TokenService tokenService;

	@RequestMapping(value = "/generateNewaccessToken",method=RequestMethod.POST)
	public ResponseEntity<Response> generateNewaccessToken(HttpServletRequest request) {

		TokenResponse tokenResponse=null;
		String refreshToken = request.getHeader("refreshToken");
	
		/*String trimrefreshToken = refreshToken.substring(1, refreshToken.length() - 1);*/
		System.out.println("trim " + refreshToken);
		boolean isvalidRefresh = tokenManupulation.refreshtokenValidation(refreshToken);
		System.out.println(isvalidRefresh);
		if (isvalidRefresh==true) {
			Tokens tokens = tokenManupulation.generateNewaccessToken();
			tokenService.updateaccesstoken(tokens);
			tokenResponse = new TokenResponse();
			tokenResponse.setStatus(11);
			tokenResponse.setMessage("new access generated successfully...");
			tokenResponse.setTokens(tokens);
			return new ResponseEntity<Response>(tokenResponse, HttpStatus.OK);
		}
		
		//if refrsh is not valid then we will delete all token obj of that user.
		tokenService.deletetokenbyRefresh(refreshToken);
		tokenResponse = new TokenResponse();
		tokenResponse.setStatus(-11);
		tokenResponse.setMessage("both acc and ref token expired, thus logout...");
		return new ResponseEntity<Response>(tokenResponse,HttpStatus.OK);
	}
}

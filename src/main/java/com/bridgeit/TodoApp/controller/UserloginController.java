package com.bridgeit.TodoApp.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.util.PasswordEncryptionUsingHashing;
import com.bridgeit.TodoApp.util.TokenManupulation;

@RestController
public class UserloginController {

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@Autowired
	TokenManupulation tokenManupulation;

	private static final Logger logger = Logger.getLogger("loginFile");
	private static final Logger logger1 = Logger.getRootLogger();

	@PostMapping(value = "/userlogin")
	public ResponseEntity<Tokens> userLogin(@RequestBody Map<String, String> reqParam, HttpServletRequest request,
			HttpServletResponse response) throws NoSuchAlgorithmException {

		Tokens tokens = null;
		try {
			logger.warn("toDoLogin is executed!");
			logger1.info("rootlogger demo...");
			System.out.println("************");
			String email = reqParam.get("email");
			String password = reqParam.get("password");
			System.out.println(email);
			System.out.println(password);
			String encriptedpwd = PasswordEncryptionUsingHashing.passwordEncryption(password);

			User user = userService.userLogin(email, encriptedpwd);
			System.out.println("user obj :" + user);

			if (user != null) {
				System.out.println("************in success");
				System.out.println("user in logincontroller :" + user);
				logger.info("validation successfull...");

				tokens = tokenManupulation.generateTokens();
				System.out.println("token generated :" + tokens);

				// save user field of token class which is mapping
				tokens.setGetUser(user);
				tokenService.saveToken(tokens);

				// we dont need to send user as a response only token is
				// required

				tokens.setGetUser(null);
				return new ResponseEntity<Tokens>(tokens, HttpStatus.OK);
			}
			return new ResponseEntity<Tokens>(tokens, HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			logger.info("validation not successfull...");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Tokens>(tokens, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@RequestMapping(value = "/app/logout", method = RequestMethod.POST)
	public ResponseEntity<Void> logoutUser(HttpServletRequest request) {

		String accessToken = request.getHeader("accessToken");
		String accessTokentodelete = accessToken.substring(1, accessToken.length() - 1);

		boolean result = false;
		try {
			result = tokenService.logoutUser(accessTokentodelete);
			if (result == true) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}
}

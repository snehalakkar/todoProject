package com.bridgeit.TodoApp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.Service.UserService;

@RestController
public class UserLoginController {

	@Autowired
	UserService userService;

	@PostMapping(value = "/userLogin")
	public ResponseEntity<Boolean> userLogin(@RequestBody Map<String, String> reqParam, BindingResult result) {
		boolean loginValidation;
		String email = reqParam.get("email");
		String password = reqParam.get("password");

		loginValidation = userService.userLogin(email, password);

		if (loginValidation == true) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
	}
}

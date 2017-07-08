package com.bridgeit.TodoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.UserFieldError;
import com.bridgeit.TodoApp.validation.ServerSideValidation;

@RestController
public class UserRegController {

	@Autowired
	ServerSideValidation serverSideValidation;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/registerUser1", method = RequestMethod.POST)
	public @ResponseBody Response registerUseri(@RequestBody User user, BindingResult result) // Response
	{
		UserFieldError err = null;
		System.out.println("11111111111111111");
		serverSideValidation.validate(user, result);

		if (result.hasErrors()) {

			err = new UserFieldError();
			err.setStatus(-1);
			err.setMessage("field error");
			return err;
		}

		userService.registerUser(user);
		err = new UserFieldError();
		err.setStatus(1);
		err.setMessage("working fine");
		return err;

	}

	/*
	 * @RequestMapping(value = "/registerUser", method = RequestMethod.POST,
	 * produces = { MediaType.APPLICATION_JSON_VALUE }) public
	 * ResponseEntity<Boolean> registerUser(@RequestBody User user,
	 * BindingResult result) { System.out.println("hello...");
	 * userService.registerUser(user); System.out.println(user.getFullName());
	 * return new ResponseEntity<Boolean>(true, HttpStatus.OK); }
	 */

	@PutMapping(value = "/updateUser", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> updateUser( @RequestBody User user, BindingResult result) {

//		user.setUserId(userId);
		
		serverSideValidation.validate(user, result);
		
		if (result.hasErrors()) {
			return new ResponseEntity<String>("not acceptable error occured",HttpStatus.NOT_ACCEPTABLE);
		}
		userService.registerUser(user);

		return new ResponseEntity<String>("user successfully updated", HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteUser/{id}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("id") int userId,@RequestBody User user) {
		user.setUserId(userId);
		int id=userService.deleteUser(userId,user);
		 if(id == 0){
			 return new ResponseEntity<Boolean>(false,HttpStatus.NOT_FOUND);
		 }
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}

}

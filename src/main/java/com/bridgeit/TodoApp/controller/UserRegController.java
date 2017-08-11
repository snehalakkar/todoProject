package com.bridgeit.TodoApp.controller;

import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.RestController;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.UserFieldError;
import com.bridgeit.TodoApp.util.PasswordEncryptionUsingHashing;
import com.bridgeit.TodoApp.validation.ServerSideValidation;

@RestController
public class UserRegController {

	@Autowired
	ServerSideValidation serverSideValidation;

	@Autowired
	UserService userService;
	
	private static final Logger logger = Logger.getLogger("regFile");
	private static final Logger logger1 = Logger.getRootLogger();

	@RequestMapping(value = "/registerUser1", method = RequestMethod.POST)
	public ResponseEntity<Response> registerUseri(@RequestBody User user, BindingResult result) throws NoSuchAlgorithmException // Response
	{
		UserFieldError err = null;
		logger1.info("rootlogger demo...");

		serverSideValidation.validate(user, result);

		String encriptedpwd=PasswordEncryptionUsingHashing.passwordEncryption(user.getPassword());
		user.setPassword(encriptedpwd);
		
		if (result.hasErrors()) {
			logger.info("binding result error,Registration fails ");
			err = new UserFieldError();
			err.setStatus(-1);
			err.setMessage("binding result error");
			return new ResponseEntity<Response>(err,HttpStatus.NOT_ACCEPTABLE);
		}

		try {
			//just to remove multiple spaces between words
			String trimfullName=user.getFullName().replaceAll("( +)"," ").trim();
			user.setFullName(trimfullName);
			
			userService.registerUser(user);
			logger.info("user registered successfully... ");
		} 
		catch (Exception e) {
			logger.error("sorry, some error occured while saving obj in DB,user not registered ",e);
			err = new UserFieldError();
			err.setStatus(2);
			err.setMessage(e.getMessage());
			return new ResponseEntity<Response>(err,HttpStatus.SERVICE_UNAVAILABLE);
			
		}
		err = new UserFieldError();
		err.setStatus(1);
		err.setMessage("working fine");
		return  new ResponseEntity<Response>(err,HttpStatus.OK);
	}


	@PutMapping(value = "/updateUser", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> updateUser( @RequestBody User user, BindingResult result) throws NoSuchAlgorithmException {

		serverSideValidation.validate(user, result);
		
		String encriptedpwd=PasswordEncryptionUsingHashing.passwordEncryption(user.getPassword());
		user.setPassword(encriptedpwd);
		
		String abc = "{\"msg\":\"user successfully updated\"}";
		String abc1 = "{\"msg\":\"not acceptable,some binding related error occured\"}";
		String abc2 = "{\"msg\":\"not acceptable,some error occured during updating obj in DB\"}";
		
		if (result.hasErrors()) {
			return new ResponseEntity<String>(abc1,HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			userService.registerUser(user);
		} catch (Exception e) {
			logger.error("sorry, some error occured while updating obj in DB,user not registered ",e);
			e.printStackTrace();
			return new ResponseEntity<String>(abc2,HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return new ResponseEntity<String>(abc, HttpStatus.OK);
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

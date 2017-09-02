package com.bridgeit.TodoApp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.UserService;

@RestController
public class EmailController {
	
	@Autowired
	UserService userService;
	
	User user=new User();

	@RequestMapping(value = "/activateStatuscode/{id}/{acc_token}",method = RequestMethod.GET)
	@Consumes(MediaType.APPLICATION_JSON)
	public void activateUserAccount(@PathVariable("id") int id, @PathVariable("acc_token") String acc_token,HttpServletResponse response) throws IOException{
		System.out.println(" in activateUserAccount  ");
		System.out.println("id "+id+" acc_token "+acc_token);
		User user=userService.validateStatuscode(id,acc_token);
		System.out.println("user b4 setting status code ");
		if(user!=null){
		user.setStatusCode(true);
		response.sendRedirect("http://localhost:8080/TodoApp/#!/login");
		}
		else{
			user.setStatusCode(false);
		}
		userService.updateUser(id, user);
	}
}

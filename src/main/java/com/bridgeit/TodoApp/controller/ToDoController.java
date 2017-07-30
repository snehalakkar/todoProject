package com.bridgeit.TodoApp.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.TodoTask;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.TodoService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.UserFieldError;

@RestController

public class ToDoController {

	@Autowired
	TodoService todoService;

	private static final Logger logger1 = Logger.getRootLogger();

	@RequestMapping(value = "/app/saveTodo", method = RequestMethod.POST)
	public ResponseEntity<Response> saveTodo(@RequestBody TodoTask todoTask, ServletRequest request) {
		UserFieldError err = null;

		try {
			/*
			 * HttpSession session = request.getSession(); User user = (User)
			 * session.getAttribute("userObject"); int uid=user.getUserId();
			 * user.setUserId(uid);
			 */

			// instead of taking user from session we are taking user from
			// requset of servletResponse
			User user = (User) request.getAttribute("userobjInFilter");
			System.out.println("user obj" + user);
			Date date = new Date();

			todoTask.setCreatedDate(date);
			todoTask.setUser(user);
			System.out.println(todoTask.getTodoId());
			todoTask.setTodoId(todoTask.getTodoId());

			System.out.println("user in todotask :" + todoTask);

			todoService.saveTodo(todoTask);
			err = new UserFieldError();
			err.setStatus(1);
			err.setMessage("todo successfully saved...");
			logger1.info("todoTask save successfully... ");
			return new ResponseEntity<Response>(err, HttpStatus.OK);
		}

		catch (Exception e) {
			logger1.error("sorry, some error occured,todoTask not saved ", e);
			err = new UserFieldError();
			err.setStatus(2);
			err.setMessage(e.getMessage());
			return new ResponseEntity<Response>(err, HttpStatus.SERVICE_UNAVAILABLE);

		}
	}

	@PutMapping(value = "/app/updateTodo", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> updateTodo(@RequestBody TodoTask todoTask) throws NoSuchAlgorithmException {

		String abc = "{\"msg\":\"todoTask successfully updated\"}";
		String abc1 = "{\"msg\":\"not acceptable,some binding related error occured\"}";
		String abc2 = "{\"msg\":\"not acceptable,some error occured during updating obj in DB\"}";

		try {
			todoService.updateTodo(todoTask);
		} catch (Exception e) {
			logger1.error("sorry, some error occured while updating obj in DB,user not registered ", e);
			e.printStackTrace();
			return new ResponseEntity<String>(abc2, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(abc, HttpStatus.OK);
	}

	@DeleteMapping(value = "/app/deleteTodo/{taskId}")
	public ResponseEntity<String> deleteUser(@PathVariable("taskId") int todoId, @RequestBody TodoTask todoTask) {

		try {
			todoService.deleteTodo(todoId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping(value = "/app/getTodoTaskById/{id}")
	public ResponseEntity<TodoTask> getTodoTaskById(@PathVariable("id") int todoId) {

		try {
			TodoTask todoTask = todoService.getTodoTaskById(todoId);

			if (todoTask != null) {
				return new ResponseEntity<TodoTask>(todoTask, HttpStatus.OK);
			} else {
				return new ResponseEntity<TodoTask>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<TodoTask>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// here we are taking an user obj from servletResponse of filter
	@PostMapping(value = "/app/getAllTodoTask")
	public ResponseEntity<List<TodoTask>> getAllTodoTask(ServletRequest request) {

		try {
			/*HttpSession session = request.getSession();
			User user = (User) session.getAttribute("userObject");*/
			
			User user=(User) request.getAttribute("userobjInFilter");
			int userid = user.getUserId();

			List<TodoTask> list = todoService.getAllTodoTask(userid);
			System.out.println("list " + list);
			if (list != null) {
				return new ResponseEntity<List<TodoTask>>(list,HttpStatus.OK);
			} else {
				return new ResponseEntity<List<TodoTask>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<TodoTask>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

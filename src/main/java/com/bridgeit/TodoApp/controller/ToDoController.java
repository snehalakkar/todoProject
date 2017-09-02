package com.bridgeit.TodoApp.controller;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bridgeit.TodoApp.DTO.TodoTask;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.DTO.WebScrapper;
import com.bridgeit.TodoApp.Service.TodoService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.UserFieldError;
import com.bridgeit.TodoApp.util.URLUtil;
import com.bridgeit.TodoApp.util.WebScrapping;

@RestController
public class ToDoController {

	@Autowired
	TodoService todoService;

	@Autowired
	WebScrapping webScrapping;

	WebScrapper webScrapper = new WebScrapper();

	private static final Logger logger1 = Logger.getRootLogger();

	@RequestMapping(value = "/app/saveTodo", method = RequestMethod.POST)
	public ResponseEntity<Response> saveTodo(@RequestBody TodoTask todoTask, ServletRequest request) {
		UserFieldError err = null;
		try {

			User user = (User) request.getAttribute("userobjInFilter");
			System.out.println("user obj" + user);
			Date date = new Date();

			todoTask.setCreatedDate(date);
			todoTask.setUser(user);
			System.out.println("todotitle " + todoTask.getTitle());
			System.out.println("tododesc " + todoTask.getDescription());
			
			//to check that desc is url or not
			boolean isValidUrl = URLUtil.isValidUrl(todoTask.getDescription());
			System.out.println("isValidUrl "+isValidUrl);
			if(isValidUrl)
			{
			WebScrapper webScrapper1= webScrapping.checkUrlForDescScrapping(todoTask.getDescription());
			todoTask.setWebScrapper(webScrapper1);
			}
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

	@RequestMapping(value = "/app/updateTodo/{taskId}", produces = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<TodoTask> updateTodo(@PathVariable("taskId") int todoId, @RequestBody TodoTask todoTask,
			ServletRequest request) throws NoSuchAlgorithmException {
		try {
			todoTask.setCreatedDate(new Date());
			System.out.println("new todo " + todoTask);
			todoTask.setTodoId(todoId);
			todoService.updateTodo(todoTask);
		} catch (Exception e) {
			logger1.error("sorry, some error occured while updating obj in DB,user not registered ", e);
			e.printStackTrace();
			System.out.println(e.getMessage());
			return new ResponseEntity<TodoTask>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<TodoTask>(todoTask, HttpStatus.OK);
	}

	@RequestMapping(value = "/app/deleteTodo/{taskId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteTodo(@PathVariable("taskId") int todoId, @RequestBody TodoTask todoTask) {
		try {
			todoService.deleteTodo(todoId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/app/getTodoTaskById/{id}", method = RequestMethod.POST)
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
	@RequestMapping(value = "/app/getAllTodoTask", method = RequestMethod.POST)
	public ResponseEntity<List<TodoTask>> getAllTodoTask(ServletRequest request) {

		try {
			User user = (User) request.getAttribute("userobjInFilter");
			int userid = user.getUserId();

			List<TodoTask> list = todoService.getAllTodoTask(userid);
			System.out.println("list " + list);
			if (list != null) {
				return new ResponseEntity<List<TodoTask>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<TodoTask>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<TodoTask>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

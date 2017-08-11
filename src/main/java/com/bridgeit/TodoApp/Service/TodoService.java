package com.bridgeit.TodoApp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.TodoApp.DAO.TodoDaoInterface;
import com.bridgeit.TodoApp.DTO.TodoTask;

@Service
public class TodoService {

	@Autowired
	TodoDaoInterface todoDaoImplementation;
	
	@Transactional
	public void saveTodo(TodoTask todoTask) {
		todoDaoImplementation.saveTodo(todoTask);	
	}

	@Transactional
	public void updateTodo(TodoTask todoTask) {
		todoDaoImplementation.updateTodo(todoTask);
	}

	@Transactional
	public void deleteTodo(int todoId) {
		todoDaoImplementation.deleteTodo(todoId);		
	}

	@Transactional
	public TodoTask getTodoTaskById(int todoId) {
		return todoDaoImplementation.getTodoTaskById(todoId);
	}

	@Transactional
	public List<TodoTask> getAllTodoTask(int userid) {
		return todoDaoImplementation.getAllTodoTask(userid);
	}

	@Transactional
	public void deleteReminder(int todoId) {
		todoDaoImplementation.deleteReminder(todoId);
	}

}

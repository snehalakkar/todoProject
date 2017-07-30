package com.bridgeit.TodoApp.DAO;

import java.util.List;

import com.bridgeit.TodoApp.DTO.TodoTask;

public interface TodoDaoInterface {

	void saveTodo(TodoTask todoTask);

	void updateTodo(TodoTask todoTask);

	void deleteTodo(int todoId);

	TodoTask getTodoTaskById(int todoId);

	List<TodoTask> getAllTodoTask(int userid);


}

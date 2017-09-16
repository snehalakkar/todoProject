package com.bridgeit.TodoApp.DAO;

import java.util.List;

import com.bridgeit.TodoApp.DTO.Collaborator;
import com.bridgeit.TodoApp.DTO.TodoTask;
import com.bridgeit.TodoApp.DTO.User;

public interface TodoDaoInterface {

	void saveTodo(TodoTask todoTask);

	void updateTodo(TodoTask todoTask);

	void deleteTodo(int todoId);

	TodoTask getTodoTaskById(int todoId);

	List<TodoTask> getAllTodoTask(int userid);

	void saveColaborator(Collaborator collaborator);

	List<User> getsharedCollaborator(int todoId);
}

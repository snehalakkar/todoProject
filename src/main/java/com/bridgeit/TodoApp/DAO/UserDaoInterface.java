package com.bridgeit.TodoApp.DAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bridgeit.TodoApp.DTO.User;

public interface UserDaoInterface {

	void registerUser(User user);

	User updateUser(int userId, User user);

	int deleteUser(int userId, User user);

	User userLogin(String email, String password);

}

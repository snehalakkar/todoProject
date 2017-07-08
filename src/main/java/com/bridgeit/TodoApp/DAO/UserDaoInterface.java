package com.bridgeit.TodoApp.DAO;

import com.bridgeit.TodoApp.DTO.User;

public interface UserDaoInterface {

	void registerUser(User user);

	User updateUser(int userId, User user);

	int deleteUser(int userId, User user);

	boolean userLogin(String email, String password);

}

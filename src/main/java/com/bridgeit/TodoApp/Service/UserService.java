package com.bridgeit.TodoApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.TodoApp.DAO.UserDaoInterface;
import com.bridgeit.TodoApp.DTO.User;


@Service
public class UserService {

	@Autowired
	UserDaoInterface userDaoImplementation;
	
	@Transactional
	public void registerUser(User user) {
		userDaoImplementation.registerUser(user);
	}

	@Transactional
	public User updateUser(int userId, User user) {
		return userDaoImplementation.updateUser(userId,user);
	}

	@Transactional
	public int deleteUser(int userId, User user) {
		return userDaoImplementation.deleteUser(userId,user);
	}

	@Transactional
	public User userLogin(String email, String password) {
		return userDaoImplementation.userLogin(email,password);
	}
	
	@Transactional
	public User getUserByEmail(String email) {
		return userDaoImplementation.getUserByEmail(email);
	}
}

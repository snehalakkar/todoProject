package com.bridgeit.TodoApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgeit.TodoApp.DAO.RedisInterface;
import com.bridgeit.TodoApp.DTO.Tokens;

@Service
public class RedisService {
	
	@Autowired
	RedisInterface redisImplementation;
	
	public void saveTokens(Tokens tokens) {
		redisImplementation.saveTokens(tokens);	
	}
	
	public Tokens findToken(String acc) {
		return redisImplementation.findToken(acc);	
	}
}

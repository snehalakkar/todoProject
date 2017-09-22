package com.bridgeit.TodoApp.DAO;

import com.bridgeit.TodoApp.DTO.Tokens;

public interface RedisInterface {
	
	void saveTokens(Tokens person);

	Tokens findToken(String acc);
	
}

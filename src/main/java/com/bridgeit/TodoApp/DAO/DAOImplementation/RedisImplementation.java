package com.bridgeit.TodoApp.DAO.DAOImplementation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bridgeit.TodoApp.DAO.RedisInterface;
import com.bridgeit.TodoApp.DTO.Tokens;

@Repository
public class RedisImplementation implements RedisInterface {

	@Autowired
	Tokens tokens;

	private static final String KEY = "Tokens";
	private RedisTemplate<String, Tokens> redisTemplate;
	private HashOperations hashOps;

	// it is simply an constructor of this class, here we has taken an
	// @autowired for taking instance of RedisTemplate
	@Autowired
	private RedisImplementation(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOps = redisTemplate.opsForHash();
	}

	@Override
	public void saveTokens(Tokens tokens) {
		System.out.println("inside save token of redis .........." + tokens);
		hashOps.put(KEY, tokens.getAccessToken(), tokens);
	}

	public Tokens findToken(String acc) {
		return (Tokens) hashOps.get(KEY, acc);
	}

}

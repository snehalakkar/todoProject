package com.bridgeit.TodoApp.json;

import com.bridgeit.TodoApp.DTO.Tokens;

public class TokenResponse extends Response{

	private Tokens tokens;

	public Tokens getTokens() {
		return tokens;
	}

	public void setTokens(Tokens tokens) {
		this.tokens = tokens;
	}
}

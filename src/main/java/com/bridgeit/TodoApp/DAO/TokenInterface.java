package com.bridgeit.TodoApp.DAO;

import com.bridgeit.TodoApp.DTO.Tokens;

public interface TokenInterface {

	void saveToken(Tokens tokens);
	
	Tokens getTokenbyAccessToken(String access);

	int getuserIdfromAcctoken(String accesstoken);

	boolean logoutUser(String accessTokentodelete);

	Tokens getTokenbyRefreshToken(String refreshToken);

	void updateaccesstoken(Tokens tokens2);

	void deletetokenbyRefresh(String refreshToken);

}

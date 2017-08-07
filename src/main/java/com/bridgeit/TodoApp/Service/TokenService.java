package com.bridgeit.TodoApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.TodoApp.DAO.TokenInterface;
import com.bridgeit.TodoApp.DTO.Tokens;

@Service
public class TokenService {

	Tokens tokens = new Tokens();
	
	@Autowired
	TokenInterface tokenDaoImplementation;

	@Transactional
	public void saveToken(Tokens tokens) {
		tokenDaoImplementation.saveToken(tokens);
	}
	
	@Transactional
	public 	Tokens getTokenbyAccessToken(String access){
		return tokenDaoImplementation.getTokenbyAccessToken(access);
	}

	@Transactional
	public int getuserIdfromAcctoken(String accesstoken) {
		return tokenDaoImplementation.getuserIdfromAcctoken(accesstoken);
	}

	@Transactional
	public boolean logoutUser(String accessTokentodelete) {
		return tokenDaoImplementation.logoutUser(accessTokentodelete);
		
	}

	@Transactional     
	public Tokens getTokenbyRefreshToken(String refreshToken) {
		return tokenDaoImplementation.getTokenbyRefreshToken(refreshToken);
	}

	@Transactional
	public void updateaccesstoken(Tokens tokens2) {
		tokenDaoImplementation.updateaccesstoken(tokens2);		
	}
	
	@Transactional
	public void deletetokenbyRefresh(String refreshToken) {
		 tokenDaoImplementation.deletetokenbyRefresh(refreshToken);
	}
}

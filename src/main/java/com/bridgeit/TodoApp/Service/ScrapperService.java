package com.bridgeit.TodoApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.TodoApp.DAO.ScrapperInterface;
import com.bridgeit.TodoApp.DTO.WebScrapper;

@Service
public class ScrapperService {
	@Autowired
	ScrapperInterface scrapperImplementation;
	
	@Transactional
	public void saveScrapper(WebScrapper webScrapper) {
		scrapperImplementation.saveScrapper(webScrapper);	
	}
}

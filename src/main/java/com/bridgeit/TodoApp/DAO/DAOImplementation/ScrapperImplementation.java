package com.bridgeit.TodoApp.DAO.DAOImplementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.bridgeit.TodoApp.DAO.ScrapperInterface;
import com.bridgeit.TodoApp.DTO.WebScrapper;

@Repository
public class ScrapperImplementation implements ScrapperInterface{
	
	@Autowired
	@Qualifier("hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void saveScrapper(WebScrapper webScrapper) {
		System.out.println("hi");
		Session session = sessionFactory.getCurrentSession();
		session.save(webScrapper);
		
	}
}

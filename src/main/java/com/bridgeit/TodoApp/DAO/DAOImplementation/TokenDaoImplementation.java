package com.bridgeit.TodoApp.DAO.DAOImplementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.bridgeit.TodoApp.DAO.TokenInterface;
import com.bridgeit.TodoApp.DTO.Tokens;

@Repository
public class TokenDaoImplementation implements TokenInterface {

	@Autowired
	@Qualifier("hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void saveToken(Tokens tokens) {
		Session session = sessionFactory.getCurrentSession();

		session.save(tokens); 
	}

	public Tokens getTokenbyAccessToken(String access) {
		Session session = sessionFactory.getCurrentSession();

		System.out.println("Access token of current user " + access);

		Query query = session.createQuery("from Tokens where accessToken = :access");
		query.setParameter("access", access);
		System.out.println((Tokens) query.uniqueResult());
		Tokens tokens = (Tokens) query.uniqueResult();
		return tokens;
	}

	

	public Tokens getTokenbyRefreshToken(String refreshToken) {
		Session session = sessionFactory.getCurrentSession();

		System.out.println("refreshToken of current user " + refreshToken);

		Query query = session.createQuery("from Tokens where refreshToken = :refresh");
		query.setParameter("refresh", refreshToken);
		System.out.println((Tokens) query.uniqueResult());
		Tokens tokens = (Tokens) query.uniqueResult();
		return tokens;
	}
	
	
	public int getuserIdfromAcctoken(String accesstoken) {
		Session session = sessionFactory.getCurrentSession();

		try {
			Criteria crit = session.createCriteria(Tokens.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("accessToken"));

			crit.setProjection(projList);

			List results = crit.list();
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("catch of saveTokens...");
		}
		return 0;
	}
	
	
	public boolean logoutUser(String accessTokentodelete) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("Access token of current user which token obj is to be delete " + accessTokentodelete);

		Query query = session.createQuery("delete from Tokens where accessToken = :access");
		query.setParameter("access", accessTokentodelete);
		int rowsaffected = query.executeUpdate();

		if (rowsaffected > 0) {
			return true;
		}
		return false;
	}

	public void updateaccesstoken(Tokens accesstoupdate) {
		Session session = sessionFactory.getCurrentSession();
		session.update(accesstoupdate);
	}

	public void deletetokenbyRefresh(String refreshToken) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("refresh token of current user which token obj is to be delete " + refreshToken);

		Query query = session.createQuery("delete from Tokens where refreshToken = :refresh");
		query.setParameter("refresh", refreshToken);
		int rowsaffected = query.executeUpdate();
	}
}

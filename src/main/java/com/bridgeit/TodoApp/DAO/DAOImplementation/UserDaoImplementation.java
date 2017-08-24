package com.bridgeit.TodoApp.DAO.DAOImplementation;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bridgeit.TodoApp.DAO.UserDaoInterface;
import com.bridgeit.TodoApp.DTO.User;

@Repository
public class UserDaoImplementation implements UserDaoInterface {

	@Autowired
	@Qualifier("hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void registerUser(User user) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.saveOrUpdate(user);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("catch of registerUser...");
		}
	}

	public User updateUser(int userId, User user) {
		Session session = sessionFactory.getCurrentSession();

		return user;
	}

	public int deleteUser(int userId, User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}

	public User userLogin(String email, String password) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("email :" + email + " password :" + password);
		Criteria criteria = session.createCriteria(User.class);
		User user = (User) criteria.add(Restrictions.conjunction().add(Restrictions.eq("email", email)))
				.add(Restrictions.eq("password", password)).uniqueResult();

		if (user != null) {
			System.out.println(user + "valid user details");
			return user;
		}

		System.out.println("user is not present");
		return user;
	}
	
	public User getUserByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		User user = (User) criteria.uniqueResult();
		System.out.println("user of specified email " + user);
		return user;
	}

}

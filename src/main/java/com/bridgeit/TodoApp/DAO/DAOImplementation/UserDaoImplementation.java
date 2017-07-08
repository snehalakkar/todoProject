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

	@Override
	public void registerUser(User user) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.saveOrUpdate(user);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("catch of registerUser...");
		}
	}

	@Override
	public User updateUser(int userId, User user) {
		Session session = sessionFactory.getCurrentSession();

		return user;
	}

	@Override
	public int deleteUser(int userId, User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;

	}

	@Override
	public boolean userLogin(String email, String password) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("email :" + email + " password :" + password);
		Criteria criteria = session.createCriteria(User.class);
		User user = (User) criteria.add(Restrictions.conjunction().add(Restrictions.eq("email", email)))
				.add(Restrictions.eq("password", password)).uniqueResult();

		if (user != null) {
			System.out.println(user + "valid user");
			return true;
		}

		System.out.println("user is not present");
		return false;
	}

}

package com.bridgeit.TodoApp.DAO.DAOImplementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bridgeit.TodoApp.DAO.TodoDaoInterface;
import com.bridgeit.TodoApp.DTO.TodoTask;

@Repository
public class TodoDaoImplementation implements TodoDaoInterface {
	@Autowired
	@Qualifier("hibernate4AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	public void saveTodo(TodoTask todoTask) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.saveOrUpdate(todoTask);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("catch of registerUser...");
		}
	}

	public void updateTodo(TodoTask todoTask) {
		Session session = sessionFactory.getCurrentSession();
		
		try {
			session.update(todoTask);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteTodo(int todoId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TodoTask.class);

		TodoTask todoTask = (TodoTask) criteria.add(Restrictions.eq("todoId", todoId)).uniqueResult();
		System.out.println("tododel obj :" + todoTask);
		try {
			session.delete(todoTask);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TodoTask getTodoTaskById(int todoId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TodoTask.class);

		TodoTask todoTask = (TodoTask) criteria.add(Restrictions.eq("todoId", todoId)).uniqueResult();
		return todoTask;
	}

	public List<TodoTask> getAllTodoTask(int userid) {
		Session session = sessionFactory.getCurrentSession();
		Query query=session.createQuery("from TodoTask where userId= "+userid);
		List<TodoTask> list=query.list();
		return list;
	}


}
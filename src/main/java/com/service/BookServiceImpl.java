package com.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Book;

@Repository
public class BookServiceImpl implements BookService
{
	
	private SessionFactory sessionFactory;
	
	//Create session
	private Session session;
	
	
	@Autowired
	public BookServiceImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
		try
		{
			session=sessionFactory.getCurrentSession();
		}
		catch(HibernateException e)
		{
			session=sessionFactory.openSession();
		}
		
	}

	@Override
	@Transactional
	public List<Book> findAll() {
		Transaction tx=session.beginTransaction();
		List<Book> books=session.createQuery("from Book").list();
		tx.commit();
		return books;
	}

	@Override
	@Transactional
	public Book findById(int id) {
		Book book=new Book();
		Transaction tx=session.beginTransaction();
		 book=session.get(Book.class,id);
		tx.commit();
		return book;
	}

	@Override
	@Transactional
	public void save(Book theBook) {
		
		Transaction tx=session.beginTransaction();
		 session.saveOrUpdate(theBook);
		tx.commit();
		
	}

	@Override
	@Transactional
	public void deleteById(int theId) {
		Transaction tx=session.beginTransaction();
		Book book=session.get(Book.class, theId);
		 session.delete(book);
		tx.commit();
		
	}

	@Override
	@Transactional
	public List<Book> searchBy(String Name, String Author) {
		Transaction tx=session.beginTransaction();
		String query="";
		
		if (Name.length() != 0 && Author.length() != 0)
			query = "from Book where name like '%" + Name + "%' or author like '%" + Author + "%'";
		else if (Name.length() != 0)
			query = "from Book where name like '%" + Name + "%'";
		else if (Author.length() != 0)
			query = "from Book where author like '%" + Author + "%'";
		else
			System.out.println("Cannot search without input data");

		List<Book> books = session.createQuery(query).list();

		tx.commit();

		return books;
		
	
	}

}
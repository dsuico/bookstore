package com.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;

@Service
public class BookServiceImpl implements BookService {
	
	private static final Logger LOG = LoggerFactory.getLogger(BookService.class);
	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> findAll() {
		List<Book> bookList = (List<Book>) bookRepository.findAll();
		
		List<Book> activeBookList = new ArrayList<>();
		
		for(Book book: bookList) {
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		
		return activeBookList;
	}
	
	public Book findById(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
	}
	
	@Override
	public List<Book> findByCategory(String category) {
		List<Book> bookList = bookRepository.findByCategory(category);
		List<Book> activeBookList = new ArrayList<>();
		
		for(Book book: bookList) {
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		
		return activeBookList;
	}
	
	public List<Book> blurrySearch(String title) {
		LOG.info("keyword: {}", title);
		
		List<Book> bookList = bookRepository.findByTitleLike("%"+title+"%");
		List<Book> activeBookList = new ArrayList<>();
			

		for(Book book: bookList) {
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		
		return activeBookList;
	}
	
}

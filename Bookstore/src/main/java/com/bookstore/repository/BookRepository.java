package com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bookstore.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	Optional<Book> findById(Long id);
	
	List<Book> findByCategory(String category);
	
	List<Book> findByTitleLike(@Param("title") String title);
}

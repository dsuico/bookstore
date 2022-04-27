package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	Optional<Book> findById(Long id);
}

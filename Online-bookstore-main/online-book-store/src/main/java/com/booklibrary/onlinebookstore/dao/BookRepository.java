package com.booklibrary.onlinebookstore.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.booklibrary.onlinebookstore.entity.Book;

@SuppressWarnings("unused")
@RepositoryRestResource
public interface BookRepository extends JpaRepository<Book, Integer> {

	
	@Query("from Book where lower(name) like :search")
	List<Book> searchBooks(@Param("search") String search);

}

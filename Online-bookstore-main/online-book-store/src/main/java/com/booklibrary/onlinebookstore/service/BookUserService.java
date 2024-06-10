package com.booklibrary.onlinebookstore.service;

import java.util.Set;

import com.booklibrary.onlinebookstore.entity.Book;
import com.booklibrary.onlinebookstore.entity.Customer;

public interface BookUserService {

	Set<Book> getBooksPurchasedBy(Customer customer);

}

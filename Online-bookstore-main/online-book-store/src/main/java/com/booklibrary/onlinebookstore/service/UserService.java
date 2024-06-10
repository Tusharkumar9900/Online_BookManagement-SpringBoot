package com.booklibrary.onlinebookstore.service;

import java.util.List;

import com.booklibrary.onlinebookstore.entity.User;

public interface UserService {

	public List<User> getAllUsers();
	
	public User getUserByUsername(String username);
}

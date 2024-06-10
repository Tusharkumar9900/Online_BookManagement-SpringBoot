package com.booklibrary.onlinebookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booklibrary.onlinebookstore.entity.Customer;
import com.booklibrary.onlinebookstore.forms.entity.ChangePassword;
import com.booklibrary.onlinebookstore.forms.entity.CustomerData;
import com.booklibrary.onlinebookstore.entity.User;

@SuppressWarnings("unused")
public interface CustomerService {

	public List<Customer> getAllCustomers();
	
	public Customer getCustomer(String username);
		
	public String saveCustomer(Customer theCustomer);
	
	public String updateCustomer(Customer theCustomer);
	
	public String removeCustomer(Customer theCustomer);
	
	public String registerCustomer(CustomerData data);

	public CustomerData getCustomerData(String username);

	public String updateCustomer(CustomerData customerData);
	
	public String updatePassword(ChangePassword changePassword);
	
}

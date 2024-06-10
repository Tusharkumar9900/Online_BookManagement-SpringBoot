package com.booklibrary.onlinebookstore.service;

import java.util.Set;

import com.booklibrary.onlinebookstore.entity.Customer;
import com.booklibrary.onlinebookstore.entity.PurchaseDetail;
import com.booklibrary.onlinebookstore.entity.PurchaseHistory;

public interface PaymentService {

	String createTransaction(Customer customer);
	
	Set<PurchaseHistory> getPurchaseHistories(Customer customer);
	
	Set<PurchaseDetail> getPurchaseDetails(PurchaseHistory purchaseHistory);

	PurchaseHistory getPurchaseHistory(Customer customer, String transId);
}

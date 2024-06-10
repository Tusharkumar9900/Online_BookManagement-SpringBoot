package com.booklibrary.onlinebookstore.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booklibrary.onlinebookstore.dao.BookUserRepository;
import com.booklibrary.onlinebookstore.dao.CustomerRepository;
import com.booklibrary.onlinebookstore.dao.PurchaseDetailRepository;
import com.booklibrary.onlinebookstore.dao.PurchaseHistoryRepository;
import com.booklibrary.onlinebookstore.entity.Book;
import com.booklibrary.onlinebookstore.entity.BookUser;
import com.booklibrary.onlinebookstore.entity.BookUserId;
import com.booklibrary.onlinebookstore.entity.Customer;
import com.booklibrary.onlinebookstore.entity.PurchaseDetail;
import com.booklibrary.onlinebookstore.entity.PurchaseDetailId;
import com.booklibrary.onlinebookstore.entity.PurchaseHistory;
import com.booklibrary.onlinebookstore.entity.ShoppingCart;
import com.booklibrary.onlinebookstore.utility.IDUtil;

@SuppressWarnings("unused")
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	CustomerRepository customerRepos;
	
	@Autowired
	PurchaseHistoryRepository purchaseHistoryRepos;
	
	@Autowired
	PurchaseDetailRepository purchaseDetailRepos;
	
	@Autowired
	BookUserRepository bookUserRepos;
	
	@Override
	@Transactional
	public String createTransaction(Customer customer) {
		//get the customer cart items and add it to his buys history;
		Set<ShoppingCart> items = customer.getShoppingCart();
		
		//create a transaction purchase history
		PurchaseHistory purchaseHistory = new PurchaseHistory(IDUtil.generatePurchaseHistoryId(),new Date());
		purchaseHistory.setCustomer(customer);
		
		Set<PurchaseDetail> purchaseDetails = null;
		Set<Book> books = new HashSet<Book>();
		for(ShoppingCart item : items) {
			double price = item.getBook().getPrice();
			int available = item.getBook().getQuantity();
			int toBuy = item.getQuantity();
			if(available < toBuy)
				return "Book named: "+item.getBook().getName() + " is out of stock!";
			available = available - toBuy;
			PurchaseDetail purchaseDetail = new PurchaseDetail(purchaseHistory,item.getBook(),price,toBuy);
			if(purchaseDetails == null)
				purchaseDetails = new HashSet<PurchaseDetail>();
			purchaseDetails.add(purchaseDetail);
			item.getBook().setQuantity(available);
			books.add(item.getBook());
		}
		
		//now add the purchase details to purchase history
		purchaseHistory.setPurchaseDetails(purchaseDetails);
		//save the purchase History
		try {
			customer.addPurchaseHistories(purchaseHistory);
			customer.getShoppingCart().clear();
			customerRepos.save(customer);
			//add the books to the customers service
			for(Book item: books) {
				BookUserId theId = new BookUserId(item, customer);
				Optional<BookUser> theOpt = bookUserRepos.findById(theId);
				if(!theOpt.isPresent()) {
					int y = bookUserRepos.addBookToUser(theId.getBook().getId(), theId.getCustomer().getUsername());
					if(y <=0) 
						throw new RuntimeException("Book and user relation not established!");
				}
			}
		}
		catch(Exception ex) {
			customer.getPurchaseHistories().remove(purchaseHistory);
			customer.setShoppingCart(items);
			ex.printStackTrace();
		}
		return purchaseHistory.getId();
	}

	@Override
	@Transactional
	public Set<PurchaseHistory> getPurchaseHistories(Customer customer) {
		Set<PurchaseHistory> histories = new HashSet<PurchaseHistory>();
		histories.addAll(purchaseHistoryRepos.findAllByCustomer(customer));
		customer.setPurchaseHistories(histories);
		return histories;
	}

	@Override
	@Transactional
	public Set<PurchaseDetail> getPurchaseDetails(PurchaseHistory purchaseHistory) {
		Set<PurchaseDetail> details = new HashSet<PurchaseDetail>();
		details.addAll(purchaseDetailRepos.findAllByHistory(purchaseHistory));
		return details;
	}

	@Override
	@Transactional
	public PurchaseHistory getPurchaseHistory(Customer customer, String transId) {
		PurchaseHistory purchaseHistory = null;
		Optional<PurchaseHistory> history = purchaseHistoryRepos.findById(transId);
		if(history.isPresent()) {
			purchaseHistory = history.get();
			if(!purchaseHistory.getCustomer().getUsername().equals(customer.getUsername()))
				purchaseHistory = null;
		}
		return purchaseHistory;
	}

}

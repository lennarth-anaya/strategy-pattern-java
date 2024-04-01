package org.example.patterns.strategy.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.db.InMemoryDB;
import org.example.patterns.strategy.model.Customer;
import org.springframework.stereotype.Service;

import lombok.Data;


@Service
@RequiredArgsConstructor
public class SignUpCustomerService {
	private final InMemoryDB db;
	
	public boolean signUp(Customer customer) {
		return db.registerCustomer(customer);
	}
}

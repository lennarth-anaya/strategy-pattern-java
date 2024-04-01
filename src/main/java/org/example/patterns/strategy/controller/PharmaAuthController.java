package org.example.patterns.strategy.controller;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.model.Customer;
import org.example.patterns.strategy.model.Physiscian;
import org.example.patterns.strategy.service.auth.PhysiscianRegistrar;
import org.example.patterns.strategy.service.auth.SignUpCustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController("/auth")
@RequiredArgsConstructor
public class PharmaAuthController {
	private final SignUpCustomerService customerService;
	private final PhysiscianRegistrar physiscianService;
	
	@PostMapping("/customer")
	public boolean customerSignUp(Customer customer) {
		return customerService.signUp(customer);
	}
	
	@PostMapping("/physiscian")
	public boolean registerPhysiscian(Physiscian physiscian) {
		return physiscianService.registerPhysiscian(physiscian);
	}
}

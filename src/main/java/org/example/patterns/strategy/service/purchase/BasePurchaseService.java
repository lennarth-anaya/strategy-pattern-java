package org.example.patterns.strategy.service.purchase;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.db.InMemoryDB;
import org.springframework.stereotype.Service;

import lombok.Data;

/**
 * Contains the steps that are common to most purchase flows,
 * so each flow doesn't have to re-implement them.
 */
@RequiredArgsConstructor
@Service
public class BasePurchaseService {
	private final InMemoryDB dbService;

	public PharmaPurchaseResponse initializePurchaseResponse(PharmaPurchaseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkDrugExistence(String drugCode, Integer amount, PharmaPurchaseResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void verifyCustomerAddressAccessibility(String deliveryAddress, PharmaPurchaseResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void reserveDrugInventory(String drugCode, Integer amount, PharmaPurchaseResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void billPurchase(String creditCard, PharmaPurchaseResponse response) {
		// TODO Auto-generated method stub
		
	}

	public void confirmDrugInventoryPurchase(String drugCode, Integer amount) {
		// TODO Auto-generated method stub
		
	}

	public void releaseDrugInventory(String drugCode, Integer amount, PharmaPurchaseResponse response) {
		// TODO Auto-generated method stub
		
	}
}

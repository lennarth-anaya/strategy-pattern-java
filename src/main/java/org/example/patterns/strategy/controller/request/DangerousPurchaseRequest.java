package org.example.patterns.strategy.controller.request;

import org.example.patterns.strategy.model.Customer;

import lombok.Data;

@Data
public class DangerousPurchaseRequest
	extends UnprescriptedPurchaseRequest
{
	private byte[] customerIdImage;
	private Customer customerData;
}

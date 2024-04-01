package org.example.patterns.strategy.controller.request;

import lombok.Data;

@Data
public class PharmaPurchaseRequest {
	private String drugCode;
	private Integer amount;
	private String deliveryAddress;
	private String creditCard;
}

package org.example.patterns.strategy.controller.request;

import org.example.patterns.strategy.model.Physiscian;

import lombok.Data;

@Data
public class PrescriptedPurchaseRequest
	extends PharmaPurchaseRequest
{
	private byte[] prescription;
	private Physiscian physiscian;
}

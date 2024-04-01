package org.example.patterns.strategy.service.purchase;

import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;

public interface PharmaPurchaseService {
	PharmaPurchaseResponse buyDrug(PharmaPurchaseRequest request);
}

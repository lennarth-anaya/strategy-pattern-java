package org.example.patterns.strategy.controller;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.controller.request.DangerousPurchaseRequest;
import org.example.patterns.strategy.controller.request.PrescriptedPurchaseRequest;
import org.example.patterns.strategy.controller.request.UnprescriptedPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.service.purchase.PharmaPurchaseService;
import org.example.patterns.strategy.service.purchase.PurchaseServiceStrategyFinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/purchase")
@RequiredArgsConstructor
public class PharmaPurchaseController {
	private final PurchaseServiceStrategyFinder strategyFinder;
	
	@PostMapping("/unprescripted")
	public PharmaPurchaseResponse buyUnprescripted(UnprescriptedPurchaseRequest request) {
		PharmaPurchaseService service = strategyFinder.findStrategy(request);
		return service.buyDrug(request);
	}
	
	@PostMapping("/prescripted")
	public PharmaPurchaseResponse buyPrescripted(PrescriptedPurchaseRequest request) {
		PharmaPurchaseService service = strategyFinder.findStrategy(request);
		return service.buyDrug(request);
	}
	
	@PostMapping("/controlled")
	public PharmaPurchaseResponse buyDangerous(DangerousPurchaseRequest request) {
		PharmaPurchaseService service = strategyFinder.findStrategy(request);
		return service.buyDrug(request);
	}
}

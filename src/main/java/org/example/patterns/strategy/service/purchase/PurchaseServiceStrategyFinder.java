package org.example.patterns.strategy.service.purchase;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.controller.request.DangerousPurchaseRequest;
import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.request.PrescriptedPurchaseRequest;
import org.example.patterns.strategy.controller.request.UnprescriptedPurchaseRequest;
import org.example.patterns.strategy.service.purchase.flow.DangerousPurchaseService;
import org.example.patterns.strategy.service.purchase.flow.PrescriptedPurchaseService;
import org.example.patterns.strategy.service.purchase.flow.UnprescriptedPurchaseService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseServiceStrategyFinder {
	private final UnprescriptedPurchaseService unprescriptedService;
	private final PrescriptedPurchaseService prescriptedService;
	private final DangerousPurchaseService dangerousService;
	
	/** this is the Strategy pattern key and the only if-else (switch) you'd find
	 * to distinguish the flow
	 */
	public PharmaPurchaseService findStrategy(PharmaPurchaseRequest request) {
		return switch(request) {
			case DangerousPurchaseRequest r -> dangerousService;
			case UnprescriptedPurchaseRequest r -> unprescriptedService;
			case PrescriptedPurchaseRequest r -> prescriptedService;
			default -> throw new RuntimeException("Invalid strategy");
		};
	}
}

package org.example.patterns.strategy.service.purchase.flow;

import static org.example.patterns.strategy.model.DrugCriticality.NO_PRESCRIPTION_NEEDED;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.db.InMemoryDB;
import org.example.patterns.strategy.service.purchase.BasePurchaseService;
import org.example.patterns.strategy.service.purchase.PharmaPurchaseService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnprescriptedPurchaseService
		implements PharmaPurchaseService
{
	private final BasePurchaseService baseFlow;
	private final InMemoryDB dbService;

	@Override
	public PharmaPurchaseResponse buyDrug(PharmaPurchaseRequest request) {
		final PharmaPurchaseResponse response = baseFlow.initializePurchaseResponse(request);

		baseFlow.checkDrugExistence(request.getDrugCode(), request.getAmount(),
				response);

		// this step is specific to this flow:
		checkDrugDoesNotRequirePrescription(request.getDrugCode(),
				response);

		baseFlow.verifyCustomerAddressAccessibility(request.getDeliveryAddress(),
				response);

		// the time it took to 2 steps above might still imply no further availability despite step 1
		baseFlow.reserveDrugInventory(request.getDrugCode(), request.getAmount(),
				response);

		// since we have already reserved the drug in our inventory, we handle exceptions
		try {
			baseFlow.billPurchase(request.getCreditCard(),
					response);
			baseFlow.confirmDrugInventoryPurchase(request.getDrugCode(), request.getAmount());
		} catch(Exception e) {
			baseFlow.releaseDrugInventory(request.getDrugCode(), request.getAmount(),
					response);
		}

		return response;
	}

	private void checkDrugDoesNotRequirePrescription(String drugCode,
													 PharmaPurchaseResponse response) {
		if (NO_PRESCRIPTION_NEEDED != dbService.findDrugCriticality(drugCode)) {
			throw new RuntimeException("Drug requires prescription");
		}
	}

}


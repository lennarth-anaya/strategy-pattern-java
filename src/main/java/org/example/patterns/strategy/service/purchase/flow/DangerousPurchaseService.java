package org.example.patterns.strategy.service.purchase.flow;

import org.example.patterns.strategy.controller.request.DangerousPurchaseRequest;
import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.request.PrescriptedPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.db.InMemoryDB;
import org.example.patterns.strategy.model.Customer;
import org.example.patterns.strategy.model.Physiscian;
import org.example.patterns.strategy.service.purchase.BasePurchaseService;
import org.example.patterns.strategy.service.purchase.PharmaPurchaseService;
import org.springframework.stereotype.Service;

/**
 * This class makes great use of PrescriptedPurchaseServcie,
 * hence extending it instead of TemplatePurchaseService
 */
@Service("dangerousService")
public class DangerousPurchaseService
	extends PrescriptedPurchaseService
	implements PharmaPurchaseService
{
	private final InMemoryDB dbService;

	public DangerousPurchaseService(BasePurchaseService baseFlow, InMemoryDB dbService) {
		super(baseFlow, dbService);
		this.dbService = dbService;
	}

	/**
	 * Since we never know if in the future, a more complex
	 * flow could derivate from this one, let's implement it
	 * like a Template Method too.
	 */
	protected void afterPrescriptionChecks(PharmaPurchaseRequest request, PharmaPurchaseResponse response) {
		processCustomerId(request, response);
		processCustomerData(request, response);
		afterCriticalChecks(request, response);
	}

	private void afterCriticalChecks(PharmaPurchaseRequest request, PharmaPurchaseResponse response) {
		// nothing to be done in this class, maybe on its subclasses
	}

	private void processCustomerId(PharmaPurchaseRequest request, PharmaPurchaseResponse response) {
		if(! (request instanceof DangerousPurchaseRequest dangReq)) {
			throw new RuntimeException("Highly controlled drug requires additional data");
		}

		byte[] customerIdImage = dangReq.getCustomerIdImage();
		System.out.println("...using external service to run awesome validation over customer ID...");
	}

	private void processCustomerData(PharmaPurchaseRequest request, PharmaPurchaseResponse response) {
		if(! (request instanceof DangerousPurchaseRequest dangReq)) {
			throw new RuntimeException("Highly controlled drug requires additional data");
		}

		Customer customerData = dangReq.getCustomerData();
		String customerName = customerData.getCustomerFullName();

		if (dbService.isCustomerRegistered(customerData)) {
			System.out.println(STR."Customer \{ customerName } registered, proceeding to deliver drug.");
			return;
		}

		boolean registered = dbService.registerCustomer(customerData);
		if (!registered) {
			String error = STR."""
				Customer \{ customerName } couldn't be registered due
				to an internal error. Highly controlled drug can't be sold.
				""";
			System.out.println(error);
			throw new RuntimeException(error);
		} else {
			System.out.println(STR."""
				Customer \{ customerName } registered successfully!,
				proceeding to deliver drug.
				""");
		}
	}

	/**
	 * If we'd want to do things on top of the validation
	 * PrescriptedPurchseService is doing.
	 */
	protected void processPrescription(PharmaPurchaseRequest request) {
		if(! (request instanceof PrescriptedPurchaseRequest prescReq)) {
			throw new RuntimeException("Highly controlled drug requires additional data");
		}

		byte[] prescriptionImage = prescReq.getPrescription();
		System.out.println("""
      		DangerousPurchase is pre-processing
    		digitalized prescription with external service...
 			""");

		// base processing
		super.processPrescription(request);

		System.out.println("""
      		DangerousPurchase is post-processing
    		digitalized prescription with external service...
 			""");
	}
}

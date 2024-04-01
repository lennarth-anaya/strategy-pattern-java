package org.example.patterns.strategy.service.purchase.flow;

import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.request.PrescriptedPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.db.InMemoryDB;
import org.example.patterns.strategy.model.Physiscian;
import org.example.patterns.strategy.service.purchase.BasePurchaseService;
import org.example.patterns.strategy.service.purchase.PharmaPurchaseService;
import org.springframework.stereotype.Service;

import static org.example.patterns.strategy.model.DrugCriticality.PRESCRIPTION_NEEDED_AND_CONTROLLED;

/**
 * Our hypothetical development team realized, while
 * developing this flow, that they are mostly replicating
 * the same flow from UnprescriptedPurchaseService.
 *
 * And it will have to be replicated again for
 * DangerousPurchaseService, so they decided to
 * implement a Template Method pattern (https://www.gofpattern.com/behavioral/patterns/template-pattern.php).
 * UnprescriptedPurchaseService could have been included
 * in the template, but we leave it to demonstrate
 * such duplication.
 */
@Service("prescriptedService")
public class PrescriptedPurchaseService
		extends TemplatePurchaseService
		implements PharmaPurchaseService
{
	private final InMemoryDB dbService;

	public PrescriptedPurchaseService(
			BasePurchaseService baseFlow,
			InMemoryDB dbService
	) {
		super(baseFlow);
		this.dbService = dbService;
	}

	/**
	 * note this method can, in turn, be also a Template Method for
	 * DangerousPurchaseService
 	 */
	protected void flowSpecificChecks(PharmaPurchaseRequest request,
									  PharmaPurchaseResponse response) {
		checkDrugCriticality(request);
		processPrescription(request);
		processPhysiscian(request);
		// Template Method in case a subclass needs it, you might want to use AOP, for instance:
		afterPrescriptionChecks(request, response);
	}

	protected void afterPrescriptionChecks(PharmaPurchaseRequest request, PharmaPurchaseResponse response) {
		// nothing to be done in this class, maybe on its subclasses
	}

	protected void checkDrugCriticality(PharmaPurchaseRequest request) {
		final String drugCode = request.getDrugCode();
		if (PRESCRIPTION_NEEDED_AND_CONTROLLED == dbService.findDrugCriticality(drugCode)) {
			throw new RuntimeException("Controlled drug requires additional data");
		}
	}

	protected void processPrescription(PharmaPurchaseRequest request) {
		if(! (request instanceof PrescriptedPurchaseRequest prescReq)) {
			throw new RuntimeException("Controlled drug requires additional data");
		}

		byte[] prescriptionImage = prescReq.getPrescription();
		System.out.println("...using external service to run awesome validation over prescription...");
	}

	private void processPhysiscian(PharmaPurchaseRequest request) {
		if(! (request instanceof PrescriptedPurchaseRequest prescReq)) {
			throw new RuntimeException("Controlled drug requires additional data");
		}

		Physiscian physiscian = prescReq.getPhysiscian();
		String drName = physiscian.getDoctorFullName();

		if (dbService.isPhysiscianRegistered(physiscian)) {
			System.out.println(STR."Physiscian \{ drName } exists, proceeding to deliver drug.");
			return;
		}

		boolean registered = dbService.registerPhysiscian(physiscian);
		if (!registered) {
			String error = STR."""
				Physiscian \{ drName } couldn't be registered due
				to an internal error. Drug can't be sold.
				""";
			System.out.println(error);
			throw new RuntimeException(error);
		} else {
			System.out.println(STR."""
				Physiscian \{ drName } registered successfully!,
				proceeding to deliver drug.
				""");
		}
	}

}

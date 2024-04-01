package org.example.patterns.strategy.service.purchase.flow;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.controller.request.PharmaPurchaseRequest;
import org.example.patterns.strategy.controller.response.PharmaPurchaseResponse;
import org.example.patterns.strategy.service.purchase.BasePurchaseService;
import org.example.patterns.strategy.service.purchase.PharmaPurchaseService;

@RequiredArgsConstructor
public abstract class TemplatePurchaseService
        implements PharmaPurchaseService
{
    private final BasePurchaseService baseFlow;

    @Override
    public PharmaPurchaseResponse buyDrug(PharmaPurchaseRequest request) {
        final PharmaPurchaseResponse response = baseFlow.initializePurchaseResponse(request);

        checkDrugExistence(request.getDrugCode(), request.getAmount(),
                response);

        // this step is where flow run specific checks,
        // checkDrugDoesNotRequirePrescription() in the case of UnprescriptedPurchaseService
        // if that would have also implemented this Template Method pattern:
        flowSpecificChecks(request, response);

        verifyCustomerAddressAccessibility(request.getDeliveryAddress(),
                response);

        // the time it took to 2 steps above might still imply no further availability despite step 1
        reserveDrugInventory(request.getDrugCode(), request.getAmount(),
                response);

        // since we have already reserved the drug in our inventory, we handle exceptions
        try {
            billPurchase(request.getCreditCard(),
                    response);
            confirmDrugInventoryPurchase(request.getDrugCode(), request.getAmount());
        } catch(Exception e) {
            releaseDrugInventory(request.getDrugCode(), request.getAmount(),
                    response);
        }

        return response;
    }

    // this can be overridden with an empty method if subclass needs nothing else
    protected abstract void flowSpecificChecks(PharmaPurchaseRequest request,
                                               PharmaPurchaseResponse response);

    // methods in the template must be protected so they can be
    // overridden by subclasses:
    protected void checkDrugExistence(String drugCode, Integer amount, PharmaPurchaseResponse response) {
        // we could move the logic from baseFlow to this template,
        // but why break the "favor composition over inheritance"
        // unnecessarily even when Template Method is about inheritance?
        baseFlow.checkDrugExistence(drugCode, amount, response);
    }

    private void verifyCustomerAddressAccessibility(String deliveryAddress, PharmaPurchaseResponse response) {
        baseFlow.verifyCustomerAddressAccessibility(deliveryAddress, response);
    }

    private void reserveDrugInventory(String drugCode, Integer amount, PharmaPurchaseResponse response) {
        baseFlow.reserveDrugInventory(drugCode, amount, response);
    }

    private void billPurchase(String creditCard, PharmaPurchaseResponse response) {
        baseFlow.billPurchase(creditCard, response);
    }

    private void confirmDrugInventoryPurchase(String drugCode, Integer amount) {
        baseFlow.confirmDrugInventoryPurchase(drugCode, amount);
    }

    private void releaseDrugInventory(String drugCode, Integer amount, PharmaPurchaseResponse response) {
        baseFlow.releaseDrugInventory(drugCode, amount, response);
    }

}

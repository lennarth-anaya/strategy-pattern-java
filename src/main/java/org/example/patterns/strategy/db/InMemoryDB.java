package org.example.patterns.strategy.db;

import static org.example.patterns.strategy.model.DrugCriticality.NO_PRESCRIPTION_NEEDED;
import static org.example.patterns.strategy.model.DrugCriticality.PRESCRIPTION_NEEDED;
import static org.example.patterns.strategy.model.DrugCriticality.PRESCRIPTION_NEEDED_AND_CONTROLLED;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.example.patterns.strategy.model.Customer;
import org.example.patterns.strategy.model.Physiscian;
import org.springframework.stereotype.Service;

import org.example.patterns.strategy.model.Drug;
import org.example.patterns.strategy.model.DrugCriticality;
import org.example.patterns.strategy.model.DrugExistence;

/** DB/session management is not the target of this example,
  * let's keep it simple
  */
@Service
public class InMemoryDB {
	private HashSet<Customer> customers = new HashSet<>();
	private HashSet<Physiscian> physiscians = new HashSet<>();
	private ConcurrentHashMap<String, DrugExistence> drugsInventory = new ConcurrentHashMap<>();
	
	// mocks drug inventory
	public InMemoryDB() {
		initializeMockInventory(new ArrayList<DrugExistence>() {{
			this.add(new DrugExistence(new Drug("drug-A",
					NO_PRESCRIPTION_NEEDED), 5));
			this.add(new DrugExistence(new Drug("drug-B",
					PRESCRIPTION_NEEDED), 8));
			this.add(new DrugExistence(new Drug("drug-X",
					PRESCRIPTION_NEEDED_AND_CONTROLLED), 4));
		}});
	}
	
	public DrugCriticality findDrugCriticality(String drugCode) {
		return ofNullable(drugsInventory.get(drugCode))
			.map(d -> d.drug().drugCriticality())
			.orElseThrow(() -> new RuntimeException(STR."drugCode '{ drugCode } doesn't exist"));
	}
	
	public boolean registerPhysiscian(Physiscian physiscian) {
		return physiscians.add(physiscian);
	}

	public boolean isPhysiscianRegistered(Physiscian physiscian) {
		return physiscians.contains(physiscian);
	}

	public Integer drugInExistence(String drugCode) {
		return ofNullable(drugsInventory.get(drugCode))
			.map(DrugExistence::existenceAmount)
			.orElse(0);
	}
	
	public boolean isDrugInExistence(String drugCode, int amount) {
		return drugInExistence(drugCode) >= amount;
	}
	
	public Integer discountDrugExistence(String drugCode, Integer amount) {
		DrugExistence de = drugsInventory.get(drugCode);
		if (de == null) {
			throw new RuntimeException(STR
					."drugCode '\{ drugCode }' not in existence");
		}
		
		Integer newExistence = de.existenceAmount() - amount;
		if (newExistence < 0) {
			throw new RuntimeException(STR
					."not enough drugCode '\{ drugCode }' in existence");
		}
		
		drugsInventory.put(drugCode, new DrugExistence(de.drug(),
				newExistence));

		return newExistence;
	}
	
	public boolean registerCustomer(Customer customer) {
		return customers.add(customer);
	}

	public boolean isCustomerRegistered(Customer customer) {
		return customers.contains(customer);
	}

	public void initializeMockInventory(List<DrugExistence> drugsInventoryList) {
		drugsInventoryList.stream().collect(Collectors.toMap(
				e-> e.drug().drugCode(),
				e -> e));
	}

}

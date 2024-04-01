package org.example.patterns.strategy.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.patterns.strategy.db.InMemoryDB;
import org.example.patterns.strategy.model.Physiscian;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhysiscianRegistrar {
	private final InMemoryDB db;
	
	public boolean registerPhysiscian(Physiscian physiscian) {
		return db.registerPhysiscian(physiscian);
	}
}

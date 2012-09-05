package org.openmrs.module.referralalerts;

import org.openmrs.Encounter;
import org.openmrs.Obs;

public enum Urgency {
	
	IMMEDIATE	(0),
	URGENT		(3),
	NON_URGENT	(10);
	
	private int daysAgo;
	
	Urgency(int daysAgo) {
		this.daysAgo = daysAgo;
	}
	
	public int getDaysAgo() {
		return daysAgo;
	}
	
	public static Urgency getUrgencyForEncounter(Encounter enc) {
		for (Obs obs : enc.getAllObs()) {
			if (obs.getConcept().getConceptId().equals(8517)) {
				switch (obs.getValueCoded().getConceptId()) {
					case 8515: return URGENT;
					case 8516: return NON_URGENT;
					case 8612: return IMMEDIATE;
				}
			}
		}
		
		return null;
	}
}

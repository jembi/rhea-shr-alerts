package org.openmrs.module.referralalerts;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
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
			if ("8517".equals(RWCSCode(obs.getConcept()))) {
				String code = RWCSCode(obs.getValueCoded());
				if ("8515".equals(code)) return URGENT;
				if ("8516".equals(code)) return NON_URGENT;
				if ("8612".equals(code)) return IMMEDIATE;
			}
		}
		
		return null;
	}
	
	private static String RWCSCode(Concept c) {
		for (ConceptMap map : c.getConceptMappings()) {
			if ("RWCS".equalsIgnoreCase(map.getSource().getName()))
				return map.getSourceCode().trim();
		}
		return null;
	}
}

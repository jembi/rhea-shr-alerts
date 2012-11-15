/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
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

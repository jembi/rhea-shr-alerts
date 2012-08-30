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
package org.openmrs.module.referralalerts.advice;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.referralalerts.ReferralAlertsService;
import org.openmrs.module.referralalerts.Urgency;
import org.springframework.aop.AfterReturningAdvice;

/**
 *
 */
public class ReferralEncounterAdvice implements AfterReturningAdvice {
	
	public static final Log log = LogFactory.getLog(ReferralEncounterAdvice.class);

	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (method.getName().contains("saveEncounter")) {
			log.info("In referralalerts encounter advice.");
			System.out.println("In referralalerts encounter advice.");
			
			Encounter enc = (Encounter)returnValue;
			EncounterType referralEncounterType = Context.getEncounterService().getEncounterType("ANC Referral");
			ReferralAlertsService ras = Context.getService(ReferralAlertsService.class);
			
			if (enc.getEncounterType().equals(referralEncounterType)) {
				log.info("Processing referral encounter for patientId=" + enc.getPatientId());
				System.out.println("Processing referral encounter for patientId=" + enc.getPatientId());
				if (Urgency.getUrgencyForEncounter(enc).equals(Urgency.IMMEDIATE))
					ras.sendAlertMessageToRapidSMS(enc);
			}
		}
	}
}

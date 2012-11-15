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
package org.openmrs.module.referralalerts.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jembi.rhea.module.rheashradapter.util.GenerateORU_R01Alert;
import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.referralalerts.ReferralAlertsService;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.parser.GenericParser;

public class ReferralAlertsServiceImpl extends BaseOpenmrsService implements ReferralAlertsService {

	protected static final Log log = LogFactory.getLog(ReferralAlertsServiceImpl.class);

		
		@Override
		public void sendAlertMessageToRapidSMS(Encounter e) {
			GenerateORU_R01Alert alert = new GenerateORU_R01Alert(); 
			try {
				
				ORU_R01 r01 = alert.generateORU_R01Message(e);
				
				GenericParser parser = new GenericParser();
				String msg = null;
				try {
					msg = parser.encode(r01,"XML");
					log.info("Reminder message is : " + msg);
				}
				catch (HL7Exception ex) {
					log.error("Exception parsing constructed message.");
				}
				alert.sendRequest(msg, e);			
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}

}

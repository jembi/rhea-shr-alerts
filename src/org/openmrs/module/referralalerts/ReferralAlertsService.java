package org.openmrs.module.referralalerts;

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;

public interface ReferralAlertsService extends OpenmrsService {
	
	public void sendAlertMessageToRapidSMS(Patient p);
	
}

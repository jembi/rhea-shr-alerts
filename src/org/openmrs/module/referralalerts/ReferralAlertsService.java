package org.openmrs.module.referralalerts;

import org.openmrs.Encounter;
import org.openmrs.api.OpenmrsService;

public interface ReferralAlertsService extends OpenmrsService {
	
	public void sendAlertMessageToRapidSMS(Encounter e);
	
}

package org.openmrs.module.referralalerts.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.referralalerts.ReferralAlertsService;

public class ReferralAlertsServiceImpl extends BaseOpenmrsService implements ReferralAlertsService {

	protected static final Log log = LogFactory.getLog(ReferralAlertsServiceImpl.class);



	@Override
	public void sendAlertMessageToRapidSMS(Patient p) {
		// TODO Auto-generated method stub
		
	}
}

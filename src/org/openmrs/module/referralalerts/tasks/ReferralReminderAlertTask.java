package org.openmrs.module.referralalerts.tasks;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.referralalerts.Urgency;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ReferralReminderAlertTask extends AbstractTask {
	
	public static final Log log = LogFactory.getLog(ReferralReminderAlertTask.class);
	

	@Override
	public void execute() {
		System.out.println("Running referral reminder alert task");
		log.info("Running referral reminder alert task");
		
		EncounterService es = Context.getEncounterService();
		for (Urgency urg : Urgency.values())
			searchForAndGenerateAlerts(es, urg.getDaysAgo());
	}

	private void searchForAndGenerateAlerts(EncounterService es, int daysAgo) {
		DateMidnight from = new DateMidnight().minusDays(daysAgo);
		DateMidnight rto = new DateMidnight().minusDays(daysAgo-1);
		DateMidnight rcto = new DateMidnight().plusDays(1);
		EncounterType referralEncounterType = Context.getEncounterService().getEncounterType("ANC Referral");
		EncounterType referralConfirmationEncounterType = Context.getEncounterService().getEncounterType("ANC Referral Confirmation");
		
		List<Encounter> referrals = es.getEncounters(
			null, null, from.toDate(), rto.toDate(), null, Collections.singleton(referralEncounterType), null, false
		);
		List<Encounter> confirmations = es.getEncounters(
			null, null, from.toDate(), rcto.toDate(), null, Collections.singleton(referralConfirmationEncounterType), null, false
		);
		
		Collections.sort(referrals, new EncComparator());
		Collections.sort(confirmations, new EncComparator());
		
		Iterator<Encounter> confirmationIter = confirmations.iterator();
		
		referralLoop:
		for (Encounter referral : referrals) {
			try {
				Encounter confirmation;
				do {
					confirmation = confirmationIter.next();
					
					if (referral.getPatientId().equals(confirmation.getPatientId()) &&
						referral.getEncounterDatetime().before(confirmation.getEncounterDatetime()))
						continue referralLoop;
				} while (referral.getPatientId().compareTo(confirmation.getPatientId()) >= 0);
				
			} catch (NoSuchElementException ex) {}
			
			generateAlert(referral);
		}
	}
	
	private void generateAlert(Encounter enc) {
		System.out.println("Generating alert for referral encounter on " + enc.getEncounterDatetime() +
			" for patient " + enc.getPatient().getPersonName() + " (patientId=" + enc.getPatientId() + ")");
		log.info("Generating alert for referral encounter on " + enc.getEncounterDatetime() +
			" for patient " + enc.getPatient().getPersonName() + " (patientId=" + enc.getPatientId() + ")");
	}
	
	
	private static class EncComparator implements Comparator<Encounter> {
		@Override
		public int compare(Encounter enc1, Encounter enc2) {
			if (enc1==enc2)
				return 0;
			
			int idComp = enc1.getPatientId().compareTo(enc2.getPatientId());
			if (idComp!=0)
				return idComp;
			
			return enc1.getEncounterDatetime().compareTo(enc2.getEncounterDatetime());
		}
	}
}

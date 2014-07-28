package org.openmrs.module.oncologypoc.api;

import java.util.Date;

import org.openmrs.Patient;


public class ExtendedPatient extends Patient{
	
	private static final long serialVersionUID = 1L;
	private Date returnDate;

	public ExtendedPatient(Patient patient) {
		super(patient);
		this.setIdentifiers(patient.getIdentifiers());
	}
	
	public Date getReturnDate() {
		return returnDate;
	}

	/**
	 * @param returnDate
	 *            The ReturnDate to set.
	 */
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}


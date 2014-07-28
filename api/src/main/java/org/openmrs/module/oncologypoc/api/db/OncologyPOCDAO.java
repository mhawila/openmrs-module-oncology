package org.openmrs.module.oncologypoc.api.db;

import java.util.Date;
import java.util.List;

import org.openmrs.module.oncologypoc.api.ExtendedPatient;
import org.openmrs.module.oncologypoc.api.SubEncounter;

/**
 * OncologyPOC-related database functions
 * 
 * @author Samuel Ndichu
 * @version 1.0
 */
public interface OncologyPOCDAO {

	public List<ExtendedPatient> getReturnPatients(Date sDate, Date eDate);

	public List<SubEncounter> getAllSubEncounters();

	public void saveSubEncounter(SubEncounter subEncounter);

	public void deleteSubEncounter(SubEncounter subEncounter);

	public SubEncounter getSubEncounter(Integer encounterId);

	public List<SubEncounter> getAllSubEncounters(Integer start, Integer length);
}

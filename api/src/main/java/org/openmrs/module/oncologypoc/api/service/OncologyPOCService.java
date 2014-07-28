package org.openmrs.module.oncologypoc.api.service;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.oncologypoc.api.ExtendedPatient;
import org.openmrs.module.oncologypoc.api.SubEncounter;
import org.openmrs.module.oncologypoc.api.db.OncologyPOCDAO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OncologyPOCService extends OpenmrsService {

	public void setOncologyPOCDAO(OncologyPOCDAO dao);

	public List<ExtendedPatient> getReturnPatients(Date sDate, Date eDate);

	public void saveSubEncounter(SubEncounter subEncounter);

	public void deleteSubEncounter(SubEncounter subEncounter);

	public SubEncounter getSubEncounter(Integer encounterId);

	public List<SubEncounter> getAllSubEncounters();
}
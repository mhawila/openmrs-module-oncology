package org.openmrs.module.oncologypoc.api.service.impl;

import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.oncologypoc.api.ExtendedPatient;
import org.openmrs.module.oncologypoc.api.SubEncounter;
import org.openmrs.module.oncologypoc.api.db.OncologyPOCDAO;
import org.openmrs.module.oncologypoc.api.service.OncologyPOCService;

/**
 * OncologyPOC-related services
 * 
 * @author Samuel ndichu
 * @version 1.0
 */
public class OncologyPOCServiceImpl extends BaseOpenmrsService implements OncologyPOCService {

	private OncologyPOCDAO dao;

	public OncologyPOCServiceImpl() {
	}
	
	public void setOncologyPOCDAO(OncologyPOCDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<ExtendedPatient> getReturnPatients(Date sDate, Date eDate) {
		return dao.getReturnPatients(sDate, eDate);
	}

	@Override
	public List<SubEncounter> getAllSubEncounters() {
		return dao.getAllSubEncounters();
	}
	
	@Override
	public void saveSubEncounter(SubEncounter subEncounter) {
		dao.saveSubEncounter(subEncounter);
	}

	@Override
	public void deleteSubEncounter(SubEncounter subEncounter) {
		dao.deleteSubEncounter(subEncounter);
	}

	@Override
	public SubEncounter getSubEncounter(Integer encounterId) {
		return dao.getSubEncounter(encounterId);
	}
}
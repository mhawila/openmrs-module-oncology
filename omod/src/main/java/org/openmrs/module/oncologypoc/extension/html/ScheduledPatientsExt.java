package org.openmrs.module.oncologypoc.extension.html;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.PortletExt;

public class ScheduledPatientsExt extends PortletExt{

	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	@Override
	public String getPortletUrl() {
		return "../module/oncologypoc/portlets/scheduledPatients";
	}
	
	public String getPortletId() {
		return "scheduledPatients";
	}

	@Override
	public String getPortletParameters() {
		return "size=full|postURL=patientView.form|showIncludeVoided=false|viewType=shortEdit";
	}
	
}

package org.openmrs.module.oncologypoc.extension.html;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.PortletExt;

public class ClinicianAlertsExt extends PortletExt{

	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	@Override
	public String getPortletUrl() {
		return "../module/oncologypoc/portlets/clinicianAlerts";
	}
	
	public String getPortletId() {
		return "clinicianAlerts";
	}

	@Override
	public String getPortletParameters() {
		return "size=full|postURL=patientView.form|showIncludeVoided=false|viewType=shortEdit";
	}
	
}

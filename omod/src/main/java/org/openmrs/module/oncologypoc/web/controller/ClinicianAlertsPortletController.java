package org.openmrs.module.oncologypoc.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Form;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.Extension.MEDIA_TYPE;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.oncologypoc.api.SubEncounter;
import org.openmrs.module.oncologypoc.api.service.OncologyPOCService;
import org.openmrs.module.web.extension.FormEntryHandler;
import org.openmrs.web.WebConstants;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.servlet.ModelAndView;

public class ClinicianAlertsPortletController  extends PortletController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String uniqueRequestId = (String) request.getAttribute(WebConstants.INIT_REQ_UNIQUE_ID);
		String lastRequestId = (String) session.getAttribute(WebConstants.OPENMRS_PORTLET_LAST_REQ_ID);
		if (uniqueRequestId.equals(lastRequestId)) {
			session.removeAttribute(WebConstants.OPENMRS_PORTLET_LAST_REQ_ID);
			session.removeAttribute(WebConstants.OPENMRS_PORTLET_CACHED_MODEL);
		}
		return super.handleRequest(request, response);
	}
	
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		OncologyPOCService service = (OncologyPOCService) Context.getService(OncologyPOCService.class);

		model.put("subEncounters", service.getAllSubEncounters());

		Map<Form, String> editUrlMap = new HashMap<Form, String>();
		List<Extension> handlers = ModuleFactory.getExtensions("org.openmrs.module.web.extension.FormEntryHandler", MEDIA_TYPE.html);
		if (handlers != null) {
			for (Extension ext : handlers) {
				FormEntryHandler handler = (FormEntryHandler) ext;
				Collection<Form> toEdit = handler.getFormsModuleCanEdit();
				if (toEdit != null) {
					if (handler.getEditFormUrl() == null)
						throw new IllegalArgumentException("form entry handler " + handler.getClass()
						        + " is trying to handle editing forms but specifies no URL");
					for (Form form : toEdit) {
						editUrlMap.put(form, handler.getEditFormUrl());
					}
				}
			}
		}
		model.put("formToEditUrlMap", editUrlMap);
	}
}
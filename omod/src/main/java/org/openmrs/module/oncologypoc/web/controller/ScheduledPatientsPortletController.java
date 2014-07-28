package org.openmrs.module.oncologypoc.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.oncologypoc.api.service.OncologyPOCService;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.servlet.ModelAndView;

public class ScheduledPatientsPortletController  extends PortletController {
	
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
	
	protected void populateModel(HttpServletRequest request, Map<String,Object> model) {
		OncologyPOCService service = (OncologyPOCService) Context.getService(OncologyPOCService.class);
		DateFormat df = OpenmrsUtil.getDateFormat(Context.getLocale());
		String sDate = request.getParameter("fromDate");
		String eDate = request.getParameter("toDate");
		Date startDate=null;
		Date endDate=null;
		
		if(sDate !=null && sDate==""){
			sDate=null;
		}
		if(eDate !=null && eDate==""){
			eDate=null;
		}
				
		try {
			if(sDate!=null && eDate!=null){
				startDate=df.parse((sDate));
				endDate=df.parse(eDate);
			} else if(sDate==null && eDate!=null){
				endDate=df.parse(eDate);
				sDate=eDate;
				startDate=df.parse((sDate));	
			} else if(sDate!=null && eDate==null){
				startDate=df.parse(sDate);
				eDate=sDate;
				endDate=df.parse(eDate);	
			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (startDate==null)
			startDate=new Date();
		
		if (endDate==null)
			endDate=new Date();
		
		model.put("patients", service.getReturnPatients(startDate, endDate));
		model.put("startDate", df.format(startDate));
		model.put("endDate", df.format(endDate));
	}
}
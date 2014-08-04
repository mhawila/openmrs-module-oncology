package org.openmrs.module.oncologypoc.web.controller;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oncologypoc.api.utils.OncologyPOCConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class LoginInterceptController {
//	@RequestMapping({ "/index.htm" })
//	public String redirectIntercepted() {
//		User autheticatedUser = Context.getAuthenticatedUser();
//
//		if ((autheticatedUser != null) && (!autheticatedUser.isSuperUser())) {
//			String prop = Context.getAdministrationService().getGlobalProperty(OncologyPOCConstants.GP_INTERCEPTROLES);
//
//			if ((prop != null) && (prop.length() > 0)) {
//				String[] interceptRoles = prop.split(",");
//				boolean interceptThisOne = false;
//
//				for (String role : interceptRoles) {
//					if (autheticatedUser.hasRole(role.trim())){
//						interceptThisOne = true;
//						break;
//					}
//				}
//
//				if (interceptThisOne) {
//					return "redirect:/module/oncologypoc/patientSearch.htm";
//				}
//			}
//		}
//		return null;
//	}
}

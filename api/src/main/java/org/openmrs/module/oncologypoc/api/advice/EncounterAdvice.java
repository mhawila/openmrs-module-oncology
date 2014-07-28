package org.openmrs.module.oncologypoc.api.advice;

import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oncologypoc.api.SubEncounter;
import org.openmrs.module.oncologypoc.api.service.OncologyPOCService;
import org.openmrs.module.oncologypoc.api.utils.OncologyPOCConstants;
import org.springframework.aop.AfterReturningAdvice;

public class EncounterAdvice implements AfterReturningAdvice {
	private static final Log log = LogFactory.getLog(EncounterAdvice.class);

	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		log.debug("Intercepted an Encounter Save");
		if (method.getName().equals("saveEncounter")) {
			User autheticatedUser = Context.getAuthenticatedUser();

			if (autheticatedUser != null) {
				Collection<Role> userRoles = autheticatedUser.getAllRoles();
	
				if (userRoles != null && userRoles.size() > 0) {
					String prop = Context.getAdministrationService().getGlobalProperty(OncologyPOCConstants.GP_INTERCEPTROLES);

					if (prop != null) {
						String[] interceptRoles = prop.split(",");
						boolean interceptThisOne = false;
						Role interceptedRole = null;
			
						for (String role : interceptRoles) {
							for (Role userRole : userRoles) {
								if (role.equalsIgnoreCase(userRole.getRole())) {
									interceptThisOne = true;
									interceptedRole = userRole;
									break;
								}
							}
							if (interceptThisOne)
								break;
						}
						
						if (interceptThisOne) {
							Encounter encounter = (Encounter) returnValue;
							if (encounter != null && encounter.getPatient() != null && encounter.getForm() != null) {
								OncologyPOCService service = (OncologyPOCService) Context.getService(OncologyPOCService.class);
								SubEncounter subEncounter = service.getSubEncounter(encounter.getEncounterId());

								if (interceptedRole.hasPrivilege(OncologyPOCConstants.PRIV_CLINICIAN)) {
									log.debug("Deleting subEncounter");
									service.deleteSubEncounter(subEncounter);
								} else {
									log.debug("Saving subEncounter");
									if (subEncounter == null) {
										subEncounter = new SubEncounter();
										subEncounter.setEncounterId(encounter.getEncounterId());
									}
									service.saveSubEncounter(subEncounter);
								}
							}
						}
					}
				}
			}
		}
	}
}
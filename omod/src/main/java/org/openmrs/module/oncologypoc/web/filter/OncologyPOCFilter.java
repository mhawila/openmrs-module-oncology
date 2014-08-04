package org.openmrs.module.oncologypoc.web.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.oncologypoc.api.utils.OncologyPOCConstants;

public class OncologyPOCFilter implements Filter {
	protected final Log log = LogFactory.getLog(getClass());
	private String homePage;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		User authenticatedUser = Context.getAuthenticatedUser();

		if (authenticatedUser != null) {
			String prop = Context.getAdministrationService().getGlobalProperty(OncologyPOCConstants.GP_INTERCEPTROLES);
			String requestURL = ((HttpServletRequest) request).getRequestURI();
			String patientId = ((HttpServletRequest) request).getParameter("patientId");
			log.debug("OncologyPOCFilter.doFilter: " + requestURL + "|" + patientId);

			if ((prop != null) && (prop.length() > 0)) {
				String[] interceptRoles = prop.split(",");
				boolean interceptThisOne = false;
				for (String role : interceptRoles) {
					if (hasRole(authenticatedUser,role.trim())){
						interceptThisOne = true;
						break;
					}
				}

				if (interceptThisOne && !authenticatedUser.isSuperUser()) {
					if (requestURL.contains("patientDashboard"))
						((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + homePage + "?patientId=" + patientId);
					if (requestURL.contains("findPatient.htm"))
						((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/module/oncologypoc/patientSearch.htm");
					//if (requestURL.contains("index.htm"))
						//((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/module/oncologypoc/patientSearch.htm");
				} else {
					chain.doFilter(request, response);
                }
			}
		}
	}

	public void init(FilterConfig config) throws ServletException {
		homePage = config.getInitParameter("oncologyHomePage");
	}

    //Custom case insensitive hasRole method
    private static  boolean hasRole(final User user,final String roleName) {
        if(user==null || roleName==null || roleName.isEmpty())return false;
        Set<Role> roles = user.getAllRoles();
        for(Role role:user.getAllRoles()) {
             if(role.getName().equalsIgnoreCase(roleName))return true;
        }
        return false;
    }
}

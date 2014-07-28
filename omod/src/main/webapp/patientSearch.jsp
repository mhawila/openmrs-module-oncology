<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="OncologyPoc View Patient Schedules" otherwise="/login.htm"
	redirect="/module/oncologypoc/patientSearch.htm" />
	
<openmrs:htmlInclude file="/moduleResources/oncologypoc/scripts/css/nav.css"/>

<style>
	.boxHeader {
		background-color: #cccccc;
		border: 1px solid #cccccc;
		border-top-left-radius: 5px 5px;
		border-top-right-radius: 5px 5px;
	}
	
	.box {
		border: 1px solid #cccccc;
	}
</style>

<script src="/openmrs/scripts/calendar/calendar.js" type="text/javascript"></script>

<script language="javascript">
	function changeTab(tabObj) {
		if (!document.getElementById || !document.createTextNode) {return;}
		if (typeof tabObj == "string")
			tabObj = document.getElementById(tabObj);
		
		if (tabObj) {
			var tabs = tabObj.parentNode.parentNode.getElementsByTagName('a');
			for (var i=0; i<tabs.length; i++) {
				if (tabs[i].className.indexOf('current') != -1) {
					manipulateClass('remove', tabs[i], 'current');
				}
				var divId = tabs[i].id.substring(0, tabs[i].id.lastIndexOf("Div"));
				var divObj = document.getElementById(divId);
				if (divObj) {
					if (tabs[i].id == tabObj.id)
						divObj.style.display = "block";
					else
						divObj.style.display = "none";
				}
			}
			addClass(tabObj, 'current');
			
		}
		return false;
	}
</script> 

<table cellpadding="0" cellspacing="0">
 	<tr>
	    <td width="20%" valign="top">
	    	<div class="left-nav-div" id="left-nav-div">
				<openmrs:hasPrivilege privilege="OncologyPoc View Patient Schedules">
					<div style="background: url('<c:url value="/moduleResources/oncologypoc/images/schedule.gif"/>') left center no-repeat">
						<a id="schedulesDiv" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Patient Schedules</a>
					</div>
				</openmrs:hasPrivilege>
				<openmrs:hasPrivilege privilege="OncologyPoc View Clinician Alerts">
					<div style="background: url('<c:url value="/moduleResources/oncologypoc/images/alerts.gif"/>') left center no-repeat">
						<a id="alertsDiv" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Clinician Alerts</a>
				 	</div>
				</openmrs:hasPrivilege>
				<div style="background: url('<c:url value="/moduleResources/oncologypoc/images/search.gif"/>') left center no-repeat">
					<a id="patientSearchDiv" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Patient Search</a> 
				</div>
			</div>
	    </td>
	    <td valign="top" width="80%">
		    <div class="content-div">
				<div id="schedules" style="display:block;">
					<openmrs:hasPrivilege privilege="OncologyPoc View Patient Schedules">
						<openmrs:extensionPoint pointId="org.openmrs.module.oncologypoc.patientSchedules" type="html">
							<openmrs:portlet id="${extension.portletId}" url="${extension.portletUrl}" parameters="${extension.portletParameters}"/>
						</openmrs:extensionPoint>
					</openmrs:hasPrivilege>
				</div>
			    <div id="alerts" style="display:none;">
					<openmrs:hasPrivilege privilege="OncologyPoc View Clinician Alerts">
						<openmrs:extensionPoint pointId="org.openmrs.clinicianAlertsPortlet" type="html">
							<openmrs:portlet id="${extension.portletId}" url="${extension.portletUrl}" parameters="${extension.portletParameters}"/>
						</openmrs:extensionPoint>
					</openmrs:hasPrivilege>
				</div>
			    <div id="patientSearch" style="display:none;">
					<h3><spring:message code="Patient.search"/></h3>	
					<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|postURL=patientView.form|showIncludeVoided=false|viewType=shortEdit" />
				</div>
			</div>
		</td>
	</tr>
</table>
<%@ include file="/WEB-INF/template/footer.jsp"%>
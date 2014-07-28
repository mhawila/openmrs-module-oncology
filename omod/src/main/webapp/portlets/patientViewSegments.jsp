<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="" otherwise="/login.htm"
	redirect="/options.form" />
	
<openmrs:htmlInclude file="/scripts/easyAjax.js" />

<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />

<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<link href="<openmrs:contextPath/>/scripts/jquery-ui/css/<spring:theme code='jqueryui.theme.name' />/jquery-ui.custom.css" type="text/css" rel="stylesheet" />

<openmrs:htmlInclude file="/scripts/flot/jquery.flot.js" />
<openmrs:htmlInclude file="/scripts/flot/jquery.flot.multiple.threshold.js"/>	
	
<script type="text/javascript">
	var timeOut = null;

	<openmrs:authentication>var userId = "${authenticatedUser.userId}";</openmrs:authentication>

	//initTabs
	$j(document).ready(function() {
		var c = getTabCookie();
		if (c == null) {
			var tabs = document.getElementById("patientTabs").getElementsByTagName("a");
			if (tabs.length && tabs[0].id)
				c = tabs[0].id;
		}
		changeTab(c);
	});
	
	function setTabCookie(tabType) {
		document.cookie = "dashboardTab-" + userId + "="+escape(tabType);
	}
	
	function getTabCookie() {
		var cookies = document.cookie.match('dashboardTab-' + userId + '=(.*?)(;|$)');
		if (cookies) {
			return unescape(cookies[1]);
		}
		return null;
	}
	
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
				var divId = tabs[i].id.substring(0, tabs[i].id.lastIndexOf("Tab"));
				var divObj = document.getElementById(divId);
				if (divObj) {
					if (tabs[i].id == tabObj.id)
						divObj.style.display = "";
					else
						divObj.style.display = "none";
				}
			}
			addClass(tabObj, 'current');
			
			setTabCookie(tabObj.id);
		}
		return false;
    }
</script>
	
<div id="patientTabs${patientVariation}">
	<ul>
		<openmrs:hasPrivilege privilege="Patient Dashboard - View Encounters Section">
			<li><a id="patientEncountersTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus"><spring:message code="patientDashboard.encounters"/></a></li>
		</openmrs:hasPrivilege>
		<openmrs:hasPrivilege privilege="Form Entry">
			<li><a id="formEntryTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus"><spring:message code="patientDashboard.formEntry"/></a></li>
		</openmrs:hasPrivilege>
		<openmrs:hasPrivilege privilege="Form Entry">
			<li><a id="flowsheetTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">FlowSheet</a></li>
		</openmrs:hasPrivilege>
	</ul>
</div>

<div id="patientSections">
	<openmrs:hasPrivilege privilege="Patient Dashboard - View Encounters Section">
		<div id="patientEncounters" style="display:none;">
			<openmrs:portlet url="patientEncounters" id="patientViewEncounters" patientId="${patient.patientId}" moduleId="oncologypoc" parameters="num=100|showPagination=true|formEntryReturnUrl=patientView.form"/>
		</div>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Form Entry">
		<div id="formEntry" style="display:none;">
			<openmrs:portlet url="personFormEntry" id="formEntryPortlet" personId="${patient.personId}"  moduleId="oncologypoc" parameters="showDecoration=true|showLastThreeEncounters=true|returnUrl=patientView.form"/>
		</div>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Patient Dashboard - View Flowsheet Section">
		<div id="flowsheet" style="display:none;">
			<openmrs:portlet url="flowsheetHtmlForm" id="flowsheet"  moduleId="flowsheet" parameters="showDecoration=true|returnUrl=patientView.form"/>
		</div>
	</openmrs:hasPrivilege>
</div>
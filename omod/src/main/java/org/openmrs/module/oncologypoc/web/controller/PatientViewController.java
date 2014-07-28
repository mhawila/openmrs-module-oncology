/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.oncologypoc.web.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientViewController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping("/module/oncologypoc/patientView.form")
	protected String renderDashboard(@RequestParam(required = true, value = "patientId") Integer patientId, ModelMap map)throws Exception {
		
		AdministrationService as = Context.getAdministrationService();
		PatientService ps = Context.getPatientService();
		ConceptService cs = Context.getConceptService();
		
		Patient patient = null;
		
		try {
			patient = ps.getPatient(patientId);
		}
		catch (ObjectRetrievalFailureException noPatientEx) {
			log.warn("There is no patient with id: '" + patientId + "'", noPatientEx);
		}
		
		if (patient == null)
			throw new ServletException("There is no patient with id: '" + patientId + "'");
		
		log.debug("patient: '" + patient + "'");
		map.put("patient", patient);
		
		// determine cause of death
		
		String causeOfDeathOther = "";
		
		if (Context.isAuthenticated()) {
			String propCause = Context.getAdministrationService().getGlobalProperty("concept.causeOfDeath");
			Concept conceptCause = Context.getConceptService().getConcept(propCause);
			
			if (conceptCause != null) {
				List<Obs> obssDeath = Context.getObsService().getObservationsByPersonAndConcept(patient, conceptCause);
				if (obssDeath.size() == 1) {
					Obs obsDeath = obssDeath.iterator().next();
					causeOfDeathOther = obsDeath.getValueText();
					if (causeOfDeathOther == null) {
						log.debug("cod is null, so setting to empty string");
						causeOfDeathOther = "";
					} else {
						log.debug("cod is valid: " + causeOfDeathOther);
					}
				} else {
					log.debug("obssDeath is wrong size: " + obssDeath.size());
				}
			} else {
				log.debug("No concept cause found");
			}
		}
		
		// add encounters if this user can view them
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_ENCOUNTERS))
			map.put("patientEncounters", Context.getEncounterService().getEncountersByPatient(patient));
		
		if (Context.hasPrivilege(PrivilegeConstants.VIEW_OBS)) {
			List<Obs> patientObs = Context.getObsService().getObservationsByPerson(patient);
			map.put("patientObs", patientObs);
			Obs latestWeight = null;
			Obs latestHeight = null;
			String bmiAsString = "?";
			try {
				String weightString = as.getGlobalProperty("concept.weight");
				ConceptNumeric weightConcept = null;
				if (StringUtils.hasLength(weightString))
					weightConcept = cs.getConceptNumeric(cs.getConcept(Integer.valueOf(weightString))
					        .getConceptId());
				String heightString = as.getGlobalProperty("concept.height");
				ConceptNumeric heightConcept = null;
				if (StringUtils.hasLength(heightString))
					heightConcept = cs.getConceptNumeric(cs.getConcept(Integer.valueOf(heightString))
					        .getConceptId());
				for (Obs obs : patientObs) {
					if (obs.getConcept().equals(weightConcept)) {
						if (latestWeight == null
						        || obs.getObsDatetime().compareTo(latestWeight.getObsDatetime()) > 0)
							latestWeight = obs;
					} else if (obs.getConcept().equals(heightConcept)) {
						if (latestHeight == null
						        || obs.getObsDatetime().compareTo(latestHeight.getObsDatetime()) > 0)
							latestHeight = obs;
					}
				}
				if (latestWeight != null)
					map.put("patientWeight", latestWeight);
				if (latestHeight != null)
					map.put("patientHeight", latestHeight);
				if (latestWeight != null && latestHeight != null) {
					double weightInKg;
					double heightInM;
					if (weightConcept.getUnits().equals("kg"))
						weightInKg = latestWeight.getValueNumeric();
					else if (weightConcept.getUnits().equals("lb"))
						weightInKg = latestWeight.getValueNumeric() * 0.45359237;
					else
						throw new IllegalArgumentException("Can't handle units of weight concept: "
						        + weightConcept.getUnits());
					if (heightConcept.getUnits().equals("cm"))
						heightInM = latestHeight.getValueNumeric() / 100;
					else if (heightConcept.getUnits().equals("m"))
						heightInM = latestHeight.getValueNumeric();
					else if (heightConcept.getUnits().equals("in"))
						heightInM = latestHeight.getValueNumeric() * 0.0254;
					else
						throw new IllegalArgumentException("Can't handle units of height concept: "
						        + heightConcept.getUnits());
					double bmi = weightInKg / (heightInM * heightInM);
					map.put("patientBmi", bmi);
					String temp = "" + bmi;
					bmiAsString = temp.substring(0, temp.indexOf('.') + 2);
				}
			}
			catch (Exception ex) {
				if (latestWeight != null && latestHeight != null)
					log.error("Failed to calculate BMI even though a weight and height were found", ex);
			}
			map.put("patientBmiAsString", bmiAsString);
		} else {
			map.put("patientObs", new HashSet<Obs>());
		}
		
		// information about whether or not the patient has exited care
		Obs reasonForExitObs = null;
		String patientVariation = "";
		Concept reasonForExitConcept = Context.getConceptService().getConcept(
			    Context.getAdministrationService().getGlobalProperty("concept.reasonExitedCare"));
		if (reasonForExitConcept != null) {
			List<Obs> patientExitObs = Context.getObsService().getObservationsByPersonAndConcept(patient,
			    reasonForExitConcept);
			if (patientExitObs != null) {
				log.debug("Exit obs is size " + patientExitObs.size());
				if (patientExitObs.size() == 1) {
					reasonForExitObs = patientExitObs.iterator().next();
					Concept exitReason = reasonForExitObs.getValueCoded();
					Date exitDate = reasonForExitObs.getObsDatetime();
					if (exitReason != null && exitDate != null) {
						patientVariation = "Exited";
					}
				} else {
					if (patientExitObs.size() == 0) {
						log.debug("Patient has no reason for exit");
					} else {
						log.error("Too many reasons for exit - not putting data into model");
					}
				}
			}
		}
		map.put("patientVariation", patientVariation);
		map.put("patientReasonForExit", reasonForExitObs);
		
		return "/module/oncologypoc/patientView";
	}
}
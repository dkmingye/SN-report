package com.sparnord.heatmaps.riskidentification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisParameter.AnalysisSimpleTypeValue;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.SystemLog;
import com.sparnord.heatmaps.grcu.GRCDataProcessing;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.assessment.AssessedObject;
import com.sparnord.heatmaps.grcu.assessment.AssessmentEngine;
import com.sparnord.heatmaps.grcu.constants.GRCCodeTemplate;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;
import com.sparnord.heatmaps.grcu.constants.GRCQuery;

public class IdentificationMethodsToolBox {

  public static void setParameters(final Map<String, List<AnalysisParameter>> parameters, final IdentificationParameters riskIdentificationParameters, final String beginDateParam, final String endDateParam,final String OrgUnitsParam, final String riskTypesParam,final String keyRiskParam) {

    for (final String paramType : parameters.keySet()) {
      for (final AnalysisParameter paramValue : parameters.get(paramType)) {
        if (paramType.equals(beginDateParam)) {
          for (final AnalysisSimpleTypeValue value : paramValue.getSimpleValues()) {
        	  //////
        	 // SystemLog.log("Begin date : "+ value.getStringValue());
        	  /////////
            riskIdentificationParameters.setBeginDate(GRCDateUtility.resetBeginDateTime((Date) value.getValue()));
          }
        } else if (paramType.equals(endDateParam)) {
          for (final AnalysisSimpleTypeValue value : paramValue.getSimpleValues()) {
        	  //////
        	 // SystemLog.log("End date : "+ value.getStringValue());
        	  /////////
            riskIdentificationParameters.setEndDate(GRCDateUtility.resetEndDateTime((Date) value.getValue()));
          }
        } else if (paramType.equals(OrgUnitsParam)) {
          for (final MegaObject value : paramValue.getValues()) {

            MegaObject classObject = value.getClassObject();
            if (classObject.sameID(GRCMetaClass.MC_ORG_UNIT)) {
                //////
          	  //SystemLog.log("Org Unit : "+ value.getProp("Short Name"));
          	  /////////
              riskIdentificationParameters.getOrgUnits().insert(value);
            }
            classObject.release();
          }
        } else if (paramType.equals(keyRiskParam)) {
            for (final AnalysisSimpleTypeValue value : paramValue.getSimpleValues()) {        				
				if(value.getStringValue().equalsIgnoreCase("1")){
				       //////
		          	  //SystemLog.log("key risk true ");
		          	  /////////
					riskIdentificationParameters.setKeyRisk();					
				}          
              }
            }/*else if (paramType.equals(processesParam)) {
          for (final MegaObject value : paramValue.getValues()) {
            MegaObject classObject = value.getClassObject();
            if (classObject.sameID(GRCMetaClass.MC_BUSINESS_PROCESS) || classObject.sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {
            	   //////
            	  SystemLog.log("Process : "+ value.getProp("Short Name"));
            	  /////////
            	riskIdentificationParameters.getProcesses().insert(value);
            }
            classObject.release();
          }

        }*/ else if (paramType.equals(riskTypesParam)) {
          for (final MegaObject value : paramValue.getValues()) {
            MegaObject classObject = value.getClassObject();
            if (classObject.sameID(GRCMetaClass.MC_RISK_TYPE)) {
            	  //////
          	  //SystemLog.log("Risk Type : "+ value.getProp("Short Name"));
          	  /////////
              riskIdentificationParameters.getRisktypes().insert(value);
            } else if (classObject.sameID(GRCMetaClass.MC_CONTROL_TYPE)) {
            	  //////
            	  //SystemLog.log("Control Type : "+ value.getProp("Short Name"));
            	  /////////
              riskIdentificationParameters.getControlTypes().insert(value);
            }
            classObject.release();
          }

        } /*else if (paramType.equals(ObjectivesParam)) {
          for (final MegaObject value : paramValue.getValues()) {
            MegaObject classObject = value.getClassObject();
            if (classObject.sameID(GRCMetaClass.MC_OBJECTIVE)) {
            	  //////
            	  SystemLog.log("Objective : "+ value.getProp("Short Name"));
            	  /////////
              riskIdentificationParameters.getObjectives().insert(value);
            }
            classObject.release();
          }

        }*/
      }
    }

    if (riskIdentificationParameters.getEndDate() == null) {
      riskIdentificationParameters.setEndDate(GRCDateUtility.resetEndDateTime(new Date()));
    }

    if (riskIdentificationParameters.beginDate == null) {
      riskIdentificationParameters.setBeginDate(GRCDateUtility.resetBeginDateTime(GRCDateUtility.getDate("1601/01/01")));
    }

  }

  /**
   * @param root megaRoot
   * @param idParameters Report parameters for riskIdentification /
   *          controlIdentification
   * @param evalBeginDate TODO
   * @param evalEndDate TODO
   * @return get the assessedObjects (risks Or controls) linked to the contexts
   *         chosen ;
   */
  public static ArrayList<AssessedObject> getRisks(final MegaRoot root, final IdentificationParameters idParameters, final Date evalBeginDate, final Date evalEndDate) {
    MegaCollection allmoAssessedObjects;
    String mcAssessedObject = GRCMetaClass.MC_RISK;
    //if no contexts we get all assessedObjects (risks OR controls) 
    if ((idParameters.getProcesses().size() == 0) && (idParameters.getOrgUnits().size() == 0) && (idParameters.getObjectives().size() == 0) && (idParameters.getRisktypes().size() == 0)) {
      allmoAssessedObjects = root.getCollection(mcAssessedObject);
    } else {
      //get all the risks linked to the context report parameters
      MegaCollection contexts = root.getSelection("");
      contexts.insert(idParameters.getProcesses());
      contexts.insert(idParameters.getOrgUnits());
      contexts.insert(idParameters.getObjectives());
      contexts.insert(idParameters.getRisktypes());
      allmoAssessedObjects = IdentificationMethodsToolBox.getRisksCollection(contexts);
      contexts.release();
    }
    //get just the assessedObject created in the range specified by begin & end date
    allmoAssessedObjects = IdentificationMethodsToolBox.getAssessedObjectInRange(root, idParameters.getBeginDate(), idParameters.getEndDate(), allmoAssessedObjects);

    ArrayList<AssessedObject> assessedObjects = new ArrayList<AssessedObject>();
    for (MegaObject assessedObject : allmoAssessedObjects) {
      assessedObjects.add(new AssessedObject(assessedObject));
    }
    assessedObjects = IdentificationMethodsToolBox.filterEvaluatedObjects(root, assessedObjects, evalBeginDate, evalEndDate);
    allmoAssessedObjects.release();
    return assessedObjects;
  }

  /**
   * @param root megaRoot
   * @param assessedObjects evaluated objects (risks/controls)
   * @param evalBeginDate the end date report parameter
   * @param evalEndDate TODO
   * @return a list evaluated objects with their status (evaluated/ not
   *         evaluated)
   */
  private static ArrayList<AssessedObject> filterEvaluatedObjects(final MegaRoot root, final ArrayList<AssessedObject> assessedObjects, final Date evalBeginDate, final Date evalEndDate) {
    ArrayList<AssessedObject> filtredAssessedObjects = assessedObjects;
    for (AssessedObject assessedObject : filtredAssessedObjects) {
      MegaCollection nodes = assessedObject.getAssessedObject().getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_NODE);
      for (MegaObject node : nodes) {
        MegaObject session = AssessmentEngine.getSessionFromNode(node);
        if (session != null) {
          String sessionStatus = session.getProp(GRCMetaAttribut.MA_SESSION_STATUS);
          if (sessionStatus.equalsIgnoreCase(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED)) {
            Date sessionEndDate = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
            if ((sessionEndDate != null) && sessionEndDate.before(evalEndDate) && sessionEndDate.after(evalBeginDate)) {
              assessedObject.setEvaluated(true);
              node.release();
              break;
            }
          }
          session.release();
        }
        node.release();
      }
      nodes.release();
    }

    return filtredAssessedObjects;
  }

  /**
   * @param contexts contexts report parameters
   * @return a collection of all the risks linked to every context parameter or
   *         to a sub-element
   */
  public static MegaCollection getRisksCollection(final MegaCollection contexts) {
    MegaRoot root = contexts.getRoot();
    MegaCollection risksFromContexts = root.getSelection("");
    for (MegaObject context : contexts) {
      //case of getting a risks as assessedObjects
      ArrayList<String> idabsPreviousSubs = new ArrayList<String>();
      risksFromContexts.insert(GRCDataProcessing.getAllElementsFromContext(context, GRCDataProcessing.getRisksQuery(context), idabsPreviousSubs));
      context.release();
    }
    root.release();
    return risksFromContexts;
  }

  /**
   * @param root megaRoot
   * @param beginDate begin date report parameter
   * @param endDate end Date report parameter
   * @param moAssessedObjects assessedObjects
   * @return filter the assessed objects by the creation date parameter
   */
  private static MegaCollection getAssessedObjectInRange(final MegaRoot root, final Date beginDate, final Date endDate, final MegaCollection moAssessedObjects) {
    MegaCollection moAssessedObjectsInRange = root.getSelection("");
    for (MegaObject moAssessedObject : moAssessedObjects) {
      Date moAssessedObjectDate = GRCDateUtility.getDateFromMega(moAssessedObject, GRCMetaAttribut.MA_CREATION_DATE);
      if (moAssessedObjectDate != null) {
        if (beginDate == null) {
          if (moAssessedObjectDate.equals(endDate) || moAssessedObjectDate.before(endDate)) {
            moAssessedObjectsInRange.insert(moAssessedObject);
          }
        } else {
          if ((moAssessedObjectDate.equals(endDate) || moAssessedObjectDate.equals(beginDate) || (moAssessedObjectDate.before(endDate) && moAssessedObjectDate.after(beginDate)))) {
            moAssessedObjectsInRange.insert(moAssessedObject);
          }
        }
      }
      moAssessedObject.release();
    }
    moAssessedObjects.release();

    return moAssessedObjectsInRange;
  }

  public static void orderRisksByContext(final AssessedObject risk, final Map<String, Map<String, String>> risksByContext, final MegaCollection elementsLinked, final MegaCollection contexts, final Map<String, MegaCollection> contextSubs) {
    for (MegaObject element : elementsLinked) {
      for (MegaObject context : contexts) {
        //check if the element linked to the incident is one of the context chosen or a sub element to it
        MegaCollection mgContextSubs = contextSubs.get(context.megaUnnamedField());
        if (mgContextSubs != null) {
          MegaObject obj = mgContextSubs.get(element.megaUnnamedField());
          boolean exist = obj.exists();
          obj.release();
          if (exist) {
            Map<String, String> contextRisksMap = risksByContext.get(context.megaUnnamedField());
            String key = GRCConstants.NOT_EVALUATED;
            if (risk.isEvaluated()) {
              key = GRCConstants.EVALUATED;
            }
            String risksLinked = contextRisksMap.get(key);
            if ((risksLinked == null) || risksLinked.equals("")) {
              risksLinked = risk.getAssessedObject().megaUnnamedField();
            } else if (!risksLinked.contains(risk.getAssessedObject().megaUnnamedField())) {
              risksLinked = risksLinked + "," + risk.getAssessedObject().megaUnnamedField();
            }
            contextRisksMap.put(key, risksLinked);
            risksByContext.put(context.megaUnnamedField(), contextRisksMap);
          }
        }
        context.release();
      }
      element.release();
    }
  }

  public static MegaCollection getElementsFromAssessedObjects(final MegaRoot root, final MegaObject context, final MegaObject moAssessedObject) {
    MegaCollection elements = root.getSelection("");
    //assessedObject is a Control
    MegaCollection elementsFromcontrol = null;
    //assessedObject is a Risk
    MegaObject contextRiskType = context.getType(GRCMetaClass.MC_RISK_TYPE);
    if (contextRiskType.getID() != null) {
      //special case for riskType;we get the riskTypes linked to the risk from a different link
      elementsFromcontrol = moAssessedObject.getCollection(GRCMetaAssociationEnd.MAE_RISK_TYPE_FROM_RISK);
      elements.insert(elementsFromcontrol);
      elementsFromcontrol.release();
    } else {
      //we get the elements linked to the risks (processes,orgUnits,objectives ...)
      elementsFromcontrol = moAssessedObject.getCollection(GRCMetaAssociationEnd.MAE_ELEMENT_AT_RISK_FROM_RISK);
      elements.insert(elementsFromcontrol);
      elementsFromcontrol.release();
    }
    contextRiskType.release();
    return elements;
  }

  public static void orderByRiskMitigation(final AssessedObject assessedRisk, final Map<String, String> riskMitigation) {
    String withControl = GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CT_RISK_WITH_CONTROL, assessedRisk.getAssessedObject().getRoot());
    String withoutControl = GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CT_RISK_WITHOUT_CONTROL, assessedRisk.getAssessedObject().getRoot());
    if (withControl.equals("")) {
      withControl = GRCConstants.WITH_CONTROL;
    }
    if (withoutControl.equals("")) {
      withoutControl = GRCConstants.WITHOUT_CONTROL;
    }
    if (riskMitigation.isEmpty()) {
      riskMitigation.put(withControl, "");
      riskMitigation.put(withoutControl, "");

    }
    String key = withoutControl;
    MegaCollection mcControls = assessedRisk.getAssessedObject().getCollection(GRCMetaAssociationEnd.MAE_CONTROL_FROM_RISK);
    if (mcControls.size() > 0) {
      key = withControl;
    }
    mcControls.release();

    String incidentsLinked = riskMitigation.get(key);
    if ((incidentsLinked == null) || incidentsLinked.equals("")) {
      incidentsLinked = assessedRisk.getAssessedObject().megaUnnamedField();
    } else {
      incidentsLinked = incidentsLinked + "," + assessedRisk.getAssessedObject().megaUnnamedField();
    }
    riskMitigation.put(key, incidentsLinked);
  }

  public static void orderByMetaAttribute(final MegaObject risk, final Map<String, String> risksByStatus, final String metaAttribute, final String withoutStatusCodeTemplate) {
    MegaRoot root = risk.getRoot();
    if (risksByStatus.isEmpty()) {
      MegaCollection mcMetaAttributes = root.getCollection(GRCMetaClass.MC_METAATTRIBUTE);
      MegaObject moMetaAttribute = mcMetaAttributes.get(metaAttribute);
      mcMetaAttributes.release();
      if (moMetaAttribute.getID() != null) {
        MegaCollection mcMetaAttributesValues = moMetaAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order", GRCMetaAttribut.MA_INTERNAL_VALUE);
        for (MegaObject metaAttributeValue : mcMetaAttributesValues) {
          risksByStatus.put(metaAttributeValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE), metaAttributeValue.getProp(GRCMetaAttribut.MA_VALUE_NAME) + ":");
          metaAttributeValue.release();
        }
        if ((withoutStatusCodeTemplate != null) && !withoutStatusCodeTemplate.equalsIgnoreCase("")) {
          String withoutValue = GRCDataProcessing.getCodeTemplate(withoutStatusCodeTemplate, root);
          risksByStatus.put(withoutValue, withoutValue + ":");
        }
        mcMetaAttributesValues.release();
      }
    }
    String riskAttributeValue = risk.getProp(metaAttribute);
    if ((riskAttributeValue != null) && riskAttributeValue.equals("")) {
      riskAttributeValue = GRCDataProcessing.getCodeTemplate(withoutStatusCodeTemplate, root);
    }
    String risksByInterValue = risksByStatus.get(riskAttributeValue);
    if (risksByInterValue != null) {
      if (risksByInterValue.split(":").length == 1) {
        risksByInterValue = risksByInterValue + risk.megaUnnamedField();
      } else {
        risksByInterValue = risksByInterValue + "," + risk.megaUnnamedField();
      }
      risksByStatus.put(riskAttributeValue, risksByInterValue);
    }
    root.release();
  }

  public static void orderByRiskEvaluation(final AssessedObject assessedRisk, final Map<String, String> riskEvaluations) {
    String evaluated = GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CT_EVALUATED, assessedRisk.getAssessedObject().getRoot());
    String notEvaluated = GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CT_NOT_EVALUATED, assessedRisk.getAssessedObject().getRoot());
    if (evaluated.equals("")) {
      evaluated = GRCConstants.EVALUATED;
    }
    if (notEvaluated.equals("")) {
      notEvaluated = GRCConstants.NOT_EVALUATED;
    }

    if (riskEvaluations.isEmpty()) {
      riskEvaluations.put(evaluated, "");
      riskEvaluations.put(notEvaluated, "");
    }
    String key = notEvaluated;
    if (assessedRisk.isEvaluated()) {
      key = evaluated;
    }
    String assessedLinked = riskEvaluations.get(key);
    if ((assessedLinked == null) || assessedLinked.equals("")) {
      assessedLinked = assessedRisk.getAssessedObject().megaUnnamedField();
    } else {
      assessedLinked = assessedLinked + "," + assessedRisk.getAssessedObject().megaUnnamedField();
    }
    riskEvaluations.put(key, assessedLinked);
  }

  public static void orderRisksByDate(final Map<String, Map<String, String>> risksByDate, final AssessedObject risk, final boolean perYear) {
    Date riskCreationDate = GRCDateUtility.getDateFromMega(risk.getAssessedObject(), GRCMetaAttribut.MA_CREATION_DATE);
    if (riskCreationDate != null) {
      String key = GRCDateUtility.getYear(riskCreationDate) + "-" + GRCDateUtility.getMonth(riskCreationDate);
      if (perYear) {
        key = "" + GRCDateUtility.getYear(riskCreationDate);
      }
      Map<String, String> riskEvaluationsByDate = risksByDate.get(key);
      if (riskEvaluationsByDate == null) {
        riskEvaluationsByDate = new HashMap<String, String>();
        riskEvaluationsByDate.put(GRCConstants.EVALUATED, "");
        riskEvaluationsByDate.put(GRCConstants.NOT_EVALUATED, "");
      }
      String keyEvaluation = GRCConstants.NOT_EVALUATED;
      if (risk.isEvaluated()) {
        keyEvaluation = GRCConstants.EVALUATED;
      }
      String incidentsLinked = riskEvaluationsByDate.get(keyEvaluation);
      if ((incidentsLinked == null) || incidentsLinked.equals("")) {
        incidentsLinked = risk.getAssessedObject().megaUnnamedField();
      } else {
        incidentsLinked = incidentsLinked + "," + risk.getAssessedObject().megaUnnamedField();
      }
      riskEvaluationsByDate.put(keyEvaluation, incidentsLinked);
      risksByDate.put(key, riskEvaluationsByDate);
    }
  }

  public static Map<String, Map<String, String>> getMapsByContexts(final MegaCollection contexts) {
    Map<String, Map<String, String>> risksByContexts = new HashMap<String, Map<String, String>>();
    for (MegaObject context : contexts) {
      Map<String, String> contextRisksMap = new HashMap<String, String>();
      contextRisksMap.put(GRCConstants.EVALUATED, "");
      contextRisksMap.put(GRCConstants.NOT_EVALUATED, "");
      risksByContexts.put(context.megaUnnamedField(), contextRisksMap);
      context.release();
    }
    return risksByContexts;
  }

  public static Map<String, MegaCollection> getMapsContextBySubs(final IdentificationParameters idParameters) {
    Map<String, MegaCollection> contextsBySubs = new HashMap<String, MegaCollection>();
    //orgUnits
    for (MegaObject context : idParameters.getProcesses()) {
      contextsBySubs.put(context.megaUnnamedField(), GRCDataProcessing.getAllSubs(context));
      context.release();
    }

    for (MegaObject context : idParameters.getOrgUnits()) {
      contextsBySubs.put(context.megaUnnamedField(), GRCDataProcessing.getAllSubs(context));
      context.release();
    }

    for (MegaObject context : idParameters.getObjectives()) {
      contextsBySubs.put(context.megaUnnamedField(), GRCDataProcessing.getAllSubs(context));
      context.release();
    }

    for (MegaObject context : idParameters.getRisktypes()) {
      contextsBySubs.put(context.megaUnnamedField(), GRCDataProcessing.getAllSubs(context));
      context.release();
    }

    return contextsBySubs;
  }

  public static void setMapsContextBySubs(final MegaCollection contexts, final Map<String, MegaCollection> contextsBySubs) {
    for (MegaObject context : contexts) {
      contextsBySubs.put(context.megaUnnamedField(), GRCDataProcessing.getAllSubs(context));
      context.release();
    }
  }

  public static void releaseMaps(final Map<String, MegaCollection> contextsBySubs) {
    for (MegaCollection mgCol : contextsBySubs.values()) {
      mgCol.release();
    }
  }

  public static void initializeContextsParameters(final MegaRoot root, final IdentificationParameters idParameters) {

    if (idParameters.getOrgUnits().size() == 0) {
      MegaCollection orgParents = root.getCollection(GRCQuery.PARENT_org_QUERY);
      idParameters.getOrgUnits().insert(orgParents);
      orgParents.release();
    }

    if (idParameters.getProcesses().size() == 0) {
      MegaCollection processParents = root.getCollection(GRCQuery.PARENT_BuProcess_QUERY);
      idParameters.getProcesses().insert(processParents);
      processParents.release();
    }
    if (idParameters.getObjectives().size() == 0) {
      MegaCollection objectiveParents = root.getCollection(GRCQuery.PARENT_OBJECTIVES_QUERY);
      idParameters.getObjectives().insert(objectiveParents);
      objectiveParents.release();
    }
    if (idParameters.getRisktypes().size() == 0) {
      MegaCollection riskTypeParents = root.getCollection(GRCQuery.PARENT_RISK_TYPES_QUERY);
      idParameters.getRisktypes().insert(riskTypeParents);
      riskTypeParents.release();
    }

    if (idParameters.getControlTypes().size() == 0) {
      MegaCollection controlTypeParents = root.getCollection(GRCQuery.QUERY_CONTROL_TYPE);
      idParameters.getControlTypes().insert(controlTypeParents);
      controlTypeParents.release();
    }
  }

  /**
   * @param root megaRoot
   * @param idParameters Report parameters for riskIdentification /
   *          controlIdentification
   * @param evalBeginDate TODO
   * @param evalEndDate TODO
   * @return get the assessedObjects (risks Or controls) linked to the contexts
   *         chosen ;
   */
  public static ArrayList<AssessedObject> getControls(final MegaRoot root, final IdentificationParameters idParameters, final Date evalBeginDate, final Date evalEndDate) {
    MegaCollection allmoAssessedObjects;
    String mcAssessedObject = GRCMetaClass.MC_CONTROL;
    if ((idParameters.getProcesses().size() == 0) && (idParameters.getOrgUnits().size() == 0) && (idParameters.getObjectives().size() == 0) && (idParameters.getControlTypes().size() == 0)) {
      allmoAssessedObjects = root.getCollection(mcAssessedObject);
    } else {
      MegaCollection contexts = root.getSelection("");
      contexts.insert(idParameters.getProcesses());
      contexts.insert(idParameters.getOrgUnits());
      contexts.insert(idParameters.getObjectives());
      contexts.insert(idParameters.getControlTypes());
      allmoAssessedObjects = IdentificationMethodsToolBox.getCtrlsCollection(contexts);
      contexts.release();
    }
    allmoAssessedObjects = IdentificationMethodsToolBox.getAssessedObjectInRange(root, idParameters.getBeginDate(), idParameters.getEndDate(), allmoAssessedObjects);
    ArrayList<AssessedObject> assessedObjects = new ArrayList<AssessedObject>();
    for (MegaObject assessedObject : allmoAssessedObjects) {
      assessedObjects.add(new AssessedObject(assessedObject));
    }
    assessedObjects = IdentificationMethodsToolBox.filterEvaluatedObjects(root, assessedObjects, evalBeginDate, evalEndDate);
    allmoAssessedObjects.release();
    return assessedObjects;
  }

  public static MegaCollection getCtrlsCollection(final MegaCollection contexts) {
    MegaRoot root = contexts.getRoot();
    MegaCollection risksFromContexts = root.getSelection("");
    for (MegaObject context : contexts) {
      ArrayList<String> idabsPreviousSubs = new ArrayList<String>();
      MegaObject objClassObject = context.getClassObject();
      if (objClassObject.sameID(GRCMetaClass.MC_RISK_TYPE)) {
        //to get the controls for the risk-types we have to get the risks from the risk-types then we get the controls from them
        risksFromContexts.insert(GRCDataProcessing.getAllCtrlsForRiskType(context, GRCMetaAssociationEnd.MAE_RISK_FROM_RISK_TYPE));
      } else {
        risksFromContexts.insert(GRCDataProcessing.getAllElementsFromContext(context, GRCDataProcessing.getCtrlsQuery(context), idabsPreviousSubs));
      }
      objClassObject.release();
      context.release();
    }
    root.release();
    return risksFromContexts;
  }

}

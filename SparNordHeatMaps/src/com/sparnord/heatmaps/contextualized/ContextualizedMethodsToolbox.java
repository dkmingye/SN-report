package com.sparnord.heatmaps.contextualized;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.grcu.GRCDataProcessing;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.assessment.AssessedObject;
import com.sparnord.heatmaps.grcu.assessment.Assessment;
import com.sparnord.heatmaps.grcu.assessment.AssessmentEngine;
import com.sparnord.heatmaps.grcu.assessment.AssessmentNode;
import com.sparnord.heatmaps.grcu.constants.GRCCodeTemplate;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;
import com.sparnord.heatmaps.grcu.constants.GRCQuery;

public class ContextualizedMethodsToolbox {

  /**
   * get the current object from parameters ( organizational process or business
   * process)
   * @param parameters
   * @return
   */
  public static MegaObject currentProcessObject(final Map<String, List<AnalysisParameter>> parameters) {

    MegaObject process = ContextualizedMethodsToolbox.currentObject(parameters, GRCMetaClass.MC_ORGANIZATIONAL_PROCESS);

    if ((process == null) || (process.getID() == null)) {
      return ContextualizedMethodsToolbox.currentObject(parameters, GRCMetaClass.MC_BUSINESS_PROCESS);
    }
    return process;
  }

  public static MegaCollection getSubProcesses(final MegaObject process, final MegaRoot root) {

    MegaCollection subProcesses = root.getSelection("");
    if (process.getClassObject().sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      subProcesses.insert(process.getCollection(GRCQuery.QUERY_SUB_BPROCESS));
      subProcesses.insert(process.getCollection(GRCQuery.QUERY_GET_ORGPROCESS_FROM_BUPROCESS));
    } else {
      subProcesses.insert(process.getCollection(GRCQuery.QUERY_SUB_ORGPROCESS));
    }

    return subProcesses;

  }

  /**
   * get all orgUnits linked to the process (organizational process/business
   * process)
   * @param root
   * @param process
   * @return
   */
  public static MegaCollection getOrgUnitsFromProcess(final MegaRoot root, final MegaObject process) {

    MegaCollection orgUnits = root.getSelection("");

    if (process.getClassObject().sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      // get org-unit from business process
      orgUnits.insert(process.getCollection(GRCQuery.QUERY_GET_ORGUNIT_FROM_BUPROCESS));

    } else {
      // get org-unit from organizational process
      orgUnits.insert(process.getCollection(GRCQuery.QUERY_GET_ORGUNIT_FROM_ORGPROCESS));
    }

    return orgUnits;
  }

  public static HashMap<String, String> fillMetaAttributeValues(final MegaRoot root, final String metaAttribute) {

    HashMap<String, String> metaAttributesValues = new LinkedHashMap<String, String>();

    MegaCollection mcMetaAttributes = root.getCollection(GRCMetaClass.MC_METAATTRIBUTE);
    MegaObject moMetaAttribute = mcMetaAttributes.get(metaAttribute);
    mcMetaAttributes.release();

    MegaCollection mcMetaAttributesValues = moMetaAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order", GRCMetaAttribut.MA_INTERNAL_VALUE);
    for (MegaObject metaAttributeValue : mcMetaAttributesValues) {
      String internalValue = metaAttributeValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE);
      String value = metaAttributeValue.getProp(GRCMetaAttribut.MA_VALUE_NAME);
      metaAttributesValues.put(internalValue, value);

      metaAttributeValue.release();
    }

    moMetaAttribute.release();
    mcMetaAttributesValues.release();

    // Add 'no status assigned' case.
    String noStatusAssigned = GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CODE_TEMP_NO_STATUS_ASSIGNED, root);
    metaAttributesValues.put("", noStatusAssigned);

    return metaAttributesValues;

  }

  public static MegaObject getRootParent(final MegaRoot root, final MegaObject moObject, final String sLinkId) {
    MegaObject moRootParent = moObject;

    // Add current object
    MegaCollection mcParents = moObject.getCollection(sLinkId);

    if ((mcParents != null) && (mcParents.size() > 0)) {
      moRootParent = mcParents.get(1);
      return ContextualizedMethodsToolbox.getRootParent(root, moRootParent, sLinkId);
    }

    return moRootParent;
  }

  public static void getObjectsByQuery(final MegaRoot root, final String sQueryID, final MegaCollection mcResultCollection) {
    MegaCollection mcObjects;
    mcObjects = root.getCollection(sQueryID);
    if (mcObjects != null) {
      mcResultCollection.insert(mcObjects);
    }
  }

  public static ArrayList<AssessedObject> getAssessedObjects(final MegaObject contextObject, final String linkQuery, final Boolean checkContext, final Date evalBeginDate, final Date evalEndDate) {
    ArrayList<AssessedObject> assessedObjects = new ArrayList<AssessedObject>();
    ArrayList<String> idabsPreviousSubs = new ArrayList<String>();
    MegaCollection allLinkedAssessedObjects = GRCDataProcessing.getAllElementsFromContext(contextObject, linkQuery, idabsPreviousSubs);
    for (MegaObject assessedObject : allLinkedAssessedObjects) {
      assessedObjects.add(new AssessedObject(assessedObject));
    }
    allLinkedAssessedObjects.release();
    MegaCollection allSubs = GRCDataProcessing.getAllSubs(contextObject);
    assessedObjects = ContextualizedMethodsToolbox.filterEvaluatedObjects(allSubs, assessedObjects, checkContext, evalBeginDate, evalEndDate);
    allSubs.release();
    return assessedObjects;
  }

  public static MegaObject currentObject(final Map<String, List<AnalysisParameter>> parameters, final String mcObject) {
    MegaObject moAssessedObject = null;
    for (final String paramType : parameters.keySet()) {
      for (final AnalysisParameter paramValue : parameters.get(paramType)) {
        for (final MegaObject value : paramValue.getValues()) {
          MegaObject classObject = value.getClassObject();
          if (classObject.sameID(mcObject)) {
            moAssessedObject = value;
          }
        }
      }
    }

    return moAssessedObject;
  }

  public static ArrayList<AssessedObject> filterEvaluatedObjects(final MegaCollection allSubContexts, final ArrayList<AssessedObject> assessedObjects, final Boolean checkContext, final Date evalBeginDate, final Date evalEndDate) {
    ArrayList<AssessedObject> filtredAssessedObjects = assessedObjects;
    for (AssessedObject assessedObject : filtredAssessedObjects) {
      MegaCollection nodes = assessedObject.getAssessedObject().getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_NODE);
      for (MegaObject node : nodes) {
        MegaObject session = AssessmentEngine.getSessionFromNode(node);
        if (session != null) {
          String sessionStatus = session.getProp(GRCMetaAttribut.MA_SESSION_STATUS);
          if (sessionStatus.equalsIgnoreCase(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED)) {
            Date sessionEndDate = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
            if ((sessionEndDate != null) && sessionEndDate.after(evalBeginDate) && sessionEndDate.before(evalEndDate)) {
              if (checkContext) {
                MegaCollection contexts = node.getCollection(GRCMetaAssociationEnd.MAE_ASSESSMENT_CONTEXT);
                if (ContextualizedMethodsToolbox.checkMeasureContexts(allSubContexts, contexts)) {
                  assessedObject.setEvaluated(true);
                  assessedObject.getContexts().insert(contexts);
                  contexts.release();
                }
              } else {
                assessedObject.setEvaluated(true);
              }
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

  public static boolean checkMeasureContexts(final MegaCollection allSubContexts, final MegaCollection contexts) {
    for (MegaObject context : contexts) {
      MegaObject _obj = allSubContexts.get(context.megaUnnamedField());
      if (_obj.exists()) {
        return true;
      }
      _obj.release();
      context.release();
    }
    return false;
  }

  /**
   * get all nodes from assessment sessions
   * @param root
   * @return
   */
  public static ArrayList<AssessmentNode> loadAssessmentNodes(final MegaRoot root) {
    ArrayList<AssessmentNode> listAssessmentNodes = new ArrayList<AssessmentNode>();

    ArrayList<Assessment> listAssessments = new ArrayList<Assessment>();

    MegaCollection assessments = root.getCollection(GRCMetaClass.MC_ASSESSMENT);
    for (MegaObject moAssessment : assessments) {
      Assessment assessment = new Assessment(moAssessment);
      Date dAssessmentEndDate = assessment.getSEndDate();
      Date currentDate = GRCDateUtility.resetEndDateTime(new Date());

      if ((dAssessmentEndDate != null)) {
        if (dAssessmentEndDate.before(currentDate) || dAssessmentEndDate.equals(currentDate)) {
          listAssessments.add(assessment);
        }
      }

    }

    assessments.release();

    for (Assessment assessment : listAssessments) {
      MegaCollection mcAssessmentNodes = root.getSelection("");
      mcAssessmentNodes.insert(assessment.getAllNodesFromAssesment());
      if ((assessment.getAssessment().getCollection(GRCMetaAssociationEnd.MAE_AGGREGATION_NODE) != null) && (assessment.getAssessment().getCollection(GRCMetaAssociationEnd.MAE_AGGREGATION_NODE).size() > 0)) {
        mcAssessmentNodes.insert(assessment.getAssessment().getCollection(GRCMetaAssociationEnd.MAE_AGGREGATION_NODE));
      }

      for (MegaObject moAssessmentNode : mcAssessmentNodes) {
        AssessmentNode assessmentNode = new AssessmentNode(moAssessmentNode);
        listAssessmentNodes.add(assessmentNode);
      }

      assessments.release();
      assessment.getAssessment().release();
      mcAssessmentNodes.release();
    }

    return listAssessmentNodes;
  }

  /**
   * get all the processes from an OrgUnit
   * @param root megaRoot
   * @param orgUnit
   * @return
   */
  public static MegaCollection getProcessesFromOrgUnit(final MegaRoot root, final MegaObject orgUnit) {

    MegaCollection processes = root.getSelection("");

    MegaCollection mcOrgUnitElements = orgUnit.getCollection(GRCMetaAssociationEnd.MAE_BUSINESS_PROCESS_FROM_ORG_UNIT);
    processes.insert(mcOrgUnitElements);
    mcOrgUnitElements.release();

    mcOrgUnitElements = orgUnit.getCollection(GRCMetaAssociationEnd.MAE_ORGANIZATIONAL_PROCESS_FROM_ORG_UNIT);
    processes.insert(mcOrgUnitElements);
    mcOrgUnitElements.release();

    return processes;
  }

  public static MegaCollection getOpenActionPlans(final MegaRoot root, final MegaCollection actionPlans) {

    MegaCollection openActionPlans = root.getSelection("");

    for (MegaObject aplan : actionPlans) {
      if (!aplan.getProp(GRCMetaAttribut.MA_ACTIONPLANSTATUS).equals(GRCConstants.IV_ACTION_PLAN_CLOSED)) {
        openActionPlans.insert(aplan);
      }

      aplan.release();
    }

    return openActionPlans;
  }

  public static Text getDiagramTitle(final MegaRoot megaRoot, final String sTitleCodeTemplate) {
    String codeTemplate = GRCDataProcessing.getCodeTemplate(sTitleCodeTemplate, megaRoot);
    String title = "<font color=\"DimGray\"><b><center> " + codeTemplate + " </center></b></font>";
    Text text = new Text(title, false);
    text.isHtml(true);
    return text;
  }

  //assessment methods
  /**
   * @param root megaRoot
   * @param mapNodes map of nodes
   * @param assessedObjects the risks selected
   * @param year the cyear selected
   * @return return all the possibles nodes with context:the main context
   *         object(or children)
   */
  public static ArrayList<AssessmentNode> loadAssessment(final MegaRoot root, final Map<CompositeIdNode, MegaCollection> mapNodes, final MegaCollection assessedObjects, final int year) {
    ArrayList<AssessmentNode> nodes = new ArrayList<AssessmentNode>();

    for (MegaObject assessedObject : assessedObjects) {

      MegaCollection allSessions = root.getCollection(GRCMetaClass.MC_AssessmentSession);

      for (MegaObject session : allSessions) {
        if (session.getProp(GRCMetaAttribut.MA_SESSION_STATUS).equalsIgnoreCase(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED)) {

          Date dateEndSession = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);

          if (dateEndSession != null) {

            int iyear = GRCDateUtility.getYear(dateEndSession);

            if (iyear == year) {

              CompositeIdNode compoIds = new CompositeIdNode();
              compoIds.setId_assessedObject(assessedObject.getProp("_idabs"));
              compoIds.setId_session(session.getProp("_idabs"));

              if (mapNodes.get(compoIds) != null) {

                for (MegaObject node : mapNodes.get(compoIds)) {
                  nodes.add(new AssessmentNode(node));

                }
              }
            }
          }
        }

        session.release();
      }

    }

    return nodes;
  }

  public static ArrayList<HeatmapCell> getCells(final MegaRoot root, final ArrayList<AssessmentNode> nodes) {
    ArrayList<HeatmapCell> cells = new ArrayList<HeatmapCell>();
    for (AssessmentNode assNode : nodes) {
      HeatmapCell tempcell = new HeatmapCell(root);// a revoir la position
      MegaCollection assValues = assNode.getNode().getCollection(GRCMetaAssociationEnd.MAE_ASS_NODE_ASS_VALUE);

      for (MegaObject assValue : assValues) {

        if ((assValue.getCollection(GRCMetaAssociationEnd.MAE_ASS_VALUE_ASS_CHARACTERISTIC).size() > 0) && (assValue.getCollection("~o18yT9lCE1(K[MetaAttributeValue]").size() > 0)) {

          MegaObject assCharacteristic = assValue.getCollection(GRCMetaAssociationEnd.MAE_ASS_VALUE_ASS_CHARACTERISTIC).get(1);

          if (assCharacteristic.sameID(GRCConstants.AC_ERM_IMPACT) || assCharacteristic.sameID(GRCConstants.AC_ERM_LIKELIHOUD)) {

            int impactValue = 0;
            int likelihoodValue = 0;

            if (assCharacteristic.sameID(GRCConstants.AC_ERM_IMPACT)) {
              try {

                impactValue = Integer.parseInt(assValue.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_VALUE_METAATTRIBUTEVALUE).get(1).getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
                if (impactValue > 0) {
                  impactValue = impactValue + 1;
                  impactValue = impactValue - 1;
                }
              } catch (NumberFormatException e) {}

              tempcell.setImpact(impactValue);
            }
            if (assCharacteristic.sameID(GRCConstants.AC_ERM_LIKELIHOUD)) {
              try {
                likelihoodValue = Integer.parseInt(assValue.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_VALUE_METAATTRIBUTEVALUE).get(1).getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
                if (likelihoodValue > 0) {
                  likelihoodValue = likelihoodValue + 1;
                  likelihoodValue = likelihoodValue - 1;
                }
              } catch (NumberFormatException e) {}

              tempcell.setLikelihood(likelihoodValue);
            }
            if ((assNode.getAssessed() != null) && !tempcell.getAssesseds().get(assNode.getAssessed()).exists()) {
              tempcell.getAssesseds().insert(assNode.getNode());
            }
          }
        }
      }
      if ((tempcell.getImpact() != 0) && (tempcell.getLikelihood() != 0)) {
        cells.add(tempcell);
      }
      cells.add(tempcell);
    }

    return cells;
  }

  public static ArrayList<HeatmapCell> filterCellsMulEv(final ArrayList<HeatmapCell> oldtable) {

    ArrayList<HeatmapCell> newtable = new ArrayList<HeatmapCell>();
    Map<String, HeatmapCell> map = new HashMap<String, HeatmapCell>();

    for (HeatmapCell cell : oldtable) {

      if (cell.getAssesseds().get(1).exists()) {

        if (map.containsKey(cell.getAssesseds().get(1).megaUnnamedField())) {

          Date d1 = new Date();
          Date d2 = new Date();

          d1 = GRCDateUtility.getDateFromMega(cell.getAssesseds().get(1), GRCMetaAttribut.MA_CREATION_DATE);
          d2 = GRCDateUtility.getDateFromMega(map.get(cell.getAssesseds().get(1).megaUnnamedField()).getAssesseds().get(1), GRCMetaAttribut.MA_CREATION_DATE);

          if (d1.after(d2)) {
            map.put(cell.getAssesseds().get(1).megaUnnamedField(), cell);
            map.remove(cell.getAssesseds().get(1).megaUnnamedField());
          }

        } else {
          map.put(cell.getAssesseds().get(1).megaUnnamedField(), cell);
        }
      }
    }
    newtable.addAll(map.values());
    return newtable;
  }

  public static ArrayList<HeatmapCell> filterCells(final ArrayList<HeatmapCell> oldtable) {
    ArrayList<HeatmapCell> newtable = new ArrayList<HeatmapCell>();

    for (HeatmapCell cell1 : oldtable) {
      if (newtable.contains(cell1)) {
        HeatmapCell teatmapCell = newtable.get(newtable.indexOf(cell1));
        teatmapCell.getAssesseds().insert(cell1.getAssesseds());

      } else {
        newtable.add(cell1);
      }

    }
    return newtable;
  }

  public static ArrayList<HeatmapCell> getCellsNetRisk(final MegaRoot root, final ArrayList<AssessmentNode> nodes) {
    ArrayList<HeatmapCell> cells = new ArrayList<HeatmapCell>();
    for (AssessmentNode assNode : nodes) {
      HeatmapCell tempcell = new HeatmapCell(root);// a revoir la position
      MegaCollection assValues = assNode.getNode().getCollection(GRCMetaAssociationEnd.MAE_ASS_NODE_ASS_VALUE);
      for (MegaObject assValue : assValues) {
        if ((assValue.getCollection(GRCMetaAssociationEnd.MAE_ASS_VALUE_ASS_CHARACTERISTIC).size() > 0) && (assValue.getCollection("~o18yT9lCE1(K[MetaAttributeValue]").size() > 0)) {

          MegaObject assCharacteristic = assValue.getCollection(GRCMetaAssociationEnd.MAE_ASS_VALUE_ASS_CHARACTERISTIC).get(1);
          if (assCharacteristic.sameID(GRCConstants.AC_ERM_CONTROL_LEVEL) || assCharacteristic.sameID(GRCConstants.AC_ERM_INHERENT_RISK)) {
            int icontrolLevelValue = 0;
            int iInherentRiskValue = 0;
            if (assCharacteristic.sameID(GRCConstants.AC_ERM_CONTROL_LEVEL)) {
              try {
                icontrolLevelValue = Integer.parseInt(assValue.getCollection("~o18yT9lCE1(K[MetaAttributeValue]").get(1).getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
              } catch (NumberFormatException e) {}
              tempcell.setControlLevel(icontrolLevelValue);
            }
            if (assCharacteristic.sameID(GRCConstants.AC_ERM_INHERENT_RISK)) {
              try {
                iInherentRiskValue = Integer.parseInt(assValue.getCollection("~o18yT9lCE1(K[MetaAttributeValue]").get(1).getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
              } catch (NumberFormatException e) {}
              tempcell.setInherentRisk(iInherentRiskValue);
            }
            if ((assNode.getAssessed() != null) && !tempcell.getAssesseds().get(assNode.getAssessed()).exists()) {
              tempcell.getAssesseds().insert(assNode.getNode());
            }
          }
        }

      }
      if ((tempcell.getControlLevel() != 0) && (tempcell.getInherentRisk() != 0)) {
        cells.add(tempcell);
      }
    }

    return cells;
  }

  public static int getColumnNetRisk(final int invx) {
    int x = 0;
    switch (invx) {
      case 1:
        x = 2;
        break;
      case 4:
        x = 3;
        break;
      case 9:
        x = 4;
        break;
      case 16:
        x = 5;
        break;
      case 25:
        x = 6;
        break;
    }

    return x;
  }

  public static int getLineNetRisk(final int invy) {
    int y = 0;
    switch (invy) {
      case 1:
        y = 6;
        break;
      case 4:
        y = 5;
        break;
      case 9:
        y = 4;
        break;
      case 16:
        y = 3;
        break;
      case 25:
        y = 2;
        break;
    }

    return y;
  }

  /**
   * built a map of nodes composed by id session,id assessed object,id context
   * @param mapNodes
   * @param root
   * @param elements TODO
   * @param sessions
   */
  public static void setmapNodes(final Map<CompositeIdNode, MegaCollection> mapNodes, final MegaRoot root, final MegaCollection elements) {

    MegaCollection sessions = root.getCollection(GRCMetaClass.MC_AssessmentSession);

    for (MegaObject session : sessions) {

      Date dateEffectiveEndSession = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
      // choose only the closed session      
      if (session.getProp(GRCMetaAttribut.MA_SESSION_STATUS).equalsIgnoreCase(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED) && (dateEffectiveEndSession != null)) {

        MegaCollection nodes = root.getSelection("");

        //get all nodes from the two possible link
        nodes.insert(session.getCollection(GRCMetaAssociationEnd.MAE_ASS_VALUE_CONTEXT));

        for (MegaObject node : nodes) {

          // we take only one assessed object 
          if (node.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_OBJECT).size() == 1) {

            MegaObject assessedObject = node.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_OBJECT).get(1);

            if (assessedObject.getID() != null) {

              //risk type & objective case
              MegaObject classObj = elements.get(1).getClassObject();
              if (classObj.sameID(GRCMetaClass.MC_RISK_TYPE) || classObj.sameID(GRCMetaClass.MC_OBJECTIVE)) {
                CompositeIdNode compositeIdNode = new CompositeIdNode();
                compositeIdNode.setId_session(session.getProp("_idabs"));
                compositeIdNode.setId_assessedObject(assessedObject.getProp("_idabs"));
                if (mapNodes.get(compositeIdNode) != null) {
                  mapNodes.get(compositeIdNode).insert(node);
                } else {
                  MegaCollection nodesForIds = root.getSelection("");
                  nodesForIds.insert(node);
                  mapNodes.put(compositeIdNode, nodesForIds);
                }
              }
              //we take one or diffrent assessment context
              MegaCollection contexts = node.getCollection(GRCMetaAssociationEnd.MAE_ASSESSMENT_CONTEXT);
              if ((contexts != null) && (contexts.size() > 0)) {
                for (MegaObject context : contexts) {
                  if (elements.get(context).exists()) {

                    CompositeIdNode compositeIdNode = new CompositeIdNode();
                    compositeIdNode.setId_session(session.getProp("_idabs"));
                    compositeIdNode.setId_assessedObject(assessedObject.getProp("_idabs"));
                    if (mapNodes.get(compositeIdNode) != null) {
                      mapNodes.get(compositeIdNode).insert(node);
                    } else {
                      MegaCollection nodesForIds = root.getSelection("");
                      nodesForIds.insert(node);
                      mapNodes.put(compositeIdNode, nodesForIds);
                    }

                  }
                }

              }
            }

          }

        }

      }

    }
  }

  public static MegaCollection getElementsFromControl(final MegaRoot root, final MegaObject context, final MegaObject control) {
    MegaCollection elements = root.getSelection("");

    MegaObject classObject = context.getClassObject();

    MegaCollection controlElements = null;
    if (classObject.sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      controlElements = control.getCollection(GRCQuery.QUERY_GET_ELEMENTS_FROM_CONTROLS);
      elements.insert(controlElements);
      controlElements.release();

    } else if (classObject.sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {
      controlElements = control.getCollection(GRCQuery.QUERY_GET_ELEMENTS_FROM_CONTROLS);
      elements.insert(controlElements);
      controlElements.release();

    } else if (classObject.sameID(GRCMetaClass.MC_ORG_UNIT)) {
      controlElements = control.getCollection(GRCQuery.QUERY_GET_ELEMENTS_FROM_CONTROLS);
      elements.insert(controlElements);
      controlElements.release();

    } else if (classObject.sameID(GRCMetaClass.MC_OBJECTIVE)) {
      controlElements = control.getCollection(GRCQuery.QUERY_GET_OBJECTIVES_FROM_CONTROLS);
      elements.insert(controlElements);
      controlElements.release();

    } else if (classObject.sameID(GRCMetaClass.MC_CONTROL_TYPE)) {
      controlElements = control.getCollection(GRCQuery.QUERY_GET_CONTROLTYPES_FROM_CONTROLS);
      elements.insert(controlElements);
      controlElements.release();

    } else if (classObject.sameID(GRCMetaClass.MC_RISK_TYPE)) {
      controlElements = ContextualizedMethodsToolbox.getRiskTypesFromControl(root, control);
      elements.insert(controlElements);
      controlElements.release();

    }

    classObject.release();

    return elements;
  }

  public static MegaCollection getRiskTypesFromControl(final MegaRoot root, final MegaObject control) {
    MegaCollection risktypes = root.getSelection("");

    MegaCollection risksFromControls = control.getCollection(GRCQuery.QUERY_GET_RISKS_FROM_CONTROLS);

    MegaCollection riskTypesFromRisks = null;
    for (MegaObject risk : risksFromControls) {
      riskTypesFromRisks = risk.getCollection(GRCMetaAssociationEnd.MAE_RISK_TYPE_FROM_RISK);
      risktypes.insert(riskTypesFromRisks);
      riskTypesFromRisks.release();

    }

    risksFromControls.release();

    return risktypes;
  }

  public static ArrayList<AssessedObject> getControlsFromRisktype(final MegaObject riskType, final String linkQuery, final Date evalBeginDate, final Date evalEndDate) {

    ArrayList<AssessedObject> assessedObjects = new ArrayList<AssessedObject>();

    MegaCollection allLinkedAssessedObjects = GRCDataProcessing.getAllCtrlsForRiskType(riskType, linkQuery);

    for (MegaObject assessedObject : allLinkedAssessedObjects) {
      assessedObjects.add(new AssessedObject(assessedObject));
    }
    allLinkedAssessedObjects.release();
    MegaCollection allSubs = GRCDataProcessing.getAllSubs(riskType);
    assessedObjects = ContextualizedMethodsToolbox.filterEvaluatedObjects(allSubs, assessedObjects, false, evalBeginDate, evalEndDate);
    allSubs.release();
    return assessedObjects;
  }

  public static MegaCollection getElementsFromAssessedObjects(final MegaRoot root, final MegaObject context, final MegaObject moAssessedObject) {

    MegaCollection elements = root.getSelection("");

    //assessedObject is a Control
    MegaCollection elementsFromcontrol = null;
    MegaObject classAssessedObject = moAssessedObject.getClassObject();
    if (classAssessedObject.sameID(GRCMetaClass.MC_CONTROL)) {
      elementsFromcontrol = ContextualizedMethodsToolbox.getElementsFromControl(root, context, moAssessedObject);
      elements.insert(elementsFromcontrol);
      elementsFromcontrol.release();

    } else {
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
    }

    classAssessedObject.release();

    return elements;
  }

  public static String getMCassessesObject(final MegaObject moAssessedObject) {

    MegaObject objectClass = moAssessedObject.getClassObject();
    if (objectClass.sameID(GRCMetaClass.MC_RISK)) {
      objectClass.release();
      return GRCMetaClass.MC_RISK;
    }

    objectClass.release();
    return GRCMetaClass.MC_CONTROL;
  }

  /**
   * @param context
   * @param root
   * @param allsubs
   * @param idabsPreviousSubs TODO
   * @return get all subelements for an object
   */
  public static MegaCollection getAllSubs(final MegaObject context, final MegaRoot root, final MegaCollection allsubs, final ArrayList<String> idabsPreviousSubs) {

    MegaCollection subs = GRCDataProcessing.getSubsQuery(context);
    allsubs.insert(subs);
    for (MegaObject sub : subs) {
      String hexaIdabs = sub.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
      if (!idabsPreviousSubs.contains(hexaIdabs)) {
        idabsPreviousSubs.add(hexaIdabs);
        allsubs.insert(ContextualizedMethodsToolbox.getAllSubs(sub, root, allsubs, idabsPreviousSubs));
      }
    }
    return allsubs;
  }

  public static void addExcelViews(final ReportContent reportContent, final View composedView, final String year) {
    Dataset d = reportContent.getDataset(composedView.getDatasetId());
    View viewWithTwoTables = (View) d.getItem("3,1");
    Dataset datasetWithTwoTable = reportContent.getDataset(viewWithTwoTables.getDatasetId());

    View impactLikeLihoodView = (View) datasetWithTwoTable.getItem("1,1");
    impactLikeLihoodView.addParameter("tabletitle", year);
    reportContent.addView(impactLikeLihoodView);

    View mitigationInherentView = (View) datasetWithTwoTable.getItem("1,2");
    mitigationInherentView.addParameter("tabletitle", year);
    reportContent.addView(mitigationInherentView);

  }

  public static void orderAssessedObjectByContext(final AssessedObject aObject, final Map<String, Map<String, String>> risksByContext, final MegaCollection elementsLinked, final MegaCollection contexts, final Map<String, MegaCollection> contextSubs, final boolean checkBySubs) {
    for (MegaObject element : elementsLinked) {
      for (MegaObject context : contexts) {
        MegaCollection mgContextSubs = contextSubs.get(context.megaUnnamedField());
        if (mgContextSubs != null) {
          MegaObject obj = mgContextSubs.get(element.megaUnnamedField());
          boolean exist = obj.exists();
          obj.release();
          if (exist) {
            Map<String, String> contextRisksMap = risksByContext.get(context.megaUnnamedField());
            String key = GRCConstants.NOT_EVALUATED;
            if (checkBySubs) {
              if (ContextualizedMethodsToolbox.checkEvaluationContext(mgContextSubs, aObject.getContexts())) {
                key = GRCConstants.EVALUATED;
              }
            } else if (aObject.isEvaluated()) {
              key = GRCConstants.EVALUATED;
            }

            String risksLinked = contextRisksMap.get(key);
            if ((risksLinked == null) || risksLinked.equals("")) {
              risksLinked = aObject.getAssessedObject().megaUnnamedField();
            } else if (!risksLinked.contains(aObject.getAssessedObject().megaUnnamedField())) {
              risksLinked = risksLinked + "," + aObject.getAssessedObject().megaUnnamedField();
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

  private static boolean checkEvaluationContext(final MegaCollection subs, final MegaCollection contextsMeasures) {
    if ((contextsMeasures != null) && (contextsMeasures.size() > 0)) {
      for (MegaObject contextsMeasure : contextsMeasures) {
        MegaObject _obj = subs.get(contextsMeasure.megaUnnamedField());
        if (_obj.exists()) {
          _obj.release();
          contextsMeasure.release();
          return true;
        }
        _obj.release();
        contextsMeasure.release();
      }
    }
    return false;
  }

  public static void orderRisksByDate(final Map<String, Map<String, String>> risksByDate, final AssessedObject risk) {
    Date riskCreationDate = GRCDateUtility.getDateFromMega(risk.getAssessedObject(), GRCMetaAttribut.MA_CREATION_DATE);
    if (riskCreationDate != null) {
      String key = GRCDateUtility.getYear(riskCreationDate) + "-" + GRCDateUtility.getMonth(riskCreationDate);
      Map<String, String> riskEvaluationsByDate = risksByDate.get(key);
      if (riskEvaluationsByDate != null) {
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
  }

  public static Map<String, Map<String, String>> getRisksbyDateMap() {
    LinkedHashMap<String, Map<String, String>> incidentsByMonths = new LinkedHashMap<String, Map<String, String>>();
    Date currentDate = GRCDateUtility.resetEndDateTime(new Date());
    //initialize the map by the eleven past month
    for (int i = 11; i >= 0; i--) {
      Date _date = GRCDateUtility.addTimeAmount(currentDate, Calendar.MONTH, -i);
      Integer month = GRCDateUtility.getMonth(_date);
      Integer year = GRCDateUtility.getYear(_date);
      Map<String, String> riskEvaluationsByDate = new HashMap<String, String>();
      riskEvaluationsByDate.put(GRCConstants.EVALUATED, "");
      riskEvaluationsByDate.put(GRCConstants.NOT_EVALUATED, "");
      incidentsByMonths.put(year + "-" + month, riskEvaluationsByDate);
    }
    return incidentsByMonths;
  }

  public static void orderElementsByContext(final MegaObject aObject, final Map<String, String> elementsByContext, final MegaCollection elementsLinked, final MegaCollection contexts, final Map<String, MegaCollection> contextSubs) {
    for (MegaObject element : elementsLinked) {
      for (MegaObject context : contexts) {
        MegaCollection mgContextSubs = contextSubs.get(context.megaUnnamedField());
        if (mgContextSubs != null) {
          MegaObject obj = mgContextSubs.get(element.megaUnnamedField());
          boolean exist = obj.exists();
          obj.release();
          if (exist) {
            String elements = elementsByContext.get(context.megaUnnamedField());
            if ((elements == null) || elements.equals("")) {
              elements = aObject.megaUnnamedField();
            } else if (!elements.contains(aObject.megaUnnamedField())) {
              elements = elements + "," + aObject.megaUnnamedField();
            }
            elementsByContext.put(context.megaUnnamedField(), elements);
          }
        }
      }
      element.release();
    }
  }

  public static Map<String, String> getMapsByContexts(final MegaCollection contexts) {
    Map<String, String> risksByContexts = new HashMap<String, String>();
    for (MegaObject context : contexts) {
      risksByContexts.put(context.megaUnnamedField(), "");
      context.release();
    }
    return risksByContexts;
  }

}


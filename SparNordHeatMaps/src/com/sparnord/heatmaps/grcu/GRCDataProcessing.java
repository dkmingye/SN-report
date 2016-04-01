package com.sparnord.heatmaps.grcu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mega.extraction.ExtractionPath;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaCurrentEnvironment;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.mega.modeling.api.util.MegaResources;
import com.sparnord.heatmaps.grcu.assessment.Evaluation;
import com.sparnord.heatmaps.grcu.colors.GRCColorsUtility;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;
import com.sparnord.heatmaps.grcu.constants.GRCQuery;
import com.mega.toolkit.errmngt.ErrorLogFormater;

/**
 * Utility java class for grouping all useful methods handling common Mega
 * operations for different reports kind
 * @author JEG
 * @since HOPEX 1.1
 */
public final class GRCDataProcessing {

  public static void getObjectsByQuery(final MegaRoot root, final String sQueryID, final MegaCollection mcResultCollection) {
    MegaCollection mcObjects;
    mcObjects = root.getCollection(sQueryID);
    if (mcObjects != null) {
      mcResultCollection.insert(mcObjects);
    }
  }

  /**
   * get the correct query to get ctrls from an object
   * @param object (process,orgUnit,riskType,Account ...)
   * @return
   */
  public static String getCtrlsQuery(final MegaObject object) {

    String sCtrlsQuery = "";
    MegaObject objectBusinessProcess = object.getType(GRCMetaClass.MC_BUSINESS_PROCESS);
    MegaObject objectOrgProcess = object.getType(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS);
    MegaObject objectOrgUnit = object.getType(GRCMetaClass.MC_ORGUNIT);
    MegaObject objectRisk = object.getType(GRCMetaClass.MC_RISK);
    MegaObject objectAccount = object.getType(GRCMetaClass.MC_ACCOUNT);
    MegaObject objectControlType = object.getType(GRCMetaClass.MC_CONTROL_TYPE);
    MegaObject objectOperation = object.getType(GRCMetaClass.MC_OPERATION);
    MegaObject objectObjective = object.getType(GRCMetaClass.MC_OBJECTIVE);

    if (objectBusinessProcess.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_BU_PROCESS;
    } else if (objectOrgProcess.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_ORG_PROCESS;
    } else if (objectOrgUnit.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_ORG;
    } else if (objectAccount.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_ACOOUNT;
    } else if (objectControlType.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_CONTROL_CONTROLTYPE;
    } else if (objectRisk.getID() != null) {
      sCtrlsQuery = GRCMetaAssociationEnd.MAE_CONTROL_FROM_RISK;
    } else if (objectOperation.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_OPERATION;
    } else if (objectOperation.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_OPERATION;
    } else if (objectObjective.getID() != null) {
      sCtrlsQuery = GRCQuery.QUERY_GET_CTRLS_FROM_OBJECTIVE;
    }

    objectBusinessProcess.release();
    objectOrgProcess.release();
    objectOrgUnit.release();
    objectRisk.release();
    objectAccount.release();
    objectControlType.release();
    objectObjective.release();

    return sCtrlsQuery;
  }

  /**
   * get the correct query to get risks from an object
   * @param object (process,orgUnit,riskType,Account ...)
   * @return
   */
  public static String getRisksQuery(final MegaObject object) {

    if (object.getClassObject().sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {

      return GRCMetaAssociationEnd.MAE_RISK_FROM_BUSINESS_PROCESS;

    } else if (object.getClassObject().sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {

      return GRCMetaAssociationEnd.MAE_RISK_FROM_ORGANIZATIONAL_PROCESS;

    } else if (object.getClassObject().sameID(GRCMetaClass.MC_ORG_UNIT)) {

      return GRCMetaAssociationEnd.MAE_RISK_FROM_ORG_UNIT;
    } else if (object.getClassObject().sameID(GRCMetaClass.MC_OBJECTIVE)) {

      return GRCQuery.QUERY_RISK_OBJECTIVE;
    } else if (object.getClassObject().sameID(GRCMetaClass.MC_RISK_TYPE)) {

      return GRCQuery.QUERY_RISK_RISKTYPE;
    }

    return "";
  }

  public static MegaCollection getAllCtrlsForRiskType(final MegaObject riskType, final String linkQuery) {
    MegaRoot root = riskType.getRoot();
    MegaCollection ctrlsForRiskType = root.getSelection("");
    root.release();

    ArrayList<String> idabsPreviousSubs = new ArrayList<String>();
    MegaCollection risksForRiskType = GRCDataProcessing.getAllElementsFromContext(riskType, linkQuery, idabsPreviousSubs);

    for (MegaObject risk : risksForRiskType) {
      ctrlsForRiskType.insert(risk.getCollection(GRCMetaAssociationEnd.MAE_CONTROL_FROM_RISK));
    }

    return ctrlsForRiskType;
  }

  public static MegaCollection getSubsQuery(final MegaObject object) {

    MegaRoot root = object.getRoot();
    MegaCollection subElements = root.getSelection("");
    root.release();

    MegaCollection mcObjectsByQuery = null;

    MegaObject objectClass = object.getClassObject();

    if (objectClass.sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_BPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_GET_ORGPROCESS_FROM_BUPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_ORGPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(GRCMetaClass.MC_ORG_UNIT)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_ORGUNIT);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(GRCMetaClass.MC_OBJECTIVE)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_OBJECTIVE);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(GRCMetaClass.MC_ACCOUNT)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_ACCOUNT);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(GRCMetaClass.MC_CONTROL_TYPE)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_CONTROLTYPE);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();
    } else if (objectClass.sameID(GRCMetaClass.MC_RISK_TYPE)) {
      mcObjectsByQuery = object.getCollection(GRCQuery.QUERY_SUB_RISKTYPE);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();
    }

    objectClass.release();

    return subElements;

  }

  public static MegaCollection getAllElementsFromContext(final MegaObject contextObject, final String linkQuery, final ArrayList<String> idabsPreviousSubs) {
    MegaRoot root = contextObject.getRoot();
    MegaCollection mcElementsFromContext = root.getSelection("");
    root.release();
    if (!linkQuery.equals("")) {
      MegaCollection mcLinkedObject = contextObject.getCollection(linkQuery);
      mcElementsFromContext.insert(mcLinkedObject);
      mcLinkedObject.release();
    }
    idabsPreviousSubs.add(contextObject.getProp(GRCMetaAttribut.MA_HEX_ID_ABS));
    MegaCollection subs = GRCDataProcessing.getSubsQuery(contextObject);
    for (MegaObject suborg : subs) {
      if (!idabsPreviousSubs.contains(suborg.getProp(GRCMetaAttribut.MA_HEX_ID_ABS))) {
        MegaCollection mcAllElementsFromContext = GRCDataProcessing.getAllElementsFromContext(suborg, linkQuery, idabsPreviousSubs);
        mcElementsFromContext.insert(mcAllElementsFromContext);
        mcAllElementsFromContext.release();
      }
      suborg.release();
    }
    subs.release();
    return mcElementsFromContext;
  }

  /**
   * @param root megaRoot
   * @param element megaoBject
   * @param parentObject megaObject parent
   * @param idabsPreviousParents TODO
   * @return search if the element is children of the context (parentObject)
   *         chosen
   */
  public static boolean isChildrenOf(final MegaObject element, final MegaObject parentObject, final ArrayList<String> idabsPreviousParents) {

    MegaCollection parents = null;
    parents = GRCDataProcessing.getParents(element);

    if (parents.size() > 0) {

      for (MegaObject parent : parents) {
        String hexaIdabs = parent.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
        if (!idabsPreviousParents.contains(hexaIdabs)) {
          idabsPreviousParents.add(hexaIdabs);
          if (parent.sameID(parentObject.getID()) || GRCDataProcessing.isChildrenOf(parent, parentObject, idabsPreviousParents)) {
            parent.release();
            parents.release();
            return true;
          }
        }
        parent.release();
      }
    }

    parents.release();

    return false;
  }

  public static MegaCollection getElementsFromControl(final MegaRoot root, final MegaObject context, final MegaObject control) {
    MegaCollection elements = root.getSelection("");

    if (context.getClassObject().sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {

      elements.insert(control.getCollection(GRCQuery.QUERY_GET_BUSINESS_PROCESS_FROM_CONTROLS));
      elements.insert(control.getCollection(GRCQuery.QUERY_GET_ORGANIZATIONAL_PROCESS_FROM_CONTROLS));

    } else if (context.getClassObject().sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {

      elements.insert(control.getCollection(GRCQuery.QUERY_GET_ORGANIZATIONAL_PROCESS_FROM_CONTROLS));

    } else if (context.getClassObject().sameID(GRCMetaClass.MC_ORG_UNIT)) {

      elements.insert(control.getCollection(GRCQuery.QUERY_GET_ENTITIES_FROM_CONTROLS));
    } else if (context.getClassObject().sameID(GRCMetaClass.MC_ACCOUNT)) {

      elements.insert(control.getCollection(GRCQuery.QUERY_GET_ACCOUNT_FROM_CONTROLS));

    } else if (context.getClassObject().sameID(GRCMetaClass.MC_CONTROL_TYPE)) {

      elements.insert(control.getCollection(GRCQuery.QUERY_GET_CONTROL_TYPE_FROM_CONTROLS));

    } else if (context.getClassObject().sameID(GRCMetaClass.MC_RISK_TYPE)) {
      elements.insert(GRCDataProcessing.getRiskTypesFromControl(root, control));

    }

    return elements;
  }

  private static MegaCollection getRiskTypesFromControl(final MegaRoot root, final MegaObject control) {
    MegaCollection risktypes = root.getSelection("");

    MegaCollection risksFromControls = control.getCollection(GRCQuery.QUERY_GET_RISKS_FROM_CONTROLS);

    for (MegaObject risk : risksFromControls) {
      risktypes.insert(risk.getCollection(GRCMetaAssociationEnd.MAE_RISK_TYPE_FROM_RISK));
    }
    return risktypes;

  }

  public static MegaCollection getParents(final MegaObject object) {
    MegaRoot root = object.getRoot();
    MegaCollection parents = root.getSelection("");
    root.release();

    MegaObject objectBusinessProcess = object.getType(GRCMetaClass.MC_BUSINESS_PROCESS);
    MegaObject objectOrgProcess = object.getType(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS);
    MegaObject objectOrgUnit = object.getType(GRCMetaClass.MC_ORGUNIT);
    MegaObject objectObjective = object.getType(GRCMetaClass.MC_OBJECTIVE);
    MegaObject objectAccount = object.getType(GRCMetaClass.MC_ACCOUNT);
    MegaObject objectRiskType = object.getType(GRCMetaClass.MC_RISK_TYPE);
    MegaObject objectControlType = object.getType(GRCMetaClass.MC_CONTROL_TYPE);

    MegaCollection mcLinkedObjects = null;

    if (objectBusinessProcess.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCConstants.QUERY_GET_PARENT_BUPROCESS_FROM_BU_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectOrgProcess.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCConstants.QUERY_GET_PARENT_ORGPROCESS_FROM_ORG_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();
      mcLinkedObjects = object.getCollection(GRCConstants.QUERY_GET_PARENT_BUPROCESS_FROM_ORG_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectOrgUnit.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCConstants.QUERY_GET_PARENT_ORGUNIT_FROM_ORGUNIT);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectObjective.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCMetaAssociationEnd.MAE_PARENT_OBJECTIVE_FROM_OBJECTIVE);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectAccount.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCMetaAssociationEnd.MAE_PARENT_ACCOUNT_FROM_ACCOUNT);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectRiskType.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCMetaAssociationEnd.MAE_PARENT_RISK_TYPE_FROM_RISK_TYPE);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectControlType.getID() != null) {
      mcLinkedObjects = object.getCollection(GRCConstants.QUERY_GET_PARENT_CONTROLTYPE_FROM_CONTROLTYPE);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    }

    objectBusinessProcess.release();
    objectOrgProcess.release();
    objectOrgUnit.release();
    objectObjective.release();
    objectAccount.release();
    objectRiskType.release();
    objectControlType.release();

    return parents;
  }

  /**
   * @param element megaObject
   * @param context megaObject To compare with
   * @return boolean
   */

  public static Boolean sameType(final MegaObject element, final MegaObject context) {

    MegaObject classMgObject = context.getClassObject();
    String classObject = classMgObject.megaField();

    MegaObject _element = element.getType(classObject);
    if (_element.getID() != null) {
      classMgObject.release();
      _element.release();
      return true;
    }

    _element.release();

    MegaObject _elementOrgProcess = element.getType(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS);
    if ((_elementOrgProcess.getID() != null) && classMgObject.sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      classMgObject.release();
      _elementOrgProcess.release();
      return true;
    }

    _elementOrgProcess.release();
    classMgObject.release();

    return false;
  }

  /**
   * @param megaobject MegaObject ID
   * @param maeField String (MetaAssociationEnd)
   * @return collMaeSize int Collection size language
   */
  public static int sizeOf(final MegaObject megaobject, final String maeField) {
    MegaCollection collMae = megaobject.getCollection(maeField);
    int collMaeSize = collMae.size();
    collMae.release();
    return collMaeSize;
  }

  /**
   * @param ID codeTemplate ID
   * @param root megaRoot
   * @return a String from the translatable code template in the current
   *         language
   */
  public static String getCodeTemplate(final String ID, final MegaRoot root) {
    if (ID == null) {
      return "";
    }
    if (ID.length() == 0) {
      return "";
    }
    MegaCurrentEnvironment env = root.currentEnvironment();
    MegaResources resources = env.resources();
    env.release();
    String sResult = resources.codeTemplate(ID, "");
    resources.basedObj.release();
    if ((sResult == null) || (sResult.length() == 0)) {
      final ErrorLogFormater err = new ErrorLogFormater();
      err.openSession(root);
      err.logMessage("Code template [" + ID + "] not found or empty.");
      err.closeSession();
      return "";
    }
    return sResult;
  }

  /**
   * get the first sub processes
   * @param process
   * @param root
   * @return
   */

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

  public static MegaCollection getAllSubElements(final MegaObject object, final ArrayList<String> idabsPreviousSubs) {
    MegaRoot root = object.getRoot();
    MegaCollection allSubElements = root.getSelection("");
    root.release();

    MegaCollection mcSubQueries = GRCDataProcessing.getSubsQuery(object);
    allSubElements.insert(mcSubQueries);
    mcSubQueries.release();

    for (MegaObject subElement : allSubElements) {
      String hexaIdabs = subElement.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
      if (!idabsPreviousSubs.contains(hexaIdabs)) {
        idabsPreviousSubs.add(hexaIdabs);

        MegaCollection _allSubElements = GRCDataProcessing.getAllSubElements(subElement, idabsPreviousSubs);
        allSubElements.insert(_allSubElements);
        _allSubElements.release();
      }

      subElement.release();
    }

    return allSubElements;
  }

  public static MegaObject getObjectFromMega(final MegaRoot root, final String metaclass, final String idObject) {
    if (!metaclass.equals("")) {
      return root.getCollection(metaclass).get(idObject);
    }
    return root.getObjectFromID(idObject);
  }

  /**
   * @param metaValue metaAttributeValue
   * @return a table that contains : t[0] = image (gif) ;t[1] = Color (in hexa)
   *         ;t[2] = ValueName
   */
  public static String[] getElementsFromMetaAttributeValue(final MegaObject metaValue) {
    String[] elements = new String[3];
    elements[0] = "";
    elements[2] = "";
    elements[1] = "";
    if ((metaValue != null) && (metaValue.getID() != null)) {
      MegaCollection metaPGRCures = metaValue.getCollection(GRCMetaAssociationEnd.MAE_PICTURE);
      if (metaPGRCures.size() > 0) {
        MegaObject metaPGRCure = metaPGRCures.get(1);
        metaPGRCures.release();
        elements[0] = metaPGRCure.megaField();
        metaPGRCure.release();
      }
      elements[2] = metaValue.getProp(GRCMetaAttribut.MA_VALUE_NAME);
      elements[1] = GRCColorsUtility.getColorOfMetattributeVal(metaValue);
    }
    return elements;
  }

  public static void releaseMgCollection(final MegaCollection collection) {
    for (MegaObject item : collection) {
      item.release();
    }
    collection.release();
  }

  public static MegaCollection getAllSubs(final MegaObject object) {
    MegaRoot root = object.getRoot();
    MegaCollection _subs = root.getSelection("");

    MegaObject _typeObj = object.getType("");
    MegaObject classObj = _typeObj.getClassObject();
    _typeObj.release();
    if (classObj.sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS) || classObj.sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_BUSINESS_PROCESS, GRCQuery.QUERY_SUB_BPROCESS, true, true);
      EP.addPathItem(GRCMetaClass.MC_BUSINESS_PROCESS, GRCQuery.QUERY_GET_ORGPROCESS_FROM_BUPROCESS, true, true);
      EP.addPathItem(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS, GRCQuery.QUERY_SUB_ORGPROCESS, true, true);
      MegaCollection subProcesses = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subProcesses;
    }

    if (classObj.sameID(GRCMetaClass.MC_ORG_UNIT)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_ORG_UNIT, GRCQuery.QUERY_SUB_ORGUNIT, true, true);
      MegaCollection subOrgs = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subOrgs;
    }

    if (classObj.sameID(GRCMetaClass.MC_OBJECTIVE)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_OBJECTIVE, GRCQuery.QUERY_SUB_OBJECTIVE, true, true);
      MegaCollection subObjectives = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subObjectives;
    }

    if (classObj.sameID(GRCMetaClass.MC_RISK_TYPE)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_RISK_TYPE, GRCQuery.QUERY_SUB_RISKTYPE, true, true);
      MegaCollection subRiskTypes = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subRiskTypes;
    }

    if (classObj.sameID(GRCMetaClass.MC_CONTROL_TYPE)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_CONTROL_TYPE, GRCQuery.QUERY_SUB_CONTROLTYPE, true, true);
      MegaCollection subRiskTypes = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subRiskTypes;
    }

    if (classObj.sameID(GRCMetaClass.MC_ACCOUNT)) {
      _subs.insert(object);
      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(root, null);
      EP.addPathItem(GRCMetaClass.MC_ACCOUNT, GRCQuery.QUERY_SUB_ACCOUNT, true, true);
      MegaCollection subRiskTypes = EP.extractDataFromPath(_subs, true);
      root.release();
      classObj.release();
      _subs.release();
      return subRiskTypes;
    }
    root.release();
    classObj.release();
    return _subs;
  }

  /**
   * @param mgObject a megaObject
   * @return the right query to get action plans
   */
  public static String getActionPlanQuery(final MegaObject mgObject) {
    MegaObject classObject = mgObject.getClassObject();
    String result = "";
    if (classObject.sameID(GRCMetaClass.MC_BUSINESS_PROCESS)) {
      result = GRCMetaAssociationEnd.MAE_OWNED_ACTION_PLAN_BUSINESS_PROCESS;

    } else if (classObject.sameID(GRCMetaClass.MC_ORGANIZATIONAL_PROCESS)) {

      result = GRCQuery.QUERY_GET_ACTION_PLANS_FROM_ORG_PROCESS;

    } else if (classObject.sameID(GRCMetaClass.MC_ORG_UNIT)) {

      result = GRCQuery.QUERY_GET_ACTION_PLANS_FROM_ORGUNIT;
    }
    classObject.release();
    return result;
  }

  /**
   * @param mgAssessedCharac assessed characteristic
   * @return a map of evaluations of the metaAttributeValues linked to the
   *         metaAttribute linked to the assessed characteristic
   */
  public static Map<Integer, Evaluation> getEvaluationFromCharacteristic(final MegaObject mgAssessedCharac) {
    Map<Integer, Evaluation> evaluations = new LinkedHashMap<Integer, Evaluation>();
    if ((mgAssessedCharac != null) && (mgAssessedCharac.getID() != null)) {
      MegaCollection metaAttributes = mgAssessedCharac.getCollection(GRCMetaAssociationEnd.MAE_ASS_CHARACTERISTIC_METAATTRIBUTE);
      if (metaAttributes.size() > 0) {
        MegaObject metaAttribute = metaAttributes.get(1);
        metaAttributes.release();
        MegaCollection metaAttributeValues = metaAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order");
        metaAttribute.release();
        for (MegaObject metaValue : metaAttributeValues) {
          String[] elementsOfMetaValue = GRCDataProcessing.getElementsFromMetaAttributeValue(metaValue);
          Evaluation metaAttributeEval = new Evaluation();
          metaAttributeEval.setMetaPicture(elementsOfMetaValue[0]);
          metaAttributeEval.setColor(elementsOfMetaValue[1]);
          metaAttributeEval.setValueName(elementsOfMetaValue[2]);
          int internalValue = Integer.valueOf(metaValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
          metaAttributeEval.setInternalValue(internalValue);
          evaluations.put(internalValue, metaAttributeEval);
          metaValue.release();
        }
      }
    }
    return evaluations;
  }

  /**
   * ARK
   * @param elements a table that contains : t[0] = image (gif) ;t[1] = Color
   *          (in hexa) ;t[2] = ValueName
   * @param metaValue metaAttributeValue linked to an assessedCharac
   */
  public static void setelements(final String[] elements, final MegaObject metaValue) {
    elements[0] = "";
    elements[2] = "";
    elements[1] = "";
    if ((metaValue != null) && (metaValue.getID() != null)) {
      MegaCollection pictures = metaValue.getCollection(GRCMetaAssociationEnd.MAE_PICTURE);
      if (pictures.size() > 0) {
        MegaObject picture = pictures.get(1);
        pictures.release();
        MegaCollection stdfiles = picture.getCollection(GRCMetaAssociationEnd.MAE_STD_FILES);
        picture.release();
        for (MegaObject std : stdfiles) {
          if (std.getProp(GRCMetaAttribut.MA_SHORT_NAME).toLowerCase().contains("gif")) {
            elements[0] = std.getProp(GRCMetaAttribut.MA_SHORT_NAME);
          }
          std.release();
        }
        stdfiles.release();
      }
      elements[2] = metaValue.getProp(GRCMetaAttribut.MA_VALUE_NAME);
      elements[1] = GRCColorsUtility.Color2Hex(GRCColorsUtility.getRGBfromParam(metaValue));
    }
  }

  public static MegaObject getFirstElement(final MegaObject megaobject, final String maeField) {
    MegaCollection collMae = megaobject.getCollection(maeField);
    MegaObject obj = collMae.get(1);
    collMae.release();
    return obj;
  }

  public static boolean isTypeOf(final MegaObject megaobject, final String metaClassID) {
    MegaObject typeObj = megaobject.getType(metaClassID);
    boolean isTypeof = typeObj.exists();
    typeObj.release();
    return isTypeof;
  }

  public static boolean contains(final MegaCollection megacollection, final MegaObject megaobject) {
    MegaObject object = megacollection.get(megaobject.getID());
    boolean bExists = false;
    if (object.exists()) {
      bExists = true;
    }
    object.release();
    return bExists;
  }

}


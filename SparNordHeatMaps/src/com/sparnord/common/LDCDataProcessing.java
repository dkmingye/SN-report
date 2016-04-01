package com.sparnord.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.mega.toolkit.errmngt.ErrorLogFormater;

public class LDCDataProcessing {

  /**
   * @param ID String
   * @param root MegaRoot
   * @return the code template string
   */
  public static String getCodeTemplate(final String ID, final MegaRoot root) {
    if (ID == null) {
      return "";
    }
    if (ID.length() == 0) {
      return "";
    }
    String sResult = root.currentEnvironment().resources().codeTemplate(ID, "");
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
   * @param month Integer
   * @return last eleven month from the given month
   */
  public static int[] lastTwelveMonths(final int month) {
    int[] months = new int[12];
    int mth = (12 - month);
    for (int i = 0; i < month; i++) {
      months[i] = month - i;
    }
    for (int j = 0; j < mth; j++) {
      months[11 - j] = month + j + 1;
    }
    return months;
  }

  /**
   * @param parameters Parameters of the current report template
   * @return User parameter
   */
  public static MegaObject currentObject(final Map<String, List<AnalysisParameter>> parameters) {
    MegaObject currentObject = null;

    for (final String paramType : parameters.keySet()) {
      for (final AnalysisParameter paramValue : parameters.get(paramType)) {

        for (final MegaObject value : paramValue.getValues()) {

          if (value.getID() != null) {
            return value;
          }

        }
      }
    }
    return currentObject;
  }

  /**
   * @param object megaObject
   * @param root MegaRoot
   * @return GET the query for the sub elements for an object
   */
  public static MegaCollection getSubsQuery(final MegaObject object) {
    MegaRoot root = object.getRoot();
    MegaCollection subElements = root.getSelection("");
    root.release();

    MegaCollection mcObjectsByQuery = null;

    MegaObject objectClass = object.getClassObject();

    if (objectClass.sameID(LDCConstants.MC_BUSINESS_PROCESS)) {
      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_SUB_BPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_GET_ORGPROCESS_FROM_BUPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(LDCConstants.MC_ORGANIZATIONAL_PROCESS)) {
      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_SUB_ORGPROCESS);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(LDCConstants.MC_ORG_UNIT)) {
      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_SUB_ORGUNIT);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(LDCConstants.MC_BUSINESS_LINE)) {
      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_SUB_BUSINESS_LINE);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    } else if (objectClass.sameID(LDCConstants.MC_RISK_TYPE)) {
      mcObjectsByQuery = object.getCollection(LDCConstants.QUERY_SUB_RISKTYPE);
      subElements.insert(mcObjectsByQuery);
      mcObjectsByQuery.release();

    }

    objectClass.release();

    return subElements;

  }

  /**
   * @param root
   * @param contextObject
   * @param linkQuery
   * @param idabsPreviousSubs TODO
   * @return get all elements (action plans,risks,controls ..) from a megaObject
   *         (process,orgUnit,objective)
   */
  public static MegaCollection getAllElementsFromContext(final MegaObject contextObject, final String linkQuery, final ArrayList<String> idabsPreviousSubs) {

    MegaRoot root = contextObject.getRoot();
    MegaCollection mcElementsFromContext = root.getSelection("");
    root.release();

    MegaCollection mcLinkedObject = contextObject.getCollection(linkQuery);
    mcElementsFromContext.insert(mcLinkedObject);
    mcLinkedObject.release();

    idabsPreviousSubs.add(contextObject.getProp(LDCConstants.MA_HEX_ID_ABS));
    MegaCollection subs = LDCDataProcessing.getSubsQuery(contextObject);
    for (MegaObject suborg : subs) {
      if (!idabsPreviousSubs.contains(suborg.getProp(LDCConstants.MA_HEX_ID_ABS))) {
        MegaCollection mcAllElementsFromContext = LDCDataProcessing.getAllElementsFromContext(suborg, linkQuery, idabsPreviousSubs);
        mcElementsFromContext.insert(mcAllElementsFromContext);
        mcAllElementsFromContext.release();

      }
      suborg.release();
    }
    subs.release();
    return mcElementsFromContext;
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
    MegaObject _elementOrgProcess = element.getType(LDCConstants.MC_ORGANIZATIONAL_PROCESS);
    if ((_elementOrgProcess.getID() != null) && classMgObject.sameID(LDCConstants.MC_BUSINESS_PROCESS)) {
      classMgObject.release();
      _elementOrgProcess.release();
      return true;
    }
    _elementOrgProcess.release();
    classMgObject.release();
    return false;
  }

  public static MegaCollection getParents(final MegaObject object) {
    MegaRoot root = object.getRoot();
    MegaCollection parents = root.getSelection("");
    root.release();

    MegaObject objectBusinessProcess = object.getType(LDCConstants.MC_BUSINESS_PROCESS);
    MegaObject objectOrgProcess = object.getType(LDCConstants.MC_ORGANIZATIONAL_PROCESS);
    MegaObject objectOrgUnit = object.getType(LDCConstants.MC_ORG_UNIT);
    MegaObject objectRiskType = object.getType(LDCConstants.MC_RISK_TYPE);
    MegaObject objectBuinessLine = object.getType(LDCConstants.MC_BUSINESS_LINE);

    MegaCollection mcLinkedObjects = null;

    if (objectBusinessProcess.getID() != null) {
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_BUPROCESS_FROM_BU_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectOrgProcess.getID() != null) {
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_ORGPROCESS_FROM_ORG_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_BUPROCESS_FROM_ORG_PROCESS);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectOrgUnit.getID() != null) {
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_ORGUNIT_FROM_ORGUNIT);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectRiskType.getID() != null) {
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_RISK_TYPE_FROM_RISK_TYPE);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    } else if (objectBuinessLine.getID() != null) {
      mcLinkedObjects = object.getCollection(LDCConstants.QUERY_GET_PARENT_BUSINESS_LINE_FROM_BUSINESS_LINE);
      parents.insert(mcLinkedObjects);
      mcLinkedObjects.release();

    }

    objectBusinessProcess.release();
    objectOrgProcess.release();
    objectOrgUnit.release();
    objectRiskType.release();
    objectBuinessLine.release();

    return parents;
  }

  /**
   * @param element megaoBject
   * @param parentObject megaObject parent
   * @param idabsPreviousParents TODO
   * @return search if the element is children of the context (parentObject)
   *         chosen
   */
  public static boolean isChildrenOf(final MegaObject element, final MegaObject parentObject, final ArrayList<String> idabsPreviousParents) {

    MegaCollection parents = LDCDataProcessing.getParents(element);

    if (parents.size() > 0) {
      for (MegaObject parent : parents) {
        String hexaIdabs = parent.getProp(LDCConstants.MA_HEX_ID_ABS);
        if (!idabsPreviousParents.contains(hexaIdabs)) {
          idabsPreviousParents.add(hexaIdabs);
          if (parent.sameID(parentObject.getID()) || LDCDataProcessing.isChildrenOf(parent, parentObject, idabsPreviousParents)) {
            parents.release();
            parent.release();
            return true;
          }
          parent.release();
          continue;
        }
        parents.release();
        parent.release();
        return false;
      }
    }

    parents.release();
    return false;
  }

}


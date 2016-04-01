package com.sparnord.heatmaps.grcu.assessment;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.grcu.GRCDataProcessing;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.constants.GRCCodeTemplate;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;

/**
 * @author JEG
 */
public class AssessmentEngine {

  public static ArrayList<AssessedObject> filterEvaluatedObjects(final MegaRoot root, final MegaObject contextObject, final ArrayList<AssessedObject> assessedObjects) {
    ArrayList<AssessedObject> filtredAssessedObjects = assessedObjects;
    // Get collection nodes
    ArrayList<AssessmentNode> listAssessmentNodes = AssessmentEngine.loadAssessmentNodes(root);
    // Check if context object is evaluated
    for (AssessmentNode assessmentNode : listAssessmentNodes) {
      for (AssessedObject assessedObject : filtredAssessedObjects) {
        if ((assessmentNode.getAssessed() != null) && (assessmentNode.getAssessed().getID() != null)) {
          if (assessmentNode.getAssessed().sameID(assessedObject.getAssessedObject().getID()) && assessmentNode.containsContextOrSubContext(contextObject)) {
            assessedObject.setEvaluated(true);
            assessedObject.getContexts().insert(assessmentNode.getContexts());
          }
        }
      }
    }

    return filtredAssessedObjects;
  }

  public static MegaObject getSessionFromNode(final MegaObject node) {

    MegaCollection sessionFromFirstLink = node.getCollection(GRCMetaAssociationEnd.MAE_NODE_ASSESSMENT_SESSION);
    MegaObject session;
    if (sessionFromFirstLink.size() > 0) {
      session = sessionFromFirstLink.get(1);
      if (session.getID() != null) {
        sessionFromFirstLink.release();
        return session;
      }

    }
    sessionFromFirstLink.release();
    return null;

  }

  /**
   * get all nodes from assessment sessions
   * @param root
   * @return
   */
  public static ArrayList<AssessmentNode> loadAssessmentNodes(final MegaRoot root) {
    return AssessmentEngine.loadAssessmentNodes(root, false);
  }

  /**
   * get all nodes from assessment sessions
   * @param root
   * @return
   */
  public static ArrayList<AssessmentNode> loadAssessmentNodes(final MegaRoot root, final boolean includeExecutionAssessment) {

    ArrayList<AssessmentNode> listAssessmentNodes = new ArrayList<AssessmentNode>();

    ArrayList<Assessment> listAssessments = new ArrayList<Assessment>();

    MegaCollection assessments = root.getCollection(GRCMetaClass.MC_ASSESSMENT);
    for (MegaObject moAssessment : assessments) {

      // Check if assessment session is from a campaing execution
      boolean isExecutionCampaign = AssessmentEngine.checkExecutionAssessment(moAssessment);
      if (!includeExecutionAssessment) {
        if (isExecutionCampaign) {
          continue;
        }
      }
      Assessment assessment = new Assessment(moAssessment);
      Date dAssessmentEndDate = assessment.getSEndDate();
      Date currentDate = GRCDateUtility.getCurrentDate();

      if ((dAssessmentEndDate != null)) {
        if (dAssessmentEndDate.before(currentDate) || dAssessmentEndDate.equals(currentDate)) {
          listAssessments.add(assessment);
        }
      }
    }

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
    }
    return listAssessmentNodes;

  }

  public static boolean checkExecutionAssessment(final MegaObject moAssessment) {
    // Get campaign.
    MegaCollection result = moAssessment.getCollection(GRCMetaAssociationEnd.MAE_ASSESSMENT_CAMPAIGN);
    if (result.size() > 0) {
      MegaObject campaign = result.get(1);
      result.release();
      return AssessmentEngine.checkExecutionCampaign(campaign);
    }

    result.release();
    return false;
  }

  public static boolean checkExecutionCampaign(final MegaObject campaign) {
    String campaingType = campaign.getProp(GRCMetaAttribut.MA_CAMPAIGN_TYPE);
    if (GRCConstants.MAV_CAMPAIGNTYPE_EXECUTION.equals(campaingType)) {
      campaign.release();
      return true;
    }
    campaign.release();
    return false;
  }

  public static boolean isClosedSession(final MegaObject moAssessment) {
    return moAssessment.getProp(GRCMetaAttribut.MA_SESSION_STATUS).equalsIgnoreCase(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED);
  }

  public static String[] getCharacteristicsTab(final Boolean executionCase) {
    String[] characteristics = new String[5];
    if (executionCase) {
      //setting the assessed characteristics linked to the execution template
      characteristics[0] = GRCConstants.AC_OK_KO;
      characteristics[1] = GRCConstants.MAV_CONTROL_EXECUTION_OK;
      characteristics[2] = GRCConstants.AC_AVG_PERCENT_OK_CONTROL_LEVEL;
      //setting the code template that will be used in the table report view
      characteristics[3] = GRCCodeTemplate.CT_NB_OK_INSTANCES;
      characteristics[4] = GRCCodeTemplate.CT_PERECENT_OK_INSTANCES;
    } else {
      //setting the assessed characteristics linked to the no execution template
      characteristics[0] = GRCConstants.AC_CONTROL_LEVEL;
      characteristics[1] = GRCConstants.MAV_CONTROL_LEVEL_PASS;
      characteristics[2] = GRCConstants.AC_AVG_PERCENT_PASS_CONTROL_LEVEL;
      //setting the code template that will be used in the table report view
      characteristics[3] = GRCCodeTemplate.CT_NB_PASS_INSTANCES;
      characteristics[4] = GRCCodeTemplate.CT_PERECENT_PASS_INSTANCES;
    }
    return characteristics;
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
   * @param mContexts measure contexts
   * @param evaluationsOfAcharac a map of all the evaluation linked to an
   *          assessed characteristic
   * @param aCharac the assessed characteristic treated
   * @return an Evaluation object of the average result for a set of measure
   *         contexts
   */
  public static Evaluation getAvgEvaluation(final MegaCollection mContexts, final Map<Integer, Evaluation> evaluationsOfAcharac, final MegaObject aCharac) {

    int sumOfEvaluations = 0;
    Evaluation eval = null;
    if ((mContexts != null) && (mContexts.size() > 0)) {
      //getting the sum of all internal value of the metaAttributeValues linked to the assessed characteristic evaluated 
      for (MegaObject mContext : mContexts) {
        AssessmentNode aNode = new AssessmentNode(mContext);
        MegaObject mgaValueACharac = aNode.getValue(aCharac.megaUnnamedField());
        if ((mgaValueACharac != null) && (mgaValueACharac.getID() != null)) {
          AssessedValue aValue = new AssessedValue(mgaValueACharac);
          MegaObject metaValue = aValue.getMetAttributeValue();
          if ((metaValue != null) && (metaValue.getID() != null)) {
            sumOfEvaluations = sumOfEvaluations + Integer.valueOf(metaValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
            metaValue.release();
          }
          mgaValueACharac.release();

        }
        aNode.release();
      }
      //getting the average evaluation object
      if (sumOfEvaluations != 0) {
        int avg = sumOfEvaluations / mContexts.size();
        for (Integer internVal : evaluationsOfAcharac.keySet()) {
          if (avg <= internVal) {
            eval = evaluationsOfAcharac.get(internVal);
            break;
          }
        }
      }
      mContexts.release();
    }

    return eval;
  }

  public static Evaluation getMaxEvaluation(final MegaCollection mContexts, final Map<Integer, Evaluation> evaluationsOfAcharac, final MegaObject aCharac) {
    int max = 0;
    Evaluation eval = null;
    if ((mContexts != null) && (mContexts.size() > 0)) {
      //getting the sum of all internal value of the metaAttributeValues linked to the assessed characteristic evaluated 
      for (MegaObject mContext : mContexts) {
        AssessmentNode aNode = new AssessmentNode(mContext);
        MegaObject mgaValueACharac = aNode.getValue(aCharac.megaUnnamedField());
        if ((mgaValueACharac != null) && (mgaValueACharac.getID() != null)) {
          AssessedValue aValue = new AssessedValue(mgaValueACharac);
          MegaObject metaValue = aValue.getMetAttributeValue();
          if ((metaValue != null) && (metaValue.getID() != null)) {
            int internVal = Integer.valueOf(metaValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
            if (max < internVal) {
              max = internVal;
            }
            metaValue.release();
          }
          mgaValueACharac.release();

        }
        aNode.release();
      }
    }

    eval = evaluationsOfAcharac.get(max);
    return eval;
  }

  /**
   * @param sessionNewNode session from the new measure context
   * @param sessionOldNode session from the old measure context
   * @return a true if the new measure context is the latest to be evaluated
   */
  public static Boolean isThelastValidevaluation(final MegaObject sessionNewNode, final MegaObject sessionOldNode) {
    Date sessionEndDate = GRCDateUtility.getDateFromMega(sessionNewNode, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
    Date sessionEndDateOldNode = GRCDateUtility.getDateFromMega(sessionOldNode, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
    if (sessionOldNode != null) {
      if (sessionEndDate.after(sessionEndDateOldNode)) {
        return true;
      }
      if (sessionEndDate.equals(sessionEndDateOldNode)) {
        Date sessionCreationDate = GRCDateUtility.getDateFromMega(sessionNewNode, GRCMetaAttribut.MA_CREATION_DATE);
        Date sessionCreationDateOldNode = GRCDateUtility.getDateFromMega(sessionOldNode, GRCMetaAttribut.MA_CREATION_DATE);
        if (sessionCreationDate.after(sessionCreationDateOldNode)) {
          return true;
        }

      }
    }

    return false;
  }

  public static MegaCollection getSessionsClosedInRange(final MegaCollection sessions, final Date begindate, final Date enddate) {

    MegaRoot root = sessions.getRoot();
    MegaCollection sessionSelected = root.getSelection("");
    root.release();

    for (MegaObject session : sessions) {
      //set the sessions rules & check if the session is the timeline defined by the begin & end date
      if (session.getProp(GRCMetaAttribut.MA_SESSION_STATUS).equals(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED) && (GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE) != null)) {
        if (begindate == null) {
          Date dateEndSession = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
          if ((dateEndSession != null) && (dateEndSession.before(enddate) || dateEndSession.equals(enddate))) {
            sessionSelected.insert(session);
          }
        } else {
          Date dateEndSession = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
          if ((((dateEndSession.equals(begindate)) || ((dateEndSession.before(enddate)) && dateEndSession.after(begindate)))) || dateEndSession.equals(enddate)) {
            sessionSelected.insert(session);
          }
        }
      }
      session.release();
    }
    return sessionSelected;
  }

  /**
   * @param sessionNewNode
   * @param sessionOldNode
   * @return
   */
  public static Boolean checkNodesDates(final MegaObject sessionNewNode, final MegaObject sessionOldNode) {
    Date sessionEndDate = GRCDateUtility.getDateFromMega(sessionNewNode, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
    Date sessionEndDateOldNode = GRCDateUtility.getDateFromMega(sessionOldNode, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
    if (sessionOldNode != null) {
      if (sessionEndDate.after(sessionEndDateOldNode)) {
        return true;
      }
      if (sessionEndDate.equals(sessionEndDateOldNode)) {
        Date sessionCreationDate = GRCDateUtility.getDateFromMega(sessionNewNode, GRCMetaAttribut.MA_CREATION_DATE);
        Date sessionCreationDateOldNode = GRCDateUtility.getDateFromMega(sessionOldNode, GRCMetaAttribut.MA_CREATION_DATE);
        if (sessionCreationDate.after(sessionCreationDateOldNode)) {
          return true;
        }

      }
    }

    return false;
  }

  public static boolean isInDatesRange(final Date mgObjectDate, final Date firstDateRange, final Date secondDateRange) {
    if ((mgObjectDate != null) && (mgObjectDate.equals(firstDateRange) || (mgObjectDate.before(secondDateRange) && mgObjectDate.after(firstDateRange)) || mgObjectDate.equals(secondDateRange))) {
      return true;
    }
    return false;
  }
}


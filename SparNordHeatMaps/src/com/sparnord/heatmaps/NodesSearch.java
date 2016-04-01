package com.sparnord.heatmaps;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCDateUtilities;
import com.sparnord.heatmaps.contextualized.ContextualizedMethodsToolbox;
import com.sparnord.heatmaps.riskidentification.IdentificationMethodsToolBox;
import com.sparnord.heatmaps.riskidentification.IdentificationParameters;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.assessment.AssessmentEngine;
import com.sparnord.heatmaps.grcu.assessment.AssessmentNode;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;

/**
 * @author ARK
 */
public class NodesSearch {

  private Hmap                        inherentRisk;
  private Hmap                        netRisk;
  private Map<String, HCell>          inherentRiskMAP;
  private Map<String, HCell>          netRiskMAP;
  private IdentificationParameters    hMapIdentParameters;
  private AssessmentNode              assessmentNode;
  private MegaCollection              nodes;
  private MegaCollection              nodes_on_map;
  private Map<String, MegaCollection> contextSubs;
  boolean                             absoluteNodes = false;
  
  public MegaCollection get_Nodes_On_Map(){
	  return nodes_on_map;
  }

  public void setValueContexts(MegaRoot root,final Hmap _inherentRisk, final Hmap _netRisk, final MegaCollection risks, final IdentificationParameters identificationParameters) {
    this.nodes_on_map=root.getSelection("");
    this.hMapIdentParameters = identificationParameters;
    this.inherentRisk = _inherentRisk;
    this.netRisk = _netRisk;
    this.inherentRiskMAP = this.inherentRisk.getMavsMap();
    this.netRiskMAP = this.netRisk.getMavsMap();
    this.absoluteNodes = this.checkIfAbsoluteNodes();
    for (MegaObject assessedObject : risks) {  	
      //MegaCollection nodes_on_risk=assessedObject.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_NODE);
    	//this.nodes = filter_nodes_on_date(root,nodes_on_risk,hMapIdentParameters.getBeginDate(),hMapIdentParameters.getEndDate());      
    	this.nodes =  assessedObject.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_NODE);
    for (MegaObject mgNode : this.nodes) {   	 
        this.assessmentNode = new AssessmentNode(mgNode);
        //Absolute nodes this.absoluteNodes
       // if (true) {
          this.setAbsoluteNodes();
        /*} else {
          //contextualized nodes
          this.setContextualizedNodes();
        }*/
        this.assessmentNode.release();
      }
      this.nodes.release();
      assessedObject.release();
    }

  }
  
	private MegaCollection filter_nodes_on_date(MegaRoot root,MegaCollection nodes, Date beginDate,Date endDate){
		
		MegaCollection filteredNodes=root.getSelection("");
		
		 for(MegaObject node:nodes){	    	    
	 	        // check if creation date is between begin date and end date
	 	        Date nodeCreationDate = LDCDateUtilities.resetTime(root, (Date)node.getProp("Creation Date","internal"));
	 	        if (!((nodeCreationDate.after(beginDate) || nodeCreationDate.equals(beginDate)) && (nodeCreationDate.before(endDate) || (nodeCreationDate.equals((endDate)))))) {
	 	          //Don't continue treatment, go to next object in loop	        	
	 	          continue;
	 	        }
	 	       filteredNodes.insert(node); 	    	 
	     }
		
		return filteredNodes;
		
	}

  /**
   * @return
   */
  private Boolean checkIfAbsoluteNodes() {
    if ((this.hMapIdentParameters.getOrgUnits().size() == 0) && (this.hMapIdentParameters.getProcesses().size() == 0)) {
      return true;
    }
    if (this.contextSubs == null) {
      this.contextSubs = new LinkedHashMap<String, MegaCollection>();
      IdentificationMethodsToolBox.setMapsContextBySubs(this.hMapIdentParameters.getProcesses(), this.contextSubs);
      IdentificationMethodsToolBox.setMapsContextBySubs(this.hMapIdentParameters.getOrgUnits(), this.contextSubs);
    }
    return false;
  }

  /**
   * @param session
   * @return
   */
  private Boolean checkSessionCharacteristics(final MegaObject mgNode) {
    MegaCollection deploymentSessions = mgNode.getCollection(GRCMetaAssociationEnd.MAE_NODE_ASSESSMENT_SESSION);
    if (deploymentSessions.size() > 0) {
      MegaObject session = deploymentSessions.get(1);
      deploymentSessions.release();
      if (session != null) {
        if (session.getProp(GRCMetaAttribut.MA_SESSION_STATUS).equals(GRCConstants.IV_ASSESSMENT_SESSION_STATUS_CLOSED)) {
          Date sessionEndDate = GRCDateUtility.getDateFromMega(session, GRCMetaAttribut.MA_ASSESSMENT_END_DATE);
          if ((sessionEndDate != null) && (sessionEndDate.before(this.hMapIdentParameters.getEndDate()) || sessionEndDate.equals(this.hMapIdentParameters.getEndDate())) && (sessionEndDate.after(this.hMapIdentParameters.getBeginDate()) || sessionEndDate.equals(this.hMapIdentParameters.getBeginDate()))) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * @param assessmentNode
   * @param firstAssessedCharac
   * @param secondAssessedCharac
   * @return
   */
  private MegaObject getAssessedValue(final String AssessedCharac) {
    MegaRoot root = this.assessmentNode.getNode().getRoot();
    MegaObject mgFirstAssessedCharac = root.getCollection(GRCMetaClass.MC_ASSESSED_CHARACTERISTIC).get(AssessedCharac);
    MegaObject assessedValueFirstCharac = this.assessmentNode.getValue(mgFirstAssessedCharac.megaUnnamedField());
    mgFirstAssessedCharac.release();
    return assessedValueFirstCharac;
  }

  /**
   * @param assessedValue
   * @return
   */
  private String getIdbsMav(final MegaObject assessedValue) {
    String hexaIdbasMAV = "";
    MegaCollection metaValues = assessedValue.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_VALUE_METAATTRIBUTEVALUE);
    if (metaValues.size() > 0) {
      MegaObject metaValue = metaValues.get(1);
      hexaIdbasMAV = metaValue.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
      metaValue.release();
    }
    metaValues.release();
    return hexaIdbasMAV;
  }

  /**
   * 
   */
  private void setAbsoluteNodes() {
    if (this.checkSessionCharacteristics(this.assessmentNode.getNode())) {
      //nodes_on_map.insert(this.assessmentNode.getNode());
      this.setValueContextToHcell();
    }

  }

  /**
   * @param assessmentNode
   */
  private void setContextualizedNodes() {
    MegaCollection contextsnodes = this.assessmentNode.getContexts();
    if ((contextsnodes != null) && (contextsnodes.size() > 0)) {
      if (this.checkSessionCharacteristics(this.assessmentNode.getNode())) {
        if (this.checkParametersAsContexts()) {
          this.setValueContextToHcell();
        }
      }
    }
  }

  /**
   * @param assessmentNode
   */
  private void setValueContextToHcell() {
    MegaObject aValueImpact = this.getAssessedValue(GRCConstants.AC_ERM_IMPACT);
    MegaObject aValueLikeLihood = this.getAssessedValue(GRCConstants.AC_ERM_LIKELIHOUD);
    MegaObject aValueMitigation = this.getAssessedValue(GRCConstants.AC_ERM_CONTROL_LEVEL);
    MegaObject aValueInherentRisk = this.getAssessedValue(GRCConstants.AC_ERM_INHERENT_RISK);
    if ((aValueImpact != null) && (aValueLikeLihood != null)) {
    	
      String cellKey = this.getIdbsMav(aValueImpact) + "," + this.getIdbsMav(aValueLikeLihood);
      aValueImpact.release();
      aValueLikeLihood.release();
      if (this.inherentRiskMAP.get(cellKey) != null) {
        Map<String, String> valueContexts = this.inherentRisk.getMeasureContexts();
        String nodeKey = this.getContextsList(this.assessmentNode.getContexts()) + "-" + this.assessmentNode.getAssessed().megaUnnamedField() + "-" + GRCConstants.AC_INHERENT_RISK;
        String sameNode = valueContexts.get(nodeKey);
        if (sameNode != null) {
          String[] _sameNode = sameNode.split("-");
          if (_sameNode.length == 2) {
            String nodeId = _sameNode[0];
            String nodeCellKey = _sameNode[1];
            if (this.inherentRiskMAP.get(nodeCellKey) != null) {
              MegaObject oldNode = this.nodes.get(nodeId);
              MegaObject sessionOldNode = AssessmentEngine.getSessionFromNode(oldNode);
              if (AssessmentEngine.checkNodesDates(this.assessmentNode.getAssessment(), sessionOldNode)) {
            	  nodes_on_map.insert(this.assessmentNode.getNode());
                valueContexts.put(nodeKey, this.assessmentNode.getNode().megaUnnamedField() + "-" + cellKey);
                this.inherentRiskMAP.get(nodeCellKey).getValueContexts().remove(nodeKey);
                this.inherentRiskMAP.get(cellKey).getValueContexts().put(nodeKey, this.assessmentNode.getNode().megaUnnamedField());
                
              }
              
              sessionOldNode.release();
              oldNode.release();
            }
          }
        } else {
        	nodes_on_map.insert(this.assessmentNode.getNode());
          valueContexts.put(nodeKey, this.assessmentNode.getNode().megaUnnamedField() + "-" + cellKey);
          this.inherentRiskMAP.get(cellKey).getValueContexts().put(nodeKey, this.assessmentNode.getNode().megaUnnamedField());
        }
      }
    }
    if ((aValueMitigation != null) && (aValueInherentRisk != null)) {
    	
      String cellKey = this.getIdbsMav(aValueMitigation) + "," + this.getIdbsMav(aValueInherentRisk);
      aValueMitigation.release();
      aValueInherentRisk.release();
      if (this.netRiskMAP.get(cellKey) != null) {
        Map<String, String> valueContexts = this.netRisk.getMeasureContexts();
        String nodeKey = this.getContextsList(this.assessmentNode.getContexts()) + "-" + this.assessmentNode.getAssessed().megaUnnamedField() + "-" + GRCConstants.AC_NET_RISK;
        String sameNode = valueContexts.get(nodeKey);
        if (sameNode != null) {
          String[] _sameNode = sameNode.split("-");
          if (_sameNode.length == 2) {
            String nodeId = _sameNode[0];
            String nodeCellKey = _sameNode[1];
            if (this.netRiskMAP.get(nodeCellKey) != null) {
              MegaObject oldNode = this.nodes.get(nodeId);
              MegaObject sessionOldNode = AssessmentEngine.getSessionFromNode(oldNode);
              if (AssessmentEngine.checkNodesDates(this.assessmentNode.getAssessment(), sessionOldNode)) {
            	  nodes_on_map.insert(this.assessmentNode.getNode());
                valueContexts.put(nodeKey, this.assessmentNode.getNode().megaUnnamedField() + "-" + cellKey);
                this.netRiskMAP.get(nodeCellKey).getValueContexts().remove(nodeKey);
                this.netRiskMAP.get(cellKey).getValueContexts().put(nodeKey, this.assessmentNode.getNode().megaUnnamedField());
             
              }
              
              sessionOldNode.release();
              oldNode.release();
            }
          }
        } else {
        	nodes_on_map.insert(this.assessmentNode.getNode());
          valueContexts.put(nodeKey, this.assessmentNode.getNode().megaUnnamedField() + "-" + cellKey);
          this.netRiskMAP.get(cellKey).getValueContexts().put(nodeKey, this.assessmentNode.getNode().megaUnnamedField());
        }
      }
    }
  }

  /**
   * @param nodesContexts
   * @return
   */
  private Boolean checkParametersAsContexts() {
    MegaCollection contexts = this.hMapIdentParameters.getObjectives().getRoot().getSelection("");
    contexts.insert(this.hMapIdentParameters.getOrgUnits());
    if (this.checkObjectsAsContexts(contexts)) {
      contexts.release();
      return true;
    }
    contexts.release();
    return false;
  }

  /**
   * @param mgObjects
   * @return
   */
  private Boolean checkObjectsAsContexts(final MegaCollection mgObjects) {
    if (mgObjects.size() > 0) {
      for (MegaCollection allSubContexts : this.contextSubs.values()) {
        if (ContextualizedMethodsToolbox.checkMeasureContexts(allSubContexts, this.assessmentNode.getContexts())) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  private String getContextsList(final MegaCollection contexts) {
    String listContexts = "";
    for (MegaObject context : contexts) {
      if (listContexts.equals("")) {
        listContexts = context.megaUnnamedField();
      } else {
        listContexts = listContexts + "," + context.megaUnnamedField();
      }
      context.release();
    }
    return listContexts;
  }

}


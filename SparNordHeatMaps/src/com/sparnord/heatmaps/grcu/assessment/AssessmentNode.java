package com.sparnord.heatmaps.grcu.assessment;

import java.util.ArrayList;
import java.util.Iterator;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaCurrentEnvironment;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.grcu.GRCDataProcessing;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.mega.toolkit.errmngt.ErrorLogFormater;

public class AssessmentNode {

  MegaObject     node;
  MegaObject     assessed;
  MegaCollection contexts;
  MegaObject     assessment;
  MegaCollection higherNodes;

  MegaCollection lowerNodes;

  public AssessmentNode(final MegaObject moAssessed, final MegaCollection mcContexts) {
    super();
    this.assessed = moAssessed;
    this.contexts = mcContexts;
  }

  public AssessmentNode(final MegaObject assessmentNode) {
    super();
    this.node = assessmentNode;
    MegaCollection assessedNodes = assessmentNode.getCollection(GRCMetaAssociationEnd.MAE_ASSESSED_OBJECT);
    MegaRoot root = assessmentNode.getRoot();
    this.assessed = assessedNodes.get(1);
    this.contexts = assessmentNode.getCollection(GRCMetaAssociationEnd.MAE_ASSESSMENT_CONTEXT);
    this.assessment = AssessmentEngine.getSessionFromNode(this.node);
    this.higherNodes = assessmentNode.getCollection(GRCMetaAssociationEnd.MAE_HIGHER_MEASURE_CONTEXT);
    this.lowerNodes = assessmentNode.getCollection(GRCMetaAssociationEnd.MAE_LOWER_MEASURE_CONTEXT);
    if (!this.assessed.exists()) {
      MegaCurrentEnvironment env = root.currentEnvironment();
      String debug = env.getUserParameter("Assessment", "Debug");
      env.release();
      if (debug.equals("1")) {
        ErrorLogFormater err = new ErrorLogFormater();
        err.openSession(root);
        err.addSessionInfo("WARNING", "Mesure Context : ===============================================================");
        err.logMessage("The Mesure Context : " + assessmentNode.megaField() + " is not linked to any Assessed Object");
        err.logMessage("============================================================================================");
        err.closeSession();
      }
    }
    root.release();
    assessedNodes.release();
  }

  public MegaCollection getHigherNodes() {
    return this.higherNodes;
  }

  public void setHigherNodes(final MegaCollection higherNodes) {
    this.higherNodes = higherNodes;
  }

  public MegaCollection getLowerNodes() {
    return this.lowerNodes;
  }

  public void setLowerNodes(final MegaCollection lowerNodes) {
    this.lowerNodes = lowerNodes;
  }

  public MegaObject getNode() {
    return this.node;
  }

  public void setNode(final MegaObject moNode) {
    this.node = moNode;
  }

  public MegaObject getAssessed() {
    return this.assessed;
  }

  public void setAssessed(final MegaObject moAssessed) {
    this.assessed = moAssessed;
  }

  public MegaCollection getContexts() {
    return this.contexts;
  }

  public void setContexts(final MegaCollection mcContexts) {
    this.contexts = mcContexts;
  }

  public MegaObject getAssessment() {
    return this.assessment;
  }

  public MegaCollection getValues() {
    return this.node != null ? this.node.getCollection(GRCMetaAssociationEnd.MAE_ASS_NODE_ASS_VALUE) : null;
  }

  public MegaObject getValue(final String caracteristic) {
    if (this.node != null) {
      MegaCollection values = this.node.getCollection(GRCMetaAssociationEnd.MAE_ASS_NODE_ASS_VALUE);
      Iterator<MegaObject> it = values.iterator();
      while (it.hasNext()) {
        MegaObject value = it.next();
        MegaCollection characteristics = value.getCollection(GRCMetaAssociationEnd.MAE_CHARACTERISTIC_BY_VALUE);
        if ((characteristics != null) && (characteristics.size() > 0)) {
          MegaObject _caracteristic = characteristics.get(1);
          characteristics.release();
          if (_caracteristic.sameID(caracteristic)) {
            return value;
          }
          value.release();
        }
      }
      values.release();
    }

    return null;
  }

  public boolean containsContext(final MegaObject contextObject) {
    for (MegaObject context : this.contexts) {
      if (context.sameID(contextObject.getID())) {
        context.release();
        return true;
      }

      context.release();
    }

    return false;
  }

  public boolean containsContextOrSubContext(final MegaObject contextObject) {

    for (MegaObject context : this.contexts) {
      if (context.sameID(contextObject.getID())) {
        context.release();
        return true;
      }

      ArrayList<String> idabsPreviousElements = new ArrayList<String>();
      MegaObject _context = context.getType("");
      MegaRoot root = this.node.getRoot();

      idabsPreviousElements.add(_context.getProp(GRCMetaAttribut.MA_HEX_ID_ABS));

      if (GRCDataProcessing.sameType(_context, contextObject) && GRCDataProcessing.isChildrenOf(_context, contextObject, idabsPreviousElements)) {
        context.release();
        _context.release();
        root.release();
        return true;
      }

      _context.release();
      context.release();
      root.release();
    }
    return false;
  }

  public boolean containsOnlyObjectContext(final MegaObject contextObject) {
    if (this.contexts != null) {
      if (this.contexts.size() == 1) {
        if (this.contexts.get(1).sameID(contextObject.getID())) {
          return true;
        }
      }
    }
    return false;
  }

  public void release() {
    this.node.release();
    if (this.assessed != null) {
      this.assessed.release();
    }
    if (this.assessment != null) {
      this.assessment.release();
    }
    this.contexts.release();
    this.higherNodes.release();
    this.lowerNodes.release();
  }

  /**
   * choose which one of the node elements to release
   * @param rNode boolean
   * @param rAssessed boolean
   * @param rAssessment boolean
   * @param rContexts boolean
   * @param rHigherNodes boolean
   * @param rLowerNodes boolean
   */
  public void release(final boolean rNode, final boolean rAssessed, final boolean rAssessment, final boolean rContexts, final boolean rHigherNodes, final boolean rLowerNodes) {
    if (rNode) {
      this.node.release();
    }
    if (rAssessed) {
      this.assessed.release();
    }
    if (rAssessment) {
      this.assessment.release();
    }
    if (rContexts) {
      this.contexts.release();
    }
    if (rHigherNodes) {
      this.higherNodes.release();
    }
    if (rLowerNodes) {
      this.lowerNodes.release();
    }
  }
}


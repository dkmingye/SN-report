package com.sparnord.heatmaps.grcu.assessment;

import java.util.ArrayList;
import java.util.Date;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;

public class Assessment {
  private MegaObject                assessment;
  private ArrayList<AssessmentNode> nodes;
  private Date                      endDate;
  private String                    sEndDate;

  public Assessment(final MegaObject assess, final ArrayList<AssessmentNode> nodes, final Date endDate) {
    super();
    this.nodes = nodes;
    this.endDate = endDate;
    this.assessment = assess;
  }

  public Assessment(final ArrayList<AssessmentNode> arrayNodes, final String psEndDate) {
    super();
    this.nodes = arrayNodes;
    this.sEndDate = psEndDate;
  }

  public Assessment(final MegaObject moAssessment) {
    super();
    this.assessment = moAssessment;
  }

  public Date getSEndDate() {
    return this.assessment != null ? GRCDateUtility.getDateFromMega(this.assessment, GRCMetaAttribut.MA_ASSESSMENT_END_DATE) : null;
  }

  public MegaObject getAssessment() {
    return this.assessment;
  }

  public void setAssess(final MegaObject assess) {
    this.assessment = assess;
  }

  public ArrayList<AssessmentNode> getNodes() {
    return this.nodes;
  }

  public MegaCollection getAllNodesFromAssesment() {
    return this.assessment.getCollection(GRCMetaAssociationEnd.MAE_NODES_BY_ASSESSMENT);
  }

  public void setNodes(final ArrayList<AssessmentNode> arrayNodes) {
    this.nodes = arrayNodes;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(final Date dEndDate) {
    this.endDate = dEndDate;
  }

}


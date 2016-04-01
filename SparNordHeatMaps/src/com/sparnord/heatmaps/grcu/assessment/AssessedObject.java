package com.sparnord.heatmaps.grcu.assessment;

import java.util.Date;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.grcu.GRCDateUtility;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;

public class AssessedObject {

  private MegaObject     assessedObject;
  private Date           creationDate;
  private boolean        isEvaluated;
  private MegaCollection contexts;
  private MegaCollection linkedElements;

  public AssessedObject(final MegaObject moAssessedObject) {
    super();
    this.assessedObject = moAssessedObject;
    this.isEvaluated = false;
    this.creationDate = GRCDateUtility.getDateFromMega(moAssessedObject, GRCConstants.MA_CREATION_DATE);

    MegaRoot root = moAssessedObject.getRoot();
    this.contexts = root.getSelection("");
    this.linkedElements = root.getSelection("");

    root.release();
  }

  public MegaCollection getLinkedElements() {
    return this.linkedElements;
  }

  public void setLinkedElements(final MegaCollection linkedElements) {
    this.linkedElements = linkedElements;
  }

  public MegaCollection getContexts() {
    return this.contexts;
  }

  public void setContexts(final MegaCollection contexts) {
    this.contexts = contexts;
  }

  public Date getCreationDate() {
    return this.creationDate;
  }

  public void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate;
  }

  public MegaObject getAssessedObject() {
    return this.assessedObject;
  }

  public void setAssessedObject(final MegaObject moAssessedObject) {
    this.assessedObject = moAssessedObject;
  }

  public boolean isEvaluated() {
    return this.isEvaluated;
  }

  public void setEvaluated(final boolean bIsEvaluated) {
    this.isEvaluated = bIsEvaluated;
  }

  @Override
  public int hashCode() {
    return this.assessedObject.megaUnnamedField().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof AssessedObject) {
      AssessedObject _assessedObject = (AssessedObject) obj;
      return this.assessedObject.sameID(_assessedObject.assessedObject.getID());
    }
    return false;
  }
  
  public void release() {
    this.assessedObject.release();
    this.linkedElements.release();
    this.contexts.release();
  }

}


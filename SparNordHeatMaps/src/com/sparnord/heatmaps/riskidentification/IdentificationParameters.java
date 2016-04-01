package com.sparnord.heatmaps.riskidentification;

import java.util.Date;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaRoot;

public class IdentificationParameters {
  Date           beginDate;
  Date           endDate;
  MegaCollection processes;
  MegaCollection orgUnits;
  MegaCollection objectives;
  MegaCollection risktypes;
  MegaCollection controlTypes;
  boolean isKeyRisk=false;

  public MegaCollection getControlTypes() {
    return this.controlTypes;
  }

  public void setControlTypes(final MegaCollection _controlTypes) {
    this.controlTypes = _controlTypes;
  }

  public IdentificationParameters(final MegaRoot root) {
    super();
    this.processes = root.getSelection("");
    this.orgUnits = root.getSelection("");
    this.objectives = root.getSelection("");
    this.risktypes = root.getSelection("");
    this.controlTypes = root.getSelection("");
    this.isKeyRisk=false;
  }
  
  public void setKeyRisk() {
	    this.isKeyRisk=true;
	  }
  public boolean keyRisk() {
	    return isKeyRisk;
	  }
  
  public Date getBeginDate() {
    return this.beginDate;
  }

  public void setBeginDate(final Date _beginDate) {
    this.beginDate = _beginDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(final Date _endDate) {
    this.endDate = _endDate;
  }

  public MegaCollection getProcesses() {
    return this.processes;
  }

  public void setProcesses(final MegaCollection _processes) {
    this.processes = _processes;
  }

  public MegaCollection getOrgUnits() {
    return this.orgUnits;
  }

  public void setOrgUnits(final MegaCollection _orgUnits) {
    this.orgUnits = _orgUnits;
  }

  public MegaCollection getObjectives() {
    return this.objectives;
  }

  public void setObjectives(final MegaCollection _objectives) {
    this.objectives = _objectives;
  }

  public MegaCollection getRisktypes() {
    return this.risktypes;
  }

  public void setRisktypes(final MegaCollection _risktypes) {
    this.risktypes = _risktypes;
  }

}



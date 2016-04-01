package com.sparnord.heatmaps.contextualized;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaRoot;

/**
 * @author HOE
 */
public class HeatmapCell {

  private MegaCollection assesseds;
  private int            ivImpact;
  private int            ivLikelihood;
  private int            ivControlLevel;
  private int            ivInherentRisk;
  private String         color;

  public HeatmapCell(final MegaRoot root) {
    super();
    this.assesseds = root.getSelection("");
  }

  @Override
  public int hashCode() {
    return this.ivImpact + this.ivLikelihood;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof HeatmapCell) {
      HeatmapCell heatmapCell = (HeatmapCell) obj;
      if (this.ivImpact != 0) {
        return (this.ivImpact == heatmapCell.ivImpact) && (this.ivLikelihood == heatmapCell.ivLikelihood);
      } else if (this.ivControlLevel != 0) {
        return (this.ivControlLevel == heatmapCell.ivControlLevel) && (this.ivInherentRisk == heatmapCell.ivInherentRisk);
      }
    }
    return false;
  }

  public MegaCollection getAssesseds() {
    return this.assesseds;
  }

  public void setAssesseds(final MegaCollection mcassesseds) {
    this.assesseds = mcassesseds;
  }

  public int getImpact() {
    return this.ivImpact;
  }

  public void setImpact(final int imp) {
    this.ivImpact = imp;
  }

  public void setLikelihood(final int lik) {
    this.ivLikelihood = lik;
  }

  public int getLikelihood() {
    return this.ivLikelihood;
  }

  public int getInherentRisk() {
    return this.ivInherentRisk;
  }

  public void setInherentRisk(final int ir) {
    this.ivInherentRisk = ir;
  }

  public int getControlLevel() {
    return this.ivControlLevel;
  }

  public void setControlLevel(final int cl) {
    this.ivControlLevel = cl;
  }

  public void setColor(final String col) {
    this.color = col;
  }

  public String getColor() {
    return this.color;
  }

}


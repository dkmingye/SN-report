package com.sparnord.heatmaps;

import java.util.Map;

import com.mega.modeling.api.MegaCollection;

public class Hmap {

  private String              tableName;
  private MegaCollection      mavFirstMaAttribute;
  private MegaCollection      mavSecondMaAttribute;
  private Map<String, String> measureContexts;
  private Map<String, HCell>  mavsMap;

  public Map<String, String> getMeasureContexts() {
    return this.measureContexts;
  }

  public void setMeasureContexts(final Map<String, String> measureContexts) {
    this.measureContexts = measureContexts;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(final String tableName) {
    this.tableName = tableName;
  }

  public MegaCollection getMavFirstMaAttribute() {
    return this.mavFirstMaAttribute;
  }

  public void setMavFirstMaAttribute(final MegaCollection mavFirstMaAttribute) {
    this.mavFirstMaAttribute = mavFirstMaAttribute;
  }

  public MegaCollection getMavSecondMaAttribute() {
    return this.mavSecondMaAttribute;
  }

  public void setMavSecondMaAttribute(final MegaCollection mavSecondMaAttribute) {
    this.mavSecondMaAttribute = mavSecondMaAttribute;
  }

  public Map<String, HCell> getMavsMap() {
    return this.mavsMap;
  }

  public void setMavsMap(final Map<String, HCell> mavsMap) {
    this.mavsMap = mavsMap;
  }

}


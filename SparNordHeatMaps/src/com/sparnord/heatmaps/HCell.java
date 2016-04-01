package com.sparnord.heatmaps;
import java.util.HashMap;
import java.util.Map;

public class HCell {
  private String              color;
  private Map<String, String> valueContexts;

  public HCell() {
    super();
    this.color = "";
    this.valueContexts = new HashMap<String, String>();
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(final String _color) {
    this.color = _color;
  }

  public Map<String, String> getValueContexts() {
    return this.valueContexts;
  }

  public void setValueContexts(final Map<String, String> _valueContexts) {
    this.valueContexts = _valueContexts;
  }

}


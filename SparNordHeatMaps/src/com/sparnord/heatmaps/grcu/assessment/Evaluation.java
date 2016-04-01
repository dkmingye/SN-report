package com.sparnord.heatmaps.grcu.assessment;

public class Evaluation {

  String valueName;
  Double value;
  String color;
  String metaPicture;
  int    internalValue;

  public Evaluation() {
    super();
    this.metaPicture = "";
    this.valueName = "";
    this.color = "";
    this.value = 0.0;
    this.internalValue = 0;
  }

  public int getInternalValue() {
    return this.internalValue;
  }

  public void setInternalValue(final int _internalValue) {
    this.internalValue = _internalValue;
  }

  public String getMetaPicture() {
    return this.metaPicture;
  }

  public void setMetaPicture(final String _metaPicture) {
    this.metaPicture = _metaPicture;
  }

  public String getValueName() {
    return this.valueName;
  }

  public void setValueName(final String _name) {
    this.valueName = _name;
  }

  public Double getValue() {
    return this.value;
  }

  public void setValue(final Double _value) {
    this.value = _value;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(final String _color) {
    this.color = _color;
  }

}


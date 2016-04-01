package com.sparnord.heatmaps.grcu.assessment;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;

public class AssessedValue {

  MegaObject                 assessedValue;

  public static final String MC_ASSESSED_VALUE           = "~)dwhSA3iDPfH[Assessed Value]";
  public static final String MA_COMPUTED_VALUE           = "~ebwhMB3iDLiH[Computed Value]";
  public static final String MAE_CHARACTERISTIC_BY_VALUE = "~kJsYO2FLEnUw[Assessed Characteristic]";

  public AssessedValue(final MegaObject moAssessedValue) {
    super();
    this.assessedValue = moAssessedValue;
  }

  public String getComputedValue() {
    if (this.assessedValue != null) {
      return this.assessedValue.getProp(AssessedValue.MA_COMPUTED_VALUE);
    }

    return null;
  }

  /**
   * ARK
   * @return the metAttributeValue Linked to the assesValue
   */
  public MegaObject getMetAttributeValue() {

    MegaCollection metaValues = this.assessedValue.getCollection("~o18yT9lCE1(K[MetaAttributeValue]");
    if (metaValues.size() > 0) {
      MegaObject metaValue = metaValues.get(1);
      metaValues.release();
      return metaValue;
    }
    metaValues.release();
    return null;
  }

}


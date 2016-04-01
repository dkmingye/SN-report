package com.sparnord.riskreport;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mega.extraction.ExtractionPath;
import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisParameter.AnalysisSimpleTypeValue;
import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.Image;
import com.mega.modeling.analysis.content.Item;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.Value;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCConstants;
import com.sparnord.common.LDCDateUtilities;
import com.sparnord.common.LDCReportViewUtility;
import com.sparnord.common.Palette;


/**
 * @author MAH
 * @version 750
 */
public class BackTestingMatrixTable {
  /*Local Variables*/
  private static final String PARAM_END_DATE   = "4AED4DF256E1219C";
  private static final String PARAM_BEGIN_DATE = "4AED4DB856E12149";
  private static final String PARAM_THRESHOLD  = "4AED4D6A56E120F6";
  private static final String PARAM_CURRENCY   = "4AED4CDE56E12097";
  private MegaCollection      colRiskType;
  private MegaCollection      colBusProc;
  private MegaCollection      colOrgProc;
  private MegaCollection      colOrgUnit;
  private MegaCollection      colBusLine;
  private Double              netLossThresholdAmount;
  private Date                beginDate, endDate;

  private ReportContent       reportContent;
  private Dataset             dataSet;
  private MegaRoot            root;
  private boolean             isExcel;

  /**
   * @param oRoot The MegaRoot
   * @param parameters The report parameters
   * @param iContext iContext
   */
  public void initializeParameter(final MegaRoot oRoot, final Map<String, List<AnalysisParameter>> parameters, final short iContext) {
    this.root = oRoot;
    this.isExcel = (iContext == AnalysisReportToolbox.cRtfDeliverable);
    this.colRiskType = oRoot.getSelection("");
    this.colBusProc = oRoot.getSelection("");
    this.colOrgUnit = oRoot.getSelection("");
    this.colOrgProc = oRoot.getSelection("");
    this.colBusLine = oRoot.getSelection("");
    this.netLossThresholdAmount = new Double(0.0);
    String localNetlossThreshold = "";
    Calendar c = Calendar.getInstance();
    c.add(Calendar.YEAR, -1);
    this.beginDate = LDCDateUtilities.resetTime(oRoot, c.getTime());
    this.endDate = LDCDateUtilities.resetTime(oRoot, new Date());
    SystemLog.initialize("C:\\Users\\ming\\Desktop\\log.txt");//for any troubleshooting
    for (String paramType : parameters.keySet()) {
      for (AnalysisParameter analysisParam : parameters.get(paramType)) {
        for (MegaObject value : analysisParam.getValues()) {
          MegaObject classObject = value.getClassObject();
          if (classObject.sameID(LDCConstants.MC_BUSINESS_LINE)) {
        	  SystemLog.log("MC_BUSINESS_LINE");
            this.colBusLine.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_BUSINESS_PROCESS)) {
        	  SystemLog.log("MC_BUSINESS_PROCESS");
            this.colBusProc.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_ORG_UNIT)) {
        	  SystemLog.log("MC_ORG_UNIT");
            this.colOrgUnit.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_ORGANIZATIONAL_PROCESS)) {
        	  SystemLog.log("MC_ORGANIZATIONAL_PROCESS ");
            this.colOrgProc.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_RISK_TYPE)) {
        	  SystemLog.log("MC_RISK_TYPE ");
            this.colRiskType.insert(value);
          }
          classObject.release();
        }
        
        //BeginDate
        if (paramType.equals(BackTestingMatrixTable.PARAM_BEGIN_DATE)) {
        	SystemLog.log("begin date ");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              this.beginDate = LDCDateUtilities.resetBeginDateTime((Date) value.getValue());
            }
          }
        }
        //EndDate
        if (paramType.equals(BackTestingMatrixTable.PARAM_END_DATE)) {
        	SystemLog.log("end date");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              this.endDate = LDCDateUtilities.resetEndDateTime((Date) value.getValue());
            }
          }
        }
        
        //Threshold
        if (paramType.equals(BackTestingMatrixTable.PARAM_THRESHOLD)) {
        	SystemLog.log("threshold");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              if (!value.getStringValue().equals("")) {
                localNetlossThreshold = value.getStringValue();
              }

            }
          }
        }
        //Currency
        if (paramType.equals(BackTestingMatrixTable.PARAM_CURRENCY)) {
        	SystemLog.log("Currency");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              if (!value.getStringValue().equals("")) {
                oRoot.currentEnvironment().setCurrentCurrency(value.getStringValue());
              }

            }
          }
        }

      }
    }
    //net loss Threshold
    if (!localNetlossThreshold.equals("")) {
      Double amount = oRoot.currentEnvironment().getCurrency().getAmount(localNetlossThreshold);
      String currency = oRoot.currentEnvironment().getCurrency().getCurrencyCode(localNetlossThreshold);
      if (!oRoot.currentEnvironment().getCurrency().getUserCurrencyCode().equalsIgnoreCase(currency)) {
        this.netLossThresholdAmount = oRoot.currentEnvironment().getCurrency().getInternalAmount(amount, currency, oRoot.currentEnvironment().getCurrency().getUserCurrencyCode(), new Date());
      } else {
        this.netLossThresholdAmount = amount;
      }

    }
    SystemLog.close();
  }

  /**
   * @param reportContentParam : report content
   * @param chartTitle : chart title
   * @return The view built by the given parameters
   */
  public View createBackTestingMatrixView(final ReportContent reportContentParam, final String chartTitle) {
    this.reportContent = reportContentParam;
    this.dataSet = new Dataset(chartTitle);
    final Dimension dimX = new Dimension("");
    final Dimension dimY = new Dimension(LDCConstants.CT_INCIDENTS);

    this.dataSet.addDimension(dimX);
    this.dataSet.addDimension(dimY);
    final int datasetID = this.reportContent.addDataset(this.dataSet);
    final View incidentLossEvoView = new View(datasetID);
    incidentLossEvoView.addParameter("legend", "bottom");
    incidentLossEvoView.addRenderer(AnalysisReportToolbox.rTable);
    MegaCollection oColRisk = this.root.getSelection("");
    if ((this.colOrgUnit.size() == 0) && (this.colBusProc.size() == 0) && (this.colOrgProc.size() == 0) && (this.colBusLine.size() == 0) && (this.colRiskType.size() == 0)) {
      oColRisk.insert(this.root.getCollection(LDCConstants.MC_RISK));
    } else {
      // get risks linked to the selected OrgUnit or their children
      oColRisk.insert(this.getRisksFromCol(this.colOrgUnit, LDCConstants.MC_ORG_UNIT, LDCConstants.QUERY_SUB_ORGUNIT));
      // get risks linked to the selected Org process or their children
      oColRisk.insert(this.getRisksFromCol(this.colOrgProc, LDCConstants.MC_ORGANIZATIONAL_PROCESS, LDCConstants.QUERY_SUB_ORGPROCESS));
      // get risks linked to the selected business process or their children
      oColRisk.insert(this.getRisksFromCol(this.colBusProc, LDCConstants.MC_BUSINESS_PROCESS, LDCConstants.QUERY_SUB_BPROCESS));
      // get risks linked to the selected business line or their children
      oColRisk.insert(this.getRisksFromCol(this.colBusLine, LDCConstants.MC_BUSINESS_LINE, LDCConstants.QUERY_SUB_BUSINESS_LINE));
      // get risks linked to the selected risk type or their children
      oColRisk.insert(this.getRisksFromCol(this.colRiskType, LDCConstants.MC_RISK_TYPE, LDCConstants.QUERY_SUB_RISKTYPE));
    }

    dimX.setSize(oColRisk.size());
    dimY.setSize(9);

    dimY.addItem(new Text(LDCConstants.CT_RISK_CODE, true));
    dimY.addItem(new Text(LDCConstants.CT_RISK, true));
    dimY.addItem(new Text(LDCConstants.CT_NET_RISK_LEVEL, true));
    dimY.addItem(new Text(LDCConstants.CT_NB_OF_INCIDENTS, true));
    dimY.addItem(new Text(LDCConstants.CT_GROSS_LOSS, true));
    dimY.addItem(new Text(LDCConstants.CT_GROSS_ACTUAL_LOSS, true));
    dimY.addItem(new Text(LDCConstants.CT_RECOVERIES, true));
    dimY.addItem(new Text(LDCConstants.CT_NET_LOSS, true));
    dimY.addItem(new Text(LDCConstants.CT_NET_ACTUAL_LOSS, true));
    Object userCurrencyId = this.root.currentEnvironment().getCurrency().getUserCurrencyId();
    int index = 1;
    HashMap<String, MegaObject> netRiskAttributeValues = this.getAttributeValues(this.root, LDCConstants.MA_NET_RISK);
    for (MegaObject risk : oColRisk) {
      int nbreIncident = 0;
      double grossLoss = 0.0;
      double grossActualLoss = 0.0;
      double recoveries = 0.0;
      double netLoss = 0.0;
      double netActualLoss = 0.0;
      StringBuffer drillDownList = new StringBuffer(LDCConstants.MC_INCIDENT + ":");
      MegaCollection oColIncident = risk.getCollection(LDCConstants.MAE_RISK_INCIDENT);

      for (MegaObject incident : oColIncident) {
        Double netLossLocalAmmount = Double.parseDouble(incident.getProp(LDCConstants.MA_NET_LOSS_LOCAL, "Internal").toString());
        // Check if Amount exceeds Threshold 
        if (this.netLossThresholdAmount == null) {
          continue;
        } else if (netLossLocalAmmount < this.netLossThresholdAmount) {
          //Don't continue treatment, go to next object in loop
          continue;
        }
        // check if declaration date is between begin date and end date
        Date mgIncidentDate = LDCDateUtilities.resetTime(this.root, (Date) incident.getProp(LDCConstants.MA_DECLARATION_DATE, "internal"));
        if (!((mgIncidentDate.after(this.beginDate) || mgIncidentDate.equals(this.beginDate)) && (mgIncidentDate.before(this.endDate) || (mgIncidentDate.equals((this.endDate)))))) {
          //Don't continue treatment, go to next object in loop
          continue;
        }
        drillDownList.append(incident.megaUnnamedField() + ",");
        nbreIncident += 1;
        grossLoss += Double.parseDouble(incident.getProp(LDCConstants.MA_GROSS_LOSS_LOCAL, "Internal").toString());
        grossActualLoss += Double.parseDouble(incident.getProp(LDCConstants.MA_GROSS_ACTUAL_LOSS_LOCAL, "Internal").toString());
        recoveries += Double.parseDouble(incident.getProp(LDCConstants.MA_RECOVERIES_LOCAL, "Internal").toString());
        netLoss += Double.parseDouble(incident.getProp(LDCConstants.MA_NET_LOSS_LOCAL, "Internal").toString());
        netActualLoss += Double.parseDouble(incident.getProp(LDCConstants.MA_NET_ACTUAL_LOSS_LOCAL, "Internal").toString());

      }
      Value nbreIncidentsValue = new Value(nbreIncident);
      if (nbreIncident > 0) {
        LDCReportViewUtility.deleteSemiColon(drillDownList);
        nbreIncidentsValue.getItemRenderer().addParameter("drilldown", drillDownList.toString());
      }
      Value grossLossValue = new Value(grossLoss, userCurrencyId);
      Value grossActualLossValue = new Value(grossActualLoss, userCurrencyId);
      Value recoveriesValue = new Value(recoveries, userCurrencyId);
      Value netLossValue = new Value(netLoss, userCurrencyId);
      Value netActualLossValue = new Value(netActualLoss, userCurrencyId);
      this.dataSet.addItem(new Text(risk.getProp(LDCConstants.MA_RISK_CODE), false), index + "," + 1);
      this.dataSet.addItem(new Text(risk.getProp(LDCConstants.MA_SHORT_NAME), false), index + "," + 2);
      Item item = this.getEnumValue(this.reportContent, netRiskAttributeValues.get(risk.getProp(LDCConstants.MA_NET_RISK)), this.isExcel);
      if (item != null) {
        this.dataSet.addItem(item, index + "," + 3);
      }
      this.dataSet.addItem(nbreIncidentsValue, index + "," + 4);
      this.dataSet.addItem(grossLossValue, index + "," + 5);
      this.dataSet.addItem(grossActualLossValue, index + "," + 6);
      this.dataSet.addItem(recoveriesValue, index + "," + 7);
      this.dataSet.addItem(netLossValue, index + "," + 8);
      this.dataSet.addItem(netActualLossValue, index + "," + 9);
      index++;
      oColIncident.release();
      risk.release();
    }
    return incidentLossEvoView;
  }

  /**
   * @param megaRoot : megaRoot
   * @param metaAttribute : MetaAttribute ID
   * @return map of the correspondent Meta Attribute value
   */
  private HashMap<String, MegaObject> getAttributeValues(final MegaRoot megaRoot, final String metaAttribute) {
    MegaCollection attributeValues = megaRoot.getCollection(LDCConstants.MC_METAATTRIBUTE).get(metaAttribute).getCollection(LDCConstants.MAE_METAATTRIBUTEVALUE);
    HashMap<String, MegaObject> attributeValuesMap = new HashMap<String, MegaObject>();
    for (MegaObject attributeValue : attributeValues) {
      attributeValuesMap.put(attributeValue.getProp(LDCConstants.MA_INTERNAL_VALUE), attributeValue);
    }
    return attributeValuesMap;
  }

  /**
   * @param ireportContent : report content
   * @param metaValue : Meta Attribute Value
   * @param excel : check if it's an excel report
   * @return item which contains the (icon or color) and description of the Meta
   *         Attribute Value
   */
  private Item getEnumValue(final ReportContent ireportContent, final MegaObject metaValue, final boolean excel) {
    if ((metaValue != null) && (metaValue.getID() != null)) {
      Dataset diagramsDataSet = new Dataset("");
      Dimension diagramsDim1 = new Dimension("");
      Dimension diagramsDim2 = new Dimension("");
      diagramsDataSet.addDimension(diagramsDim1);
      diagramsDataSet.addDimension(diagramsDim2);

      View DiagramsView = new View(ireportContent.addDataset(diagramsDataSet));
      DiagramsView.addRenderer(AnalysisReportToolbox.rTable);
      DiagramsView.addParameter("borderWidth", "0");
      diagramsDim1.setSize(1);
      diagramsDim2.setSize(2);

      // r��cuparation des trois params essentiels
      String[] elements = new String[3];
      elements = BackTestingMatrixTable.getElementsFromMetaAttributeValue(metaValue);
      if ((elements[0] != null) && !elements[0].equalsIgnoreCase("")) {
        diagramsDataSet.addItem(new Image(elements[0]), "1,1");
      }
      Text text = new Text(" " + elements[2], false);
      if (excel) {
        text.getItemRenderer().addParameter("color", elements[1]);
        return text;
      }

      diagramsDataSet.addItem(text, "1,2");
      return DiagramsView;
    }
    return null;
  }

  /**
   * @param metaValue metaAttributeValue
   * @return a table that contains : t[0] = id metaPicture ;t[1] = Color (in
   *         hexa) ;t[2] = ValueName
   */

  public static String[] getElementsFromMetaAttributeValue(final MegaObject metaValue) {
    String[] elements = new String[3];
    elements[0] = "";
    elements[2] = "";
    elements[1] = "";
    if ((metaValue != null) && (metaValue.getID() != null)) {
      MegaCollection metaPictures = metaValue.getCollection(LDCConstants.MAE_PICTURE);
      if (metaPictures.size() > 0) {
        MegaObject metaPicture = metaPictures.get(1);
        metaPictures.release();
        elements[0] = metaPicture.megaField();
        metaPicture.release();
      }
      elements[2] = metaValue.getProp(LDCConstants.MA_VALUE_NAME);
      elements[1] = Palette.Color2Hex(Palette.getRGBfromParam(metaValue));
    }
    return elements;
  }

  /**
   * The risks related to the given collection
   * @param colSource The collection from which we collect the incidents
   * @param idMetaclass The identifier of the collection MetaClass
   * @param idMAE_SubType The identifier of the MetaAssociationEnd to obtain the
   *          children of the same type from the given collection
   * @return MegaCollection
   */
  private MegaCollection getRisksFromCol(final MegaCollection colSource, final String idMetaclass, final String idMAE_SubType) {
    if (!colSource.isEmpty()) {
      MegaCollection colIncidentFromSrcCol = this.root.getSelection("");
      colIncidentFromSrcCol.insert(colSource);

      ExtractionPath EP = new ExtractionPath();
      EP.initExtractionPath(this.root, null);
      EP.addPathItem(idMetaclass, idMAE_SubType, false, true);
      if (idMetaclass.equalsIgnoreCase(LDCConstants.MC_RISK_TYPE)) {
        EP.addPathItem(idMetaclass, LDCConstants.MAE_RISKTYPE_RISK, true, false);
      } else {
        EP.addPathItem(idMetaclass, LDCConstants.QUERY_GET_RISKS_FROM_RISK_ELEMENT, true, false);
      }
      return EP.extractDataFromPath(colIncidentFromSrcCol, false);
    }
    return null;
  }

}



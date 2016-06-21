
package com.sparnord.riskreport;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import com.sparnord.common.FilterTools;
import com.sparnord.common.LDCConstants;
import com.sparnord.common.LDCDateUtilities;
import com.sparnord.common.LDCReportViewUtility;
import com.sparnord.common.Palette;


/**
 * @author MAH
 * @version 750
 */
public class RiskIncidentReportGenerator {
  /*Local Variables*/
	 //for duplicate report template 
	 /* private static final String PARAM_END_DATE   = "D930D95256E23F76";
	  private static final String PARAM_BEGIN_DATE = "D930D95156E23E32";
	  private static final String PARAM_THRESHOLD  = "D930D95556E241F9";
	  private static final String PARAM_CURRENCY   = "D930D95456E240BA";*/
	  
	//new report template SN risk and incident report 	
  private static final String PARAM_END_DATE   = "B846BA3756E254D8";
  private static final String PARAM_BEGIN_DATE = "B846BA1C56E25485";
  private static final String PARAM_THRESHOLD  = "B846BA8956E2552E";
  private static final String PARAM_CURRENCY   = "B846B96356E25415";
	
  private static final String METAPICTURE_MEGEFIELD_RISK="~64SZgqB)yK91[Risk]";
  private static final String METAPICTURE_MEGEFIELD_KEY_RISK="~gXBHnOK6LvmU[RISK_KEY.ICO]";
  
	/* for back testing template
	private static final String PARAM_END_DATE   = "4AED4DF256E1219C";
	private static final String PARAM_BEGIN_DATE = "4AED4DB856E12149";
	private static final String PARAM_THRESHOLD  = "4AED4D6A56E120F6";
	private static final String PARAM_CURRENCY   = "4AED4CDE56E12097";
  */
  private MegaCollection      colRiskType;
  private MegaCollection      colBusProc;
  private MegaCollection      colOrgProc;
  private MegaCollection      colOrgUnit;
  private MegaCollection      colBusLine;
  private Double              netLossThresholdAmount;
  private Date                beginDate, endDate;
  private boolean			  isHtml=false; 

  private MegaRoot            root;
  ReportContent reportContent;
  int overallIncident=0;
  int incidents_Cal=0;
  static String riskCode="~PHawL1394f31[Risk Code]";
  
  public RiskIncidentReportGenerator(boolean isHtml){
	  this.isHtml=isHtml;
	  reportContent=new ReportContent("");
  }
  /**
   * @param oRoot The MegaRoot
   * @param parameters The report parameters
   * @param iContext iContext
   */
  public void initializeParameter(final MegaRoot oRoot, final Map<String, List<AnalysisParameter>> parameters, final short iContext) {
    this.root = oRoot;
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
    for (String paramType : parameters.keySet()) {
      for (AnalysisParameter analysisParam : parameters.get(paramType)) {
        for (MegaObject value : analysisParam.getValues()) {
          MegaObject classObject = value.getClassObject();
          if (classObject.sameID(LDCConstants.MC_BUSINESS_LINE)) {
        	 // SystemLog.log("MC_BUSINESS_LINE");
            this.colBusLine.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_BUSINESS_PROCESS)) {
        	 // SystemLog.log("MC_BUSINESS_PROCESS");
            this.colBusProc.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_ORG_UNIT)) {
        	  //SystemLog.log("MC_ORG_UNIT");
            this.colOrgUnit.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_ORGANIZATIONAL_PROCESS)) {
        	 // SystemLog.log("MC_ORGANIZATIONAL_PROCESS ");
            this.colOrgProc.insert(value);
          }
          if (classObject.sameID(LDCConstants.MC_RISK_TYPE)) {
        	  //SystemLog.log("MC_RISK_TYPE ");
            this.colRiskType.insert(value);
          }
          classObject.release();
        }
        
        //BeginDate
        if (paramType.equals(RiskIncidentReportGenerator.PARAM_BEGIN_DATE)) {
        	//SystemLog.log("begin date ");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              this.beginDate = LDCDateUtilities.resetBeginDateTime((Date) value.getValue());
            }
          }
        }
        //EndDate
        if (paramType.equals(RiskIncidentReportGenerator.PARAM_END_DATE)) {
        	//SystemLog.log("end date");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              this.endDate = LDCDateUtilities.resetEndDateTime((Date) value.getValue());
            }
          }
        }
        
        //Threshold
        if (paramType.equals(RiskIncidentReportGenerator.PARAM_THRESHOLD)) {
        	//SystemLog.log("threshold");
          for (final AnalysisSimpleTypeValue value : analysisParam.getSimpleValues()) {
            if (!value.getStringValue().isEmpty()) {
              if (!value.getStringValue().equals("")) {
                localNetlossThreshold = value.getStringValue();
              }

            }
          }
        }
        //Currency
        if (paramType.equals(RiskIncidentReportGenerator.PARAM_CURRENCY)) {
        	//SystemLog.log("Currency");
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
  }

  /**
   * @param reportContentParam : report content
   * @param chartTitle : chart title
   * @return The view built by the given parameters
   */
  public ReportContent generateContent() {
	   
	MegaCollection oColRisk = this.root.getSelection("");
    if ((this.colOrgUnit.size() == 0) && (this.colBusProc.size() == 0) && (this.colOrgProc.size() == 0) && (this.colBusLine.size() == 0) && (this.colRiskType.size() == 0)) {
      oColRisk.insert(this.root.getCollection(LDCConstants.MC_RISK));
    } else {
      // get risks linked to the selected OrgUnit or their children
    	//SystemLog.log("org size: "+this.colOrgUnit.size());
    	//String query_GetRisk_from_owning_entity=QueryGenerator.getQuery_from_owning_entity(this.colOrgUnit);
    	//SystemLog.log(query_GetRisk_from_owning_entity);
    	MegaCollection selectedRisks_from_owning_entity = getRisks_From_OwningEntity_v2(this.root,this.colOrgUnit);
    	//this.root.getSelection(query_GetRisk_from_owning_entity, 1, riskCode);
      //oColRisk.insert(this.getRisksFromCol(this.colOrgUnit, LDCConstants.MC_ORG_UNIT, LDCConstants.QUERY_SUB_ORGUNIT));
    	//SystemLog.log("size : "+selectedRisks_from_owning_entity.size());
      oColRisk.insert(selectedRisks_from_owning_entity);     
    	// get risks linked to the selected Org process or their children
      oColRisk.insert(this.getRisksFromCol(this.colOrgProc, LDCConstants.MC_ORGANIZATIONAL_PROCESS, LDCConstants.QUERY_SUB_ORGPROCESS));
      // get risks linked to the selected business process or their children
      oColRisk.insert(this.getRisksFromCol(this.colBusProc, LDCConstants.MC_BUSINESS_PROCESS, LDCConstants.QUERY_SUB_BPROCESS));
      // get risks linked to the selected business line or their children
      oColRisk.insert(this.getRisksFromCol(this.colBusLine, LDCConstants.MC_BUSINESS_LINE, LDCConstants.QUERY_SUB_BUSINESS_LINE));
      // get risks linked to the selected risk type or their children
      oColRisk.insert(this.getRisksFromCol(this.colRiskType, LDCConstants.MC_RISK_TYPE, LDCConstants.QUERY_SUB_RISKTYPE));
    }
    //filter by risk type
    //MegaCollection filtered_riskList =FilterTools.filterRisksByType(this.root,oColRisk, this.colRiskType);
    //sort the risk list
    ArrayList<MegaObject> riskList = new ArrayList<MegaObject>();
    for (MegaObject riskItem : oColRisk) {
    	riskList.add(riskItem);
    }
    Collections.sort(riskList, new Comparator<MegaObject>() {
    	@Override
        public int compare(MegaObject o1, MegaObject o2) {
    		try{
    		  return Integer.parseInt(o1.getProp("Risk Code"))-Integer.parseInt(o2.getProp("Risk Code"));
    		}catch(Exception e){
    		  return 0;
    		}   
        }
    });      

    for (MegaObject risk : riskList) {
    	  addRiskView(risk);
    }

      return reportContent;
  }
  
/// generate view for one risk
  
  private void addRiskView(MegaObject risk){
	//risk headline 
	//Text riskHeadline=new Text("<h2 style=\"margin-left:380px;\">Risk #"+RiskOperator.getCode(risk)+"</h2>", false);
	//riskHeadline.isHtml(true);
	//reportContent.addText(riskHeadline); 
	 ///////////
	final Dataset datasetRisk_part1=new Dataset(""); // for the first 9 attributes
	final Dataset datasetRisk_part2=new Dataset(""); // for the rest attributes
	 //dimensions
	final Dimension dimV_part1=new Dimension("");
	final Dimension dimH_part1=new Dimension("");
	final Dimension dimV_part2=new Dimension("");
	final Dimension dimH_part2=new Dimension("");
	dimV_part1.setSize(1);
	dimH_part1.setSize(11);
	dimV_part2.setSize(1);
	dimH_part2.setSize(3);
	 
	datasetRisk_part1.addDimension(dimV_part1);
	datasetRisk_part1.addDimension(dimH_part1);
	datasetRisk_part2.addDimension(dimV_part2);
	datasetRisk_part2.addDimension(dimH_part2);
	
	dimH_part1.addItem(new Text(" ", false)); 
	dimH_part1.addItem(styleText_verdana("Code")); 
	dimH_part1.addItem(styleText_verdana("Name"));
	dimH_part1.addItem(styleText_verdana("Status")); 
	dimH_part1.addItem(styleText_verdana("Parent Risk Type"));
	dimH_part1.addItem(styleText_verdana("Risk Type")); 
	dimH_part1.addItem(styleText_verdana("Owning Entity"));
	dimH_part1.addItem(styleText_verdana("Scope"));
	dimH_part1.addItem(styleText_verdana("Expected Loss")); 
	dimH_part1.addItem(styleText_verdana("Impact")); 
	dimH_part1.addItem(styleText_verdana("Likelihood")); 
	dimH_part2.addItem(styleText_verdana("Comment")); 
	dimH_part2.addItem(styleText_verdana("Loss Calculation"));
	dimH_part2.addItem(styleText_verdana("Identification Mode")); 		 	 	 	 	 	  	 	
	
	datasetRisk_part1.addItem(new Image("risk.gif","risk.gif"), 1+","+1);
	datasetRisk_part1.addItem(styleText_verdana("Risk #"+RiskOperator.getCode(risk)), 1+","+2);// risk code
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getShortName(risk)), 1+","+3); // Name
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getRiskStatus(risk)), 1+","+4); // Status
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getParentRiskType(risk)), 1+","+5); // Parent risk type
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getRiskType(risk)), 1+","+6);// risk type
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getOwningEntity(risk)), 1+","+7);// entity
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getElementAtRisk(risk)), 1+","+8);// element at risk
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getExpectedLoss(risk)), 1+","+9);//Expected loss
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getImpactERM(risk)), 1+","+10);//Impact
	datasetRisk_part1.addItem(styleText_verdana(RiskOperator.getLikeLihood(risk)), 1+","+11);//likelihood
	datasetRisk_part2.addItem(styleText_verdana(RiskOperator.getComment(risk)), 1+","+1);//comment
	datasetRisk_part2.addItem(styleText_verdana(RiskOperator.getLossCalculation(risk)), 1+","+2);//Loss Calculation
	datasetRisk_part2.addItem(styleText_verdana(RiskOperator.getIdentificationMode(risk)), 1+","+3);//Identification mode	

	String impactLevel=RiskOperator.getImpactERM(risk);
	String likelihoodLevel=RiskOperator.getLikeLihood(risk);			
		 //replace image with key risk img
		 if(RiskOperator.isKeyRisk(risk)){
			datasetRisk_part1.addItem(new Image("key risk (bizcon).gif", "key risk"), 1+","+1); 
			//RISK.ICO.gif METAPICTURE_MEGEFIELD_KEY_RISK
		 }
		 
		 if(isHtml){
			////impact erm text and img
			datasetRisk_part1.addItem(viewGeneration_Color(impactLevel), 1+","+10); 
			////likelihood text and img
			datasetRisk_part1.addItem(viewGeneration_Color(likelihoodLevel), 1+","+11);
			
		 } else {
			////impact erm text with background color
			datasetRisk_part1.addItem(textGeneration_Color(impactLevel), 1+","+10); 
			////likelihood text with background color
			datasetRisk_part1.addItem(textGeneration_Color(likelihoodLevel), 1+","+11);
		 }
	 		 	
	
	 /// add risk view part 1
	 final View riskView_part1=new View(reportContent.addDataset(datasetRisk_part1));//id
	 riskView_part1.addParameter("tablewidth", "860");
	 riskView_part1.addRenderer(AnalysisReportToolbox.rTable);	 
	 reportContent.addView(riskView_part1);
	 /// add risk view part 2
	 final View riskView_part2=new View(reportContent.addDataset(datasetRisk_part2));//id
	 riskView_part2.addParameter("tablewidth", "860");
	 riskView_part2.addRenderer(AnalysisReportToolbox.rTable);	 
	 reportContent.addView(riskView_part2);
	 
	 //controls title separate line
	 if(RiskOperator.getPreventiveControl(risk).size()>0){
		 Text control_separate_line=new Text("<br>", false);
		 control_separate_line.isHtml(true);
		 reportContent.addText(control_separate_line); 
		 /////////// add control view
		 View ControlTableView=generateViewForControlObjects(risk);
	 	 reportContent.addView(ControlTableView);
	 }
	 //incidents title separate line
	 if(risk.getCollection(LDCConstants.MAE_RISK_INCIDENT).size()>0){
	 	 Text incident_separate_line=new Text("<br>", false);
	 	 incident_separate_line.isHtml(true);
	 	 reportContent.addText(incident_separate_line); 
	 	 /////////// add incident view
	 	 View incidentTableView=generateViewForIncidents(risk);
	 	 reportContent.addView(incidentTableView);
	 }
	 
	 
	 //start separate line, ending one risk here
	 Text sepLine=new Text("<br><center>****************************************************************************</center><br>", false);
	 sepLine.isHtml(true);
	 reportContent.addText(sepLine);
	 ///////////
	  
	  
  }
  
  // generate control view for risk
  private View generateViewForControlObjects(MegaObject risk){
	 	 
		 MegaCollection controls=RiskOperator.getPreventiveControl(risk);
		
		 final Dataset controlDataset=new Dataset("");
	 	 
	 	 final Dimension dimV=new Dimension("");
	 	 final Dimension dimH=new Dimension("");
	 	 dimV.setSize(controls.size());
	 	 dimH.setSize(6);
	 	
	 	 controlDataset.addDimension(dimV);
	 	 controlDataset.addDimension(dimH);	 	 
	 
	 	 if(controls.size()>0){	 		 		
	 		 
	 		 dimH.addItem(new Text(" ", false));
	 		 dimH.addItem(styleText_verdana("Code"));
		 	 dimH.addItem(styleText_verdana("Name"));
		 	 dimH.addItem(styleText_verdana("Control Objective")); 
		 	 dimH.addItem(styleText_verdana("Entity Owner"));
		 	 dimH.addItem(styleText_verdana("Control Nature")); 
		 		 	 
	 	   for (int i=1;i<=controls.size();i++){
	 		 controlDataset.addItem(new Image("control (grc).gif", "control (grc).gif"), i+","+1);
	 		 controlDataset.addItem(styleText_verdana("Control #"+ControlOperator.getCode(controls.get(i))), i+","+2);// control code
		 	 controlDataset.addItem(styleText_verdana(ControlOperator.getShortName(controls.get(i))), i+","+3); // Name		 	  
		 	 controlDataset.addItem(styleText_verdana(ControlOperator.getControlObjectiv(controls.get(i))), i+","+4); // Control objective
		 	 controlDataset.addItem(styleText_verdana(ControlOperator.getOwningEntity(controls.get(i))), i+","+5); // owning entity
		 	 controlDataset.addItem(styleText_verdana(ControlOperator.getControlNature(controls.get(i))), i+","+6);// control nature
	 	   }
	 	 }
	 	
	 	 final View controlView=new View(reportContent.addDataset(controlDataset));//id
	 	 controlView.addParameter("tablewidth", "860");
	 	 controlView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return controlView;
		
	}
  
  /// generate incident view for one risk
  private View generateViewForIncidents(MegaObject risk){
	  
	    Object userCurrencyId = this.root.currentEnvironment().getCurrency().getUserCurrencyId();
	  	double grossActualLoss = 0.0;
	  	double recoveries = 0.0;
	  	double netActualLoss = 0.0;
	 	 
	     MegaCollection incidents = risk.getCollection(LDCConstants.MAE_RISK_INCIDENT);
	     MegaCollection filteredIncidents=root.getSelection("");
	     
	     for(MegaObject selectedIncident:incidents){
	    	    Double netLossLocalAmmount = Double.parseDouble(selectedIncident.getProp(LDCConstants.MA_NET_LOSS_LOCAL, "Internal").toString());
	 	        // Check if Amount exceeds Threshold 
	 	        if (this.netLossThresholdAmount == null) {
	 	          continue;
	 	        } else if (netLossLocalAmmount < this.netLossThresholdAmount) {
	 	        	//skip object if amount lower than threshold
	 	          //Don't continue treatment, go to next object in loop
	 	          continue;
	 	        }
	 	        // check if occurence date is between begin date and end date
	 	        Date mgIncidentDate = LDCDateUtilities.resetTime(this.root, (Date) selectedIncident.getProp(LDCConstants.MA_DECLARATION_DATE, "internal"));
	 	        if (!((mgIncidentDate.after(this.beginDate) || mgIncidentDate.equals(this.beginDate)) && (mgIncidentDate.before(this.endDate) || (mgIncidentDate.equals((this.endDate)))))) {
	 	          //Don't continue treatment, go to next object in loop	        	
	 	          continue;
	 	        }
	 	       
	 	       /*SystemLog.log("Declaration date:"+mgIncidentDate.toString());
	        	SystemLog.log("Begin date:"+this.beginDate.toString());
	        	SystemLog.log("End date:"+this.endDate.toString());
	        	SystemLog.log("-----------------------------");*/
	 	       filteredIncidents.insert(selectedIncident); 	    	 
	     }
		
			 	 
	 	 if(filteredIncidents.size()>0){	
	 		 final Dataset incidentDataset=new Dataset("");
		 	 
		 	 final Dimension dimV=new Dimension("");
		 	 final Dimension dimH=new Dimension("");
		 	 dimV.setSize(filteredIncidents.size()+1);
		 	 dimH.setSize(13);
		 	
		 	 incidentDataset.addDimension(dimV);
		 	 incidentDataset.addDimension(dimH);
	 		 
	 		 dimH.addItem(new Text(" ", false));
	 		 dimH.addItem(styleText_verdana("Code"));
	 		 dimH.addItem(styleText_verdana("Occurrence Date"));
	 		 dimH.addItem(styleText_verdana("Near Miss"));
		 	 dimH.addItem(styleText_verdana("Name"));
		 	 dimH.addItem(styleText_verdana("Description"));
		 	 dimH.addItem(styleText_verdana("Parent Risk Type")); 
		 	 dimH.addItem(styleText_verdana("Risk Type"));
		 	 dimH.addItem(styleText_verdana("Scope"));//entity connected to the incident
		 	 dimH.addItem(styleText_verdana("Control Failed")); 
		 	 dimH.addItem(styleText_verdana("Net Actual Loss"));	
		 	 dimH.addItem(styleText_verdana("Gross Actual Loss"));	
		 	 dimH.addItem(styleText_verdana("Recoveries"));	
		 	 
	 	   for (int i=1;i<=filteredIncidents.size();i++){
	 		 
	 		incidents_Cal+=1;
	 		incidentDataset.addItem(new Image("incident (bizcon).gif", "incident (bizcon).gif"), i+","+1);// icon image
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getCode(filteredIncidents.get(i))), i+","+2);// code
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getDate(filteredIncidents.get(i))), i+","+3);// Date
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getNearMiss(filteredIncidents.get(i))), i+","+4);// Near Miss
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getName(filteredIncidents.get(i))), i+","+5);// Name
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getComment(filteredIncidents.get(i))), i+","+6);// comment
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getParentRiskType(filteredIncidents.get(i))), i+","+7);// Parent Risk Type
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getRiskType(filteredIncidents.get(i))), i+","+8);// Risk Type
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getEntity(filteredIncidents.get(i))), i+","+9);// entity
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getControl(filteredIncidents.get(i))), i+","+10);// Control Failed
	 		//incidentDataset.addItem(new Value(IncidentOperator.getNetActualLoss(filteredIncidents.get(i)), userCurrencyId),i+","+11);// Net Actual Loss
	 		//incidentDataset.addItem(new Value(IncidentOperator.getGrossActualLoss(filteredIncidents.get(i)), userCurrencyId), i+","+12);// Gross Actual Loss
	 		//incidentDataset.addItem(new Value(IncidentOperator.getRecoveries(filteredIncidents.get(i)), userCurrencyId), i+","+13);// Recoveries
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getNetActualLossString(filteredIncidents.get(i))),i+","+11);// Net Actual Loss
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getGrossActualLossString(filteredIncidents.get(i))), i+","+12);// Gross Actual Loss
	 		incidentDataset.addItem(styleText_verdana(IncidentOperator.getRecoveriesString(filteredIncidents.get(i))), i+","+13);// Recoveries
	 		
	 		
	 		 grossActualLoss += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_GROSS_ACTUAL_LOSS_LOCAL, "Internal").toString());
	         recoveries += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_RECOVERIES_LOCAL, "Internal").toString());
	         netActualLoss += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_NET_ACTUAL_LOSS_LOCAL, "Internal").toString());
	 		
	 	   }
	 	      //add sum to the view
		      Value grossActualLossValue = new Value(grossActualLoss, userCurrencyId);
		      Value recoveriesValue = new Value(recoveries, userCurrencyId);
		      Value netActualLossValue = new Value(netActualLoss, userCurrencyId);
		      incidentDataset.addItem(styleText_verdana("Sum"),filteredIncidents.size()+1+","+1);
		      incidentDataset.addItem(netActualLossValue,filteredIncidents.size()+1+","+11);
		      incidentDataset.addItem(grossActualLossValue,filteredIncidents.size()+1+","+12);
		      incidentDataset.addItem(recoveriesValue,filteredIncidents.size()+1+","+13);
		 	  /////////////
		      
		      final View incidentView=new View(reportContent.addDataset(incidentDataset));//id
			  incidentView.addParameter("tablewidth", "860");
			  incidentView.addRenderer(AnalysisReportToolbox.rTable);
			  return incidentView;
			 	 
	 	 }else {
	 		  return null;
	 	 }
	}
  
  private Text textGeneration_Color(String level){
		Text levelText=styleText_verdana(level);
		levelText.getItemRenderer().addParameter("color",ColorCode.getColorCodeFromText(level));
		return levelText;
	}
  
  private View viewGeneration_Color(String level){
		 final Dataset myDataset=new Dataset("");
	 	 final Dimension dimV=new Dimension("");
	 	 final Dimension dimH=new Dimension("");
	 	 dimV.setSize(1);
	 	 dimH.setSize(2);
	 	myDataset.addDimension(dimV);
	 	myDataset.addDimension(dimH);	
	 	myDataset.addItem(getColorImage(level), 1+","+1);
	 	myDataset.addItem(styleText_verdana(level), 1+","+2);
	 	 
	 	 final View myView=new View(reportContent.addDataset(myDataset));//id
	 	 myView.addParameter("borderWidth", "0");
	 	 myView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return myView;
	}
	
	private Image getColorImage(String level){
		switch (level.toLowerCase()) {
     case "very low":  	return new Image("square_g4.gif", level);
     case "low": 		return new Image("square_g2.gif", level);
     case "medium":  	return new Image("square_y2.gif", level);
     case "high":  		return new Image("square_o2.gif", level);
     case "very high":  	return new Image("square_r4.gif", level);
     case "rare":  		return new Image("square_g4.gif", level);
     case "possible": 	return new Image("square_g2.gif", level);
     case "likely":  	return new Image("square_y2.gif", level);
     case "probable":  	return new Image("square_o2.gif", level);
     case "certain":  	return new Image("square_r4.gif", level);
     default: 			return new Image("", level);    
		}
	}
	
	private Text styleText_verdana(String text){
		Text styleText=new Text("<p style=\"word-wrap:break-word;margin:0;font-family:verdana;font-size:9px;\">"+text+"</p>", false);
		styleText.isHtml(true);
	 	return styleText;
	}

	public static MegaCollection getRisks_From_OwningEntity_v2(final MegaRoot root, MegaCollection orgUnits) {   
	    MegaCollection allRisks=root.getSelection("");
	    boolean hasOrgUnits=orgUnits.size()>0? true:false;	
	    int orgunit_num=0;
		if(hasOrgUnits){
		    for(MegaObject orgunit:orgUnits){
		    	orgunit_num++;
	           String query="Select [Org-Unit] Into @orgv Where [Aggregation of].[Absolute Identifier] Deeply "
						 +"'"+orgunit.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+orgunit.getProp("Absolute Identifier")+"'";						
	           query+=" Select [Risk] Where [Owning Entity] in @orgv";
	           allRisks.insert(root.getSelection(query));
	           //SystemLog.log(" orgunit_num:"+orgunit_num);
		    }
		    return allRisks;
		}else {
			
			return root.getSelection("select [Risk]");
		}
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



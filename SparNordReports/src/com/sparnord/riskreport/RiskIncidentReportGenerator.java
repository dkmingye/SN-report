
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
      //int nbreIncident = 0;
     // MegaCollection oColIncident = risk.getCollection(LDCConstants.MAE_RISK_INCIDENT);
      
      addRiskView(risk);
	 	 
     /* ////////////////////////start incidents calculation////////////////
      for (MegaObject incident : oColIncident) {
        Double netLossLocalAmmount = Double.parseDouble(incident.getProp(LDCConstants.MA_NET_LOSS_LOCAL, "Internal").toString());
        // Check if Amount exceeds Threshold 
        if (this.netLossThresholdAmount == null) {
          continue;
        } else if (netLossLocalAmmount < this.netLossThresholdAmount) {
        	//skip object if amount lower than threshold
          //Don't continue treatment, go to next object in loop
          continue;
        }
        // check if declaration date is between begin date and end date
        Date mgIncidentDate = LDCDateUtilities.resetTime(this.root, (Date) incident.getProp(LDCConstants.MA_DECLARATION_DATE, "internal"));
        if (!((mgIncidentDate.after(this.beginDate) || mgIncidentDate.equals(this.beginDate)) && (mgIncidentDate.before(this.endDate) || (mgIncidentDate.equals((this.endDate)))))) {
          //Don't continue treatment, go to next object in loop
          continue;
        }       
        nbreIncident += 1;
        
      }
      ////////////////////////end incidents////////////////
	      overallIncident+=nbreIncident;*/
    }
    
    
      //reportContent.addText(new Text("risk size "+oColRisk.size(),false));
      // reportContent.addText(new Text("incidents size "+overallIncident,false));
      //reportContent.addText(new Text("cal incidents size "+incidents_Cal,false));
      return reportContent;
  }
  
/// generate view for one risk
  
  private void addRiskView(MegaObject risk){
	//risk headline 
	Text riskHeadline=new Text("<h2 style=\"margin-left:380px;\">Risk #"+RiskOperator.getCode(risk)+"</h2>", false);
	riskHeadline.isHtml(true);
	reportContent.addText(riskHeadline); 
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
	dimH_part1.addItem(new Text("Code", false)); 
	dimH_part1.addItem(new Text("Name", false));
	dimH_part1.addItem(new Text("Status", false)); 
	dimH_part1.addItem(new Text("Parent Risk Type", false));
	dimH_part1.addItem(new Text("Risk Type", false)); 
	dimH_part1.addItem(new Text("Owning Entity", false));
	dimH_part1.addItem(new Text("Scope", false));
	dimH_part1.addItem(new Text("Expected Loss", false)); 
	dimH_part1.addItem(new Text("Impact", false)); 
	dimH_part1.addItem(new Text("Likelihood", false)); 
	dimH_part2.addItem(new Text("Comment", false)); 
	dimH_part2.addItem(new Text("Loss Calculation", false));
	dimH_part2.addItem(new Text("Identification Mode", false)); 		 	 	 	 	 	  	 	
	
	datasetRisk_part1.addItem(new Image("risk.gif", "riskgif"), 1+","+1);
	datasetRisk_part1.addItem(new Text("Risk #"+RiskOperator.getCode(risk), false), 1+","+2);// risk code
	datasetRisk_part1.addItem(new Text(RiskOperator.getShortName(risk), false), 1+","+3); // Name
	datasetRisk_part1.addItem(new Text(RiskOperator.getRiskStatus(risk), false), 1+","+4); // Status
	datasetRisk_part1.addItem(new Text(RiskOperator.getParentRiskType(risk), false), 1+","+5); // Parent risk type
	datasetRisk_part1.addItem(new Text(RiskOperator.getRiskType(risk), false), 1+","+6);// risk type
	datasetRisk_part1.addItem(new Text(RiskOperator.getOwningEntity(risk), false), 1+","+7);// entity
	datasetRisk_part1.addItem(new Text(RiskOperator.getElementAtRisk(risk), false), 1+","+8);// element at risk
	datasetRisk_part1.addItem(new Text(RiskOperator.getExpectedLoss(risk), false), 1+","+9);//Expected loss
	datasetRisk_part1.addItem(new Text(RiskOperator.getImpactERM(risk), false), 1+","+10);//Impact
	datasetRisk_part1.addItem(new Text(RiskOperator.getLikeLihood(risk), false), 1+","+11);//likelihood
	datasetRisk_part2.addItem(new Text(RiskOperator.getComment(risk), false), 1+","+1);//comment
	datasetRisk_part2.addItem(new Text(RiskOperator.getLossCalculation(risk), false), 1+","+2);//Loss Calculation
	datasetRisk_part2.addItem(new Text(RiskOperator.getIdentificationMode(risk), false), 1+","+3);//Identification mode	

	String impactLevel=RiskOperator.getImpactERM(risk);
	String likelihoodLevel=RiskOperator.getLikeLihood(risk);			
		 //replace image with key risk img
		 if(RiskOperator.isKeyRisk(risk)){
			datasetRisk_part1.addItem(new Image("key risk (bizcon).gif", "key risk"), 1+","+1); 
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
	 riskView_part1.addParameter("tablewidth", "830");
	 riskView_part1.addRenderer(AnalysisReportToolbox.rTable);	 
	 reportContent.addView(riskView_part1);
	 /// add risk view part 2
	 final View riskView_part2=new View(reportContent.addDataset(datasetRisk_part2));//id
	riskView_part2.addParameter("tablewidth", "830");
	riskView_part2.addRenderer(AnalysisReportToolbox.rTable);	 
	 reportContent.addView(riskView_part2);
	 
	 //controls title separate line
		 Text controlTitle=new Text("<br><h2 style=\"margin-left:380px;\">Controls</h2>", false);
		 controlTitle.isHtml(true);
		 reportContent.addText(controlTitle); 
		 /////////// add control view
		 View ControlTableView=generateViewForControlObjects(risk);
	 	 reportContent.addView(ControlTableView);
		 
	 //incidents title separate line
	 Text incidentTitle=new Text("<br><h2 style=\"margin-left:380px;\">Incidents</h2>", false);
	 incidentTitle.isHtml(true);
	 reportContent.addText(incidentTitle); 
	 
	 /////////// add incident view
	 View incidentTableView=generateViewForIncidents(risk);
	 reportContent.addView(incidentTableView);
	 
	 //start separate line, ending one risk here
	 Text sepLine=new Text("<br>****************************************************************************<br>", false);
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
	 		 dimH.addItem(new Text("Code", false));
		 	 dimH.addItem(new Text("Name", false));
		 	 dimH.addItem(new Text("Control Objective", false)); 
		 	 dimH.addItem(new Text("Entity Owner", false));
		 	 dimH.addItem(new Text("Control Nature", false)); 
		 	 dimH.addItem(new Text("Action Plan", false));	
		 	 
		 		 	 
	 	   for (int i=1;i<=controls.size();i++){
	 		 controlDataset.addItem(new Image("control (grc).gif", "control (grc).gif"), i+","+1);
	 		 controlDataset.addItem(new Text("Control #"+ControlOperator.getCode(controls.get(i)), false), i+","+2);// control code
		 	 controlDataset.addItem(new Text(ControlOperator.getShortName(controls.get(i)), false), i+","+3); // Name		 	  
		 	 controlDataset.addItem(new Text(ControlOperator.getControlObjectiv(controls.get(i)), false), i+","+4); // Control objective
		 	 controlDataset.addItem(new Text(ControlOperator.getOwningEntity(controls.get(i)), false), i+","+5); // owning entity
		 	 controlDataset.addItem(new Text(ControlOperator.getControlNature(controls.get(i)), false), i+","+6);// control nature
		 	 controlDataset.addItem(new Text(ControlOperator.getActionPlan(controls.get(i)), false), i+","+7);// action plan	 		 
	 	   
	 	   }
	 	 }
	 	
	 	 final View controlView=new View(reportContent.addDataset(controlDataset));//id
	 	 controlView.addParameter("tablewidth", "830");
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
		 	 dimV.setSize(incidents.size()+1);
		 	 dimH.setSize(13);
		 	
		 	 incidentDataset.addDimension(dimV);
		 	 incidentDataset.addDimension(dimH);
	 		 
	 		 dimH.addItem(new Text(" ", false));
	 		 dimH.addItem(new Text("Code", false));
	 		 dimH.addItem(new Text("Occurrence Date", false));
	 		 dimH.addItem(new Text("Near Miss", false));
		 	 dimH.addItem(new Text("Name", false));
		 	 dimH.addItem(new Text("Description", false));
		 	 dimH.addItem(new Text("Parent Risk Type", false)); 
		 	 dimH.addItem(new Text("Risk Type", false));
		 	 dimH.addItem(new Text("Scope", false));//entity connected to the incident
		 	 dimH.addItem(new Text("Control Failed", false)); 
		 	 dimH.addItem(new Text("Net Actual Loss", false));	
		 	 dimH.addItem(new Text("Gross Actual Loss", false));	
		 	 dimH.addItem(new Text("Recoveries", false));	
		 	 
	 	   for (int i=1;i<=filteredIncidents.size();i++){		   
	 		incidents_Cal+=1;
	 		incidentDataset.addItem(new Image("incident (bizcon).gif", "incident (bizcon).gif"), i+","+1);// icon image
	 		incidentDataset.addItem(new Text(IncidentOperator.getCode(filteredIncidents.get(i)), false), i+","+2);// code
	 		incidentDataset.addItem(new Text(IncidentOperator.getDate(filteredIncidents.get(i)), false), i+","+3);// Date
	 		incidentDataset.addItem(new Text(IncidentOperator.getNearMiss(filteredIncidents.get(i)), false), i+","+4);// Near Miss
	 		incidentDataset.addItem(new Text(IncidentOperator.getName(filteredIncidents.get(i)), false), i+","+5);// Name
	 		incidentDataset.addItem(new Text(IncidentOperator.getComment(filteredIncidents.get(i)), false), i+","+6);// comment
	 		incidentDataset.addItem(new Text(IncidentOperator.getParentRiskType(filteredIncidents.get(i)), false), i+","+7);// Parent Risk Type
	 		incidentDataset.addItem(new Text(IncidentOperator.getRiskType(filteredIncidents.get(i)), false), i+","+8);// Risk Type
	 		incidentDataset.addItem(new Text(IncidentOperator.getEntity(filteredIncidents.get(i)), false), i+","+9);// entity
	 		incidentDataset.addItem(new Text(IncidentOperator.getControl(filteredIncidents.get(i)), false), i+","+10);// Control Failed
	 		incidentDataset.addItem(new Value(IncidentOperator.getNetActualLoss(filteredIncidents.get(i)), userCurrencyId),i+","+11);// Net Actual Loss
	 		incidentDataset.addItem(new Value(IncidentOperator.getGrossActualLoss(filteredIncidents.get(i)), userCurrencyId), i+","+12);// Gross Actual Loss
	 		incidentDataset.addItem(new Value(IncidentOperator.getRecoveries(filteredIncidents.get(i)), userCurrencyId), i+","+13);// Recoveries
	 		
	 		
	 		 grossActualLoss += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_GROSS_ACTUAL_LOSS_LOCAL, "Internal").toString());
	         recoveries += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_RECOVERIES_LOCAL, "Internal").toString());
	         netActualLoss += Double.parseDouble(filteredIncidents.get(i).getProp(LDCConstants.MA_NET_ACTUAL_LOSS_LOCAL, "Internal").toString());

	 	   }
	 	      //add sum to the view
		      Value grossActualLossValue = new Value(grossActualLoss, userCurrencyId);
		      Value recoveriesValue = new Value(recoveries, userCurrencyId);
		      Value netActualLossValue = new Value(netActualLoss, userCurrencyId);
		      
		      incidentDataset.addItem(new Text("Sum",false),incidents.size()+1+","+1);
		      incidentDataset.addItem(netActualLossValue,incidents.size()+1+","+11);
		      incidentDataset.addItem(grossActualLossValue,incidents.size()+1+","+12);
		      incidentDataset.addItem(recoveriesValue,incidents.size()+1+","+13);
		 	  /////////////
		      
		      final View incidentView=new View(reportContent.addDataset(incidentDataset));//id
			  incidentView.addParameter("tablewidth", "830");
			  incidentView.addRenderer(AnalysisReportToolbox.rTable);
			  return incidentView;
			 	 
	 	 }else {
	 		  return null;
	 	 }
	}
  
  private Text textGeneration_Color(String level){
		Text levelText=new Text(level,false);
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
	 	myDataset.addItem(new Text(level, false), 1+","+2);
	 	 
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



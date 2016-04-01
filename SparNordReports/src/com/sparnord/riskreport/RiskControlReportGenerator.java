package com.sparnord.riskreport;

import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.Image;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;

public class RiskControlReportGenerator {
	MegaCollection risks;
	ReportContent reportContent;
	
	public RiskControlReportGenerator(MegaCollection risks, ReportContent reportContent){
		this.risks=risks;
		this.reportContent=reportContent;		
	}
	
	public ReportContent getReportRiskControl(){
		
		 for(MegaObject risk : risks){
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
	 	dimH_part1.addItem(new Text("Scope", false));// element at risk
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
	 	////part 2 
	 	datasetRisk_part2.addItem(new Text(RiskOperator.getComment(risk), false), 1+","+1);//comment
	 	datasetRisk_part2.addItem(new Text(RiskOperator.getLossCalculation(risk), false), 1+","+2);//Loss Calculation
	 	datasetRisk_part2.addItem(new Text(RiskOperator.getIdentificationMode(risk), false), 1+","+3);//Identification mode	
	 	
	 
	 	String impactLevel=RiskOperator.getImpactERM(risk);
	 	String likelihoodLevel=RiskOperator.getLikeLihood(risk);
	 	     //replace image with key risk img
	 		 if(RiskOperator.isKeyRisk(risk)){
	 			datasetRisk_part1.addItem(new Image("key risk (bizcon).gif", "key risk"), 1+","+1); 
	 		 }
	 		 ////impact erm text and img
		 	datasetRisk_part1.addItem(viewGeneration_Color(impactLevel), 1+","+10); 
	 		
	 		 ////likelihood text and img
		 	datasetRisk_part1.addItem(viewGeneration_Color(likelihoodLevel), 1+","+11);	 	
	 	
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
	 	 
	 	 //control title separate line
	 	 Text controlTitle=new Text("<br><h2 style=\"margin-left:380px;\">Controls</h2>", false);
	 	 controlTitle.isHtml(true);
	 	 reportContent.addText(controlTitle); 
	 	 ///////////
	 	 View ControlTableView=generateViewForControlObjects(risk);
	 	 reportContent.addView(ControlTableView);
	 	 
	 	 //start separate line, ending one risk here
	 	 Text sepLine=new Text("<br>****************************************************************************<br>", false);
	 	 sepLine.isHtml(true);
	 	 reportContent.addText(sepLine);
	 	 ///////////
	 	
		}
		 
		return reportContent;
	}
	
	
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
	
	private String getColor(String level){
		switch (level.toLowerCase()) {
        case "very low":  	return "#225F16";
        case "low": 		return "#4EDA37";
        case "medium":  	return "#FFD55B";
        case "high":  		return "#FF9228";
        case "very high":  	return "#D12800";
        case "rare":  		return "#225F16";
        case "possible": 	return "#4EDA37";
        case "likely":  	return "#FFD55B";
        case "probable":  	return "#FF9228";
        case "certain":  	return "#D12800";
        default: 			return "";    
     }
	}

}

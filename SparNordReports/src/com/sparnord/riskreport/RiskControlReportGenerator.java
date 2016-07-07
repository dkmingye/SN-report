package com.sparnord.riskreport;

import java.util.ArrayList;

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
	ArrayList<MegaObject> risks;
	ReportContent reportContent;
	private boolean isHtml;
	
	public RiskControlReportGenerator(ArrayList<MegaObject> risks, ReportContent reportContent,boolean isHtml){
		this.risks=risks;
		this.reportContent=reportContent;		
		this.isHtml=isHtml;
	}
	
	public ReportContent getReportRiskControl(){
		Text html_center_begin=new Text("<center>", false);
		Text html_center_close=new Text("</center>", false);
		html_center_begin.isHtml(true);
		html_center_close.isHtml(true);
		
		 for(MegaObject risk : risks){
			 boolean isKeyRisk=RiskOperator.isKeyRisk(risk);
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
	 	
	 	dimH_part1.addItem(styleText_verdana_auto(" ")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Code")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Name"));
	 	dimH_part1.addItem(styleText_verdana_auto("Status")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Parent Risk Type"));
	 	dimH_part1.addItem(styleText_verdana_auto("Risk Type")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Owning Entity"));
	 	dimH_part1.addItem(styleText_verdana_auto("Scope"));// element at risk
	 	dimH_part1.addItem(styleText_verdana_auto("Expected Loss")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Impact")); 
	 	dimH_part1.addItem(styleText_verdana_auto("Likelihood")); 
	 	dimH_part2.addItem(styleText_verdana_auto("Comment")); 
	 	dimH_part2.addItem(styleText_verdana_auto("Loss Calculation"));
	 	dimH_part2.addItem(styleText_verdana_auto("Identification Mode")); 		 	 	 	 	 	 	  	 	
	 	
	 	datasetRisk_part1.addItem(new Image("risk.gif", "riskgif"), 1+","+1);
	 	datasetRisk_part1.addItem(styleText_verdana_auto("Risk #"+RiskOperator.getCode(risk)), 1+","+2);// risk code
	 	datasetRisk_part1.addItem(styleText_verdana_breakWord(RiskOperator.getShortName(risk)), 1+","+3); // Name
	 	datasetRisk_part1.addItem(styleText_verdana_auto(RiskOperator.getRiskStatus(risk)), 1+","+4); // Status
	 	datasetRisk_part1.addItem(styleText_verdana_breakWord(RiskOperator.getParentRiskType(risk)), 1+","+5); // Parent risk type
	 	datasetRisk_part1.addItem(styleText_verdana_breakWord(RiskOperator.getRiskType(risk)), 1+","+6);// risk type
	 	datasetRisk_part1.addItem(styleText_verdana_breakWord(RiskOperator.getOwningEntity(risk)), 1+","+7);// entity
	 	datasetRisk_part1.addItem(styleText_verdana_breakWord(RiskOperator.getElementAtRisk(risk)), 1+","+8);// element at risk
	 	datasetRisk_part1.addItem(styleText_verdana_auto(RiskOperator.getExpectedLoss(risk)), 1+","+9);//Expected loss
	 	datasetRisk_part1.addItem(styleText_verdana_auto(RiskOperator.getImpactERM(risk)), 1+","+10);//Impact
	 	datasetRisk_part1.addItem(styleText_verdana_auto(RiskOperator.getLikeLihood(risk)), 1+","+11);//likelihood
	 	////part 2 
	 	datasetRisk_part2.addItem(styleText_verdana_breakWord(RiskOperator.getComment(risk)), 1+","+1);//comment
	 	datasetRisk_part2.addItem(styleText_verdana_breakWord(RiskOperator.getLossCalculation(risk)), 1+","+2);//Loss Calculation
	 	datasetRisk_part2.addItem(styleText_verdana_auto(RiskOperator.getIdentificationMode(risk)), 1+","+3);//Identification mode	
	 	
	 
	 	String impactLevel=RiskOperator.getImpactERM(risk);
	 	String likelihoodLevel=RiskOperator.getLikeLihood(risk);
	 	     //replace image with key risk img
	 		 if(isKeyRisk){
	 			datasetRisk_part1.addItem(new Image("key risk (bizcon).gif", "key risk"), 1+","+1); 
	 		 }
	 		 
	 		 if(isHtml){
	 			////impact erm text and img
	 			//datasetRisk_part1.addItem(viewGeneration_Color(impactLevel,isKeyRisk), 1+","+10);
	 			datasetRisk_part1.addItem(viewGeneration_Color_On_Heatmap(impactLevel,likelihoodLevel,isKeyRisk,true), 1+","+10); 
	 			////likelihood text and img
	 			//datasetRisk_part1.addItem(viewGeneration_Color(likelihoodLevel,isKeyRisk), 1+","+11);
	 			datasetRisk_part1.addItem(viewGeneration_Color_On_Heatmap(impactLevel,likelihoodLevel,isKeyRisk,false), 1+","+11); 
	 			
	 		 } else { 			 
	 			////impact erm text with background color
	 			//datasetRisk_part1.addItem(textGeneration_Color(impactLevel,isKeyRisk), 1+","+10); 
	 			datasetRisk_part1.addItem(textGeneration_Color_On_Heatmap(impactLevel,likelihoodLevel,isKeyRisk,true), 1+","+10); 
	 			////likelihood text with background color
	 			//datasetRisk_part1.addItem(textGeneration_Color(likelihoodLevel,isKeyRisk), 1+","+11);
	 			datasetRisk_part1.addItem(textGeneration_Color_On_Heatmap(impactLevel,likelihoodLevel,isKeyRisk,false), 1+","+11); 
	 		 }
	 		  	
	 	
	 	 /// add risk view part 1
	 	 final View riskView_part1=new View(reportContent.addDataset(datasetRisk_part1));//id
	 	 riskView_part1.addParameter("tablewidth", "860");
	 	 riskView_part1.addRenderer(AnalysisReportToolbox.rTable);	
	 	 reportContent.addText(html_center_begin);
	 	 reportContent.addView(riskView_part1);
	 	 reportContent.addText(html_center_close);
	 	 /// add risk view part 2
	 	 final View riskView_part2=new View(reportContent.addDataset(datasetRisk_part2));//id
	 	 riskView_part2.addParameter("tablewidth", "860");
	 	 riskView_part2.addRenderer(AnalysisReportToolbox.rTable);
	 	 reportContent.addText(html_center_begin);
	 	 reportContent.addView(riskView_part2);
	 	 reportContent.addText(html_center_close);
	 	 
	 	 //control title separate line
	 	 if(RiskOperator.getPreventiveControl(risk).size()>0){
		 	 Text controlTitle=new Text("<br>", false);
		 	 controlTitle.isHtml(true);
		 	 reportContent.addText(controlTitle); 
		 	 ///////////
		 	 View ControlTableView=generateViewForControlObjects(risk);
		 	 reportContent.addText(html_center_begin);
		 	 reportContent.addView(ControlTableView);
		 	 reportContent.addText(html_center_close);
	 	 }
	 	 //start separate line, ending one risk here
	 	 Text sepLine=new Text("<br><center>****************************************************************************</center><br>", false);
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
	 		 
	 		 dimH.addItem(styleText_verdana_auto(" "));
	 		 dimH.addItem(styleText_verdana_auto("Code"));
		 	 dimH.addItem(styleText_verdana_auto("Name"));
		 	 dimH.addItem(styleText_verdana_auto("Control Objective")); 
		 	 dimH.addItem(styleText_verdana_auto("Entity Owner"));
		 	 dimH.addItem(styleText_verdana_auto("Control Nature")); 
		 		 	 
	 	   for (int i=1;i<=controls.size();i++){
	 		 controlDataset.addItem(new Image("control (grc).gif", "control (grc).gif"), i+","+1);
	 		 controlDataset.addItem(styleText_verdana_auto("Control #"+ControlOperator.getCode(controls.get(i))), i+","+2);// control code
		 	 controlDataset.addItem(styleText_verdana_breakWord(ControlOperator.getShortName(controls.get(i))), i+","+3); // Name		 	  
		 	 controlDataset.addItem(styleText_verdana_breakWord(ControlOperator.getControlObjectiv(controls.get(i))), i+","+4); // Control objective
		 	 controlDataset.addItem(styleText_verdana_breakWord(ControlOperator.getOwningEntity(controls.get(i))), i+","+5); // owning entity
		 	 controlDataset.addItem(styleText_verdana_auto(ControlOperator.getControlNature(controls.get(i))), i+","+6);// control nature
	 	   }
	 	 }
	 	
	 	 final View controlView=new View(reportContent.addDataset(controlDataset));//id
	 	 controlView.addParameter("tablewidth", "860");
	 	 controlView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return controlView;
		
	}
	
	private View viewGeneration_Color_On_Heatmap(String impact_level,String likelihood_level,boolean isKeyRisk, boolean flag){
		 final Dataset myDataset=new Dataset("");
	 	 final Dimension dimV=new Dimension("");
	 	 final Dimension dimH=new Dimension("");
	 	 dimV.setSize(1);
	 	 dimH.setSize(2);
	 	myDataset.addDimension(dimV);
	 	myDataset.addDimension(dimH);
	 	String level=flag?impact_level:likelihood_level;
	 	
	 	if(isKeyRisk){
	 		//myDataset.addItem(getColorImageKeyRisk(level), 1+","+1);
	 		myDataset.addItem(getColorImage_Heatmap_KeyRisk(impact_level,likelihood_level), 1+","+1);
	 	}else {
	 		//myDataset.addItem(getColorImage(level), 1+","+1);
	 		myDataset.addItem(getColorImage_Heatmap(impact_level,likelihood_level), 1+","+1);
	 	}
	 	
	 	myDataset.addItem(styleText_verdana_auto(level), 1+","+2);
	 	 
	 	 final View myView=new View(reportContent.addDataset(myDataset));//id
	 	 myView.addParameter("borderWidth", "0");
	 	 myView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return myView;
	}
	
	private View viewGeneration_Color(String level,boolean isKeyRisk){
		 final Dataset myDataset=new Dataset("");
	 	 final Dimension dimV=new Dimension("");
	 	 final Dimension dimH=new Dimension("");
	 	 dimV.setSize(1);
	 	 dimH.setSize(2);
	 	myDataset.addDimension(dimV);
	 	myDataset.addDimension(dimH);
	 	
	 	if(isKeyRisk){
	 		myDataset.addItem(getColorImageKeyRisk(level), 1+","+1);
	 	}else {
	 		myDataset.addItem(getColorImage(level), 1+","+1);
	 	}
	 	
	 	myDataset.addItem(styleText_verdana_auto(level), 1+","+2);
	 	 
	 	 final View myView=new View(reportContent.addDataset(myDataset));//id
	 	 myView.addParameter("borderWidth", "0");
	 	 myView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return myView;
	}
	
	  private Text textGeneration_Color_On_Heatmap(String impact_level,String likelihood_level,boolean isKeyRisk,boolean flag){
			Text levelText=flag?styleText_verdana_auto(impact_level):styleText_verdana_auto(likelihood_level);
			if(isKeyRisk){
				levelText.getItemRenderer().addParameter("color", ColorCode.getColorCodeFromText_On_Heatmap_KeyRisk(impact_level,likelihood_level));
			}else{
				levelText.getItemRenderer().addParameter("color", ColorCode.getColorCodeFromText_On_Heatmap(impact_level,likelihood_level));
			}
			
			return levelText;
		}
	  
	  private Text textGeneration_Color(String level,boolean isKeyRisk){
			Text levelText=styleText_verdana_auto(level);
			if(isKeyRisk){
				levelText.getItemRenderer().addParameter("color", ColorCode.getColorCodeFromText_KeyRisk(level));
			}else{
				levelText.getItemRenderer().addParameter("color", ColorCode.getColorCodeFromText(level));
			}
			
			return levelText;
		}
	  
	private Image getColorImage_Heatmap(String impact_level,String likelihood_level){
		
			switch (impact_level.toLowerCase()+","+likelihood_level.toLowerCase()) {
			
				case "very high,rare":			return new Image("square_y3.gif", "");
				case "very high,possible":		return new Image("square_y3.gif", "");
				case "very high,likely":		return new Image("square_y3.gif", "");
				case "very high,probable":		return new Image("square_r4.gif", "");
				case "very high,certain":		return new Image("square_r4.gif", "");
				
				case "high,rare":				return new Image("square_g4.gif", "");
				case "high,possible":			return new Image("square_y3.gif", "");
				case "high,likely":				return new Image("square_y3.gif", "");
				case "high,probable":			return new Image("square_y3.gif", "");
				case "high,certain":			return new Image("square_r4.gif", "");
				
				case "medium,rare":				return new Image("square_g4.gif", "");
				case "medium,possible":			return new Image("square_g4.gif", "");
				case "medium,likely":			return new Image("square_y3.gif", "");
				case "medium,probable":			return new Image("square_y3.gif", "");
				case "medium,certain":			return new Image("square_y3.gif", "");
				
				case "low,rare":				return new Image("square_g4.gif", "");
				case "low,possible":			return new Image("square_g4.gif", "");
				case "low,likely":				return new Image("square_g4.gif", "");
				case "low,probable":			return new Image("square_g4.gif", "");
				case "low,certain":				return new Image("square_y3.gif", "");
				
				case "very low,rare":			return new Image("square_g4.gif", "");
				case "very low,possible":		return new Image("square_g4.gif", "");
				case "very low,likely":			return new Image("square_g4.gif", "");
				case "very low,probable":		return new Image("square_g4.gif", "");
				case "very low,certain":		return new Image("square_g4.gif", "");
		        default: 						return new Image("", "");
			}

		}
	
	private Image getColorImage_Heatmap_KeyRisk(String impact_level,String likelihood_level){
		
		switch (impact_level.toLowerCase()+","+likelihood_level.toLowerCase()) {
		
			case "very high,rare":			return new Image("square_g4.gif", "");
			case "very high,possible":		return new Image("square_y3.gif", "");
			case "very high,likely":		return new Image("square_y3.gif", "");
			case "very high,probable":		return new Image("square_r4.gif", "");
			case "very high,certain":		return new Image("square_r4.gif", "");
			
			case "high,rare":				return new Image("square_g4.gif", "");
			case "high,possible":			return new Image("square_g4.gif", "");
			case "high,likely":				return new Image("square_y3.gif", "");
			case "high,probable":			return new Image("square_y3.gif", "");
			case "high,certain":			return new Image("square_r4.gif", "");
			
			case "medium,rare":				return new Image("square_g4.gif", "");
			case "medium,possible":			return new Image("square_g4.gif", "");
			case "medium,likely":			return new Image("square_g4.gif", "");
			case "medium,probable":			return new Image("square_y3.gif", "");
			case "medium,certain":			return new Image("square_y3.gif", "");
			
			case "low,rare":				return new Image("square_g4.gif", "");
			case "low,possible":			return new Image("square_g4.gif", "");
			case "low,likely":				return new Image("square_g4.gif", "");
			case "low,probable":			return new Image("square_g4.gif", "");
			case "low,certain":				return new Image("square_g4.gif", "");
			
			case "very low,rare":			return new Image("square_g4.gif", "");
			case "very low,possible":		return new Image("square_g4.gif", "");
			case "very low,likely":			return new Image("square_g4.gif", "");
			case "very low,probable":		return new Image("square_g4.gif", "");
			case "very low,certain":		return new Image("square_g4.gif", "");
	        default: 						return new Image("", "");
		}

	}
	  
	private Image getColorImage(String level){
		switch (level.toLowerCase()) {
        case "very low":  	return new Image("square_g4.gif", level);
        case "low": 		return new Image("square_g4.gif", level);
        case "medium":  	return new Image("square_y3.gif", level);
        case "high":  		return new Image("square_y3.gif", level);
        case "very high":  	return new Image("square_r4.gif", level);
        case "rare":  		return new Image("square_g4.gif", level);
        case "possible": 	return new Image("square_g4.gif", level);
        case "likely":  	return new Image("square_y3.gif", level);
        case "probable":  	return new Image("square_y3.gif", level);
        case "certain":  	return new Image("square_r4.gif", level);
        default: 			return new Image("", level);    
     }
	}
	
	private Image getColorImageKeyRisk(String level){
		switch (level.toLowerCase()) {
        case "very low":  	return new Image("square_g4.gif", level);
        case "low": 		return new Image("square_g4.gif", level);
        case "medium":  	return new Image("square_g4.gif", level);
        case "high":  		return new Image("square_y3.gif", level);
        case "very high":  	return new Image("square_r4.gif", level);
        case "rare":  		return new Image("square_g4.gif", level);
        case "possible": 	return new Image("square_g4.gif", level);
        case "likely":  	return new Image("square_g4.gif", level);
        case "probable":  	return new Image("square_y3.gif", level);
        case "certain":  	return new Image("square_r4.gif", level);
        default: 			return new Image("", level);    
     }
	}
	
	private Text styleText_verdana_breakWord(String text){
		Text styleText=new Text("<p style=\"word-break:break-all;margin:0;font-family:verdana;font-size:9px;\">"+text+"</p>", false);
		styleText.isHtml(true);
	 	return styleText;
	}
	
	private Text styleText_verdana_auto(String text){
		Text styleText=new Text("<p style=\"margin:0;font-family:verdana;font-size:9px;\">"+text+"</p>", false);
		styleText.isHtml(true);
	 	return styleText;
	}
}

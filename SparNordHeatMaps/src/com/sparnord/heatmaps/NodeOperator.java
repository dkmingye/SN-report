package com.sparnord.heatmaps;

import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.Image;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;

public class NodeOperator {
	
    //MegaObject aValueLikeLihood = this.getAssessedValue(GRCConstants.AC_ERM_LIKELIHOUD);
    //MegaObject aValueMitigation = this.getAssessedValue(GRCConstants.AC_ERM_CONTROL_LEVEL);
   // MegaObject aValueInherentRisk = this.getAssessedValue(GRCConstants.AC_ERM_INHERENT_RISK);

	public static String getShortName(MegaObject node){
		return node.getProp("Short Name");
	}
	public static String getAbsId(MegaObject node){
		return node.getProp("Absolute Identifier");
	}
		
		public static String getAssessedObject(MegaObject node){
			MegaCollection assessedObjects= node.getCollection("Assessed Object");
			if(assessedObjects.size()>0){
				MegaObject risk=assessedObjects.get(1);
				return risk.getProp("Short Name");
			}
			 return "";
		}
		
		public static View getImpact(ReportContent reportContent,MegaObject node){
			MegaCollection assessedValues=node.getCollection("Assessed Value");
			if(assessedValues.size()>0){
				for(MegaObject assessedValue:assessedValues){
					MegaCollection assessedCharacs=assessedValue.getCollection("Assessed Characteristic");
					if(assessedCharacs.size()>0){
						MegaObject assessCharac=assessedCharacs.get(1);
						if(assessCharac.getProp("Short Name").equalsIgnoreCase("Impact")){
							MegaCollection propertyValues=assessedValue.getCollection("Property Value");
							if(propertyValues.size()>0){
								MegaObject propertyValue=propertyValues.get(1);
								return viewGeneration_Color(reportContent,propertyValue.getProp("Value Name"));
							}
						}
					}
				}
			}
			
			return EmptyView(reportContent);
		}
		
		public static View getLikelihood(ReportContent reportContent,MegaObject node){
			MegaCollection assessedValues=node.getCollection("Assessed Value");
			if(assessedValues.size()>0){
				for(MegaObject assessedValue:assessedValues){
					MegaCollection assessedCharacs=assessedValue.getCollection("Assessed Characteristic");
					if(assessedCharacs.size()>0){
						MegaObject assessCharac=assessedCharacs.get(1);
						if(assessCharac.getProp("Short Name").equalsIgnoreCase("Likelihood")){
							MegaCollection propertyValues=assessedValue.getCollection("Property Value");
							if(propertyValues.size()>0){
								MegaObject propertyValue=propertyValues.get(1);
								return viewGeneration_Color(reportContent,propertyValue.getProp("Value Name"));
							}
						}
					}
				}
			}
			
			return EmptyView(reportContent);
		}
		
		public static View getInherentRisk(ReportContent reportContent,MegaObject node){
			MegaCollection assessedValues=node.getCollection("Assessed Value");
			if(assessedValues.size()>0){
				for(MegaObject assessedValue:assessedValues){
					MegaCollection assessedCharacs=assessedValue.getCollection("Assessed Characteristic");
					if(assessedCharacs.size()>0){
						MegaObject assessCharac=assessedCharacs.get(1);
						if(assessCharac.getProp("Short Name").equalsIgnoreCase("Inherent Risk")){
							MegaCollection propertyValues=assessedValue.getCollection("Property Value");
							if(propertyValues.size()>0){
								MegaObject propertyValue=propertyValues.get(1);
								return viewGeneration_Color(reportContent,propertyValue.getProp("Value Name"));
							}
						}
					}
				}
			}
			return EmptyView(reportContent);
		}
		
		public static View getControlLevel(ReportContent reportContent,MegaObject node){
			MegaCollection assessedValues=node.getCollection("Assessed Value");
			if(assessedValues.size()>0){
				for(MegaObject assessedValue:assessedValues){
					MegaCollection assessedCharacs=assessedValue.getCollection("Assessed Characteristic");
					if(assessedCharacs.size()>0){						
						MegaObject assessCharac=assessedCharacs.get(1);
						if(assessCharac.getProp("Short Name").equalsIgnoreCase("Control  Level")||assessCharac.getProp("Short Name").equalsIgnoreCase("Control Level")){							
							MegaCollection propertyValues=assessedValue.getCollection("Property Value");
							if(propertyValues.size()>0){								
								MegaObject propertyValue=propertyValues.get(1);
								return viewGeneration_Color(reportContent,propertyValue.getProp("Value Name"));
							}
						}
					}
				}
			}
			return EmptyView(reportContent);
		}
		
		public static View getNetRisk(ReportContent reportContent,MegaObject node){
			MegaCollection assessedValues=node.getCollection("Assessed Value");
			if(assessedValues.size()>0){
				for(MegaObject assessedValue:assessedValues){
					MegaCollection assessedCharacs=assessedValue.getCollection("Assessed Characteristic");
					if(assessedCharacs.size()>0){
						MegaObject assessCharac=assessedCharacs.get(1);
						if(assessCharac.getProp("Short Name").equalsIgnoreCase("Net Risk")){
							MegaCollection propertyValues=assessedValue.getCollection("Property Value");
							if(propertyValues.size()>0){
								MegaObject propertyValue=propertyValues.get(1);
								return viewGeneration_Color(reportContent,propertyValue.getProp("Value Name"));
							}
						}
					}
				}
			}
			return EmptyView(reportContent);
		}
		
		
		private static Image getColorImage(String level){
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
	        case "very strong": return new Image("square_g4.gif", level);
	        case "strong": 		return new Image("square_g2.gif", level);
	        case "weak":  		return new Image("square_o2.gif", level);
	        case "very weak":  	return new Image("square_r4.gif", level);
	        default: 			return new Image("", level);    
	     }
		}
		
	/*	private static Image getColorImage(String level){
			switch (level.toLowerCase()) {
	        case "very low":  	return new Image("~w6RYcDvF55W0[SQUARE_G4]");
	        case "low": 		return new Image("~OJu6pk1Y5rP0[SQUARE_G2-3]");
	        case "medium":  	return new Image("~9Vxe)XQ2EXNU[SQUARE_Y2]");
	        case "high":  		return new Image("~aGu6qk1Y5XU0[SQUARE_O2-3]");
	        case "very high":  	return new Image("~1Ju6rk1Y5De0[SQUARE_R4-4]");
	        case "rare":  		return new Image("~w6RYcDvF55W0[SQUARE_G4]");
	        case "possible": 	return new Image("~OJu6pk1Y5rP0[SQUARE_G2-3]");
	        case "likely":  	return new Image("~9Vxe)XQ2EXNU[SQUARE_Y2]");
	        case "probable":  	return new Image("~aGu6qk1Y5XU0[SQUARE_O2-3]");
	        case "certain":  	return new Image("~1Ju6rk1Y5De0[SQUARE_R4-4]");
	        case "very strong": return new Image("~w6RYcDvF55W0[SQUARE_G4]");
	        case "strong": 		return new Image("~OJu6pk1Y5rP0[SQUARE_G2-3]");
	        case "weak":  		return new Image("~aGu6qk1Y5XU0[SQUARE_O2-3]");
	        case "very weak":  	return new Image("~1Ju6rk1Y5De0[SQUARE_R4-4]");
	        default: 			return new Image("");    
	     }
		}*/
		
		private static View viewGeneration_Color(ReportContent reportContent,String level){
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
		
		private static View EmptyView(ReportContent reportContent){
			 final Dataset myDataset=new Dataset("");
		 	 final Dimension dimV=new Dimension("");
		 	 final Dimension dimH=new Dimension("");
		 	 dimV.setSize(1);
		 	 dimH.setSize(2);
		 	 myDataset.addDimension(dimV);
		 	 myDataset.addDimension(dimH);	
		 	 
		 	 final View myView=new View(reportContent.addDataset(myDataset));//id
		 	 myView.addParameter("borderWidth", "0");
		 	 myView.addRenderer(AnalysisReportToolbox.rTable);
		 	 return myView;
		}
		
		

		
		

}

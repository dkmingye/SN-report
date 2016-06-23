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
import com.mega.modeling.api.MegaRoot;

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
		
		public static String getAssessedObjectCode(MegaObject node){
			MegaRoot mroot=node.getRoot();
			MegaCollection assessedObjects= node.getCollection("Assessed Object").filter(mroot.getCollection("Risk").getTypeID());
			if(assessedObjects.size()>0){
				MegaObject risk=assessedObjects.get(1);
				return risk.getProp("Risk Code");
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
		
		public static Text getImpactText(MegaObject node){	
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
								String levelName=propertyValue.getProp("Value Name");
								Text impactText=new Text(levelName,false);
								impactText.getItemRenderer().addParameter("color", getColorCode(levelName));
								return impactText;
							}
						}
					}
				}
			}
			
			return new Text("",false);
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
		
		public static Text getLikelihoodText(MegaObject node){
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
								String levelName=propertyValue.getProp("Value Name");
								Text likelihoodText=new Text(levelName,false);
								likelihoodText.getItemRenderer().addParameter("color", getColorCode(levelName));
								return likelihoodText;
							}
						}
					}
				}
			}
			
			return new Text("",false);
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
		
		public static Text getInherentRiskText(MegaObject node){
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
								String levelName=propertyValue.getProp("Value Name");
								Text inherentRiskText=new Text(levelName,false);
								inherentRiskText.getItemRenderer().addParameter("color", getColorCode(levelName));
								return inherentRiskText;
							}
						}
					}
				}
			}
			return new Text("",false);
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
		
		public static Text getControlLevelText(MegaObject node){
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
								String levelName=propertyValue.getProp("Value Name");
								Text controlLevelText=new Text(levelName,false);
								controlLevelText.getItemRenderer().addParameter("color", getColorCode(levelName));
								return controlLevelText;
							}
						}
					}
				}
			}
			return new Text("",false);
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
		
		public static Text getNetRiskText(MegaObject node){
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
								String levelName=propertyValue.getProp("Value Name");
								Text netRiskText=new Text(levelName,false);
								netRiskText.getItemRenderer().addParameter("color", getColorCode(levelName));
								return netRiskText;
							}
						}
					}
				}
			}
			return new Text("",false);
		}
		
		private static Image getColorImage(String level){
			switch (level.toLowerCase()) {
		      case "very low":  	return new Image("square_g4.gif", level);
		      case "low": 			return new Image("square_g4.gif", level);
		      case "medium":  		return new Image("square_y3.gif", level);
		      case "high":  		return new Image("square_y3.gif", level);
		      case "very high":  	return new Image("square_r4.gif", level);
		      case "rare":  		return new Image("square_g4.gif", level);
		      case "possible": 		return new Image("square_g4.gif", level);
		      case "likely":  		return new Image("square_y3.gif", level);
		      case "probable":  	return new Image("square_y3.gif", level);
		      case "certain":  		return new Image("square_r4.gif", level);
		      case "very strong": 	return new Image("square_g4.gif", level);
		      case "strong": 		return new Image("square_g4.gif", level);
		      case "weak":  		return new Image("square_y3.gif", level);
		      case "very weak":  	return new Image("square_r4.gif", level);
	        default: 				return new Image("", level);    
	     }
		}
		
		private static String getColorCode(String level){
			switch (level.toLowerCase()) {
	        case "very low":  	return "00FF00";
	        case "low": 		return "00FF00";
	        case "medium":  	return "FFFF00";
	        case "high":  		return "FFFF00";
	        case "very high":  	return "FF0000";
	        case "rare":  		return "00FF00";
	        case "possible": 	return "00FF00";
	        case "likely":  	return "FFFF00";
	        case "probable":  	return "FFFF00";
	        case "certain":  	return "FF0000";
	        case "very strong": return "00FF00";
	        case "strong": 		return "00FF00";
	        case "weak":  		return "FFFF00";
	        case "very weak":  	return "FF0000";
	        default: 			return "";    
	     }
		}
		
/*		private static Image getColorImage(String level){
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
		
		private static String getColorCode(String level){
			switch (level.toLowerCase()) {
	        case "very low":  	return "225F16";
	        case "low": 		return "4EDA37";
	        case "medium":  	return "FFD55B";
	        case "high":  		return "FF9228";
	        case "very high":  	return "D12800";
	        case "rare":  		return "225F16";
	        case "possible": 	return "4EDA37";
	        case "likely":  	return "FFD55B";
	        case "probable":  	return "FF9228";
	        case "certain":  	return "D12800";
	        case "very strong": return "225F16";
	        case "strong": 		return "4EDA37";
	        case "weak":  		return "FF9228";
	        case "very weak":  	return "D12800";
	        default: 			return "";    
	     }
		}*/
		
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

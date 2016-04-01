package com.sparnord.heatmaps.mytools;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.SystemLog;

public class FilterTools {
	
	public static MegaCollection filterRisksByType(MegaRoot root, MegaCollection riskList,MegaCollection colRiskType ){	
		if(colRiskType.size()==0){
			return riskList;			
		}else {
			MegaCollection riskList_filtered = root.getSelection("");
			for(MegaObject risk:riskList){
			 String riskTYpe=RiskOperator.getRiskType(risk);
			 for(MegaObject rType:colRiskType){
				 if(riskTYpe.equalsIgnoreCase(rType.getProp("Short Name"))){
					 riskList_filtered.insert(risk);
					 //SystemLog.log("match risk type "+RiskOperator.getShortName(risk));
				 }
			 }		 			
		   }
			return riskList_filtered;
		}
	}
	
	public static MegaCollection filterRisksByTypeDeeply(MegaRoot root,MegaCollection riskList, MegaCollection colRiskType ){	
		if(colRiskType.size()==0){
			return riskList;			
		}else {
			MegaCollection riskList_filtered = root.getSelection("");
			//SystemLog.log("size of all deeply risk types"+getAllSubRiskTypes(root,colRiskType).size());
			riskList_filtered=filterRisksByType(root,riskList,getAllSubRiskTypes(root,colRiskType));
			return riskList_filtered;
		}
	}
	
	public static MegaCollection filterKeyRisks(MegaRoot root, MegaCollection riskList,boolean keyRisk){		
			MegaCollection riskList_key = root.getSelection("");
			MegaCollection riskList_not_key = root.getSelection("");
			for(MegaObject risk:riskList){
			 String isKey=risk.getProp("Key Risk");
			 if(isKey.equalsIgnoreCase("1")){
				 riskList_key.insert(risk);
				// SystemLog.log("one key risk");
			 }else {
				 riskList_not_key.insert(risk);
			 }		 			
		   }
			return keyRisk?riskList_key:riskList_not_key;
		
	}
	
	 public static MegaCollection getAllSubRiskTypes(MegaRoot root,MegaCollection colRiskType){
		 MegaCollection allSubRiskTypes = root.getSelection("");		
		 for(MegaObject riskType:colRiskType){
			 String query="Select [Risk Type] Where [Parent Risk Type].[Absolute Identifier] Deeply "
					 +"'"+riskType.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+riskType.getProp("Absolute Identifier")+"'";
			// SystemLog.log(query);
			 MegaCollection subTypes=root.getSelection(query);			
			if(subTypes.size()>0){
				allSubRiskTypes.insert(subTypes);
			}			
		 }		 		 		 
		 return allSubRiskTypes;				
	   }
}


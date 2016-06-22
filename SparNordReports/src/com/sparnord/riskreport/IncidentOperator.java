package com.sparnord.riskreport;


import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.sparnord.common.LDCConstants;

public class IncidentOperator {
	
	public static String getCode(MegaObject incident){
		return incident.getProp("Code \\Incident");
	}
	
	public static String getName(MegaObject incident){
		return incident.getProp("Short Name");
	}
	

	
	public static String getDate(MegaObject incident){
		return incident.getProp("Occurrence Date","Display").toString();
	}
	
	public static String getComment(MegaObject incident){
		return incident.getProp("Comment","Display").toString();
	}
	
	/*public static Date getOccurenceDate(MegaObject incident){
		return (Date)incident.getProp("Occurrence Date","internal");
	}
	*/
	public static String getParentRiskType(MegaObject incident){
		MegaCollection riskTypes= incident.getCollection("Risk Type");
		if(riskTypes.size()>0){
			MegaObject riskType=riskTypes.get(1);
			MegaCollection parentRiskTypes= riskType.getCollection("Parent Risk Type");
			if(parentRiskTypes.size()>0){
				MegaObject parentRiskType=parentRiskTypes.get(1);
				return parentRiskType.getProp("Short Name");
			}
		}
		 return "";
	}
	
	public static String getRiskType(MegaObject incident){
		MegaCollection riskTypes= incident.getCollection("Risk Type");
		if(riskTypes.size()>0){
			MegaObject riskType=riskTypes.get(1);
			return riskType.getProp("Short Name");
		}
		 return "";
	}
	
	public static String getControl(MegaObject incident){
		MegaCollection controls= incident.getCollection("Control");
		if(controls.size()>0){
			MegaObject control=controls.get(1);
			return control.getProp("Short Name");
		}
		 return "";
	}
	
	
	public static String getNearMiss(MegaObject incident){
		String nearmiss=incident.getProp("Near Miss","Display").toString();
		if(nearmiss.contains("1")){
			return "Yes";
		}
		return "No";
	}
	
	public static boolean isNearMiss(MegaObject incident){
		String nearmiss=incident.getProp("Near Miss","Display").toString();
		if(nearmiss.contains("1")){
			return true;
		}
		return false;
	}
	
	public static String getEntity(MegaObject incident){
		MegaCollection entities= incident.getCollection("Entity");
		if(entities.size()>0){
			MegaObject entity=entities.get(1);
			return entity.getProp("Short Name");
		}
		 return "";
	}
	
	public static Double getNetActualLoss(MegaObject incident){
		return Double.parseDouble(incident.getProp(LDCConstants.MA_NET_ACTUAL_LOSS_LOCAL, "Internal").toString());
	}
	
	public static String getNetActualLossString(MegaObject incident){
		return incident.getProp(LDCConstants.MA_NET_ACTUAL_LOSS_LOCAL, "Display").toString();
	}
	
	public static Double getGrossActualLoss(MegaObject incident){
		return Double.parseDouble(incident.getProp(LDCConstants.MA_GROSS_ACTUAL_LOSS_LOCAL, "Internal").toString());
	}
	
	public static String getGrossActualLossString(MegaObject incident){
		return incident.getProp(LDCConstants.MA_GROSS_ACTUAL_LOSS_LOCAL, "Display").toString();
	}
	
	public static Double getRecoveries(MegaObject incident){
		return Double.parseDouble(incident.getProp(LDCConstants.MA_RECOVERIES_LOCAL, "Internal").toString());
	}
	
	public static String getRecoveriesString(MegaObject incident){
		return incident.getProp(LDCConstants.MA_RECOVERIES_LOCAL, "Display").toString();
	}

}

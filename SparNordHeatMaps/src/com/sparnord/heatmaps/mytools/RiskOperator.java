package com.sparnord.heatmaps.mytools;

import java.util.Date;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;

public class RiskOperator {
	// // metafield of some attributes
	static String shortName = "~Z20000000D60[Short Name]";
	static String name = "~210000000900[Name]";
	static String absID = "~310000000D00[Absolute Identifier]";
	
	public static String getCode(MegaObject risk){
		return risk.getProp("Risk Code");
	}
	
	public static Date getCreationDate(MegaObject risk){
		return (Date)risk.getProp("Creation Date","internal");
	}
	
	public static String getShortName(MegaObject risk){
		return risk.getProp("Short Name");
	}
	
	public static String getRiskStatus(MegaObject risk){
		return risk.getProp("Risk Status \\obj", "Display").toString();
	}
	
	public static String getParentRiskType(MegaObject risk){
		MegaCollection riskTypes= risk.getCollection("Risk Type");
		if(riskTypes.size()>0){
			MegaCollection parentRiskTypes=riskTypes.get(1).getCollection("Parent Risk Type");
			if(parentRiskTypes.size()>0){
				MegaObject parentRiskType=parentRiskTypes.get(1);
				return parentRiskType.getProp("Short Name");
			}
		}
		 return "";
	}
	
	public static String getRiskType(MegaObject risk){
		MegaCollection riskTypes= risk.getCollection("Risk Type");
		if(riskTypes.size()>0){
			MegaObject riskType=riskTypes.get(1);
			return riskType.getProp("Short Name");
		}
		 return "";
	}
	
	public static String getElementAtRisk(MegaObject risk){
		MegaCollection elements= risk.getCollection("Element at Risk");
		if(elements.size()>0){
			MegaObject element=elements.get(1);
			return element.getProp("Short Name");
		}
		 return "";
	}
	
	public static String getOwningEntity(MegaObject risk){
		MegaCollection entities= risk.getCollection("Owning Entity");
		if(entities.size()>0){
			MegaObject entity=entities.get(1);
			return entity.getProp("Short Name");
		}
		 return "";
	}
	
	public static String getExpectedLoss(MegaObject risk){
		return risk.getProp("Expected Loss", "Display").toString();
	}
	
	public static String getIdentificationMode(MegaObject risk){
		return risk.getProp("Risk Identification Mode", "Display").toString();
	}
	
	public static String getComment(MegaObject risk){
		return risk.getProp("Comment");
	}
	
	public static String getLossCalculation(MegaObject risk){
		return risk.getProp("Loss calculation");
	}
	
	public static MegaCollection getPreventiveControl(MegaObject risk){
		MegaCollection controls= risk.getCollection("Preventive Control");
		 return controls;
	}
	
	public static String getImpactERM(MegaObject risk){
		return risk.getProp("Impact \\ERM", "Display").toString();
	}
	
	public static String getLikeLihood(MegaObject risk){
		return risk.getProp("Likelihood (ERM)", "Display").toString();
	}
	
	public static String getActionPlan(MegaObject risk){
		MegaCollection actionPlans= risk.getCollection("Action Plan");
		if(actionPlans.size()>0){
			MegaObject entity=actionPlans.get(1);
			return entity.getProp("Short Name");
		}
		 return "";
	}
	
	
}

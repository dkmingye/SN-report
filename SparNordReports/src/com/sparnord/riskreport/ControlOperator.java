package com.sparnord.riskreport;

import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;

public class ControlOperator {
	// // metafield of some attributes
	static String shortName = "~Z20000000D60[Short Name]";
	static String name = "~210000000900[Name]";
	static String absID = "~310000000D00[Absolute Identifier]";
	
	
	public static String getCode(MegaObject control){
		return control.getProp("Control Code");
	}
	
	public static String getShortName(MegaObject control){
		return control.getProp("Short Name");
	}
	
	public static String getControlObjectiv(MegaObject control){
		return control.getProp("Control Objective","Display").toString();
	}
	
	public static String getOwningEntity(MegaObject control){
		MegaCollection entities= control.getCollection("Owning Entity");
		if(entities.size()>0){
			MegaObject entity=entities.get(1);
			return entity.getProp("Short Name");
		}
		 return "";
	}
	
	public static String getControlNature(MegaObject control){
		return control.getProp("Control Nature", "Display").toString();
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
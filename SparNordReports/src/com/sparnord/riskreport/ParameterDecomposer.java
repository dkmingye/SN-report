package com.sparnord.riskreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisParameter.AnalysisSimpleTypeValue;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCDateUtilities;

public class ParameterDecomposer {
	boolean isKeyRisk = false;
	ArrayList<MegaObject> OrgUnits;
	ArrayList<MegaObject> RiskTypes;
	public Date                beginDate, endDate;
	private static final String PARAM_BEGIN_DATE = "C476C4C256E610A2";
	private static final String PARAM_END_DATE   = "C476C4E056E610F7";
	
	
	public ParameterDecomposer(Map<String, List<AnalysisParameter>> paramMap,MegaRoot oRoot){
		OrgUnits= new ArrayList<MegaObject>();
		RiskTypes=new ArrayList<MegaObject>();
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -1);
		this.beginDate = LDCDateUtilities.resetTime(oRoot, c.getTime());
	    this.endDate = LDCDateUtilities.resetTime(oRoot, new Date());
		
		for (final String paramType : paramMap.keySet()) {
			for (final AnalysisParameter paramSlot : paramMap.get(paramType)) {
				ArrayList<MegaObject> paramSlotValues = paramSlot.getValues();
				
				if (paramSlotValues.size() == 0
						&& paramSlot.getParameterObject().megaField()
								.toLowerCase().contains("key risk")) {
					
					AnalysisSimpleTypeValue keyRiskValue = paramSlot.getSimpleValues().get(0);
					
					if(keyRiskValue.getStringValue().equalsIgnoreCase("1")){
						isKeyRisk=true;						
					}
						
				}
				
				if(paramSlotValues.size()>0){
					if(paramSlotValues.get(0).getClassObject().sameID("~QrUiM9B5iCN0[Org-Unit]")){						
						OrgUnits= new ArrayList<MegaObject>(paramSlotValues);
					}else if(paramSlotValues.get(0).getClassObject().sameID("~7)tbkKS9zar0[Risk Type]")){
						RiskTypes=new ArrayList<MegaObject>(paramSlotValues);
					}
				}
				 //BeginDate
		        if (paramType.equals(ParameterDecomposer.PARAM_BEGIN_DATE)) {
		        	//SystemLog.log("begin date ");
		          for (final AnalysisSimpleTypeValue value : paramSlot.getSimpleValues()) {
		            if (!value.getStringValue().isEmpty()) {
		              this.beginDate = LDCDateUtilities.resetBeginDateTime((Date) value.getValue());
		            }
		          }
		        }
		        //EndDate
		        if (paramType.equals(ParameterDecomposer.PARAM_END_DATE)) {
		        	//SystemLog.log("end date");
		          for (final AnalysisSimpleTypeValue value : paramSlot.getSimpleValues()) {
		            if (!value.getStringValue().isEmpty()) {
		              this.endDate = LDCDateUtilities.resetEndDateTime((Date) value.getValue());
		            }
		          }
		        }								
			}
			
			
		}
	}

}

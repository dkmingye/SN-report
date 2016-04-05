package com.sparnord.riskreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.Analysis;
import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisRenderingToolbox;
import com.mega.modeling.analysis.AnalysisReport;
import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.AnalysisReportWithContext;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.api.MegaCOMObject;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCConstants;
import com.sparnord.common.LDCDateUtilities;
import com.sparnord.common.FilterTools;

public class RiskControlReport implements AnalysisReportWithContext {
	static String shortName = "~Z20000000D60[Short Name]";
	static String name = "~210000000900[Name]";
	static String riskCode="~PHawL1394f31[Risk Code]";
	private Boolean isExcel = false;
	
	boolean isKeyRisk = false;

	@Override
	public ReportContent getReportContent(final MegaRoot root,
			final Map<String, List<AnalysisParameter>> paramMap,
			final Analysis analysis,final Object userData) {

		//SystemLog.initialize("C:\\Users\\ming\\Desktop\\log.txt");//for any troubleshooting
		if (analysis.getDr().toString().contains("XLS")) {
		      this.isExcel = true;
		    }
		ReportContent reportContent = new ReportContent("");
		
		if (paramMap.isEmpty()) {
			reportContent.addText(new Text("report parameter is empty",false));
			return reportContent;			
		}
		
		try{
			// Get Context
		    MegaCOMObject oContext = analysis.getMegaContext();
		    short iContext = AnalysisRenderingToolbox.getGenerationMode(oContext);
		    ///////determine report format
		    Boolean isHtml = (iContext != AnalysisReportToolbox.cRtfDeliverable) && (!this.isExcel);
		    		    
			//decompose parameters
			ParameterDecomposer myParameters=new ParameterDecomposer(paramMap,root);
			MegaCollection risks_filtered_1 = getRisks_From_OwningEntity_v2(root,myParameters.OrgUnits);
			//filter by risk types
		    MegaCollection risks_filtered_2=FilterTools.filterRisksByTypeDeeply(root, risks_filtered_1, myParameters.RiskTypes);
		    //filter key risk
		     MegaCollection risks_filtered_3;
		    if(myParameters.isKeyRisk){
		    	risks_filtered_3 =FilterTools.filterKeyRisks(root,risks_filtered_2,true);
		    }else {
		    	risks_filtered_3=FilterTools.filterKeyRisks(root, risks_filtered_2,false);
		    }
		    
			//filtering risk on the created date
			MegaCollection filteredRisks=filter_risks_on_date(root,risks_filtered_3,myParameters.beginDate,myParameters.endDate);
			//report generation
			reportContent=new RiskControlReportGenerator(filteredRisks, reportContent,isHtml).getReportRiskControl();						
			
		} catch (Exception e){
			
			reportContent.addText(new Text("report error : "+e.getMessage(),false));
		}
		//SystemLog.close();
		return reportContent;

	}
	
	public static MegaCollection getRisks_From_OwningEntity_v2(final MegaRoot root, ArrayList<MegaObject> orgUnits) {   
	    MegaCollection allRisks=root.getSelection("");
	    boolean hasOrgUnits=orgUnits.size()>0? true:false;	
	    int orgunit_num=0;
		if(hasOrgUnits){
		    for(MegaObject orgunit:orgUnits){
		    	orgunit_num++;
	           String query="Select [Org-Unit] Into @orgv Where [Aggregation of].[Absolute Identifier] Deeply "
						 +"'"+orgunit.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+orgunit.getProp("Absolute Identifier")+"'";						
	           query+=" Select [Risk] Where [Owning Entity] in @orgv";
	           allRisks.insert(root.getSelection(query,1, riskCode));
	           //SystemLog.log(" orgunit_num:"+orgunit_num);
		    }
		    return allRisks;
		}else {
			
			return root.getSelection("select [Risk]");
		}
	  }
	
	private MegaCollection filter_risks_on_date(MegaRoot root,MegaCollection risks, Date beginDate,Date endDate){
		MegaCollection filteredRisks=root.getSelection("");
		 for(MegaObject risk:risks){	    	    
	 	        // check if occurence date is between begin date and end date
	 	        Date theRiskDate = LDCDateUtilities.resetTime(root, RiskOperator.getCreationDate(risk));
	 	        if (!((theRiskDate.after(beginDate) || theRiskDate.equals(beginDate)) && (theRiskDate.before(endDate) || (theRiskDate.equals((endDate)))))) {
	 	          //Don't continue treatment, go to next object in loop	        	
	 	          continue;
	 	        }
	 	        filteredRisks.insert(risk); 	    	 
	     }
		return filteredRisks;
	}

}



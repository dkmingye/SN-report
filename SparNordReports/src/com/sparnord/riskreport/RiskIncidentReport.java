package com.sparnord.riskreport;

import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.Analysis;
import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisRenderingToolbox;
import com.mega.modeling.analysis.AnalysisReport;
import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.AnalysisReportWithContext;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCOMObject;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCReportViewUtility;

public class RiskIncidentReport implements AnalysisReportWithContext{
	static String shortName = "~Z20000000D60[Short Name]";
	static String name = "~210000000900[Name]";
	static String riskCode="~PHawL1394f31[Risk Code]";
	private Boolean isExcel = false;
	
	  public ReportContent getReportContent(final MegaRoot root, final Map<String, List<AnalysisParameter>> parameters, final Analysis analysis, final Object userData) {
		    root.setDefault("@skipConfidential");

		  //SystemLog.initialize("C:\\Users\\ming\\Desktop\\log.txt");//for any troubleshooting
		  //Excel case
		    if (analysis.getDr().toString().contains("XLS")) {
		      this.isExcel = true;
		    }
		ReportContent reportContent = new ReportContent("");
		
		
		try{
			// Get Context
		    MegaCOMObject oContext = analysis.getMegaContext();
		    short iContext = AnalysisRenderingToolbox.getGenerationMode(oContext);
		    ///////determine report format
		    Boolean isHtml = (iContext != AnalysisReportToolbox.cRtfDeliverable) && (!this.isExcel);
		    		    
		    RiskIncidentReportGenerator riskIncident_Report_Controller = new RiskIncidentReportGenerator(isHtml);
		    riskIncident_Report_Controller.initializeParameter(root, parameters, iContext);
		    reportContent=riskIncident_Report_Controller.generateContent();
		    
		    //SystemLog.close();
		} catch (Exception e){
			reportContent.addText(new Text("report error : "+e.getMessage(),false));
			
		}
		
		return reportContent;

	}

}

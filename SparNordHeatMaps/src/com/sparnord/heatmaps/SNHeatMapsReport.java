package com.sparnord.heatmaps;

import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.Analysis;
import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisRenderingToolbox;
import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.AnalysisReportWithContext;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCOMObject;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.mytools.FilterTools;
import com.sparnord.heatmaps.riskidentification.IdentificationMethodsToolBox;
import com.sparnord.heatmaps.riskidentification.IdentificationParameters;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCReportParameters;

/**
 * @author ARK
 */

public class SNHeatMapsReport implements AnalysisReportWithContext {
  MegaRoot               root;

  // Report Parameters
  private MegaCollection risks;
  private ReportContent  reportContent;
  private Hmap           inherentRiskMap;
  private Hmap           netRiskMap;
  private Boolean        isExcel = false;
  private final static String BEGIN_DATE_PARAM_HexaAbsId	="E2FCE3E356EB29C3";
  private final static String END_DATE_PARAM_HexaAbsId		="E2FCE3FE56EB2A17";
  private final static String ORGUNIT_PARAM_HexaAbsId		="E2FCE44756EB2AC5";
  private final static String RISKTYPE_PARAM_HexaAbsId		="E2FCE46856EB2B18";
  private final static String KEYRISK_PARAM_HexaAbsId		="E2FCE41A56EB2A6A";

  @Override
  public ReportContent getReportContent(final MegaRoot megaRoot, final Map<String, List<AnalysisParameter>> parameters, final Analysis analysis, final Object userData) {

    // Initialization
    this.root = megaRoot;
    //SystemLog.initialize("C:\\Users\\ming\\Desktop\\log.txt");//for any troubleshooting   
    // Get Context
    MegaCOMObject oContext = analysis.getMegaContext();
    short iContext = AnalysisRenderingToolbox.getGenerationMode(oContext);

    //Excel case
    if (analysis.getDr().toString().contains("XLS")) {
      this.isExcel = true;
    }

    // Set parameters
    IdentificationParameters hMapIdParameters = new IdentificationParameters(this.root);
    IdentificationMethodsToolBox.setParameters(parameters, hMapIdParameters, BEGIN_DATE_PARAM_HexaAbsId, END_DATE_PARAM_HexaAbsId,ORGUNIT_PARAM_HexaAbsId,RISKTYPE_PARAM_HexaAbsId,KEYRISK_PARAM_HexaAbsId);

    // Get assessed Objects 
     MegaCollection risks_filtered_1 = HeatMapsMethodToolBox.getRisks_From_OwningEntity_v2(this.root, hMapIdParameters);
     //SystemLog.log("Risk size :"+risks_filtered_1.size());
    //filter by risk types
     MegaCollection risks_filtered_2  =FilterTools.filterRisksByTypeDeeply(megaRoot, risks_filtered_1, hMapIdParameters.getRisktypes());
    // SystemLog.log("after risk type filter, the risk size :"+risks_filtered_2.size());
    //filter key risk
     MegaCollection risks_filtered_3;
    if(hMapIdParameters.keyRisk()){
    	risks_filtered_3 =FilterTools.filterKeyRisks(megaRoot,risks_filtered_2,true);
       // SystemLog.log("after key risk filter, the risk size :"+risks_filtered_3.size());
    }else {
    	risks_filtered_3=FilterTools.filterKeyRisks(megaRoot, risks_filtered_2,false);
    	//SystemLog.log("after key risk filter, the risk size :"+risks_filtered_3.size());
    }
    //get maps
    HeatMapsMethodToolBox hmap = new HeatMapsMethodToolBox();
    this.inherentRiskMap = hmap.createHeatMapsCell(this.root, GRCMetaAttribut.MA_IMPACT, GRCMetaAttribut.MA_LIKELIHOOD, GRCMetaAttribut.MA_INHERENT_RISK);
    this.netRiskMap = hmap.createHeatMapsCell(this.root, GRCMetaAttribut.MA_CONTROL_LEVEL, GRCMetaAttribut.MA_INHERENT_RISK, GRCMetaAttribut.MA_NET_RISK);
    //search Node
    NodesSearch nSearch = new NodesSearch();
    nSearch.setValueContexts(this.root,this.inherentRiskMap, this.netRiskMap, risks_filtered_3 , hMapIdParameters);
    hMapIdParameters.getControlTypes().release();
    hMapIdParameters.getOrgUnits().release();
    hMapIdParameters.getObjectives().release();
    hMapIdParameters.getProcesses().release();
    hMapIdParameters.getRisktypes().release();
    risks_filtered_3.release();

    this.reportContent = new ReportContent("");
    Boolean isHtml = (iContext != AnalysisReportToolbox.cRtfDeliverable) && (!this.isExcel);
    hmap.setViews(this.root, this.reportContent, isHtml, this.inherentRiskMap, this.netRiskMap,hMapIdParameters.keyRisk());
    
    ///view for assessment nodes
     //Nodes title separate line
  	 Text nodesTitle=new Text("<br><h2 style=\"margin-left:380px;\">Assessment nodes</h2>", false);
  	 nodesTitle.isHtml(true);
  	 reportContent.addText(nodesTitle); 
  	 
  	 /////////// add Nodes view
  	 View nodesTableView=NodesView.getView(nSearch.get_Nodes_On_Map(), reportContent);
  	 reportContent.addView(nodesTableView);
    
     //SystemLog.close();
    
    return this.reportContent;

  }
}

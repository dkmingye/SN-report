package com.sparnord.heatmaps;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;
import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.heatmaps.riskidentification.IdentificationMethodsToolBox;
import com.sparnord.heatmaps.riskidentification.IdentificationParameters;
import com.sparnord.heatmaps.grcu.colors.GRCColorsUtility;
import com.sparnord.heatmaps.grcu.constants.GRCConstants;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAssociationEnd;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;

public class HeatMapsMethodToolBox {

  public Hmap createHeatMapsCell(final MegaRoot root, final String firstMetAttribute, final String secondMetAttribute, final String resultMetaattribute) {

    Hmap hmap = new Hmap();
    Map<String, String> measureContexts = new HashMap<String, String>();
    hmap.setMeasureContexts(measureContexts);
    Map<String, HCell> heatMap = new LinkedHashMap<String, HCell>();
    MegaCollection mAttributes = root.getCollection(GRCMetaClass.MC_METAATTRIBUTE);
    MegaObject mgFirstMetAttribute = mAttributes.get(firstMetAttribute);
    MegaObject mgSecongMetAttribute = mAttributes.get(secondMetAttribute);
    MegaObject mgResultMetaattribute = mAttributes.get(resultMetaattribute);
    mAttributes.release();
    Map<Integer, String> resultMetaattributeColors = this.getMavsColors(mgResultMetaattribute);
    MegaCollection mavsFirstMetAttribute = mgFirstMetAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, -1, "order");
    MegaCollection mavsSecongMetAttribute = mgSecongMetAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order");
    String mgFirstMetAttributeName = this.getMetaAttributename(mgFirstMetAttribute.getProp(GRCMetaAttribut.MA_SHORT_NAME));
    String mgSecongMetAttributeName = this.getMetaAttributename(mgSecongMetAttribute.getProp(GRCMetaAttribut.MA_SHORT_NAME));
    String tableName = mgFirstMetAttributeName + "/" + mgSecongMetAttributeName;
    mgFirstMetAttribute.release();
    mgSecongMetAttribute.release();
    hmap.setTableName(tableName);
    hmap.setMavFirstMaAttribute(mavsFirstMetAttribute);
    hmap.setMavSecondMaAttribute(mavsSecongMetAttribute);
    for (MegaObject mavFirstMetAttribute : mavsFirstMetAttribute) {
      for (MegaObject mavSecondMetAttribute : mavsSecongMetAttribute) {
        String heatMapCellKey = mavFirstMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS) + "," + mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
        Integer intValueOfmavFirstMetAttribute = Integer.valueOf(mavFirstMetAttribute.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
        Integer intValueOfmavSecondMetAttribute = Integer.valueOf(mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
        HCell hcell = new HCell();
        String cellColor = this.getCellColor(intValueOfmavFirstMetAttribute * intValueOfmavSecondMetAttribute, resultMetaattributeColors);
        hcell.setColor(cellColor);
        heatMap.put(heatMapCellKey, hcell);
        mavSecondMetAttribute.release();
      }
      mavFirstMetAttribute.release();
    }
    hmap.setMavsMap(heatMap);
    return hmap;
  }

  private String getMetaAttributename(final String metaName) {
    if (metaName.contains("\\")) {
      return metaName.substring(0, metaName.indexOf("\\"));
    }
    return metaName;
  }

  /**
   * @param metaAttribute
   * @return
   */
  private Map<Integer, String> getMavsColors(final MegaObject metaAttribute) {
    Map<Integer, String> mavsColor = new LinkedHashMap<Integer, String>();
    MegaCollection metaAttributeValues = metaAttribute.getCollection(GRCMetaAssociationEnd.MAE_META_ATTRIBUTE_VALUE, "order", GRCMetaAttribut.MA_INTERNAL_VALUE);
    for (MegaObject metaAttributeValue : metaAttributeValues) {
      String color = GRCColorsUtility.Color2Hex(GRCColorsUtility.getRGBfromParam(metaAttributeValue));
      Integer intValue = Integer.valueOf(metaAttributeValue.getProp(GRCMetaAttribut.MA_INTERNAL_VALUE));
      mavsColor.put(intValue, color);
      metaAttributeValue.release();
    }
    metaAttributeValues.release();
    return mavsColor;
  }

  /**
   * @param result
   * @param mavsColor
   * @return
   */
  private String getCellColor(final Integer result, final Map<Integer, String> mavsColor) {
    for (Integer internVal : mavsColor.keySet()) {
      if (result <= internVal) {
        return mavsColor.get(internVal);
      }
    }
    return "000000";
  }

  private View getReportView(final ReportContent reportContent, final Dataset dataset) {
    Dimension dimension1 = new Dimension("");
    Dimension dimension2 = new Dimension("");
    dataset.addDimension(dimension1);
    dataset.addDimension(dimension2);
    dimension1.setSize(2);
    dimension2.setSize(1);
    View diagramView = new View(reportContent.addDataset(dataset));
    diagramView.addParameter("borderWidth", "0");
    diagramView.addRenderer(AnalysisReportToolbox.rTable);
    return diagramView;
  }

  private View getTitlesView(final String hMapInherentRisk, final String hMapnetRisk, final ReportContent reportContent) {
    final Dataset paramDatasettableTitle = new Dataset("");
    Dimension dimensiontableTitle1 = new Dimension("");
    Dimension dimensiontableTitle2 = new Dimension("");
    paramDatasettableTitle.addDimension(dimensiontableTitle1);
    paramDatasettableTitle.addDimension(dimensiontableTitle2);
    dimensiontableTitle1.setSize(1);
    dimensiontableTitle2.setSize(2);
    Text tex_hMapInherentRisk = new Text("<div  style=\"text-align: center;font-size:9px;font-family:arial\"> <b>" + hMapInherentRisk + "</b> </div>", false);
    tex_hMapInherentRisk.isHtml(true);
    tex_hMapInherentRisk.getItemRenderer().addParameter("color", GRCColorsUtility.HEATMAP_TITLE);
    Text tex_hMapnetRisk = new Text("<div  style=\"text-align: center;font-size:9px;font-family:arial\"> <b>" + hMapnetRisk + "</b> </div>", false);
    tex_hMapnetRisk.isHtml(true);
    tex_hMapnetRisk.getItemRenderer().addParameter("color", GRCColorsUtility.HEATMAP_TITLE);
    paramDatasettableTitle.addItem(tex_hMapInherentRisk, "1,1");
    paramDatasettableTitle.addItem(tex_hMapnetRisk, "1,2");
    View diagramViewtableTitle = new View(reportContent.addDataset(paramDatasettableTitle));
    diagramViewtableTitle.addParameter("borderWidth", "0");
    diagramViewtableTitle.addParameter("tablewidth", "100%");
    diagramViewtableTitle.addRenderer(AnalysisReportToolbox.rTable);
    return diagramViewtableTitle;
  }

  public void setViews(final MegaRoot root, final ReportContent reportContent, final Boolean isHtml, final Hmap inherentRiskMap, final Hmap netRiskMap, boolean isKeyRisk,NodesSearch nSearch) {
    View v_tableInherentRisk;
    View v_tableNetRisk;
    if (isHtml) {
      Dataset globalDataSet = new Dataset("");
      View globalView = this.getReportView(reportContent, globalDataSet);
      reportContent.addView(globalView);
      Dataset reportViews = new Dataset("");
      Dimension dimension1 = new Dimension("");
      Dimension dimension2 = new Dimension("");
      reportViews.addDimension(dimension1);
      reportViews.addDimension(dimension2);
      dimension1.setSize(1);
      dimension2.setSize(2);
      View tablesreportViews = new View(reportContent.addDataset(reportViews));
      tablesreportViews.addParameter("borderWidth", "0");
      tablesreportViews.addRenderer(AnalysisReportToolbox.rTable);
      View tablesTitles = this.getTitlesView(inherentRiskMap.getTableName(), netRiskMap.getTableName(), reportContent);
      globalDataSet.addItem(tablesTitles, "1,1");
      globalDataSet.addItem(tablesreportViews, "2,1");
      TablePresentation tableInherentRisk = new TablePresentation();
      v_tableInherentRisk = tableInherentRisk.createReportTable(root, reportContent, inherentRiskMap,isKeyRisk,true);
      v_tableInherentRisk.addParameter("drilldownaction", GRCConstants.HEATMAP_DRILLDOWN_METAWIZARD);
      TablePresentation tableNetRisk = new TablePresentation();
      v_tableNetRisk = tableNetRisk.createReportTable(root, reportContent, netRiskMap,isKeyRisk,false);
      v_tableNetRisk.addParameter("drilldownaction", GRCConstants.HEATMAP_DRILLDOWN_METAWIZARD);     
      reportViews.addItem(v_tableInherentRisk, "1,1");
      reportViews.addItem(v_tableNetRisk, "1,2");
      
      ///view for assessment nodes
      //Nodes title separate line
   	 Text nodesTitle=new Text("<br><h2 style=\"margin-left:380px;\">Assessment nodes</h2>", false);
   	 nodesTitle.isHtml(true);
   	 reportContent.addText(nodesTitle); 
   	 
   	 /////////// add Nodes view
   	 View nodesTableView=NodesView.getView(nSearch.get_Nodes_On_Map(), reportContent,isKeyRisk);
   	 reportContent.addView(nodesTableView);
   	 
    } else {
      TablePresentationExport tableInherentRisk = new TablePresentationExport();
      v_tableInherentRisk = tableInherentRisk.createReportTable(root, reportContent, inherentRiskMap,isKeyRisk,true);
      TablePresentationExport tableNetRisk = new TablePresentationExport();
      v_tableNetRisk = tableNetRisk.createReportTable(root, reportContent, netRiskMap,isKeyRisk,false);       
      reportContent.addView(v_tableInherentRisk);
      reportContent.addView(v_tableNetRisk);
      
    ///view for assessment nodes
      //Nodes title separate line
   	 Text nodesTitle=new Text("<br><h2 style=\"margin-left:380px;\">Assessment nodes</h2>", false);
   	 nodesTitle.isHtml(true);
   	 reportContent.addText(nodesTitle); 
   	 
   	 /////////// add Nodes view
   	 View nodesTableView=NodesView.getExportView(nSearch.get_Nodes_On_Map(), reportContent,isKeyRisk);
   	 reportContent.addView(nodesTableView);
   
    }
  }

  public static MegaCollection getRisks(final MegaRoot root, final IdentificationParameters idParameters) {
    MegaCollection allmoAssessedObjects;
    //if no contexts we get all assessedObjects (risks OR controls) 
    if ((idParameters.getProcesses().size() == 0) && (idParameters.getOrgUnits().size() == 0) && (idParameters.getObjectives().size() == 0) && (idParameters.getRisktypes().size() == 0)) {
      allmoAssessedObjects = root.getCollection(GRCMetaClass.MC_RISK);
    } else {
      //get all the risks linked to the context report parameters
      MegaCollection contexts = root.getSelection("");
      //contexts.insert(idParameters.getProcesses());
      contexts.insert(idParameters.getOrgUnits());
      //contexts.insert(idParameters.getObjectives());
      contexts.insert(idParameters.getRisktypes());
      allmoAssessedObjects = IdentificationMethodsToolBox.getRisksCollection(contexts);
      contexts.release();
    }
    return allmoAssessedObjects;
  }
  
  public static MegaCollection getRisks_From_OwningEntity(final MegaRoot root, final IdentificationParameters idParameters) {
	    MegaCollection orgUnits=idParameters.getOrgUnits();	   
	    boolean hasOrgUnits=orgUnits.size()>0? true:false;
	    String query="";
		if(hasOrgUnits){
		    for(MegaObject orgunit:orgUnits){
	           query=query+" Select [Org-Unit] Into @orgm Where [Aggregation of].[Absolute Identifier] Deeply "
						 +"'"+orgunit.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+orgunit.getProp("Absolute Identifier")+"'";						
			 }	    
		    query=query+" Select [Risk] Where [Owning Entity] in @orgm";  
		    //SystemLog.log(query);
		    return root.getSelection(query);
		}else {
			return root.getSelection("select [Risk]");
		}	
	  }
  
  public static MegaCollection getRisks_From_OwningEntity_large(final MegaRoot root, final IdentificationParameters idParameters) {
	    MegaCollection orgUnits=idParameters.getOrgUnits();	  
	    MegaCollection totalRisks=root.getSelection("");
	    int orgunit_interval=50;
	    int orgunit_num=0;
	    boolean hasOrgUnits=orgUnits.size()>0? true:false;	   
	    String query_part2="";
		if(hasOrgUnits){
		    for(MegaObject orgunit:orgUnits){
		    	orgunit_num++;	
		    	query_part2=query_part2+"([Aggregation of].[Absolute Identifier] Deeply "
						 +"'"+orgunit.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+orgunit.getProp("Absolute Identifier")+"')";
	  
		    	if((orgunit_num % orgunit_interval!=0)&&orgunit_num!=orgUnits.size()){
		    		query_part2=query_part2+" Or ";
		    	}
		    			    	           
	           if(orgunit_num % orgunit_interval==0){
	        	   String query="Select [Org-Unit] Into @orgm Where "+query_part2+" Select [Risk] Where [Owning Entity] in @orgm";  
	        	   totalRisks.insert(root.getSelection(query));
	        	   //SystemLog.log(" orgunit_num:"+orgunit_num+"  query: "+"");
	        	  // SystemLog.log(" risk size:"+root.getSelection(query).size());	        	   
	        	   query_part2="";
	        	   
		    	}else if(orgUnits.size()==orgunit_num){
		    		String query="Select [Org-Unit] Into @orgm Where "+query_part2+" Select [Risk] Where [Owning Entity] in @orgm";  
		        	totalRisks.insert(root.getSelection(query));
		        	//SystemLog.log(" orgunit_num:"+orgunit_num+"  query: "+"");
		        	//SystemLog.log(" risk size:"+root.getSelection(query).size());		        	
		        	query_part2="";
		        	
		    	}
		    }	    
		    
		    //SystemLog.log(query);
		    return totalRisks;
		}else {
			return root.getSelection("select [Risk]");
		}	
	  }
  
  public static MegaCollection getRisks_From_OwningEntity_v2(final MegaRoot root, final IdentificationParameters idParameters) {
	    MegaCollection orgUnits=idParameters.getOrgUnits();	   
	    MegaCollection allRisks=root.getSelection("");
	    boolean hasOrgUnits=orgUnits.size()>0? true:false;	
	    int orgunit_num=0;
		if(hasOrgUnits){
		    for(MegaObject orgunit:orgUnits){
		    	orgunit_num++;
	           String query="Select [Org-Unit] Into @orgv Where [Aggregation of].[Absolute Identifier] Deeply "
						 +"'"+orgunit.getProp("Absolute Identifier")+"' Or [Absolute Identifier]='"+orgunit.getProp("Absolute Identifier")+"'";						
	           query+=" Select [Risk] Where [Owning Entity] in @orgv";
	           allRisks.insert(root.getSelection(query));
	           //SystemLog.log(" orgunit_num:"+orgunit_num);
		    }
		    return allRisks;
		}else {
			
			return root.getSelection("select [Risk]");
		}
	  }

}


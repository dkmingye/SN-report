package com.sparnord.heatmaps;

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
import com.sparnord.heatmaps.grcu.GRCDataProcessing;
import com.sparnord.heatmaps.grcu.GRCReportViewUtility;
import com.sparnord.heatmaps.grcu.colors.GRCColorsUtility;
import com.sparnord.heatmaps.grcu.constants.GRCCodeTemplate;
import com.sparnord.heatmaps.grcu.constants.GRCMetaAttribut;
import com.sparnord.heatmaps.grcu.constants.GRCMetaClass;

/**
 * @author ARK
 */

public class TablePresentation {

  private MegaRoot      mgRoot;
  private Dataset       heatMapDataset;
  private Dimension     dimFirstMaAttribute;
  private Dimension     dimSecondMaAttribute;
  private ReportContent rContent;
  private Hmap          hMap;
  private boolean 		keyRisk=false;
  private boolean 		isFirstHeatMap=false;

  public View createReportTable(final MegaRoot root, final ReportContent reportContent, final Hmap heatMap, boolean isKeyRisk,boolean isFirstHeatMap) {
    // Create Table
    this.mgRoot = root;
    this.rContent = reportContent;
    this.hMap = heatMap;
    this.heatMapDataset = new Dataset("");
    this.dimFirstMaAttribute = new Dimension("");
    this.dimSecondMaAttribute = new Dimension("");
    this.dimFirstMaAttribute.setSize(this.hMap.getMavFirstMaAttribute().size() + 1);
    this.dimSecondMaAttribute.setSize(this.hMap.getMavSecondMaAttribute().size() + 1);
    this.heatMapDataset.addDimension(this.dimFirstMaAttribute);
    this.heatMapDataset.addDimension(this.dimSecondMaAttribute);
    this.keyRisk=isKeyRisk;
    this.isFirstHeatMap=isFirstHeatMap;

    this.fillHeatMap();

    // Create Table view
    final View vTable = new View(reportContent.addDataset(this.heatMapDataset));
    vTable.addRenderer(AnalysisReportToolbox.rTable);
    vTable.getItemRenderer().addParameter("class", "charttable");
    // return table view
    return vTable;
  }

  /**
 * 
 */
  private void fillHeatMap() {
    Map<String, HCell> mavsMap = this.hMap.getMavsMap();
    int ligne = 1;
    int column = 1;
    int totalValueContexts = 0;
    boolean setTitles = false;
    for (MegaObject mavFirstMetAttribute : this.hMap.getMavFirstMaAttribute()) {
    	String scaleTitle=mavFirstMetAttribute.getProp(GRCMetaAttribut.MA_VALUE_NAME);
    	 if(isFirstHeatMap){
	        if(scaleTitle.equalsIgnoreCase("very high")){
	        	scaleTitle = keyRisk ? ">= 500.000.000kr" : ">= 5.000.000kr";
	        }else if(scaleTitle.equalsIgnoreCase("high")){
	        	scaleTitle = keyRisk ? "< 500.000.000kr" : "< 5.000.000kr";      	 
	        }else if(scaleTitle.equalsIgnoreCase("medium")){
	        	scaleTitle = keyRisk ? "< 150.000.000kr" : "< 1.000.000kr";
	        } else if(scaleTitle.equalsIgnoreCase("low")){
	        	scaleTitle = keyRisk ? "< 50.000.000kr" : "< 200.000kr";
	        } else if(scaleTitle.equalsIgnoreCase("very low")){
	        	scaleTitle = keyRisk ? "< 10.000.000kr" : "< 50.000kr";   	 
	        } 
    	 }
      Text tex_mavName = this.getTitlesTexts(scaleTitle);
      
      this.dimFirstMaAttribute.addItem(tex_mavName);
      int totalLine = 0;
      for (MegaObject mavSecondMetAttribute : this.hMap.getMavSecondMaAttribute()) {
        if (!setTitles) {
          tex_mavName = this.getTitlesTexts(mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_VALUE_NAME));
          tex_mavName.getItemRenderer().addParameter("columnwidth", "60px");  
          if(isFirstHeatMap){
        	  this.dimSecondMaAttribute.addItem(likelihoodConvertor(mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_VALUE_NAME)));
          }else{
        	  this.dimSecondMaAttribute.addItem(tex_mavName);
          }
        }
        String heatMapCellKey = mavFirstMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS) + "," + mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
        HCell hcell = mavsMap.get(heatMapCellKey);
        totalLine = totalLine + hcell.getValueContexts().size();
        Text value;
        if(isFirstHeatMap){
        	//value = new Text("<div  style=\"text-align:center;color:#" + GRCColorsUtility.TITLE_COLOR + ";font-size:11px;font-family:arial;font-weight:bold\"> <b>" + getAssessedRiskCode(hcell) + " </b></div>", false);
        	value = new Text("<table width=\"120\"><tr><td height=\"120\"><center><p align=\"center\">"+getAssessedRiskCode(hcell)+"</p></center><td></tr></table>", false);
        	
        }else{
        	//value = new Text("<div  style=\"text-align:center;color:#" + GRCColorsUtility.TITLE_COLOR + ";font-size:11px;font-family:arial;font-weight:bold\"> <b>" + hcell.getValueContexts().size() + " </b></div>", false);
        	//value = new Text("<div  style=\"text-align:center;color:#" + GRCColorsUtility.TITLE_COLOR + ";font-size:11px;font-family:arial;font-weight:bold\"> <b>" + getAssessedRiskCode(hcell) + " </b></div>", false);
        	value = new Text("<table width=\"120\"><tr><td height=\"120\"><center><p align=\"center\">"+getAssessedRiskCode(hcell)+"</p></center><td></tr></table>", false);       	
        }
        
        value.isHtml(true);
        if(isFirstHeatMap){
        	value.getItemRenderer().addParameter("color", getColor(ligne,column,keyRisk));
        }else{
        	//value.getItemRenderer().addParameter("color", hcell.getColor());
        	value.getItemRenderer().addParameter("color", getColor(ligne,column,keyRisk));
        }        
        value.getItemRenderer().addParameter("drilldown", this.getDrillDown(hcell.getValueContexts()).toString());
        this.heatMapDataset.addItem(value, ligne + "," + column);
        column++;
      }
      
      Text ittotal = this.getTitlesTexts(String.valueOf(totalLine));
      this.heatMapDataset.addItem(ittotal, ligne + "," + column);
      totalValueContexts = totalValueContexts + totalLine;
      ligne++;
      column = 1;
      setTitles = true;
    }
    this.dimSecondMaAttribute.addItem(new Text(" ", false));
    Text totalTitle = this.getTitlesTexts(GRCDataProcessing.getCodeTemplate(GRCCodeTemplate.CODE_TEMP_TOTAL, this.mgRoot));
    this.dimFirstMaAttribute.addItem(totalTitle);
    this.setTotalColumns();
    Text totalAll = this.getTitlesTexts(String.valueOf(totalValueContexts));
    this.heatMapDataset.addItem(totalAll, (this.hMap.getMavFirstMaAttribute().size() + 1) + "," + (this.hMap.getMavSecondMaAttribute().size() + 1));

    final View heatMapView = new View(this.rContent.addDataset(this.heatMapDataset));
    heatMapView.addRenderer(AnalysisReportToolbox.rTable);
    heatMapView.getItemRenderer().addParameter("class", "charttable");
  }
  /**
   * author ming
   */
  private String getAssessedRiskCode(HCell hcell){
	  String RisksCode="";
	  for (Map.Entry<String, String> entry : hcell.getValueContexts().entrySet())
      {
		  MegaObject assNode=mgRoot.getObjectFromID(entry.getValue());		 
		  if(assNode!=null){
			  MegaCollection risks=assNode.getCollection("Assessed Object").filter("~W5faeGPxySL0[Risk]");
			  if(risks.size()>0){
				  for(MegaObject risk:risks){
					  if(!RisksCode.contains("#"+risk.getProp("Risk Code")+" ")){
						  RisksCode=RisksCode+"#"+risk.getProp("Risk Code")+" ";
					  }
					  
				  }
			  }
		  }         
      }
	  if(RisksCode==""){
		  return "<div style=\"visibility: hidden\">0</div>";
	  }
	  return RisksCode;	  
  }
  /**
   * author ming
   */
  private Text likelihoodConvertor(String likelihood){
	  switch(likelihood.toLowerCase()){
		  case "rare":		return getTitlesTexts("20-100 Years");
		  case "possible":	return getTitlesTexts("10-20 Years");
		  case "likely":	return getTitlesTexts("5-10 Years");
		  case "probable":	return getTitlesTexts("2-5 Years");
		  case "certain":	return getTitlesTexts("Less Than 2 Years");
		  default: 			return new Text("",false);
	  }
  }
  
  /**
   * author ming
   */
  
  private String getColor(int ligne,int column,boolean isKeyRisk){
	  if(isKeyRisk){
		  switch(ligne+","+column){
		  	case "1,1": return "00FF00";case "1,2": return "FFFF00";case "1,3": return "FFFF00";case "1,4": return "FF0000";case "1,5": return "FF0000";
		  	case "2,1": return "00FF00";case "2,2": return "00FF00";case "2,3": return "FFFF00";case "2,4": return "FFFF00";case "2,5": return "FF0000";
		  	case "3,1": return "00FF00";case "3,2": return "00FF00";case "3,3": return "00FF00";case "3,4": return "FFFF00";case "3,5": return "FFFF00";
		  	case "4,1": return "00FF00";case "4,2": return "00FF00";case "4,3": return "00FF00";case "4,4": return "00FF00";case "4,5": return "00FF00";
		  	case "5,1": return "00FF00";case "5,2": return "00FF00";case "5,3": return "00FF00";case "5,4": return "00FF00";case "5,5": return "00FF00";
		  	default: return "";
		  }
	  }else {	  
		  switch(ligne+","+column){
		  	case "1,1": return "FFFF00";case "1,2": return "FFFF00";case "1,3": return "FFFF00";case "1,4": return "FF0000";case "1,5": return "FF0000";
		  	case "2,1": return "00FF00";case "2,2": return "FFFF00";case "2,3": return "FFFF00";case "2,4": return "FFFF00";case "2,5": return "FF0000";
		  	case "3,1": return "00FF00";case "3,2": return "00FF00";case "3,3": return "FFFF00";case "3,4": return "FFFF00";case "3,5": return "FFFF00";
		  	case "4,1": return "00FF00";case "4,2": return "00FF00";case "4,3": return "00FF00";case "4,4": return "00FF00";case "4,5": return "FFFF00";
		  	case "5,1": return "00FF00";case "5,2": return "00FF00";case "5,3": return "00FF00";case "5,4": return "00FF00";case "5,5": return "00FF00";
		  	default: return "";
		  }
	  }
  }
  
  /**
 * 
 */
  private void setTotalColumns() {
    int ligne = this.hMap.getMavSecondMaAttribute().size() + 1;
    int column = 1;
    for (MegaObject mavSecondMetAttribute : this.hMap.getMavSecondMaAttribute()) {
      int totalColumn = 0;
      for (MegaObject mavFirstMetAttribute : this.hMap.getMavFirstMaAttribute()) {
        String heatMapCellKey = mavFirstMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS) + "," + mavSecondMetAttribute.getProp(GRCMetaAttribut.MA_HEX_ID_ABS);
        HCell hcell = this.hMap.getMavsMap().get(heatMapCellKey);
        totalColumn = totalColumn + hcell.getValueContexts().size();
        Text ittotal = this.getTitlesTexts(String.valueOf(totalColumn));
        this.heatMapDataset.addItem(ittotal, ligne + "," + column);
      }
      column++;
    }
  }

  /**
   * @param str
   * @return
   */
  private Text getTitlesTexts(final String str) {
    String html_str = "<div  style=\"text-align: center;font-size:9px;font-family:arial\"> <b>" + str + "</b> </div>";
    Text tex_str = new Text(html_str, false);
    tex_str.isHtml(true);
    tex_str.getItemRenderer().addParameter("color", GRCColorsUtility.HEATMAP_TITLE);
    return tex_str;
  }

  /**
   * @param nodes
   * @return
   */
  private StringBuffer getDrillDown(final Map<String, String> nodes) {
    StringBuffer objectsId = new StringBuffer(GRCMetaClass.MC_AssessmentValueContext + ":");
    for (String nodeMegaField : nodes.values()) {
      objectsId.append(nodeMegaField + ",");
    }
    GRCReportViewUtility.deleteSemiColon(objectsId);
    return objectsId;
  }
}


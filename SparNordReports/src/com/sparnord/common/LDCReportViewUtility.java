package com.sparnord.common;

import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.Value;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaRoot;
import com.mega.toolkit.errmngt.ErrorLogFormater;

/**
 * @author JEG
 */
public class LDCReportViewUtility {

  public static Text getGlobalTitle(final MegaRoot megaRoot, final String sTitleCodeTemplate, final int iValue) {
    String codeTemplate = LDCDataProcessing.getCodeTemplate(sTitleCodeTemplate, megaRoot);
    String title = "<p  style=\"font-weight:bold;text-align: center; color:#" + Palette.TITLE_COLOR + "; font-size:160%;\"> <b>" + codeTemplate + " : " + iValue + "</b> </p>";
    Text globalTitle = new Text(title, false);
    globalTitle.isHtml(true);
    return globalTitle;
  }

  /**
   * @param objectsId stringBiffuer for the drill down
   */
  public static void deleteSemiColon(StringBuffer objectsId) {
    if (objectsId.toString().lastIndexOf(",") == (objectsId.toString().length() - 1)) {
      objectsId = objectsId.deleteCharAt(objectsId.length() - 1);
    }
  }

  /**
   * return an empty view (empty bar chart / empty grey pie (noData))
   * @param reportContent
   * @param isPie true :pie ;false :barchart
   * @param title : the title of the empty chart
   * @return
   */
  public static View getEmptyChartsView(final ReportContent reportContent, final boolean isPie, final String direction, final String title) {
    Dataset emptyDataset = new Dataset(title);
    Dimension dimension1 = new Dimension("");
    Dimension dimension2 = new Dimension("");
    emptyDataset.addDimension(dimension1);
    dimension1.setSize(1);

    View diagramView = new View(reportContent.addDataset(emptyDataset));
    if (!isPie) {
      emptyDataset.addDimension(dimension2);
      dimension1.setSize(3);
      dimension2.setSize(1);
      dimension1.addItem(new Text(" ", false));
      dimension1.addItem(new Text(" ", false));
      dimension1.addItem(new Text(" ", false));
      emptyDataset.addItem(new Value(0.0), 1 + "," + 1);
      emptyDataset.addItem(new Value(0.0), 2 + "," + 1);
      emptyDataset.addItem(new Value(0.0), 3 + "," + 1);
      diagramView.addRenderer(AnalysisReportToolbox.rBarChart);
      diagramView.addParameter("colors", Palette.NO_DATA_COLOR);
      diagramView.addParameter("fontangle", "45");
      diagramView.addParameter("3Ddepth", "00");
      diagramView.addParameter("AggregateLabelFormat", "{value}");
      diagramView.addParameter("direction", direction);
      diagramView.addParameter("orientation", "landscape");
      diagramView.addParameter("width", "420");
      diagramView.getItemRenderer().addParameter("class", "charttable");
    } else {
      LDCReportViewUtility.setTechParamPieChart(diagramView, true, "");
      diagramView.addParameter("legendformat", "{label}");
      diagramView.addParameter("labelformat", "");
      dimension1.addItem(new Text(LDCConstants.CT_NO_DATA, true), 1);
      emptyDataset.addItem(new Value(1.0), "1");
    }

    return diagramView;
  }

  /**
   * std parameters for a pie chart in ic reports
   * @param view
   * @param noData
   * @param colors
   */
  public static void setTechParamPieChart(final View view, final boolean noData, final String colors) {
    view.addRenderer(AnalysisReportToolbox.rPieChart);

    if (noData) {
      view.addParameter("colors", Palette.NO_DATA_COLOR);
    } else {
      view.addParameter("colors", colors);
    }

    view.addParameter("width", "400");
    view.addParameter("sectorstyle", "7");
    view.addParameter("fontangle", "45");
    view.addParameter("legendformat", "{label}");
    view.addParameter("labelformat", LDCConstants.DATA_LABEL_PERCENT);
    view.addParameter("font", "Arial");
    view.addParameter("fontsize", "8");
    view.addParameter("hidelabel", "0.0");
    view.addParameter("orientation", "landscape");
    view.getItemRenderer().addParameter("class", "charttable");
  }

  /**
   * ARK std parameters for bar chart
   * @param viewBarChart
   * @param isHorizontal
   * @param colors
   * @param DataLabelFormat : see nle barchart renderer
   * @param stacked possible values :yes or no
   */
  public static void setTechParamBar(final View viewBarChart, final boolean isHorizontal, final String colors, final String DataLabelFormat, final String stacked) {
    viewBarChart.addRenderer(AnalysisReportToolbox.rBarChart);
    if (isHorizontal) {
      viewBarChart.addParameter("direction", "horizontal");
    } else {
      viewBarChart.addParameter("direction", "vertical");
    }
    viewBarChart.addParameter("width", "420");
    viewBarChart.addParameter("truncate", "true");
    viewBarChart.addParameter("fontangle", "60");
    viewBarChart.addParameter("3Ddepth", "00");
    viewBarChart.addParameter("stacked", stacked);
    viewBarChart.addParameter("colors", colors);
    viewBarChart.addParameter("explodesector", "4");
    viewBarChart.addParameter("sectorstyle", "6");
    viewBarChart.addParameter("legendformat", "{label}:{percent}%");
    viewBarChart.addParameter("DataLabelFormat", DataLabelFormat);
    viewBarChart.addParameter("font", "Arial");
    viewBarChart.addParameter("fontsize", "8");
    viewBarChart.addParameter("hidelabel", "0.0");
    viewBarChart.addParameter("softlighting", "6");
    viewBarChart.addParameter("orientation", "landscape");
    viewBarChart.getItemRenderer().addParameter("class", "charttable");
  }

  public static View getReportView(final ReportContent reportContent, final Dataset dataset) {
    Dimension dimension1 = new Dimension("");
    Dimension dimension2 = new Dimension("");
    dataset.addDimension(dimension1);
    dataset.addDimension(dimension2);
    dimension1.setSize(4);
    dimension2.setSize(1);
    View diagramView = new View(reportContent.addDataset(dataset));
    diagramView.addParameter("borderWidth", "0");
    diagramView.addRenderer(AnalysisReportToolbox.rTable);

    return diagramView;
  }

  /**
   * @param reportContent
   * @param oneDiagramme
   * @return a view for the the odd charts
   */
  public static View getOneChartView(final ReportContent reportContent, final View oneDiagramme) {
    Dataset dataset = new Dataset("");
    Dimension dimension1 = new Dimension("");
    Dimension dimension2 = new Dimension("");
    dataset.addDimension(dimension1);
    dataset.addDimension(dimension2);
    dimension1.setSize(1);
    dimension2.setSize(1);
    View diagramView = new View(reportContent.addDataset(dataset));
    diagramView.addRenderer(AnalysisReportToolbox.rTable);
    diagramView.getItemRenderer().addParameter("class", "charttable");
    diagramView.addParameter("tablewidth", "100%");
    dataset.addItem(oneDiagramme, "1,1");
    return diagramView;
  }

  public static Text getDiagramTitle(final MegaRoot megaRoot, final String sTitleCodeTemplate) {
    String codeTemplate = LDCReportViewUtility.getCodeTemplate(sTitleCodeTemplate, megaRoot);
    String title = "<font color=\"DimGray\"><b><center> " + codeTemplate + " </center></b></font>";
    Text text = new Text(title, false);
    text.isHtml(true);
    return text;
  }

  /**
   * @param ID codeTemplate ID
   * @param root megaRoot
   * @return a String from the translatable code template in the current
   *         language
   */
  public static String getCodeTemplate(final String ID, final MegaRoot root) {
    if (ID == null) {
      return "";
    }
    if (ID.length() == 0) {
      return "";
    }
    String sResult = root.currentEnvironment().resources().codeTemplate(ID, "");
    if ((sResult == null) || (sResult.length() == 0)) {
      final ErrorLogFormater err = new ErrorLogFormater();
      err.openSession(root);
      err.logMessage("Code template [" + ID + "] not found or empty.");
      err.closeSession();
      return "";
    }
    return sResult;
  }

}


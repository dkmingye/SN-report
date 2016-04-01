package com.sparnord.riskreport;

import java.util.List;
import java.util.Map;

import com.mega.modeling.analysis.Analysis;
import com.mega.modeling.analysis.AnalysisParameter;
import com.mega.modeling.analysis.AnalysisRenderingToolbox;
import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.AnalysisReportWithContext;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCOMObject;
import com.mega.modeling.api.MegaRoot;
import com.sparnord.common.LDCReportViewUtility;

/**
 * @author MAH
 * @version 750
 */
public class BackTestingMatrix implements AnalysisReportWithContext {

  @Override
  public ReportContent getReportContent(final MegaRoot root, final Map<String, List<AnalysisParameter>> parameters, final Analysis analysis, final Object userData) {
    root.setDefault("@skipConfidential");

    // Get Context
    MegaCOMObject oContext = analysis.getMegaContext();
    short iContext = AnalysisRenderingToolbox.getGenerationMode(oContext);

    final ReportContent reportContent = new ReportContent("");
    BackTestingMatrixTable backTestingMatrixTable = new BackTestingMatrixTable();
    backTestingMatrixTable.initializeParameter(root, parameters, iContext);
    //Global dataset
    Dataset globalDataSet = new Dataset("");
    View globalView = LDCReportViewUtility.getReportView(reportContent, globalDataSet);

    View backTestingMatrixView;
    backTestingMatrixView = backTestingMatrixTable.createBackTestingMatrixView(reportContent, "");
    backTestingMatrixView.getItemRenderer().addParameter("class", "charttable");

    if (iContext != AnalysisReportToolbox.cRtfDeliverable) {
      reportContent.addView(globalView);
      globalDataSet.addItem(backTestingMatrixView, "1,1");

    } else { // If in PDF/EXCEL export  
      reportContent.addView(backTestingMatrixView);
    }
    return reportContent;
  }

}

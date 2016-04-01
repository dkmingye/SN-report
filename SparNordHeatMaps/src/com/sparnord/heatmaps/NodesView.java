package com.sparnord.heatmaps;

import com.mega.modeling.analysis.AnalysisReportToolbox;
import com.mega.modeling.analysis.content.Dataset;
import com.mega.modeling.analysis.content.Dimension;
import com.mega.modeling.analysis.content.Image;
import com.mega.modeling.analysis.content.ReportContent;
import com.mega.modeling.analysis.content.Text;
import com.mega.modeling.analysis.content.View;
import com.mega.modeling.api.MegaCollection;

public class NodesView {
	
	public static View getView(MegaCollection nodes, ReportContent reportContent){		
		 final Dataset nodesDataset=new Dataset("");	 	 
	 	 final Dimension dimV=new Dimension("");
	 	 final Dimension dimH=new Dimension("");
	 	 dimV.setSize(nodes.size());
	 	 dimH.setSize(8);
	 	
	 	 nodesDataset.addDimension(dimV);
	 	 nodesDataset.addDimension(dimH);	 	 
	 
	 	 if(nodes.size()>0){	 		 		
	 		 
	 		 dimH.addItem(new Text(" ", false));
	 		 dimH.addItem(new Text("Local name", false));
		 	 dimH.addItem(new Text("Assessed Object", false));
		 	 dimH.addItem(new Text("Impact", false)); 
		 	 dimH.addItem(new Text("Likelihood", false));
		 	 dimH.addItem(new Text("Inherent Risk", false)); 
		 	 dimH.addItem(new Text("Control Level", false));
		 	 dimH.addItem(new Text("Net Risk", false));
		 	 		 		 	 
	 	   for (int i=1;i<=nodes.size();i++){
	 		nodesDataset.addItem(new Image("Assessment Value Context (bizcon).gif", "Assessment Value Context (bizcon).gif"), i+","+1);
	 		nodesDataset.addItem(new Text(NodeOperator.getShortName(nodes.get(i)), false), i+","+2);
	 		nodesDataset.addItem(new Text(NodeOperator.getAssessedObject(nodes.get(i)), false), i+","+3); 	 	  
	 		nodesDataset.addItem(NodeOperator.getImpact(reportContent,nodes.get(i)), i+","+4); 
	 		nodesDataset.addItem(NodeOperator.getLikelihood(reportContent,nodes.get(i)), i+","+5); 
	 		nodesDataset.addItem(NodeOperator.getInherentRisk(reportContent,nodes.get(i)), i+","+6);
	 		nodesDataset.addItem(NodeOperator.getControlLevel(reportContent,nodes.get(i)),  i+","+7); 
	 		nodesDataset.addItem(NodeOperator.getNetRisk(reportContent,nodes.get(i)), i+","+8); 
	 	   }
	 	 }
	 	
	 	 final View nodesView=new View(reportContent.addDataset(nodesDataset));//id
	 	 nodesView.addParameter("tablewidth", "830");
	 	 nodesView.addRenderer(AnalysisReportToolbox.rTable);
	 	 return nodesView;
	}

}

package com.sparnord.riskreport;

import com.mega.modeling.api.MegaCollection;


public class QueryGenerator {
	
	public static String getQuery_Report1(ParameterDecomposer parameters){
		boolean QueryWithOrgUnit=false;
		boolean QueryWithRiskType=false;
		boolean QueryWithKeyRisk=false;
		String query="Select [Risk] Where ";
		if(parameters.OrgUnits.size()>0){
			  query=query+"(";
			  for (int i=0;i<parameters.OrgUnits.size();i++) {
				   if(i==(parameters.OrgUnits.size()-1)){
					   //last item
					   query=query+"[Owning Entity].[Absolute Identifier]='"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"'";
		                
				   }else{
					   query=query+"[Owning Entity].[Absolute Identifier]='"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"' Or ";
		                
				   }
                 }
			  query=query+")";
			  QueryWithOrgUnit=true;
		}
		
		if(parameters.RiskTypes.size()>0){
			if(QueryWithOrgUnit){
				query=query+" And ";
			}
			  query=query+"(";
			  for (int i=0;i<parameters.RiskTypes.size();i++) {
				   if(i==(parameters.RiskTypes.size()-1)){
					   //last item
					   query=query+"[Risk Type].[Absolute Identifier]='"+parameters.RiskTypes.get(i).getProp("Absolute Identifier")+"'";		                
				   }else{
					   query=query+"[Risk Type].[Absolute Identifier]='"+parameters.RiskTypes.get(i).getProp("Absolute Identifier")+"' Or ";		                
				   }
                 }
			  query=query+")";
			  QueryWithRiskType=true;			
		}
		
		if(parameters.isKeyRisk){			
			if(QueryWithOrgUnit || QueryWithRiskType){
				query=query+" And [Key Risk]='1'";
			}else if(!QueryWithOrgUnit && !QueryWithRiskType){
				query=query+"[Key Risk]='1'";
			}
			QueryWithKeyRisk=true;
		} else{
			if(QueryWithOrgUnit || QueryWithRiskType){
				query=query+" And ([Key Risk]='0' Or Not [Key Risk])";
			}else if(!QueryWithOrgUnit && !QueryWithRiskType){
				query=query+"[Key Risk]='0' Or Not [Key Risk]";
			}
			//QueryWithKeyRisk=true;		
		}
			
		if(!QueryWithOrgUnit&&!QueryWithRiskType&&!QueryWithKeyRisk){
			return "";
		}
		
		return query;
	}
	
	public static String getQuery_Report1_v2(ParameterDecomposer parameters){
		boolean QueryWithOrgUnit=false;
		boolean QueryWithRiskType=false;
		boolean QueryWithKeyRisk=false;
		String query="";
		if(parameters.OrgUnits.size()>0){
			  query=query+"Select [Org-Unit] Into @org Where ";
			  
			  for (int i=0;i<parameters.OrgUnits.size();i++) {
				   if(i==(parameters.OrgUnits.size()-1)){
					   //last item
					   query=query+"([Aggregation of].[Absolute Identifier] Deeply '"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"'"+" Or [Absolute Identifier]='"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"')";
		                
				   }else{
					   query=query+"([Aggregation of].[Absolute Identifier] Deeply '"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"'"+" Or [Absolute Identifier]='"+parameters.OrgUnits.get(i).getProp("Absolute Identifier")+"')"+" Or ";
		                
				   }
                 }		  
			  QueryWithOrgUnit=true;
		}
		
		if(QueryWithOrgUnit){
			query=query+" " +
					"Select [Risk] Where ([Owning Entity] in @org)";
		}else {
			query=query+"Select [Risk] Where ";
		}
		
		if(parameters.RiskTypes.size()>0){
			if(QueryWithOrgUnit){
				query=query+" And ";
			}
			  query=query+"(";
			  for (int i=0;i<parameters.RiskTypes.size();i++) {
				   if(i==(parameters.RiskTypes.size()-1)){
					   //last item
					   query=query+"[Risk Type].[Absolute Identifier]='"+parameters.RiskTypes.get(i).getProp("Absolute Identifier")+"'";		                
				   }else{
					   query=query+"[Risk Type].[Absolute Identifier]='"+parameters.RiskTypes.get(i).getProp("Absolute Identifier")+"' Or ";		                
				   }
                 }
			  query=query+")";
			  QueryWithRiskType=true;			
		}
		
		if(parameters.isKeyRisk){			
			if(QueryWithOrgUnit || QueryWithRiskType){
				query=query+" And [Key Risk]='1'";
			}else if(!QueryWithOrgUnit && !QueryWithRiskType){
				query=query+"[Key Risk]='1'";
			}
			QueryWithKeyRisk=true;
		} else{
			if(QueryWithOrgUnit || QueryWithRiskType){
				query=query+" And ([Key Risk]='0' Or Not [Key Risk])";
			}else if(!QueryWithOrgUnit && !QueryWithRiskType){
				query=query+"[Key Risk]='0' Or Not [Key Risk]";
			}
			//QueryWithKeyRisk=true;		
		}
			
		if(!QueryWithOrgUnit&&!QueryWithRiskType&&!QueryWithKeyRisk){
			return "";
		}
		
		return query;
	}
	
	public static String getQuery_from_owning_entity(MegaCollection orgUnits){
		boolean QueryWithOrgUnit=false;
		String query="";
		if(orgUnits.size()>0){
			  query=query+"Select [Org-Unit] Into @orgx Where ";
			  
			  for (int i=1;i<=orgUnits.size();i++) {
				   if(i==(orgUnits.size())){
					   //last item			   
					   query=query+"([Aggregation of].[Absolute Identifier] Deeply '"+orgUnits.get(i).getProp("Absolute Identifier")+"'"+" Or [Absolute Identifier]='"+orgUnits.get(i).getProp("Absolute Identifier")+"')";
		                
				   }else{
					   query=query+"([Aggregation of].[Absolute Identifier] Deeply '"+orgUnits.get(i).getProp("Absolute Identifier")+"'"+" Or [Absolute Identifier]='"+orgUnits.get(i).getProp("Absolute Identifier")+"')"+" Or ";
		                
				   }
                 }		  
			  QueryWithOrgUnit=true;
		}
		
		if(QueryWithOrgUnit){
			query=query+" " +
					"Select [Risk] Where [Owning Entity] in @orgx";
		}else {
			return query;
		}
		return query;
	}

}

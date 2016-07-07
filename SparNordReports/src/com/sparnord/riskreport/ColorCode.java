package com.sparnord.riskreport;

import com.mega.modeling.analysis.content.Image;

public class ColorCode {
	
	public static String getColorCodeFromText(String txt){

			switch (txt.toLowerCase()) {
		      case "very low":  	return "00FF00";
		      case "low": 			return "00FF00";
		      case "medium":  		return "FFFF00";
		      case "high":  		return "FFFF00";
		      case "very high":  	return "FF0000";
		      case "rare":  		return "00FF00";
		      case "possible": 		return "00FF00";
		      case "likely":  		return "FFFF00";
		      case "probable":  	return "FFFF00";
		      case "certain":  		return "FF0000";
		      case "very strong": 	return "225F16";
		      case "strong": 		return "4EDA37";
		      case "weak":  		return "FF9228";
		      case "very weak":  	return "D12800";
		      default: 				return "";    
		   }
	}
	public static String getColorCodeFromText_On_Heatmap(String impact_level,String likelihood_level){

		switch (impact_level.toLowerCase()+","+likelihood_level.toLowerCase()) {
		
		case "very high,rare":			return "FFFF00";
		case "very high,possible":		return "FFFF00";
		case "very high,likely":		return "FFFF00";
		case "very high,probable":		return "FF0000";
		case "very high,certain":		return "FF0000";
		
		case "high,rare":				return "00FF00";
		case "high,possible":			return "FFFF00";
		case "high,likely":				return "FFFF00";
		case "high,probable":			return "FFFF00";
		case "high,certain":			return "FF0000";
		
		case "medium,rare":				return "00FF00";
		case "medium,possible":			return "00FF00";
		case "medium,likely":			return "FFFF00";
		case "medium,probable":			return "FFFF00";
		case "medium,certain":			return "FFFF00";
		
		case "low,rare":				return "00FF00";
		case "low,possible":			return "00FF00";
		case "low,likely":				return "00FF00";
		case "low,probable":			return "00FF00";
		case "low,certain":				return "FFFF00";
		
		case "very low,rare":			return "00FF00";
		case "very low,possible":		return "00FF00";
		case "very low,likely":			return "00FF00";
		case "very low,probable":		return "00FF00";
		case "very low,certain":		return "00FF00";
        default: 						return "";
	}
}
	
	public static String getColorCodeFromText_On_Heatmap_KeyRisk(String impact_level,String likelihood_level){
		
		switch (impact_level.toLowerCase()+","+likelihood_level.toLowerCase()) {
		
			case "very high,rare":			return "00FF00";
			case "very high,possible":		return "FFFF00";
			case "very high,likely":		return "FFFF00";
			case "very high,probable":		return "FF0000";
			case "very high,certain":		return "FF0000";
			
			case "high,rare":				return "00FF00";
			case "high,possible":			return "00FF00";
			case "high,likely":				return "FFFF00";
			case "high,probable":			return "FFFF00";
			case "high,certain":			return "FF0000";
			
			case "medium,rare":				return "00FF00";
			case "medium,possible":			return "00FF00";
			case "medium,likely":			return "00FF00";
			case "medium,probable":			return "FFFF00";
			case "medium,certain":			return "FFFF00";
			
			case "low,rare":				return "00FF00";
			case "low,possible":			return "00FF00";
			case "low,likely":				return "00FF00";
			case "low,probable":			return "00FF00";
			case "low,certain":				return "00FF00";
			
			case "very low,rare":			return "00FF00";
			case "very low,possible":		return "00FF00";
			case "very low,likely":			return "00FF00";
			case "very low,probable":		return "00FF00";
			case "very low,certain":		return "00FF00";
	        default: 						return "";
		}

	}
	
	public static String getColorCodeFromText_KeyRisk(String txt){

		switch (txt.toLowerCase()) {
	      case "very low":  	return "00FF00";
	      case "low": 			return "00FF00";
	      case "medium":  		return "00FF00";
	      case "high":  		return "FFFF00";
	      case "very high":  	return "FF0000";
	      case "rare":  		return "00FF00";
	      case "possible": 		return "00FF00";
	      case "likely":  		return "00FF00";
	      case "probable":  	return "FFFF00";
	      case "certain":  		return "FF0000";
	      case "very strong": 	return "225F16";
	      case "strong": 		return "4EDA37";
	      case "weak":  		return "FF9228";
	      case "very weak":  	return "D12800";
	      default: 				return "";    
	   }
}

}

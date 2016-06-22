package com.sparnord.riskreport;

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

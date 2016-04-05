package com.sparnord.riskreport;

public class ColorCode {
	
	public static String getColorCodeFromText(String txt){

			switch (txt.toLowerCase()) {
		      case "very low":  	return "225F16";
		      case "low": 			return "4EDA37";
		      case "medium":  		return "FFD55B";
		      case "high":  		return "FF9228";
		      case "very high":  	return "D12800";
		      case "rare":  		return "225F16";
		      case "possible": 		return "4EDA37";
		      case "likely":  		return "FFD55B";
		      case "probable":  	return "FF9228";
		      case "certain":  		return "D12800";
		      case "very strong": 	return "225F16";
		      case "strong": 		return "4EDA37";
		      case "weak":  		return "FF9228";
		      case "very weak":  	return "D12800";
		      default: 				return "";    
		   }
	}

}

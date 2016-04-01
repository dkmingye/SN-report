package com.sparnord.heatmaps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Date;

public class SystemLog {

	static FileWriter writer;
	static String fileName;
	static PrintStream fileStream;

	public static void initialize(String log_file) {
		try {
			fileStream = new PrintStream(new File(log_file));
			fileStream.println("**********Spar Nord Report 1 Log file**************");
			fileStream.println("============Log Start at: " + new Date()
					+ "=========");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    // write into file and print content
	public static void log(String output) {
		fileStream.println(output);
		System.out.println(output);
	}
	// write into file and not print content
	public static void logNP(String output) {
		fileStream.println(output);
	}

	public static void close() {
		fileStream.println("============Log End at: " + new Date()
				+ "=========");
		System.out.println("============Log end=============");
	}

	public static void drawLine() {
		fileStream.println("------------------------------------");
	}
}

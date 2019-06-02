package edu.handong.analysis.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Utils{
	
	public static ArrayList<String> getLines(String file, boolean removeHeader){
		ArrayList<String> lines = new ArrayList<String>();
		String line = "";
		//Open the file for reading
		
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			while((line = bReader.readLine()) != null) {
				lines.add(line);
			}
			bReader.close();
		}
		catch(IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		if(removeHeader)
			lines.remove(0);
		
		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		try {

			File fileName = new File(targetFileName);
			
			if(!fileName.exists()) {
			fileName.getParentFile().mkdirs();
			fileName.createNewFile();
			}
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName));
			
			for(String line: lines) {
				dos.write((line + "\n").getBytes());
			}
			dos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
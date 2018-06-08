package saving;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import graphics.ThreeDPolygon;

public class Load {

	
	public ArrayList<ThreeDPolygon> load(String file) {
		
		File loadFile = new File("saves/" + file);
	
		ArrayList<ThreeDPolygon> threeDPolygons = new ArrayList<ThreeDPolygon>();
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(loadFile));
			
			
			int totalLines = 0;
			
			while(br.readLine() != null) {
				totalLines++;
			}
			
			br.close();
			
			br = new BufferedReader(new FileReader(loadFile));

			for(int i = 0; i < totalLines; i++) {
				
				ArrayList<Double> xPoints = new ArrayList<Double>();
				ArrayList<Double> yPoints = new ArrayList<Double>();
				ArrayList<Double> zPoints = new ArrayList<Double>();
				boolean direction = false;
				
				String currentLine = br.readLine();
				
				int commasCount = 1;
				
				String currentNumber = new String("");
				
				for(int j = 0; j < currentLine.length(); j++) {
				
					char currentCharacter = currentLine.charAt(j);
					
					if(currentCharacter == ',') {
						
						if(commasCount == 1) {
							
							xPoints.add(Double.parseDouble(currentNumber));
							currentNumber = "";
							
						}else if(commasCount == 2) {
							
							yPoints.add(Double.parseDouble(currentNumber));
							currentNumber = "";
							
						}else {
						
							zPoints.add(Double.parseDouble(currentNumber));
							currentNumber = "";
							commasCount = 0;
						}
						
						commasCount++;
						
					}else if(currentCharacter == ';'){
						
						direction = Boolean.parseBoolean(currentNumber);
						
					}else {
						
						currentNumber += currentCharacter;
					}
					
					
				}
				
				double[] xPointsArray = new double[xPoints.size()];
				double[] yPointsArray = new double[yPoints.size()];
				double[] zPointsArray = new double[zPoints.size()];
				
				for(int j = 0; j < xPointsArray.length; j++) {
					
					xPointsArray[j] = xPoints.get(j);
					yPointsArray[j] = yPoints.get(j);
					zPointsArray[j] = zPoints.get(j);
					
				}
				
				threeDPolygons.add(new ThreeDPolygon(xPointsArray, yPointsArray, zPointsArray, Color.BLUE, direction));
				
				
				
			}
			
			br.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return threeDPolygons;

	}
	
}

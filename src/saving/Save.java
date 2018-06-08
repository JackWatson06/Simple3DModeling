package saving;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import graphics.ThreeDPolygon;

public class Save {

	
	public void save(String fileName, ArrayList<ThreeDPolygon> polygonList) {
		
		File file = new File("saves/" + fileName + ".s3d");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			
			for(int i = 0; i < polygonList.size(); i++) {
			
				for(int j = 0; j < polygonList.get(i).x.length; j++) {
					
					bw.write(Double.toString(polygonList.get(i).x[j]) + "," + Double.toString(polygonList.get(i).y[j]) + "," + Double.toString(polygonList.get(i).z[j]) + ",");
					
				}
				
				bw.write(Boolean.toString(polygonList.get(i).drawingDirection) + ";");
				
				bw.write("\r\n");
				
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

package controls;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Screen;
import graphics.ThreeDPolygon;
import main.Main;

public class Extrude {

	public static boolean planeFound;
	
	private ArrayList<ThreeDPolygon> extrudingPolygons = new ArrayList<ThreeDPolygon>();
	
	private ArrayList<ThreeDPolygon> organizedExtruding = new ArrayList<ThreeDPolygon>();
	
	private ThreeDPolygon currentPolygon;
	private ThreeDPolygon extrudePolygon;
	
	//0 - x axis, 1 - y axis, 2 - z axis
	private int extrudeAxis = 0;
	
	private double startingExtrudeValue = 0.0;
	
	private int numberOfPoints = 0;
	
	private int lastMouseX;
	
	//Sets the plane in which is being extruded
	
	public void setPlane(ThreeDPolygon polygon) {
		
		Main.setExtrudeLabels();
		
		this.currentPolygon = polygon;
		
		currentPolygon.c = Color.BLUE;
		
		numberOfPoints = polygon.x.length;
		
		planeFound = true;
		
		//X - Axis
		
		boolean flag = true;
		double first = polygon.x[0];
		
		for(int i = 1; i < polygon.x.length && flag; i++){
			
		  if (polygon.x[i] != first) flag = false;
		  
		}
		
		if (flag) {
			extrudeAxis = 0;
			startingExtrudeValue = polygon.x[0];
			return;
		}
		
		//Y - Axis
		
		flag = true;
		first = polygon.y[0];

		for(int i = 1; i < polygon.y.length && flag; i++){
			
		  if (polygon.y[i] != first) flag = false;
		  
		}
		
		if (flag) {
			startingExtrudeValue = polygon.y[0];
			extrudeAxis = 1;
			return;
		}
		
		//Z - Axis
		
		flag = true;
		first = polygon.z[0];

		for(int i = 1; i < polygon.z.length && flag; i++){
			
		  if (polygon.z[i] != first) flag = false;
		  
		}
		
		if (flag) {
			startingExtrudeValue = polygon.z[0];
			extrudeAxis = 2;
			return;
		}
		
	}
	
	//Sets the extrude number when a dimension is set
	
	public void setNumber(double one) {
		
		
		if(extrudeAxis == 0) {

			for(int i = 0; i < extrudePolygon.x.length; i++) {

				extrudePolygon.x[i] = startingExtrudeValue + one;

			}
			
		}else if(extrudeAxis == 1) {

			for(int i = 0; i < extrudePolygon.x.length; i++) {

				extrudePolygon.y[i] = startingExtrudeValue + one;

			}


		}else {

			for(int i = 0; i < extrudePolygon.x.length; i++) {

				extrudePolygon.z[i] = startingExtrudeValue + one;

			}

		}
		
		finalize();
		
		
	}
	
	//Sets the polygons that are being extruded to make a 3D shape
	
	public void setPolygons(boolean incOrDec) {
		
		
		extrudingPolygons.clear();
		
		if(extrudePolygon == null) {
			extrudePolygon = new ThreeDPolygon(currentPolygon);
		}
		
		double rate = 0.04;
		
		if(!incOrDec) {
			rate = -rate;
		}

		if(extrudeAxis == 0) {

			for(int i = 0; i < extrudePolygon.x.length; i++) {

				extrudePolygon.x[i] += rate;

			}
			
			Main.extrudeText.setText(Double.toString(Math.round((extrudePolygon.x[0] - startingExtrudeValue) * 100.0) / 100.0));

			for(int i = 0; i < numberOfPoints - 1; i++) {
				

				extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[i], currentPolygon.x[i + 1], extrudePolygon.x[i + 1], extrudePolygon.x[i] }, 
						new double[] {currentPolygon.y[i], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[i] },
						new double[] {currentPolygon.z[i], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[i] },
						Color.BLUE, false));
				
				if(i == numberOfPoints - 2) {

					extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[0], currentPolygon.x[i + 1],  extrudePolygon.x[i + 1], extrudePolygon.x[0] }, 
							new double[] {currentPolygon.y[0], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[0] },
							new double[] {currentPolygon.z[0], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[0] },
							Color.BLUE, false));
					
				}
				
			}
			
			extrudingPolygons.add(extrudePolygon);
			extrudingPolygons.add(currentPolygon);

		}else if(extrudeAxis == 1) {


			for(int i = 0; i < extrudePolygon.y.length; i++) {

				extrudePolygon.y[i] += rate;

			}
			
			Main.extrudeText.setText(Double.toString(Math.round((extrudePolygon.y[0] - startingExtrudeValue) * 100.0) / 100.0));
			
			for(int i = 0; i < numberOfPoints - 1; i++) {
				

				extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[i], currentPolygon.x[i + 1], extrudePolygon.x[i + 1], extrudePolygon.x[i] }, 
						new double[] {currentPolygon.y[i], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[i] },
						new double[] {currentPolygon.z[i], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[i] },
						Color.BLUE, false));
				
				if(i == numberOfPoints - 2) {

					extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[0], currentPolygon.x[i + 1],  extrudePolygon.x[i + 1], extrudePolygon.x[0] }, 
							new double[] {currentPolygon.y[0], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[0] },
							new double[] {currentPolygon.z[0], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[0] },
							Color.BLUE, false));
					
				}
				
			}
			
			extrudingPolygons.add(extrudePolygon);
			extrudingPolygons.add(currentPolygon);

			

		}else {

			for(int i = 0; i < extrudePolygon.z.length; i++) {

				extrudePolygon.z[i] += rate;

			}
			
			Main.extrudeText.setText(Double.toString(Math.round((extrudePolygon.z[0] - startingExtrudeValue) * 100.0) / 100.0));
			
			for(int i = 0; i < numberOfPoints - 1; i++) {
				

				extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[i], currentPolygon.x[i + 1], extrudePolygon.x[i + 1], extrudePolygon.x[i] }, 
						new double[] {currentPolygon.y[i], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[i] },
						new double[] {currentPolygon.z[i], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[i] },
						Color.BLUE, false));
				
				if(i == numberOfPoints - 2) {

					extrudingPolygons.add(new ThreeDPolygon(new double[] {currentPolygon.x[0], currentPolygon.x[i + 1],  extrudePolygon.x[i + 1], extrudePolygon.x[0] }, 
							new double[] {currentPolygon.y[0], currentPolygon.y[i + 1], extrudePolygon.y[i + 1], extrudePolygon.y[0] },
							new double[] {currentPolygon.z[0], currentPolygon.z[i + 1], extrudePolygon.z[i + 1], extrudePolygon.z[0] },
							Color.BLUE, false));
					
				}
				
			}
			
			extrudingPolygons.add(extrudePolygon);
			extrudingPolygons.add(currentPolygon);

		}
			
		
	}
	
	//Finalizes the extrude
	
	public void finalize() {
		
		findDrawingDirection();
		
		Main.removeWidthHeight();
		
		for(int i = 0; i < extrudingPolygons.size(); i++) {
			
			Screen.threeDPolygons.add(extrudingPolygons.get(i));
			
		}
		
		exit();
		
		
	}
	
	//Finds the direction in which the faces of the newly create 3D planes point (either in or out)
	
	private void findDrawingDirection() {
		
		if(extrudeAxis == 0) {
		
			
			extrudingPolygons.get(0).drawingDirection = false;
			extrudingPolygons.get(2).drawingDirection = true;
			
			extrudingPolygons.get(1).drawingDirection = true;
			extrudingPolygons.get(3).drawingDirection = false;
			
			if(extrudingPolygons.get(4).x[0] < extrudingPolygons.get(5).x[0]) {
				
				extrudingPolygons.get(4).drawingDirection = false;
				extrudingPolygons.get(5).drawingDirection = true;
				
				if(extrudingPolygons.get(4).x[0] < 0.0) {
					extrudingPolygons.get(1).drawingDirection = false;
					extrudingPolygons.get(3).drawingDirection = true;
				}
				
			}else {
				
				extrudingPolygons.get(4).drawingDirection = true;
				extrudingPolygons.get(5).drawingDirection = false;
				
				if(extrudingPolygons.get(5).x[0] < 0.0) {
					extrudingPolygons.get(1).drawingDirection = false;
					extrudingPolygons.get(3).drawingDirection = true;
				}
				
			}
		
		}else if(extrudeAxis == 1) {
			
			
			extrudingPolygons.get(0).drawingDirection = true;
			extrudingPolygons.get(2).drawingDirection = false;
			
			
			extrudingPolygons.get(1).drawingDirection = false;
			extrudingPolygons.get(3).drawingDirection = true;
			
			if(extrudingPolygons.get(4).y[0] < extrudingPolygons.get(5).y[0]) {
				
				extrudingPolygons.get(4).drawingDirection = false;
				extrudingPolygons.get(5).drawingDirection = true;
				
			}else {
				
				extrudingPolygons.get(4).drawingDirection = true;
				extrudingPolygons.get(5).drawingDirection = false;
				
			}
			
			
			
		}else {
			
			
			extrudingPolygons.get(0).drawingDirection = false;
			extrudingPolygons.get(2).drawingDirection = true;

			extrudingPolygons.get(1).drawingDirection = true;
			extrudingPolygons.get(3).drawingDirection = false;
			
			if(extrudingPolygons.get(4).z[0] < extrudingPolygons.get(5).z[0]) {
				
				extrudingPolygons.get(4).drawingDirection = false;
				extrudingPolygons.get(5).drawingDirection = true;
				
			}else {
				
				extrudingPolygons.get(4).drawingDirection = true;
				extrudingPolygons.get(5).drawingDirection = false;
				
			}
			
			
		}
		
	}
	
	//Exits the extrude
	
	private void exit() {
		planeFound = false;
		extrudingPolygons.clear();
		extrudePolygon = null;
		currentPolygon = null;
	}
	
	//Organizes the current 3D polygons so the rendering is not messed up
	
	public void organizePolygons() {
		
		ArrayList<ThreeDPolygon> organizingArray = new ArrayList<ThreeDPolygon>(extrudingPolygons);
		organizedExtruding.clear();
		
		
		for(int i = 0; i < extrudingPolygons.size(); i++) {
			
			double currentMax = 0;
			int currentMaxIValue = 0;
			
			for(int j = 0; j < organizingArray.size(); j++) {
				
				if(organizingArray.get(j).AvgDistance > currentMax) {
					currentMax = organizingArray.get(j).AvgDistance;
					currentMaxIValue = j;
				}
				
			}
			
			organizedExtruding.add(organizingArray.get(currentMaxIValue));
			
			organizingArray.remove(currentMaxIValue);
			
		}
		
		organizingArray.clear();
		
	}

	//Updates the current 3D polygons
	
	public void update(int mouseX) {
		
		if(!(mouseX == lastMouseX))	setPolygons(mouseX > lastMouseX);

		lastMouseX = mouseX;
		
		
		for(int i = 0; i < extrudingPolygons.size(); i++) {	
			
			extrudingPolygons.get(i).updatePolygon();
			
		}
		
		organizePolygons();
		
	}
	
	//Renders the current 3D polygon
	
	public void render(Graphics g) {
		
		for(int i = 0; i < organizedExtruding.size(); i++) {
			
			organizedExtruding.get(i).polygon.render(g);
			
			
		}
		
		//extrudingPolygons.get(1).polygon.render(g);
		//extrudingPolygons.get(2).polygon.render(g);
		
	}
	
	
	
	
	
}

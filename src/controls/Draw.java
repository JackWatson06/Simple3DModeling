package controls;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Screen;
import graphics.ThreeDPolygon;
import main.Main;
import math.Calculator;
import math.Vector;

public class Draw {

	public static int RECTANGLE_MODE = 0;
	public static int TRIANGLE_MODE = 1;
	public static int FREEHAND_MODE = 2;
	
	public boolean alwaysRender = false;
	
	private ArrayList<Integer> xPoints = new ArrayList<Integer>();
	private ArrayList<Integer> yPoints = new ArrayList<Integer>();
	
	private int startingMouseX, startingMouseY;
	
	public int currentMode = -1;
	public static boolean inMode = false;
	public boolean startingPoint = false;
	public boolean planeFound = false;
	
	private int clickCounter = 0;
	
	private double[] finalXPoints;
	private double[] finalYPoints;
	private double[] finalZPoints;
	
	public double width, height;
	
	private double doubleSetX = 0.0D;
	private double doubleSetY = 0.0D;
	
	private boolean direction = false;
	
	private double constantCoord = 1.0D;
	private int constantCoordI = 0;
	
	public void setSartingMouse(int mouseX, int mouseY) {
		
		startingPoint = true;
		
		this.startingMouseX = mouseX;
		this.startingMouseY = mouseY;
		
	}
	
	//Updates the current rectangle
	
	public void update(int currentMouseX, int currentMouseY) {
		
		if(currentMode == 0) {
			
			xPoints.clear();
			yPoints.clear();
			

				
			xPoints.add(startingMouseX);
			yPoints.add(startingMouseY);

			xPoints.add(startingMouseX);
			yPoints.add(currentMouseY);

			xPoints.add(currentMouseX);
			yPoints.add(currentMouseY);

			xPoints.add(currentMouseX);
			yPoints.add(startingMouseY);

			
			Main.widthText.setText(Double.toString(Math.round(((double)(currentMouseX - startingMouseX) / Screen.zoom) * 100.0) / 100.0));
			Main.heightText.setText(Double.toString(Math.round(((double)(startingMouseY - currentMouseY) / Screen.zoom) * 100.0) / 100.0));
			
		}else if(currentMode == 1) {
			
			
		}
		
	}
	
	//Sets the drawing mode (only one which is rectangle)
	
	public void setMode(int mode) {
		
		this.currentMode = mode;
		inMode = true;
		
	}
	
	//This is for setting the numbers for dimesions
	
	public void setNumbers(double one, double two) {

		doubleSetX = startingMouseX + (one * Screen.zoom);
		doubleSetY = startingMouseY - (two * Screen.zoom);

		xPoints.clear();
		yPoints.clear();

		xPoints.add(startingMouseX);
		yPoints.add(startingMouseY);

		xPoints.add(startingMouseX);
		yPoints.add((int) doubleSetY);

		xPoints.add((int) doubleSetX);
		yPoints.add((int) doubleSetY);

		xPoints.add((int) doubleSetX);
		yPoints.add(startingMouseY);

		finializePoints();

	}
	
	//Handles when the user clicks the mouse button
	
	public void click() {
		
		
		if(currentMode == 0) {
			
			if(clickCounter == 0) {
				
				
				finializePoints();
				
				return;
				
			}
			
		}
		
		clickCounter++;
		
	}
	
	//Finalizes the two dimesional points and turns it into a 3D plane
	
	public void finializePoints() {
		
		Main.removeWidthHeight();
		
		if(constantCoordI == 0) {
			
			double[] yAxisPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, constantCoord, 0, 0);
			double[] viewToPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, Screen.ViewTo[0], Screen.ViewTo[1], Screen.ViewTo[2]);
			double deltaX = viewToPoint[0] - yAxisPoint[0];
			double deltaY = yAxisPoint[1] - viewToPoint[1];
			
			
			
			finalXPoints = new double[xPoints.size()];
			finalYPoints = new double[yPoints.size()];
			finalZPoints = new double[xPoints.size()];


			for(int i = 0; i < finalXPoints.length; i++) {

				finalXPoints[i] = constantCoord;
				if(direction) {
					finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / -Screen.zoom;
				}else {
					finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / Screen.zoom;
				}
				finalZPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / Screen.zoom;

			}

			Screen.drawingPolygons.add(new ThreeDPolygon(finalXPoints, finalYPoints, finalZPoints, Color.DARK_GRAY, direction));

			alwaysRender = true;

			exitMode();
		}else if(constantCoordI == 1) {

			
			double[] yAxisPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, 0, constantCoord, 0);
			double[] viewToPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, Screen.ViewTo[0], Screen.ViewTo[1], Screen.ViewTo[2]);
			double deltaX = viewToPoint[0] - yAxisPoint[0];
			double deltaY = yAxisPoint[1] - viewToPoint[1];
			
			
			
			finalXPoints = new double[xPoints.size()];
			finalYPoints = new double[yPoints.size()];
			finalZPoints = new double[xPoints.size()];


			for(int i = 0; i < finalXPoints.length; i++) {

				if(direction) {
					finalXPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / Screen.zoom;
				}else {
					finalXPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / -Screen.zoom;
				}
				finalYPoints[i] = constantCoord;
				finalZPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / Screen.zoom;

			}

			Screen.drawingPolygons.add(new ThreeDPolygon(finalXPoints, finalYPoints, finalZPoints, Color.DARK_GRAY, direction));

			alwaysRender = true;

			exitMode();
			
			
		}else {
			
			
			double[] yAxisPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, 0, 0, constantCoord);
			double[] viewToPoint = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, Screen.ViewTo[0], Screen.ViewTo[1], Screen.ViewTo[2]);
			double deltaX = viewToPoint[0] - yAxisPoint[0];
			double deltaY = yAxisPoint[1] - viewToPoint[1];
			
			
			
			finalXPoints = new double[xPoints.size()];
			finalYPoints = new double[yPoints.size()];
			finalZPoints = new double[xPoints.size()];


			for(int i = 0; i < finalXPoints.length; i++) {

				
				if(!direction) {
					if(Screen.ViewFrom[1] != 0.0) {
						if(Screen.ViewTo[0] > 0.0) {
							finalXPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / -Screen.zoom;
							finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / Screen.zoom;
							finalZPoints[i] = constantCoord;
						}else {
							finalXPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / Screen.zoom;
							finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / -Screen.zoom;
							finalZPoints[i] = constantCoord;
						}

					}else {
						
						finalXPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / Screen.zoom;
						finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / -Screen.zoom;
						finalZPoints[i] = constantCoord;
					}
				}else {
					
					if(Screen.ViewTo[0] > 0.0) {
						finalXPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / Screen.zoom;
						finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / Screen.zoom;
						finalZPoints[i] = constantCoord;
					}else {
						finalXPoints[i] = (((height / 2) - yPoints.get(i)) + deltaY) / -Screen.zoom;
						finalYPoints[i] = ((xPoints.get(i) - (width / 2)) + deltaX) / -Screen.zoom;
						finalZPoints[i] = constantCoord;
					}

				}

			}

			Screen.drawingPolygons.add(new ThreeDPolygon(finalXPoints, finalYPoints, finalZPoints, Color.DARK_GRAY, direction));

			alwaysRender = true;

			exitMode();
			
		}
	}
	
	//Gets distance between two three dimesional points.
	
	public double getDistance(double x, double y, double z, double x1, double y1, double z1) {
		
		return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2) + Math.pow(z - z1, 2));
	}
	
	//Exits the current mode

	public void exitMode() {
		
		currentMode = -1;
		inMode = false;
		startingPoint = false;
		clickCounter = 0;
		
	}
	
	//Exits the drawing
	
	public void exitDrawing() {
		
		currentMode = -1;
		inMode = false;
		planeFound = false;
		startingPoint = false;
		clickCounter = 0;
		
	}
	
	//This works off of the assumption that the polygons have a minimum of 3 coordinate points (which would have to be the case in order to actually create a plan in 3D).
	
	public double[][] findNewView(ThreeDPolygon polygon) {
		
		
		Vector initialVector = new Vector(polygon.x[1] - polygon.x[0], polygon.y[1] - polygon.y[0], polygon.z[1] - polygon.z[0]);
		Vector perpVector = new Vector(polygon.x[2] - polygon.x[1], polygon.y[2] - polygon.y[1], polygon.z[2] - polygon.z[1]);
		
		Vector crossProduct = perpVector.crossProduct(initialVector);

		
		constantCoordI = findConstantCoord(initialVector, perpVector);
		
		int newViewToXY;
		
		if(constantCoordI == 0) {
			constantCoord = polygon.x[0];
			newViewToXY = findBottomLeftCorner(polygon.y, polygon.z);
		}else if(constantCoordI == 1) {
			constantCoord = polygon.y[0];
			newViewToXY = findBottomLeftCorner(polygon.x, polygon.z);
		}else {
			constantCoord = polygon.z[0];
			newViewToXY = findBottomLeftCorner(polygon.x, polygon.y);
		}
		

		double[] newViewTo = new double[3];
		
		if(constantCoordI == 0) {
			newViewTo[0] = constantCoord;
			newViewTo[1] = polygon.y[newViewToXY];
			newViewTo[2] = polygon.z[newViewToXY];
		}else if(constantCoordI == 1) {
			newViewTo[0] = polygon.x[newViewToXY];
			newViewTo[1] = constantCoord;
			newViewTo[2] = polygon.z[newViewToXY];
		}else {
			newViewTo[0] = polygon.x[newViewToXY];
			newViewTo[1] = polygon.y[newViewToXY];
			newViewTo[2] = constantCoord;
		}
		
		direction = polygon.drawingDirection;
		
		
		planeFound = true;
		
		double scaling = 20;
		
		if(!direction) {
			scaling = -scaling;
		}

		
		return new double[][] {new double[] {newViewTo[0] + (Math.abs(crossProduct.x) * scaling), newViewTo[0]}, new double[] { newViewTo[1] + (Math.abs(crossProduct.y) * scaling), newViewTo[1] }, new double[] {newViewTo[2] + (Math.abs(crossProduct.z) * scaling), newViewTo[2]}};

		
	}
	
	//Finds the bottom left corner of the two dimensional polygon
	
	private int findBottomLeftCorner(double[] coord1, double[] coord2) {
		
		int iValue = 0;
		double sum = coord1[0] + coord2[0];
		
		for(int i = 1; i < coord1.length; i++) {
			
			if(coord1[i] + coord2[i] < sum) {
				iValue = i;
				sum = coord1[i] + coord2[i];
			}
			
		}
		
		return iValue;
		
	}
	
	//Finds which coord is to be remained constant
	
	private int findConstantCoord(Vector one, Vector two) {
		
		
		if(one.x == two.x) {

			return 0;
		}
		
		if(one.y == two.y) {

			return 1;
		}
		
		if(one.z == two.z) {

			return 2;
		}
		
		return 0;
		
		
	}
	
	//Renders the current two dimesional rectangle
	public void render(Graphics g) {

		for(int i = 0; i < xPoints.size() - 1; i++) {

			g.setColor(Color.BLACK);
			g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));

			if(i == xPoints.size() - 2) {
				g.drawLine(xPoints.get(0), yPoints.get(0), xPoints.get(xPoints.size() - 1), yPoints.get(yPoints.size() - 1));
			}

		}



		for(int i = 0; i < xPoints.size(); i++) {


			g.setColor(Color.GREEN);
			g.drawOval(xPoints.get(i), yPoints.get(i), 2, 2);

		}
		
		
	}
	
	
}

/*
 * 
	public void finializePoints(DisplayAxis[] axis) {
		
		
		
		double[] firstAxisXY = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, 0, 0, Screen.ViewTo[2]);
		double[] secondAxisXY = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, 0, Screen.ViewTo[1], 0);
		
		System.out.println(firstAxisXY[0] + "," + firstAxisXY[1]);
		
		System.out.println(secondAxisXY[0] + "," + secondAxisXY[1]);
		
		finalXPoints = new double[xPoints.size()];
		finalYPoints = new double[yPoints.size()];
		finalZPoints = new double[xPoints.size()];
		
		double[][] projected2DPoints = new double[finalXPoints.length][2];
		
		System.out.println("Zoom: " + Screen.zoom);
		
		for(int i = 0; i < finalXPoints.length; i++) {
			
			finalXPoints[i] = (xPoints.get(i) - firstAxisXY[0]) / Screen.zoom * (760.0 / firstAxisXY[0]);
			finalYPoints[i] = constantCoord;
			finalZPoints[i] = (secondAxisXY[1] - yPoints.get(i)) / Screen.zoom;
			
		}
		
		for(int i = 0; i < finalXPoints.length; i++) {
			

			
			//projected2DPoints[i] = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, finalXPoints[i], finalYPoints[i], finalZPoints[i]);
				
		}
		
		for(int i = 0; i < finalXPoints.length; i++) {
			
			double pointX = (xPoints.get(i) - firstAxisXY[0]);
			double pointY = (secondAxisXY[1] - yPoints.get(i));
			
			
			//finalXPoints[i] = pointX / Screen.zoom * (pointX / (projected2DPoints[i][0] - firstAxisXY[0])) * firstAxis[0] / ;
			//finalYPoints[i] = constantCoord;
			//finalZPoints[i] = pointY / Screen.zoom * (pointY / (secondAxisXY[1] - projected2DPoints[i][1]));
			
		}
		
		Screen.drawingPolygons.add(new ThreeDPolygon(finalXPoints, finalYPoints, finalZPoints, Color.PINK, direction));
		
		alwaysRender = true;
		
		exitMode();
	}
*/
 

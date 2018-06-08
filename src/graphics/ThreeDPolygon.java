package graphics;

import java.awt.Color;

import math.Calculator;

public class ThreeDPolygon {

	public Color c;
	public double[] x, y, z;
	public TwoDPolygon polygon;
	public double AvgDistance;
	//True is positive drawingDirection, False is negative drawingDirection
	public boolean drawingDirection;
	
	public ThreeDPolygon(double[] x, double[] y, double[] z, Color c, boolean drawingDirection) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
		this.drawingDirection = drawingDirection;
		
		createTwoDPolygon();
		
	}
	
	public ThreeDPolygon(ThreeDPolygon clone) {
		
		this.x = clone.x.clone();
		this.y = clone.y.clone();
		this.z = clone.z.clone();
		this.c = Color.BLUE;
		if(clone.drawingDirection) {
			this.drawingDirection = true;
		}else {
			this.drawingDirection = false;
		}
		
		createTwoDPolygon();
		
	}
	
	private void createTwoDPolygon() {
		
		double[][] points = Calculator.ThreeDProjection(Screen.ViewFrom, Screen.ViewTo, x, y, z);
		
		polygon = new TwoDPolygon(points[0], points[1], c);
		
	}
	
	public void updatePolygon() {
		
		double[][] points = Calculator.ThreeDProjection(Screen.ViewFrom, Screen.ViewTo, x, y, z);
		
		polygon.updatePoints(points[0], points[1]);
		
		AvgDistance = GetDist();
		
	}
	
	
	double GetDist()
	{
		double total = 0;
		for(int i=0; i<x.length; i++)
			total += GetDistanceToP(i);
		return total / x.length;
	}
	
	double GetDistanceToP(int i)
	{
		return Math.sqrt((Screen.ViewFrom[0]-x[i])*(Screen.ViewFrom[0]-x[i]) + 
						 (Screen.ViewFrom[1]-y[i])*(Screen.ViewFrom[1]-y[i]) +
						 (Screen.ViewFrom[2]-z[i])*(Screen.ViewFrom[2]-z[i]));
	}
	
	
}

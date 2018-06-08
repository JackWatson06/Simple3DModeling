package graphics;

import java.awt.Color;
import java.awt.Graphics;

import math.Calculator;

public class DisplayAxis {


	private Color color;
	
	double originX, originY, originZ, pointX, pointY, pointZ;
	double drawXOrigin, drawYOrigin, drawXPoint, drawYPoint;
	
	public double axisX = 0;
	public double axisY = 0;
	
	public double tValue = 0.0D;
	
	int length;
	
	public DisplayAxis(float originX, float originY, float originZ, float pointX, float pointY, float pointZ, int length, Color color) {
		
		this.color = color;
		
		this.originX = originX;
		this.originY = originY;
		this.originZ = originZ;
		
		this.pointX = pointX * length;
		this.pointY = pointY * length;
		this.pointZ = pointZ * length;
		
	}
	
	public double[] findCoord(double[] ViewFrom, double[] ViewTo) {
		
		double[] directionPoints = Calculator.ThreeDProjectionPoint(ViewFrom, ViewTo, pointX, pointY, pointZ);
		
		drawXPoint = directionPoints[0];
		drawYPoint = directionPoints[1];
		
		return new double[] {drawXPoint, drawYPoint};
	}
	
	public void update() {
		
		double[] originPoints = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, originX, originY, originZ);
		
		drawXOrigin = originPoints[0];
		drawYOrigin = originPoints[1];
		
		double[] directionPoints = Calculator.ThreeDProjectionPoint(Screen.ViewFrom, Screen.ViewTo, pointX, pointY, pointZ);
		
		drawXPoint = directionPoints[0];
		drawYPoint = directionPoints[1];
		
		axisX = drawXPoint;
		axisY = drawYPoint;
		
		
	}
	
	public void render(Graphics g) {
		
		g.setColor(color);
		g.drawLine((int)(drawXOrigin), (int)(drawYOrigin), (int)(drawXPoint), (int)(drawYPoint));
			
	}
	
	
	
	
	
	
}

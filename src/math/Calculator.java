package math;

import graphics.Screen;

public class Calculator {
	
	static public double[][] ThreeDProjection(double[] ViewFrom, double[] ViewTo, double[] x, double[] y, double[] z) {
		
		double[] drawX = new double[x.length];
		double[] drawY = new double[y.length];
		
		double[] drawCenter = findMiddle(ViewFrom, ViewTo, ViewTo[0], ViewTo[1], ViewTo[2]);
		
		for(int i = 0; i < x.length; i++) {
			double[] dataXY = transform(ViewFrom, ViewTo, x[i], y[i], z[i]);
			drawX[i] = ((-Screen.zoom) * drawCenter[0]) + (Screen.width / 2) + Screen.zoom * dataXY[0];
			drawY[i] = ((-Screen.zoom) * drawCenter[1]) + (Screen.height / 2) + Screen.zoom * dataXY[1];
		}
		
		
		return new double[][] {drawX, drawY};
	}
	
	static public double[] ThreeDProjectionPoint(double[] ViewFrom, double[] ViewTo, double x, double y, double z) {
		
		
		double drawX;
		double drawY;
		
		double[] drawCenter = findMiddle(ViewFrom, ViewTo, ViewTo[0], ViewTo[1], ViewTo[2]);
		

		double[] dataXY = transform(ViewFrom, ViewTo, x, y, z);
		drawX = ((-Screen.zoom) * drawCenter[0]) + (Screen.width / 2) + Screen.zoom * dataXY[0];
		drawY = ((-Screen.zoom) * drawCenter[1]) + (Screen.height / 2) + Screen.zoom * dataXY[1];

		
		
		return new double[] {drawX, drawY};
		
	}
	
	static double[] findMiddle(double[] ViewFrom, double[] ViewTo, double x, double y, double z){
		
		double[] dataXY = transform(ViewFrom, ViewTo, x, y, z);
		
		return new double[] {dataXY[0], dataXY[1]};
		
	}
	
	static Vector getRotationVector(double[] ViewFrom, double[] ViewTo) {
		
		
		double dx = Math.abs(ViewFrom[1] - ViewTo[1]);
		double dy = Math.abs(ViewFrom[0] - ViewTo[0]);
		
		if(ViewFrom[1] > ViewTo[1]) {
			dx = -dx;
		}
		
		if(ViewFrom[0] < ViewTo[0]) {
			dy = -dy;
		}
		
		Vector rotation = new Vector(dx, dy, 0);
		
		return rotation;
	}
	
	static double[] transform(double[] ViewFrom, double[] ViewTo, double x, double y, double z) {
		
		
		//This represents the vector from the view point to the point in which the camera is looking at.
		Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
		
		Vector DirectionVector = new Vector(1, 1, 1);				
		Vector PlaneVector1 = ViewVector.crossProduct(DirectionVector);
		Vector PlaneVector2 = ViewVector.crossProduct(PlaneVector1);
		Vector NormalVector = PlaneVector1.crossProduct(PlaneVector2);
		
		Vector RotationVector = getRotationVector(ViewFrom, ViewTo);
		Vector RotationPlane = ViewVector.crossProduct(RotationVector);
		Vector RotationPlanePerp = ViewVector.crossProduct(RotationPlane);
		
		Vector viewToPoint = new Vector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2]);
		
		//double t = ViewVector.length / (ViewVector.x * viewToPoint.x + ViewVector.y * viewToPoint.y + ViewVector.z * viewToPoint.z);
		
		double t = ((NormalVector.x * ViewTo[0] + NormalVector.y * ViewTo[1] +  NormalVector.z * ViewTo[2]) - (NormalVector.x * ViewFrom[0] + NormalVector.y * ViewFrom[1] + NormalVector.z * ViewFrom[2])) 
				/ (NormalVector.x * viewToPoint.x + NormalVector.y * viewToPoint.y + NormalVector.z * viewToPoint.z);
		
		x = ViewFrom[0] + viewToPoint.x * t;
		y = ViewFrom[1] + viewToPoint.y * t;
		z = ViewFrom[2] + viewToPoint.z * t;
		
		if(t >= 0) {
			
			double DrawX = RotationPlanePerp.x * x + RotationPlanePerp.y * y + RotationPlanePerp.z * z;
			double DrawY = RotationPlane.x * x + RotationPlane.y * y + RotationPlane.z * z;
			
			return new double[] {DrawX, DrawY};
			
		}
		
		return new double[] {0, 0};
		
	}

}

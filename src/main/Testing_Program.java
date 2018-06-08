package main;

import math.Vector;

public class Testing_Program {

	public Testing_Program() {
		
		//Represents the point that will be transformed into a two dimensional point.
		
		double x = 5;
		double y = 1;
		double z = 1;
		
		
		double[] ViewTo = new double[] {5, 1, 1};
		double[] ViewFrom = new double[] {7, 7, 7};
		
		Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
		
		printVector(ViewVector, "View Vector");
		
		Vector RotationVector = getRotationVector(ViewFrom, ViewTo);
		
		printVector(RotationVector, "Rotation Vector");
		
		Vector RotationPlane = ViewVector.crossProduct(RotationVector);
		
		printVector(RotationPlane, "Rotation Plane");
		
		Vector RotationPlanePerp = ViewVector.crossProduct(RotationPlane);
		
		printVector(RotationPlanePerp, "Rotation Plane Perp.");
		
		
		Vector viewToPoint = new Vector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2]);
		
		printVector(viewToPoint, "View To Point");
		
		double t = ((ViewVector.x * ViewTo[0] + ViewVector.y * ViewTo[1] + ViewVector.z * ViewTo[2]) - 
				(ViewVector.x * ViewFrom[0] + ViewVector.y * ViewFrom[1] + ViewVector.z * ViewFrom[2])) / 
				(ViewVector.x * viewToPoint.x + ViewVector.y * viewToPoint.y + ViewVector.z * viewToPoint.z);
		
		System.out.println("Dot Product (VV . VT) " + (ViewVector.x * ViewTo[0] + ViewVector.y * ViewTo[1] + ViewVector.z * ViewTo[2]));
		
		System.out.println("Dot Product (VV . VF) " + (ViewVector.x * ViewFrom[0] + ViewVector.y * ViewFrom[1] + ViewVector.z * ViewFrom[2]));
		
		System.out.println("Dot Product (VV . VTP) " + (ViewVector.x * viewToPoint.x + ViewVector.y * viewToPoint.y + ViewVector.z * viewToPoint.z));
		
		System.out.println("T Value: " + t);
		
		System.out.println("Degrees between VV and VTP: " + Math.toDegrees(Math.acos((ViewVector.x * viewToPoint.x + ViewVector.y * viewToPoint.y + ViewVector.z * viewToPoint.z))));
		
		x = ViewFrom[0] + viewToPoint.x * t;
		y = ViewFrom[1] + viewToPoint.y * t;
		z = ViewFrom[2] + viewToPoint.z * t;
		
		System.out.println("New X,Y,Z: " + " x = " + x + " y = " + y + " z = " + z);
		
		if(t >= 0) {
			
			//This detemrines the x, and y coordinate by finding the length of a specific vector (need to do more research).
			double DrawX = RotationPlanePerp.x * x + RotationPlanePerp.y * y + RotationPlanePerp.z * z;
			double DrawY = RotationPlane.x * x + RotationPlane.y * y + RotationPlane.z * z;
			
			System.out.println("DrawX: " + DrawX);
			System.out.println("DrawY: " + DrawY);
			
		}
		
		
	}
	
	static Vector getRotationVector(double[] ViewFrom, double[] ViewTo) {
		
		
		double dx = Math.abs(ViewFrom[0] - ViewTo[0]);
		double dy = Math.abs(ViewFrom[1] - ViewTo[1]);
		double xRot, yRot;
		
		System.out.println("DX: " + dx);
		System.out.println("DY: " + dy);
		
		xRot = dy / (dx + dy);
		
		System.out.println("XRot: " + xRot);
		
		yRot = dx / (dx + dy);
		
		System.out.println("YRot: " + yRot);
		
		if(ViewFrom[1] > ViewTo[1]) {
			xRot = -xRot;
		}
		
		if(ViewFrom[0] < ViewTo[0]) {
			yRot = -yRot;
		}
		
		Vector rotation = new Vector(xRot, yRot, 0);
		
		return rotation;
	}
	
	
	private void printVector(Vector vector, String name) {
		
		System.out.println(name + ": " + " x = " + vector.x + " y = " + vector.y + " z = " + vector.z);
		
	}
	
	public static void main(String[] args) {
		
		new Testing_Program();
		
	}
	
	//-0.9486832980505138 * 0.2176428750330035 + 0.31622776601683794 * 0.6529286250990105
	
}

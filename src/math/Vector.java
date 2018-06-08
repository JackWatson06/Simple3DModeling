package math;

public class Vector {
	
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public double length;
	
	public Vector(double x, double y, double z) {
		
		this.length = Math.sqrt((x * x) + (y * y) + (z * z));
		if(length > 0) {
			this.x = x / length;
			this.y = y / length;
			this.z = z / length;
		}
		
		//System.out.println(this.x + " , " + this.y + " , " + this.z + " , " + length);
		
		//System.out.println("After: " + (Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z)));
		
	}
	
	public Vector crossProduct(Vector V) {
		
		Vector product = new Vector((y * V.z) - (z * V.y), (z * V.x) - (x * V.z), (x * V.y) - (y * V.x));
		
		return product;
		
	}
	

}

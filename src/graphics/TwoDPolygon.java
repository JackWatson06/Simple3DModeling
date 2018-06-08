package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class TwoDPolygon extends Polygon{

	private static final long serialVersionUID = 1L;
	
	Color c;
	
	public boolean isMouseOver = false;
	
	
	public TwoDPolygon(double[] x, double[] y, Color c) {
		
		
		int[] xIntegers = new int[x.length];
		int[] yIntegers = new int[y.length];
		
		for(int i = 0; i < x.length; i++) {
			xIntegers[i] = (int) x[i];
			yIntegers[i] = (int) y[i];
		}
		
		xpoints = xIntegers;
		ypoints = yIntegers;
		npoints = x.length;
		
		this.c = c;
		
	}
	
	public void updatePoints(double[] x, double[] y) {
		
		int[] xIntegers = new int[x.length];
		int[] yIntegers = new int[y.length];
		
		for(int i = 0; i < x.length; i++) {
			xIntegers[i] = (int) x[i];
			yIntegers[i] = (int) y[i];
		}
		
		xpoints = xIntegers;
		ypoints = yIntegers;
		npoints = x.length;
		
	}

	public boolean isMouseOver(int mouseX, int mouseY) {


		int i;
		int j;
		boolean result = false;
		
		for (i = 0, j = xpoints.length - 1; i < xpoints.length; j = i++) {
			if ((ypoints[i] > mouseY) != (ypoints[j] > mouseY) && (mouseX < (xpoints[j] - xpoints[i]) * (mouseY - ypoints[i]) / (ypoints[j]-ypoints[i]) + xpoints[i])) {
				result = !result;
			}
		}
		
		return result;

	}
	


	public void render(Graphics g) {
		
		
		if(c != null) {
			
			if(!isMouseOver) {
				g.setColor(c);
			}else {
				g.setColor(c.darker());
			}

			g.fillPolygon(this);

			
			g.setColor(Color.BLACK);
			g.drawPolygon(this);
		}else {
			g.drawPolygon(this);
		}
		
		
		isMouseOver = false;
		
	}

}



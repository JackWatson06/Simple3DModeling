package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import controls.Draw;
import controls.Extrude;
import listener.InputManager;
import main.Main;
import math.Vector;
import saving.Load;
import saving.Save;

public class Screen extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public static int width, height;
	
	//This represents the point you are viewing from (the point where you would be standing)
	public static double[] ViewFrom = new double[] { -10, 10, 10};
	
	//This represents the point you are looking towards (and does not represent the field of view)
	public static double[] ViewTo = new double[] {0, 0, 0};
	
	//This holds the number of polygons that are currently created.
	static int NumberOfPolygons = 0;
	
	//TwoDPolygon twoDTesting = new TwoDPolygon(new double[] {0, 3, 700} , new double[] {5, 67, 100}, Color.RED);
	
	public static int zoom = 50;
	
	private DisplayAxis[] axis;
	
	private ThreeDPolygon[] basicPlanes = new ThreeDPolygon[3];
	
	public static ArrayList<ThreeDPolygon> threeDPolygons = new ArrayList<ThreeDPolygon>();
	
	private ArrayList<ThreeDPolygon> organizedOrder = new ArrayList<ThreeDPolygon>();
	
	public static ArrayList<ThreeDPolygon> drawingPolygons = new ArrayList<ThreeDPolygon>();
	
	InputManager inputManager = new InputManager();
	InputManager keyListener;
	
	private boolean running = false;
	
	public static boolean drawingMode = false;
	public static boolean extrudeMode = false;
	public boolean constantRotation = false;
	public static boolean focusRequest = true;
	
	public boolean renderBasicPlanes = false;
	
	private Draw drawing = new Draw();
	private Extrude extrude = new Extrude();
	
	private Load load = new Load();
	private Save save = new Save();
	
	public String fileName = new String("Testing");
	
	private BufferedImage backgroundImage;
	private Image renderImage;
	
	private int frames = 0, ticks = 0;
	
	public Screen() {

		this.addKeyListener(inputManager);
		this.addMouseWheelListener(inputManager);
		this.addMouseMotionListener(inputManager);
		this.addMouseListener(inputManager);
		
		
	}
	
	public void setListener(InputManager listener) {
		keyListener = listener;
	}
	
	public void loop() {
		
		running = true;
		
		init();
		
		int idealFPS = 60;
		long updatePerTick = 1000000000 / idealFPS;
		
		long lastTime = System.nanoTime();
		long lastSecond = System.currentTimeMillis();
		
		boolean shouldRender = false;
		
		int frames = 0;
		int ticks = 0;
		


		while(running){
			
			long now = System.nanoTime();
			
			while(now - lastTime > updatePerTick){
				tick();
				lastTime += updatePerTick;
				ticks++;
				shouldRender = true;
			}
			
            if ( now - lastTime > updatePerTick){
               lastTime = now - updatePerTick;
            }
			
			if(shouldRender){

				repaint();
				frames++;
				shouldRender = false;
			}
			
			if(System.currentTimeMillis() - lastSecond > 1000){
				System.out.println("Frames: " + frames + " Ticks: " + ticks);
				this.frames = frames;
				this.ticks = ticks;
				frames = 0;
				ticks = 0;
				lastSecond = System.currentTimeMillis();
			}
			
			
			while(now - lastTime < updatePerTick){
				
                Thread.yield();
                
                try {
                	Thread.sleep(1);
                }catch(Exception e) {
                	e.printStackTrace();
                } 
				
				now = System.nanoTime();
			}
		}
		
	}
	
	void init() {
		
		Screen.width = getWidth();
		Screen.height = getHeight();
		
		drawing.width = width;
		drawing.height = height;
		
		
		//threeDPolygons.add(new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {0, 0, 1, 1}, new double[] {0, 0, 0, 0}, Color.BLUE, true));
		//threeDPolygons.add(new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {0, 0, 1, 1}, new double[] {1, 1, 1, 1}, Color.GREEN, false));
		//threeDPolygons.add(new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {0, 0, 0, 0}, new double[] {0, 0, 1, 1}, Color.RED, false));
		//threeDPolygons.add(new ThreeDPolygon(new double[] {0, 0, 0, 0} , new double[] {0, 1, 1, 0}, new double[] {0, 0, 1, 1}, Color.BLACK, true));
		//threeDPolygons.add(new ThreeDPolygon(new double[] {1, 1, 1, 1} , new double[] {0, 1, 1, 0}, new double[] {0, 0, 1, 1}, Color.CYAN, false));
		//threeDPolygons.add(new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {1, 1, 1, 1}, new double[] {0, 0, 1, 1}, Color.BLUE, true));
		
		axis = new DisplayAxis[3];
		
		axis[0] = new DisplayAxis(0, 0, 0, 1, 0, 0, 1, Color.BLUE);
		axis[1] = new DisplayAxis(0, 0, 0, 0, 1, 0, 1, Color.GREEN);
		axis[2] = new DisplayAxis(0, 0, 0, 0, 0, 1, 1, Color.RED);
		
		basicPlanes[0] = new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {0, 0, 0, 0}, new double[] {0, 0, 1, 1}, Color.LIGHT_GRAY, false);
		basicPlanes[1] = new ThreeDPolygon(new double[] {0, 0, 0, 0} , new double[] {0, 1, 1, 0}, new double[] {0, 0, 1, 1}, Color.LIGHT_GRAY, false);
		basicPlanes[2] = new ThreeDPolygon(new double[] {0, 1, 1, 0} , new double[] {0, 0, 1, 1}, new double[] {0, 0, 0, 0}, Color.LIGHT_GRAY, false);
		
		try {
			backgroundImage = ImageIO.read(Main.class.getResourceAsStream("/Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
	void tick() {
		if(focusRequest) {
			this.requestFocus();
		}
		
		if(!constantRotation) {
			moveCamera();
		}else {
			constantRotation();
		}
		
		
		for(int i = 0; i < NumberOfPolygons; i++) {
			
			threeDPolygons.get(i).updatePolygon();
		}
		
		for(int i = 0; i < axis.length; i++) {
			
			axis[i].update();
			
		}
		
		if(drawing.alwaysRender) {
			
			for(int i = 0; i < drawingPolygons.size(); i++) {
				
				drawingPolygons.get(i).updatePolygon();
				
			}
			
			
			
		}
		
		
		if(extrudeMode && Extrude.planeFound) {
			
			extrude.update(inputManager.mouseX);
			
		}
		
		boolean mouseOverPolygon = false;
		int iValuePolygon = 0;
		
		if(drawingMode && !drawing.planeFound) {
			
			if(threeDPolygons.size() == 0) {
				threeDPolygons.add(basicPlanes[0]);
				threeDPolygons.add(basicPlanes[1]);
				threeDPolygons.add(basicPlanes[2]);
				renderBasicPlanes = true;
			}
			
			int currentX = inputManager.mouseX;
			int currentY = inputManager.mouseY;

			ArrayList<Integer> iValues = new ArrayList<Integer>();

			for(int i = 0; i < NumberOfPolygons; i++) {

				if(threeDPolygons.get(i).polygon.isMouseOver(currentX, currentY)) {
					iValues.add(i);
				}

			}

			int greatestIValue = 0;

			for(int i = 0; i < iValues.size(); i++) {

				if(i == 0) { 
					greatestIValue = iValues.get(i);
				}else {

					if(threeDPolygons.get(iValues.get(i)).AvgDistance < threeDPolygons.get(greatestIValue).AvgDistance) {
						greatestIValue = iValues.get(i);
					}	
				}
			}

			if(iValues.size() > 0) {
				threeDPolygons.get(greatestIValue).polygon.isMouseOver = true;
				mouseOverPolygon = true;
				iValuePolygon = greatestIValue;
			}
			
		}
		
		if(extrudeMode && !Extrude.planeFound) {
			
			int currentX = inputManager.mouseX;
			int currentY = inputManager.mouseY;

			ArrayList<Integer> iValues = new ArrayList<Integer>();

			for(int i = 0; i < drawingPolygons.size(); i++) {

				if(drawingPolygons.get(i).polygon.isMouseOver(currentX, currentY)) {
					iValues.add(i);
				}

			}

			int greatestIValue = 0;

			for(int i = 0; i < iValues.size(); i++) {

				if(i == 0) { 
					greatestIValue = iValues.get(i);
				}else {

					if(drawingPolygons.get(iValues.get(i)).AvgDistance < drawingPolygons.get(greatestIValue).AvgDistance) {
						greatestIValue = iValues.get(i);
					}	
				}
			}

			if(iValues.size() > 0) {
				drawingPolygons.get(greatestIValue).polygon.isMouseOver = true;
				mouseOverPolygon = true;
				iValuePolygon = greatestIValue;
			}
			
		}
		
		if(drawingMode && drawing.startingPoint) {
			
			int currentX = inputManager.mouseX;
			int currentY = inputManager.mouseY;
			
			drawing.update(currentX, currentY);
			
		}
		
		if(drawingMode && Draw.inMode) {
			if(!inputManager.firstBox.equals("")) {
				Main.widthText.setText(inputManager.firstBox);
			}
			if(!inputManager.secondBox.equals("")) {
			    Main.heightText.setText(inputManager.secondBox);
			}
		}
		
		if(extrudeMode && Extrude.planeFound) {
			
			if(!inputManager.firstBox.equals("")) {
				
				Main.extrudeText.setText(inputManager.firstBox);
				
			}
			
		}
		
		if(inputManager.numberTrigger) {
			inputManager.numberTrigger = false;
			
			if(Draw.inMode) {
				
				drawing.setNumbers(Double.parseDouble(inputManager.firstBox), Double.parseDouble(inputManager.secondBox));
				
			}
			
			if(Extrude.planeFound) {
				
				extrude.setNumber(Double.parseDouble(inputManager.firstBox));
			}
		}

		
		catchClick(mouseOverPolygon, iValuePolygon);

		organizePolygons();
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(renderImage != null) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
		}else {
			g.drawImage(backgroundImage, 0, 0, width, height, null);
		}
		
		
		
		if(axis != null) {
			for(int i = 0; i < axis.length; i++) {
			
				axis[i].render(g);
			
			}
		}
		
		for(int i = 0; i < NumberOfPolygons; i++) {
			
			organizedOrder.get(i).polygon.render(g);
			
		}
		
		if(drawing.alwaysRender) {
			
			for(int i = 0; i < drawingPolygons.size(); i++) {
				
				drawingPolygons.get(i).polygon.render(g);
				
			}
			
			
		}
		
		
		if(drawingMode && drawing.startingPoint) {
			drawing.render(g);
		}
		
		if(extrudeMode && Extrude.planeFound) {
			
			extrude.render(g);
			
		}
		
		g.drawString("Frames: " + frames + " Ticks: " + ticks, width - 160, height - 20);
		
	}
	
	public void organizePolygons() {
		
		ArrayList<ThreeDPolygon> organizingArray = new ArrayList<ThreeDPolygon>(threeDPolygons);
		organizedOrder.clear();
		
		NumberOfPolygons = 0;
		
		for(int i = 0; i < threeDPolygons.size(); i++) {
			
			double currentMax = 0;
			int currentMaxIValue = 0;
			
			for(int j = 0; j < organizingArray.size(); j++) {
				
				if(organizingArray.get(j).AvgDistance > currentMax) {
					currentMax = organizingArray.get(j).AvgDistance;
					currentMaxIValue = j;
				}
				
			}
			
			organizedOrder.add(organizingArray.get(currentMaxIValue));
			NumberOfPolygons++;
			
			organizingArray.remove(currentMaxIValue);
			
		}
		
		organizingArray.clear();
		
	}
	
	public double getDistance(double x, double y, double z, double x1, double y1, double z1) {
		
		return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2) + Math.pow(z - z1, 2));
	}
	
	private int lastX = 0, lastY = 0;
	private int[] moveVector = new int[2];
	
	public void moveCamera() {
		
		
		if(inputManager.mouseWheelMoved) {
			
			inputManager.mouseWheelMoved = false;
			
			
			if(inputManager.mouseWheelDirection) {
				
				zoom += 2;
				
			}else {
				
				if(zoom > 4) {
					zoom -= 2;
				}
			
			}

			
		}
		
		
		if(inputManager.mousePressed && !drawing.planeFound) {
			
			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
			
			int currentX = (int) mousePosition.getX();
			int currentY = (int) mousePosition.getY();
			
			if(lastX == 0) lastX = currentX;
			if(lastY == 0) lastY = currentY;
				
			moveVector[0] = currentX - lastX;
			moveVector[1] = currentY - lastY;
			
			
			lastX = currentX;
			lastY = currentY;
			

			
			//int angleSpeed = 150;

			int angleSpeed = 100;

			
			if(moveVector[1] > 0) {


				double radiusBefore = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);


				double dx = Math.abs(ViewFrom[1] - ViewTo[1]);
				double dy = Math.abs(ViewFrom[0] - ViewTo[0]);

				if(ViewFrom[1] > ViewTo[1]) {
					dx = -dx;
				}

				if(ViewFrom[0] < ViewTo[0]) {
					dy = -dy;
				}

				Vector rotation = new Vector(dx, dy, 0);

				Vector zAxis = new Vector(0, 0, 1);
				Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);

				double angle = Math.acos(zAxis.z * ViewVector.z);


				if(angle < 3.04) {
					double theta = -( Math.PI / angleSpeed);

					double[] axisC = new double[] {rotation.x, rotation.y, rotation.z};

					double[] zYXRotation = new double[] {

							Math.cos(theta) + (axisC[0] * axisC[0]) * (1 - Math.cos(theta)),  (axisC[0] * axisC[1]) * (1 - Math.cos(theta)) - (axisC[2] * Math.sin(theta)), (axisC[0] * axisC[2]) * (1 - Math.cos(theta)) + (axisC[1] * Math.sin(theta)),
							(axisC[1] * axisC[0]) * (1 - Math.cos(theta)) + (axisC[2] * Math.sin(theta)), Math.cos(theta) + (axisC[1] * axisC[1]) * (1 - Math.cos(theta)), (axisC[1] * axisC[2]) * (1 - Math.cos(theta)) - (axisC[0] * Math.sin(theta)),
							(axisC[2] * axisC[0]) * (1 - Math.cos(theta)) - (axisC[1] * Math.sin(theta)), (axisC[2] * axisC[1]) * (1 - Math.cos(theta)) + (axisC[0] * Math.sin(theta)),  Math.cos(theta) + (axisC[2] * axisC[2]) * (1 - Math.cos(theta))

					};

					ViewFrom[0] = zYXRotation[0] * ViewFrom[0] + zYXRotation[1] * ViewFrom[1] + zYXRotation[2] * ViewFrom[2];
					ViewFrom[1] = zYXRotation[3] * ViewFrom[0] + zYXRotation[4] * ViewFrom[1] + zYXRotation[5] * ViewFrom[2];
					ViewFrom[2] = zYXRotation[6] * ViewFrom[0] + zYXRotation[7] * ViewFrom[1] + zYXRotation[8] * ViewFrom[2];

					double radiusAfter = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);

					double radiusRatio = radiusBefore / radiusAfter;

					ViewFrom[0] = ViewFrom[0] * radiusRatio;
					ViewFrom[1] = ViewFrom[1] * radiusRatio;
					ViewFrom[2] = ViewFrom[2] * radiusRatio;
				}

			}

			if(moveVector[1] < 0) {

				double radiusBefore = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);


				double dx = Math.abs(ViewFrom[1] - ViewTo[1]);
				double dy = Math.abs(ViewFrom[0] - ViewTo[0]);

				if(ViewFrom[1] > ViewTo[1]) {
					dx = -dx;
				}

				if(ViewFrom[0] < ViewTo[0]) {
					dy = -dy;
				}

				Vector rotation = new Vector(dx, dy, 0);

				Vector zAxis = new Vector(0, 0, 1);
				Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);

				double angle = Math.acos(zAxis.z * ViewVector.z);


				if(angle > 0.1) {

					double theta =  Math.PI / angleSpeed;

					double[] axisC = new double[] {rotation.x, rotation.y, rotation.z};

					double[] zYXRotation = new double[] {

							Math.cos(theta) + (axisC[0] * axisC[0]) * (1 - Math.cos(theta)),  (axisC[0] * axisC[1]) * (1 - Math.cos(theta)) - (axisC[2] * Math.sin(theta)), (axisC[0] * axisC[2]) * (1 - Math.cos(theta)) + (axisC[1] * Math.sin(theta)),
							(axisC[1] * axisC[0]) * (1 - Math.cos(theta)) + (axisC[2] * Math.sin(theta)), Math.cos(theta) + (axisC[1] * axisC[1]) * (1 - Math.cos(theta)), (axisC[1] * axisC[2]) * (1 - Math.cos(theta)) - (axisC[0] * Math.sin(theta)),
							(axisC[2] * axisC[0]) * (1 - Math.cos(theta)) - (axisC[1] * Math.sin(theta)), (axisC[2] * axisC[1]) * (1 - Math.cos(theta)) + (axisC[0] * Math.sin(theta)),  Math.cos(theta) + (axisC[2] * axisC[2]) * (1 - Math.cos(theta))

					};

					ViewFrom[0] = zYXRotation[0] * ViewFrom[0] + zYXRotation[1] * ViewFrom[1] + zYXRotation[2] * ViewFrom[2];
					ViewFrom[1] = zYXRotation[3] * ViewFrom[0] + zYXRotation[4] * ViewFrom[1] + zYXRotation[5] * ViewFrom[2];
					ViewFrom[2] = zYXRotation[6] * ViewFrom[0] + zYXRotation[7] * ViewFrom[1] + zYXRotation[8] * ViewFrom[2];

					double radiusAfter = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);

					double radiusRatio = radiusBefore / radiusAfter;

					ViewFrom[0] = ViewFrom[0] * radiusRatio;
					ViewFrom[1] = ViewFrom[1] * radiusRatio;
					ViewFrom[2] = ViewFrom[2] * radiusRatio;

				}

			}

			if(moveVector[0] > 0) {

				double radiusBefore = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);


				double theta =  Math.PI / angleSpeed;

				double[] axisC = new double[] {0, 0, 1};

				double[] zYXRotation = new double[] {

						Math.cos(theta) + (axisC[0] * axisC[0]) * (1 - Math.cos(theta)),  (axisC[0] * axisC[1]) * (1 - Math.cos(theta)) - (axisC[2] * Math.sin(theta)), (axisC[0] * axisC[2]) * (1 - Math.cos(theta)) + (axisC[1] * Math.sin(theta)),
						(axisC[1] * axisC[0]) * (1 - Math.cos(theta)) + (axisC[2] * Math.sin(theta)), Math.cos(theta) + (axisC[1] * axisC[1]) * (1 - Math.cos(theta)), (axisC[1] * axisC[2]) * (1 - Math.cos(theta)) - (axisC[0] * Math.sin(theta)),
						(axisC[2] * axisC[0]) * (1 - Math.cos(theta)) - (axisC[1] * Math.sin(theta)), (axisC[2] * axisC[1]) * (1 - Math.cos(theta)) + (axisC[0] * Math.sin(theta)),  Math.cos(theta) + (axisC[2] * axisC[2]) * (1 - Math.cos(theta))

				};

				ViewFrom[0] = zYXRotation[0] * ViewFrom[0] + zYXRotation[1] * ViewFrom[1] + zYXRotation[2] * ViewFrom[2];
				ViewFrom[1] = zYXRotation[3] * ViewFrom[0] + zYXRotation[4] * ViewFrom[1] + zYXRotation[5] * ViewFrom[2];
				ViewFrom[2] = zYXRotation[6] * ViewFrom[0] + zYXRotation[7] * ViewFrom[1] + zYXRotation[8] * ViewFrom[2];

				double radiusAfter = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);

				double radiusRatio = radiusBefore / radiusAfter;

				ViewFrom[0] = ViewFrom[0] * radiusRatio;
				ViewFrom[1] = ViewFrom[1] * radiusRatio;
				ViewFrom[2] = ViewFrom[2] * radiusRatio;

			}


			if(moveVector[0] < 0) {

				double radiusBefore = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);


				double theta = - (Math.PI / angleSpeed);

				double[] axisC = new double[] {0, 0, 1};

				double[] zYXRotation = new double[] {

						Math.cos(theta) + (axisC[0] * axisC[0]) * (1 - Math.cos(theta)),  (axisC[0] * axisC[1]) * (1 - Math.cos(theta)) - (axisC[2] * Math.sin(theta)), (axisC[0] * axisC[2]) * (1 - Math.cos(theta)) + (axisC[1] * Math.sin(theta)),
						(axisC[1] * axisC[0]) * (1 - Math.cos(theta)) + (axisC[2] * Math.sin(theta)), Math.cos(theta) + (axisC[1] * axisC[1]) * (1 - Math.cos(theta)), (axisC[1] * axisC[2]) * (1 - Math.cos(theta)) - (axisC[0] * Math.sin(theta)),
						(axisC[2] * axisC[0]) * (1 - Math.cos(theta)) - (axisC[1] * Math.sin(theta)), (axisC[2] * axisC[1]) * (1 - Math.cos(theta)) + (axisC[0] * Math.sin(theta)),  Math.cos(theta) + (axisC[2] * axisC[2]) * (1 - Math.cos(theta))

				};

				ViewFrom[0] = zYXRotation[0] * ViewFrom[0] + zYXRotation[1] * ViewFrom[1] + zYXRotation[2] * ViewFrom[2];
				ViewFrom[1] = zYXRotation[3] * ViewFrom[0] + zYXRotation[4] * ViewFrom[1] + zYXRotation[5] * ViewFrom[2];
				ViewFrom[2] = zYXRotation[6] * ViewFrom[0] + zYXRotation[7] * ViewFrom[1] + zYXRotation[8] * ViewFrom[2];

				double radiusAfter = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);

				double radiusRatio = radiusBefore / radiusAfter;

				ViewFrom[0] = ViewFrom[0] * radiusRatio;
				ViewFrom[1] = ViewFrom[1] * radiusRatio;
				ViewFrom[2] = ViewFrom[2] * radiusRatio;

			}

		}
		

		
	}
	
	//Normalizes the viewing window which will set it to the default position.
	
	public void normalize() {
		ViewFrom[0] = -10;
		ViewFrom[1] = 10;
		ViewFrom[2] = 10;
		
		ViewTo[0] = 0;
		ViewTo[1] = 0;
		ViewTo[2] = 0;
		
	}
	
	//Creates a constant rotation.
	
	public void constantRotation() {
		int angleSpeed = 150;
		
		double radiusBefore = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);


		double theta =  Math.PI / angleSpeed;

		double[] axisC = new double[] {0, 0, 1};

		double[] zYXRotation = new double[] {

				Math.cos(theta) + (axisC[0] * axisC[0]) * (1 - Math.cos(theta)),  (axisC[0] * axisC[1]) * (1 - Math.cos(theta)) - (axisC[2] * Math.sin(theta)), (axisC[0] * axisC[2]) * (1 - Math.cos(theta)) + (axisC[1] * Math.sin(theta)),
				(axisC[1] * axisC[0]) * (1 - Math.cos(theta)) + (axisC[2] * Math.sin(theta)), Math.cos(theta) + (axisC[1] * axisC[1]) * (1 - Math.cos(theta)), (axisC[1] * axisC[2]) * (1 - Math.cos(theta)) - (axisC[0] * Math.sin(theta)),
				(axisC[2] * axisC[0]) * (1 - Math.cos(theta)) - (axisC[1] * Math.sin(theta)), (axisC[2] * axisC[1]) * (1 - Math.cos(theta)) + (axisC[0] * Math.sin(theta)),  Math.cos(theta) + (axisC[2] * axisC[2]) * (1 - Math.cos(theta))

		};

		ViewFrom[0] = zYXRotation[0] * ViewFrom[0] + zYXRotation[1] * ViewFrom[1] + zYXRotation[2] * ViewFrom[2];
		ViewFrom[1] = zYXRotation[3] * ViewFrom[0] + zYXRotation[4] * ViewFrom[1] + zYXRotation[5] * ViewFrom[2];
		ViewFrom[2] = zYXRotation[6] * ViewFrom[0] + zYXRotation[7] * ViewFrom[1] + zYXRotation[8] * ViewFrom[2];

		double radiusAfter = getDistance(ViewFrom[0], ViewFrom[1], ViewFrom[2], ViewTo[0], ViewTo[1], ViewTo[2]);

		double radiusRatio = radiusBefore / radiusAfter;

		ViewFrom[0] = ViewFrom[0] * radiusRatio;
		ViewFrom[1] = ViewFrom[1] * radiusRatio;
		ViewFrom[2] = ViewFrom[2] * radiusRatio;
	}
	
	private void catchClick(boolean mouseOverPolygon, int iValuePolygon) {
		
		if(drawingMode && !Draw.inMode && inputManager.mouseClicked) {
			
			if(mouseOverPolygon) {
				
				double[][] newViewFrom = drawing.findNewView(threeDPolygons.get(iValuePolygon));
				
				for(int i = 0; i < newViewFrom.length; i++) {

					ViewFrom[i] = newViewFrom[i][0];
					ViewTo[i] = newViewFrom[i][1];
				}
				
				
				
				if(Math.abs(ViewFrom[2]) > Math.abs(ViewFrom[0]) && Math.abs(ViewFrom[2]) > Math.abs(ViewFrom[1])) {
					
					ViewFrom[0] = 0.00000001;
					
				}
				
			}
			
			inputManager.mouseClicked = false;
			
		}
		
		if(extrudeMode && !Extrude.planeFound && inputManager.mouseClicked) {
			
			if(mouseOverPolygon) {
				
				ThreeDPolygon extruding = new ThreeDPolygon(drawingPolygons.get(iValuePolygon));
				
				extruding.c = Color.BLUE;

				
				extrude.setPlane(extruding);
				
				drawingPolygons.remove(iValuePolygon);
				
			}
			
			inputManager.mouseClicked = false;
			
		}
	
		if(extrudeMode && Extrude.planeFound && inputManager.mouseClicked) {
			
			extrude.finalize();
			
			extrudeMode = false;
			
			inputManager.mouseClicked = false;
			
		}
		
		if(drawingMode && Draw.inMode && !drawing.startingPoint && inputManager.mouseClicked) {
			
			
			drawing.setSartingMouse(inputManager.mouseX, inputManager.mouseY);
			
			inputManager.mouseClicked = false;
		}
		
		if(drawingMode && Draw.inMode && drawing.startingPoint && inputManager.mouseClicked) {
			
			
			drawing.click();
			
			inputManager.mouseClicked = false;
			
		}
		
	}
	
	public void save() {
		
		System.out.println(fileName);
		
		save.save(fileName, threeDPolygons);
		
	}
	
	public void createNew(String name) {

		
		threeDPolygons.clear();
		NumberOfPolygons = 0;
		normalize();
		
		System.out.println(fileName);
		
	}
	
	public void load(String fileName) {
		
		this.fileName = fileName.substring(0, fileName.length() - 4);
		
		threeDPolygons = load.load(fileName);
		
	}
	
	public void setMode(int mode) {
		
		drawing.setMode(mode);
		
	}
	
	
	public void resetDrawing() {
		
		if(renderBasicPlanes) {
			
			threeDPolygons.remove(basicPlanes[0]);
			threeDPolygons.remove(basicPlanes[1]);
			threeDPolygons.remove(basicPlanes[2]);
			
			NumberOfPolygons -= 3;
			
			renderBasicPlanes = false;
			
		}
		
		drawing.exitDrawing();
	}
	
	public boolean foundPlane() {
		return drawing.planeFound;
	}

	
	
	
	
}

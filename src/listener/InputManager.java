package listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import controls.Draw;
import controls.Extrude;
import graphics.Screen;
import main.Main;


public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	
	public int mouseX = 0;
	public int mouseY = 0;
	
	public boolean w = false;
	public boolean s = false;
	public boolean a = false;
	public boolean d = false;
	
	public boolean mousePressed = false;
	public boolean mouseClicked = false;
	public boolean mouseWheelMoved = false;
	public boolean mouseWheelDirection = false;
	
	public boolean numberTrigger = false;
	
	private int tabCounter = 0;
	public boolean tab = false;
	
	public String firstBox = new String("");
	public String secondBox = new String("");
	
	public int[] moveVector = new int[2];
	
	public int originalX, originalY;
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if(e.getWheelRotation() < 0) {
			mouseWheelDirection = false;
		}else {
			mouseWheelDirection = true;
		}

		
		mouseWheelMoved = true;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		mouseClicked = true;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		mousePressed = true;
		
		originalX = e.getX();
		originalY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		mousePressed = false;

	}

	@Override
	public void keyPressed(KeyEvent e) {

		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			

			Main.setExtrudeLabels();

			if(Screen.drawingMode && Draw.inMode) {
				tabCounter++;
				if(tabCounter > 2) {
					tabCounter = 0;
				}
			}
			
			if(Screen.extrudeMode && Extrude.planeFound) {
				tabCounter++;
				if(tabCounter > 1) {
					tabCounter = 0;
				}
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			numberTrigger = true;
			
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

			if(Screen.drawingMode && Draw.inMode) {
				if(tabCounter == 1 && !firstBox.equals("")) {
					firstBox = firstBox.substring(0, firstBox.length() - 1);
				}else if(tabCounter == 2 && !secondBox.equals("")) {
					secondBox = secondBox.substring(0, firstBox.length() - 1);
				}
			}
			
			if(Screen.extrudeMode && Extrude.planeFound) {
				if(tabCounter == 1 && !firstBox.equals("")) {
					firstBox = firstBox.substring(0, firstBox.length() - 1);
				}
			}
		}
		
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
		if(Screen.drawingMode &&  Draw.inMode) {
			
			
			if(tabCounter == 1) {
				if(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == '.' && !firstBox.contains(".")) {

					firstBox += e.getKeyChar();
				}
			}else if(tabCounter == 2) {
				if(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == '.' && !secondBox.contains(".")) {

					secondBox += e.getKeyChar();

				}
			}
			
		}
		
		if(Screen.extrudeMode && Extrude.planeFound) {
			
			if(tabCounter == 1) {
				if(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == '.' && !firstBox.contains(".")) {
					firstBox += e.getKeyChar();
				}
			}
			
		}
		
	}

}

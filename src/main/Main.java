package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import controls.Draw;
import graphics.Screen;

public class Main{

	//This width, height element represents the width, height of the actual drawing canvas and does not include the buttons.
	public static int width, height;
	

	public static JTextField widthText = new JTextField(5);
	public static JTextField heightText = new JTextField(5);
	public static JTextField extrudeText = new JTextField(5);
	private static JLabel extrudeLabel = new JLabel("Distance (in)");
	private static JLabel widthLabel = new JLabel("Width (in)");
	private static JLabel heightLabel = new JLabel("Height (in)");
	
	private static JButton normalizeView = new JButton("Normalize");
	private static JButton toggleRotation = new JButton("Toggle Rotation");
	private static Screen screen = new Screen();
	
	private static int screenWidth, screenHeight;
	private static int canvasWidth;
	
	private String workingDirectory;
	
	
	
	public Main() {
		workingDirectory = System.getProperty("user.dir");
		
		JFrame frame = new JFrame();

		
		JButton open = new JButton("Open");
		JButton save = new JButton("Save");
		JButton newB = new JButton("New");
		JButton draw = new JButton("Draw");
		JButton extrude = new JButton("Extrude");
		
		JButton rectangle = new JButton("Rectangle");
		JButton dimension = new JButton("Dimension");
		JButton finish = new JButton("Finish");
		

		
		JFileChooser fileChooser = new JFileChooser(workingDirectory + "/saves/");
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
		
	    Dimension toolkit = Toolkit.getDefaultToolkit().getScreenSize();
	    screenWidth = toolkit.width;
	    screenHeight = toolkit.height;
	    
	    int widthCanvas = (int) (screenWidth * 0.6);
	    int heightCanvas = (int) (screenHeight * 0.6);
	    int heightButtons = (int) (screenHeight * 0.075);

	    widthLabel.setForeground(Color.BLACK);
	    heightLabel.setForeground(Color.BLACK);
	    
	    width = widthCanvas;
	    height = heightCanvas;
	    
	    canvasWidth = widthCanvas;
	    
		buttonPanel.setPreferredSize(new Dimension(widthCanvas, heightButtons));
		
		buttonPanel.setBackground(Color.BLACK);
		
	    Screen screen = new Screen();

	    
	    frame.setTitle("Simple 3D");
	    frame.setSize(widthCanvas, heightCanvas + heightButtons);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocation((screenWidth / 2) - (width / 2), (screenHeight / 2) - ((heightCanvas + heightButtons) / 2));
	    frame.setLayout(new BorderLayout());
	    
	    ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(draw) || e.getSource().equals(finish)) {

					Screen.drawingMode = !Screen.drawingMode;
					
					if(Screen.drawingMode) {
						
						buttonPanel.removeAll();
						
						buttonPanel.add(rectangle);
						buttonPanel.add(dimension);
						buttonPanel.add(finish);
						
						frame.revalidate();
						frame.repaint();
						
					}else {
						
						buttonPanel.removeAll();
						
						buttonPanel.add(open);
						buttonPanel.add(save);
						buttonPanel.add(newB);
						buttonPanel.add(draw);
						buttonPanel.add(extrude);
						
						screen.resetDrawing();
						
						frame.revalidate();
						frame.repaint();
						
						
					}
					
				}
				
				if(e.getSource().equals(rectangle)) {
					
					if(Screen.drawingMode && !Draw.inMode && screen.foundPlane()) {
						
						
						screen.setMode(Draw.RECTANGLE_MODE);
						
					    screen.add(widthLabel);
					    screen.add(heightText);
					    screen.add(widthText);
					    screen.add(heightLabel);
					    
					    Insets insets = screen.getInsets();
					    Dimension size = widthLabel.getPreferredSize();
					    
					    widthLabel.setBounds( insets.left + widthCanvas - size.width - (int) (screenWidth * 0.04), (int) (screenHeight * 0.01) + insets.top,
				                size.width, size.height);
					    
					    size = widthText.getPreferredSize();
					    
					    widthText.setBounds( insets.left + widthCanvas - size.width - (int) (screenWidth * 0.015), (int) (screenHeight * 0.01) + insets.top,
				                size.width, size.height);
					    
					    size = heightLabel.getPreferredSize();
					    
					    heightLabel.setBounds( insets.left + widthCanvas - size.width - (int) (screenWidth * 0.04), (int) (screenHeight * 0.03) + insets.top,
				                size.width, size.height);
					    
					    size = heightText.getPreferredSize();
					    
					    heightText.setBounds( insets.left + widthCanvas - size.width - (int) (screenWidth * 0.015), (int) (screenHeight * 0.03) + insets.top,
				                size.width, size.height);
					    
					    screen.revalidate();
						
					}
					
				}
				
				if(e.getSource().equals(extrude)) {
					
					Screen.extrudeMode = !Screen.extrudeMode;
					
				}
				
				if(e.getSource().equals(normalizeView)) {
					screen.normalize();
				}
				
				if(e.getSource().equals(open)) {
					
					int returnValue = fileChooser.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						screen.load(selectedFile.getName());
					}
					
				}
				
				if(e.getSource().equals(save)) {
					
					screen.save();
					
				}
				
				if(e.getSource().equals(newB)) {
					newWindow();
				}
				
				if(e.getSource().equals(toggleRotation)) {
					
					screen.constantRotation = !screen.constantRotation;
					screen.normalize();
					
					
				}
				
			}
	    	
	    };
	    
	    
	    screen.setLayout(null);
	    

	    
	    open.addActionListener(action);
	    save.addActionListener(action);
	    newB.addActionListener(action);
	    draw.addActionListener(action);
	    extrude.addActionListener(action);
	    
	    rectangle.addActionListener(action);
	    dimension.addActionListener(action);
	    finish.addActionListener(action);
	    
	    normalizeView.addActionListener(action);
	    toggleRotation.addActionListener(action);
	    
	    
		buttonPanel.add(open);
		buttonPanel.add(save);
		buttonPanel.add(newB);
		buttonPanel.add(draw);
		buttonPanel.add(extrude);
	    
	    frame.add(buttonPanel, BorderLayout.NORTH);
	    frame.add(screen, BorderLayout.CENTER);
	    
	    screen.add(normalizeView);
	    screen.add(toggleRotation);
	    
	    Insets insets = screen.getInsets();
	    Dimension size = normalizeView.getPreferredSize();
	    normalizeView.setBounds((int) (screenWidth * 0.005) + insets.left, (int) (screenHeight * 0.01) + insets.top,
	                 size.width, size.height);
	    
	    size = toggleRotation.getPreferredSize();
	    toggleRotation.setBounds((int) (screenWidth * 0.005) + insets.left, (int) (screenHeight * 0.04) + insets.top,
	                 size.width, size.height);
	    
	    frame.setVisible(true);

	    
	    screen.setFocusable(true);
	    screen.requestFocus();
	    
	    screen.loop();
	}
	
	public void newWindow() {
		
		Screen.focusRequest = false;
		
		JFrame newWindow = new JFrame();
		
		JButton close = new JButton("Cancel");
		JButton ok = new JButton("Create");
		JTextField title = new JTextField(20);
		JLabel text = new JLabel("Name");
		
		newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newWindow.setSize(350, 100);
		newWindow.setTitle("New Window");
		newWindow.setLayout(new FlowLayout());
		
		newWindow.setLocation((screenWidth / 2) - (newWindow.getWidth() / 2), (screenHeight / 2) - (newWindow.getHeight() / 2));
		
		ActionListener action = new ActionListener() {


			public void actionPerformed(ActionEvent e) {

				if(e.getSource().equals(close)) {
					Screen.focusRequest = true;
					newWindow.dispose();
				}
				
				if(e.getSource().equals(ok)) {
					Screen.focusRequest = true;
					screen.fileName = text.getText();
					screen.createNew(title.getText());
					newWindow.dispose();
					
				}
				
				
			}
			
			
		};
		
		ok.addActionListener(action);
		close.addActionListener(action);
		
		newWindow.add(text);
		newWindow.add(title);
		newWindow.add(ok);
		newWindow.add(close);
		

		
		newWindow.setVisible(true);
		
		
		
	}
	
	public static void removeWidthHeight() {
		

		screen.removeAll();
		
		screen.remove(normalizeView);
	    screen.remove(toggleRotation);
	    screen.add(widthLabel);
	    screen.add(heightText);
	    screen.add(widthText);
	    screen.add(heightLabel);
	    
	    Insets insets = screen.getInsets();
	    Dimension size = normalizeView.getPreferredSize();
	    normalizeView.setBounds((int) (screenWidth * 0.005) + insets.left, (int) (screenHeight * 0.01) + insets.top,
	                 size.width, size.height);
	    
	    size = toggleRotation.getPreferredSize();
	    toggleRotation.setBounds((int) (screenWidth * 0.005) + insets.left, (int) (screenHeight * 0.04) + insets.top,
	                 size.width, size.height);
	    
		screen.invalidate();
		screen.repaint();

		
	}
	
	public static void setExtrudeLabels() {
		
	    screen.add(extrudeText);
	    screen.add(extrudeLabel);
	    
	    Insets insets = screen.getInsets();
	    Dimension size = extrudeLabel.getPreferredSize();
	    
	    extrudeLabel.setBounds( insets.left + canvasWidth - size.width - (int) (screenWidth * 0.04), (int) (screenHeight * 0.01) + insets.top,
                size.width, size.height);
	    
	    size = extrudeText.getPreferredSize();
	    
	    extrudeText.setBounds( insets.left + canvasWidth - size.width - (int) (screenWidth * 0.015), (int) (screenHeight * 0.01) + insets.top,
                size.width, size.height);
		
	    screen.revalidate();
	    screen.repaint();
	}
	
	
	public static void main(String[] args) {
		
		new Main();
	    
	    
		
	}
	
}

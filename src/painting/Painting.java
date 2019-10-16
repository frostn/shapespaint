package painting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JFileChooser;

import shapes.Curve;
import shapes.Ellipse;
import shapes.Line;
import shapes.Polygon2;
import shapes.Rectangle;

public class Painting extends JComponent {

	private static final long serialVersionUID = 7489248266428470272L;
	private Grid grid;
	private static ArrayList<Shape> shapes;

	public Painting(int w, int h) {
		setPreferredSize(new Dimension(w, h));
		grid = new Grid(0, 0, w, h);
	}

	public Painting(int w, int h, ArrayList<Shape> shapes) {
		this(w, h);
		Painting.shapes = shapes;
	}

	public static void setShapes(Shape s) {
		Painting.shapes.add(s);
	}
	
	public static void setCurve(Curve s) {
		Painting.shapes.add(s);
		System.out.println(s);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		grid.display(g2);
		for (Shape s: Painting.shapes) {
			if (s instanceof Ellipse) {
				g2.setColor(((Ellipse) s).getDrawColor());
				if (((Ellipse) s).isVisible()) {
					g2.fill(s);
				}
				else {
					g2.draw(s);
				}
			}
			else if (s instanceof Line) {
				g2.setColor(((Line) s).getDrawColor());
				g2.draw(s);
			}
			else if (s instanceof Rectangle) {
				g2.setColor(((Rectangle) s).getDrawColor());
				if (((Rectangle) s).isVisible()) {
					g2.fill(s);
				}
				else {
					g2.draw(s);
				}
			}
			else if (s instanceof Polygon2) {
				g2.setColor(((Polygon2) s).getDrawColor());
				if (((Polygon2) s).isVisible()) {
					g2.fill(s);
				}
				else {
					g2.draw(s);
				}
			}
			else if (s instanceof Curve) {
				g2.setColor(((Curve) s).getDrawColor());
				g2.draw(s);
			}
		}
	}
	
	public void showGrid() {
		if (!grid.isVisible()) grid.toggleGrid();
	}
	
	public void hideGrid() {
		if (grid.isVisible()) grid.toggleGrid();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Painting canvas = new Painting(800, 800);
		String name = canvas.loadPainting();

		// JFRAME
		JFrame frame = new JFrame();
		frame.setTitle("Painting - " + name);
		frame.getContentPane().setBackground(Color.white);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		
		for (int i = 0; i < Painting.shapes.size(); i++) {
			System.out.println(shapes.get(i));
		}
	}

	String loadPainting() {
		String name = "no file found - running demo string ";
		
        String defaultDrawing = "circle, 175, 600, 30, 30, -65536\n" +
        "filledCircle, 290, 520, 40, 40, -256\n"	+
        "circle, 375, 425, 47, 47, -16711936\n" +
        "filledCircle, 475, 325, 48, 48, -16776961\n"	+
        "circle, 550, 225, 47, 47, -256\n" +
        "filledCircle, 625, 100, 48, 48, -65536\n" +
        "50, 50, 587, 100, -13421569\n" +
        "50, 200, 587, 100, -13421569\n";
        
        if (Painting.shapes == null) {
        	Painting.shapes = new ArrayList<Shape>();
        }	
        	
        JFileChooser jfc = new JFileChooser(".");
        int option = jfc.showOpenDialog(null);
        
        Scanner sc3 = null;
        
    	try {
    		if (option == JFileChooser.APPROVE_OPTION) {
	        	File shapeFile = jfc.getSelectedFile();
	        	sc3 = new Scanner(shapeFile);
	        	name = shapeFile.getName();
	        	if (grid.isVisible()) {
	        		grid.toggleGrid();
	        	}
    		}
	        else {
	        	sc3 = new Scanner(defaultDrawing);
	        	if (!grid.isVisible()) {
	        		grid.toggleGrid();
	        	}
	        }
    	} catch (FileNotFoundException e) {
    		sc3 = new Scanner(defaultDrawing);
    		//e.printStackTrace();
    	} finally {
    		while (sc3.hasNextLine()) {
    			String line = sc3.nextLine();
    			if (line.length() > 1) {
    				String type = line.substring(0, line.indexOf(','));
    				switch(type) {
    				case "filledCircle": 
    				case "circle":
    					Painting.shapes.add(new Ellipse(line));
    					break;
    				case "filledRectangle":
    				case "rectangle":
    					Painting.shapes.add(new Rectangle(line));
    					break;
    				case "filledPolygon":
    				case "polygon":
    					Painting.shapes.add(new Polygon2(line));
    					break;
    				case "curve":
    					Painting.shapes.add(new Curve(line));
    					break;
    				default:
    					Painting.shapes.add(new Line(line));
    					break;
    				}
    			}
    		}
    		sc3.close();
    	}
    	return name;
	}
}
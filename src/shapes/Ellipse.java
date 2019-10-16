package shapes;

import java.awt.Color;
import java.awt.geom.Ellipse2D.Double;
import java.util.Scanner;
import java.lang.String;

public class Ellipse extends Double {

	private static final long serialVersionUID = -448960191756718259L;
	private boolean fill;
	private Color c;

	public Ellipse(int x, int y, int w, int h, Color c) {
		super(x,y,w,h);
		this.c = c;
		
	}
	
	public Ellipse(int x, int y, int w, int h, Color c, boolean fill) {
		this(x,y,w,h,c);
		this.fill = fill;
	}
	
	public Ellipse(String line) {
		Scanner sc2 = new Scanner(line);
		sc2.useDelimiter(",\\s*");
		String type = sc2.next();
		if (type.charAt(0) == 'f') fill = true;
		x = sc2.nextInt();
		y = sc2.nextInt();
		width = sc2.nextInt();
		height = sc2.nextInt();
		int rgb = sc2.nextInt();
		c = new Color(rgb);
		sc2.close();
	}
	
	public Color getDrawColor() {
		return c;
	}
	
	public boolean isVisible() {
		return fill;
	}
	
	public String toString() {
		return String.format("%s, %d, %d, %d, %d, %d%n", (fill ? "filledCircle" : "circle"), (int) x, (int) y,
				(int) width, (int) height, c.getRGB());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ellipse e = new Ellipse("filledCircle, 475, 325, 48, 48, -16776961");
	    Ellipse e1 = new Ellipse( "circle, 375, 425, 47, 47, -16711936");
	    System.out.print(e);
	    System.out.print(e1);
	}
}

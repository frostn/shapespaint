package shapes;

import java.awt.geom.Rectangle2D.Double;
import java.awt.Color;
import java.util.Scanner;
import java.lang.String;

public class Rectangle extends Double {

	private static final long serialVersionUID = 1L;

	private boolean fill;
	private Color c;

	public Rectangle(int x, int y, int w, int h, Color c) {
		super(x,y,w,h);
		this.c = c;
		
	}
	
	public Rectangle(int x, int y, int w, int h, Color c, boolean fill) {
		this(x,y,w,h,c);
		this.fill = fill;
	}
	
	public Rectangle(String line) {
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
		return String.format("%s, %d, %d, %d, %d, %d%n", (fill ? "filledRectangle " : "rectangle "), (int) x, (int) y,
				(int) width, (int) height, c.getRGB());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Rectangle e = new Rectangle("filledRectangle, 475, 325, 48, 48, -16777216");
	    Rectangle e1 = new Rectangle("rectangle, 375, 425, 47, 47, -16777216");
	    System.out.print(e);
	    System.out.print(e1);
	}
}

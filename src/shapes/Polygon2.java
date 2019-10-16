package shapes;

import java.awt.Color;
import java.awt.Polygon;
//import java.util.Scanner;
import java.lang.String;

import painting.Painting;

public class Polygon2 extends Polygon {

	private static final long serialVersionUID = 1L;
	private boolean fill;
	private Color c;

	public Polygon2(int[] xpts, int[] ypts, int npts, Color c) {
		super(xpts,ypts,npts);
		this.c = c;
		
	}
	
	public Polygon2(int[] xpts, int[] ypts, int npts, Color c, boolean fill) {
		this(xpts,ypts,npts,c);
		this.fill = fill;
	}
	
	public Polygon2(String line) {
		String data[] = line.split(",");
		for (String d : data) {
			d.replaceAll(",", "");
			d.replaceAll(" ", "");
		}
		int npoints = Integer.parseInt(data[1]);
		if (data[0].charAt(0) == 'f') fill = true;
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];
		for (int i = 0; i < npoints; i++) {
			xpoints[i] = Integer.parseInt(data[i+2]);
		}
		for (int j = 0; j < npoints; j++) {
			ypoints[j] = Integer.parseInt(data[j+npoints+2]);
		}
		int rgb = Integer.parseInt(data[data.length-1]);
		c = new Color(rgb);
		Painting.setShapes(new Polygon2(xpoints, ypoints, npoints, c, fill));
	}
	
	public Color getDrawColor() {
		return c;
	}
	
	public boolean isVisible() {
		return fill;
	}
	
	public String toString() {
		String typeColor = (fill ? "filledPolygon," : "polygon,") + c.getRGB();
		String xy = "";
		for(int i = 0; i < npoints; i++) {
			xy += "," + xpoints[i] + "," + ypoints[i];
		}
		return String.format("%s%s%n", typeColor, xy);
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Polygon2 e = new Polygon2("filledPolygon,3,100,140,180,250,290,250,-16777216");
	    Polygon2 e1 = new Polygon2("polygon,3,400,420,440,400,380,400,-16777216");
	    System.out.print(e);
	    System.out.print(e1);
	}
}

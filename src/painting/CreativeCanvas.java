package painting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import shapes.Curve;
import shapes.Ellipse;
import shapes.Line;
import shapes.Polygon2;
import shapes.Rectangle;

public class CreativeCanvas extends JFrame implements ActionListener, ItemListener {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Shape> shapes;
	private String shapeType = "Line";
	private boolean fill;
	private Color color;
	//private JTextField[] mouseCoords;
	private String[] text = { "Pressed", "Clicked", "Released", "Entered", "Exited", "Dragged", "X:", "Y:" };
	private JPanel pSouth;
	private Painting canvas;
	private JTextField[] mouseStates;
	//private Point pointStart;
	//private Point pointEnd;
	private Shape selectedShape;
	private Shape clipboardShape;
	private Shape redoShape;
	//private File file;
	private String fileName = null;
	
	public CreativeCanvas() {
		super("Drawing");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setJMenuBar(createMenuBar());
		shapes = new ArrayList<Shape>();
		canvas = new Painting(1200, 800, shapes);
		SwingUtilities.updateComponentTreeUI(canvas);
		canvas.addMouseListener(new MyMouseListener());
		canvas.addMouseMotionListener(new MyMouseMotionListener());
		add("Center", canvas);
		// Create the array of text fields.
		pSouth = new JPanel();
		mouseStates = new JTextField[8];
		for (int i = 0; i < mouseStates.length; i++) {
			mouseStates[i] = new JTextField(text[i], 10);
			mouseStates[i].setEditable(false);
			pSouth.add(mouseStates[i]);
		}
		add("South", pSouth);
		pack();
		setVisible(true);
	}
	
	public void clearTextFields() {
		for (int i = 0; i < 6; i++)
			mouseStates[i].setBackground(Color.lightGray);
	}

	class MyMouseListener implements MouseListener {
		Point pointStart = null;
		Point pointEnd = null;
		
		public void mousePressed(MouseEvent e) {
			clearTextFields();
			mouseStates[0].setBackground(Color.yellow);
			pointStart = e.getPoint();
		}

		public void mouseClicked(MouseEvent e) {
			clearTextFields();
			mouseStates[1].setBackground(Color.yellow);
			for (Shape s: shapes) {
				if (s.contains(e.getPoint())) {
					selectedShape = s;
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			clearTextFields();
			mouseStates[3].setBackground(Color.yellow);
		}

		public void mouseExited(MouseEvent e) {
			clearTextFields();
			mouseStates[4].setBackground(Color.yellow);
		}

		public void mouseReleased(MouseEvent e) {
			clearTextFields();
			mouseStates[2].setBackground(Color.yellow);
			pointEnd = e.getPoint();
			switch (shapeType) {
			case "Line":
				shapes.add(new Line(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, color));
				break;
			case "Rectangle":
				shapes.add(new Rectangle(Math.min(pointStart.x, pointEnd.x), Math.min(pointStart.y, pointEnd.y), Math.abs(pointEnd.x - pointStart.x), Math.abs(pointEnd.y - pointStart.y), color, fill));
				break;
			case "Ellipse":
				shapes.add(new Ellipse(Math.min(pointStart.x, pointEnd.x), Math.min(pointStart.y, pointEnd.y), Math.abs(pointEnd.x - pointStart.x), Math.abs(pointEnd.y - pointStart.y), color, fill));
				break;
			case "Curve":
				break;
			}
			pointStart = null;
			repaint();
		}
	}

	class MyMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			clearTextFields();
			mouseStates[5].setBackground(Color.yellow);
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());
		}
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F); // mnemonics use ALT_MASK

		String[] commands = { "Open", "Save", "Save As..", "Clear Screen" };
		int[] keyEvents = { KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_C };

		for (int i = 0; i < commands.length; i++) {
			menuItem = new JMenuItem(commands[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvents[i], ActionEvent.ALT_MASK));
			menuItem.addActionListener(this);
			fileMenu.add(menuItem);
		}
		menuBar.add(fileMenu);

		JMenu drawMenu = new JMenu("Draw");
		drawMenu.setMnemonic(KeyEvent.VK_D); // mnemonics use ALT_MASK
		cbMenuItem = new JCheckBoxMenuItem("Grid", true);
		cbMenuItem.addItemListener(this);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke('g'));
		drawMenu.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Fill", false);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke('f'));
		cbMenuItem.addItemListener(this);
		drawMenu.add(cbMenuItem);
		menuItem = new JMenuItem("Edit Color");
		menuItem.setAccelerator(KeyStroke.getKeyStroke('c'));
		menuItem.addActionListener(this);
		drawMenu.add(menuItem);

		String[] cmds = { "Line", "Curve", "Ellipse", "Rectangle", "Polygon" };
		int[] drawKeyEvents = { KeyEvent.VK_L, KeyEvent.VK_Q, KeyEvent.VK_E, KeyEvent.VK_S, KeyEvent.VK_P };
		for (int i = 0; i < cmds.length; i++) {
			menuItem = new JMenuItem(cmds[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(drawKeyEvents[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			drawMenu.add(menuItem);
		}
		menuBar.add(drawMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		String[] commands2 = { "Cut", "Copy", "Paste", "Undo", "Redo" };
		int[] editKeyEvents = { KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_Z, KeyEvent.VK_R };
		for (int i = 0; i < commands2.length; i++) {
			menuItem = new JMenuItem(commands2[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(editKeyEvents[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			editMenu.add(menuItem);
		}
		menuBar.add(editMenu);

		return menuBar;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBoxMenuItem source = (JCheckBoxMenuItem) (e.getSource());
		switch(source.getText()) {
		case "Fill": 
			fill = source.getState();
			break;
		case "Grid":
			if (source.getState()) canvas.showGrid();
			else canvas.hideGrid();
			repaint();
			break;
		}
		// temporary feedback
		String eventData = "Item event detected.\n" + "    Event source: " + source.getText() + " (an instance of "
				+ source.getClass() + ")\n" + "    New state: "
				+ ((e.getStateChange() == ItemEvent.SELECTED) ? "selected" : "unselected");
		System.out.println(eventData);
		System.out.printf("shapeType : %s, fill : %b, color : %d%n%n", shapeType, fill, color.getRGB());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.contentEquals("Edit Color")) {
			color = JColorChooser.showDialog(this, "Select Color", color);
			pSouth.setBackground(color);
		}

		switch (command) {
		case "Line":
			shapeType = "Line";
			break;
		case "Curve":
			shapeType = "Curve";
			break;
		case "Ellipse":
			shapeType = "Ellipse";
			break;
		case "Rectangle":
			shapeType = "Rectangle";
			break;
		case "Polygon":
			shapeType = "Polygon";
			break;
		case "Cut":
			clipboardShape = selectedShape;
			shapes.remove(selectedShape);
			repaint();
			break;
		case "Copy":
			clipboardShape = selectedShape;
			break;
		case "Paste":
			shapes.add(clipboardShape);
			repaint();
			break;
		case "Undo":
			redoShape = shapes.get(shapes.size() - 1);
			shapes.remove(shapes.size() - 1);
			repaint();
			break;
		case "Redo":
			shapes.add(redoShape);
			redoShape = null;
			repaint();
			break;
		case "Open":
			shapes.clear();
			canvas.loadPainting();
			repaint();
			break;
//		case "Save As..":
//			fileName = (JOptionPane.showInputDialog("File Name?") + ".txt");
//			file = new File(fileName);
//			FileWriter fw = null;
//			try {
//				fw = new FileWriter(file);
//				for (Shape s : shapes) {
//					fw.write(s.toString());
//				}
//			} catch (IOException ioe) {
//				ioe.printStackTrace();
//			} finally {
//				try {
//					fw.close();
//				} catch (IOException ioe2) {
//					ioe2.printStackTrace();
//				}
//			}
//			break;
//		case "Save":
//			if (fileName != null) {
//				try {
//					FileWriter fw = new FileWriter(file);
//					for (Shape s : shapes) {
//						fw.write(s.toString() + "\n");
//					}
//					fw.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//			else {
//				command = "Save As..";
//			}
//			break;
		case "Clear Screen":
			shapes.clear();
			repaint();
			break;
		}

		// temporary feedback
		Object o = e.getSource();
		String className = o.getClass().getName().substring(o.getClass().getName().lastIndexOf('.') + 1);
		System.out.printf("Event: %s%n    ActionCommand: %s%n   Source class: %s%n", e.getClass(), command, className);
		System.out.printf("shapeType : %s, fill : %b, color : %d%n%n", shapeType, fill, color.getRGB());
	}
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception unused) {
			; // Ignore exception because we can't do anything. Will use default.
		}
		new CreativeCanvas();
	}

}

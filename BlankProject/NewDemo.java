/*
 * File: NewDemo.java
 * ---------------------
 * This class is a blank one that you can change at will. Remember, if you change
 * the class name, you'll need to change the filename so that it matches.
 * Then you can extend GraphicsProgram, ConsoleProgram, or DialogProgram as you like.
 */

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.BorderLayout;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.*;

@SuppressWarnings("serial")
public class NewDemo extends GraphicsProgram {
	public void run() {
		drawRect3D(new Point3D(50, 50, 10), 50, 50, 50);
		draw2DPolygon(polygon);
		rotate2DPolygon(polygon, new GPoint(70, 70), Math.PI/6);
		drawLine(10, 10, 70, 90);
		drawLine(10, 90, 90, 70);
		
	}
	
	public void drawRect3D(Point3D origin, int l, int w, int h){
		int ox = (int) origin.getX(), oy = (int) origin.getY();
		GPoint[] face1 = {new GPoint (ox, oy), new GPoint(ox + l, oy), new GPoint(ox + l, oy + h), new GPoint(ox, oy + h)};
		draw2DPolygon(face1);
		ox = ox + w/2;
		oy = oy + w/2;
		GPoint[] face2 = {new GPoint(ox, oy), new GPoint(ox + l, oy), new GPoint(ox + l, oy + h), new GPoint(ox, oy + h)};
		draw2DPolygon(face2);
		for(int i = 0; i < face1.length; i++){
			drawLine(face1[i].getX(), face1[i].getY(), face2[i].getX(), face2[i].getY());
		}
	}
	
	public void drawLine3D(Point3D p1, Point3D p2, int viewDistance){
		double x0 = p1.getX() * viewDistance/p1.getZ() + 25; 
		double y0 = 25 - (0.8)*p1.getY()*viewDistance/p1.getZ();
		double x1 = 25 + p2.getX()*viewDistance/2;
		double y1 = 25 - (0.8)*p2.getY()*viewDistance/p2.getZ();
		drawLine((int)x0, (int)y0, (int)x1, (int)y1);
	}
	
	public GPoint[] rotate2DPolygon(GPoint[] vertices, GPoint anchor, double theta){
		for(int i = 0; i < vertices.length; i++){
			GPoint pt = vertices[i];
			double x = pt.getX(), y = pt.getY();
			double x_rot = ((x - anchor.getX()) * Math.cos(theta) - (anchor.getY() - y) * Math.sin(theta)) + anchor.getX(), y_rot = ((x - anchor.getX()) * Math.sin(theta) + (anchor.getY() - y) * Math.cos(theta)) + anchor.getY();
			pt.setLocation(x_rot, y_rot);
		}
		return vertices;
	}
	
	public GPoint[] scale2DPolygon(GPoint[] vertices, int scalingFactor){
		for(int i = 0; i < vertices.length; i++){
			GPoint pt = vertices[i];
			pt.setLocation(pt.getX() * scalingFactor, pt.getY() * scalingFactor);
			vertices[i] = pt;
		}
		return vertices;
	}
	
	public void draw2DPolygon(GPoint[] vertices){
		if(vertices.length < 2) return;
		for(int i = 0; i < vertices.length - 1; i++){
			drawLine(vertices[i].getX(), vertices[i].getY(), vertices[i + 1].getX(), vertices[i + 1].getY());
		}
		drawLine(vertices[vertices.length - 1].getX(), vertices[vertices.length - 1].getY(), vertices[0].getX(), vertices[0].getY());
	}
	
	private void drawLine(double x, double y, double x2, double y2) {
		drawLine2D((int) x, (int) y, (int) x2, (int) y2);
		
	}

	public void draw2DTriangle(int x0, int y0, int x1, int y1, int x2, int y2){
		//Takes three sets of coordinates, sorts (clockwise), and draws lines to form triangle
		if((x0 == x1 && x1 == x2) || (y0 == y1 && y1 == y2)){ //Not a triangle: horizontal line instead
			return;
		}
		drawLine2D(x0, y0, x1, y1);
		drawLine2D(x1, y1, x2, y2);
		drawLine2D(x2, y2, x0, y0);
	}
	
	public void drawLine2D(int x0, int y0, int x1, int y1){
		//System.out.println("(" + x0 + ", " + y0 + ") to (" + x1 + ", " + y1 + ")");
		int currentPointX = x0, currentPointY = y0;
		int dx = x1 - x0, dy = y1 - y0;
		int error = 0;
		int x_inc = 1, y_inc = 1;
		if(dx < 0){
			dx = -dx; x_inc = -1;
		}
		if(dy < 0){
			dy = -dy; y_inc = -1;
		}
		if(dx > dy){
			for(int i = 0; i < dx; i++){
				plotPixel(currentPointX, currentPointY);
				error += dy; //Increment error: Line drawn must be as close to desired line as possible
				if(error > dx){
					currentPointY += y_inc;
					error -= dx;
				}
				currentPointX += x_inc;
			}	
		}
		else{
			for(int i = 0; i < dy; i++){
				plotPixel(currentPointX, currentPointY);
				error += dx;
				if(error > dy){
					currentPointX += x_inc;
					error -= dy;
				}
				currentPointY += y_inc;
			}
		}
	}
	
	public void plotPixel(int x, int y){
		plotPixel(x, y, Color.BLACK);
	}
	
	public void plotPixel(int x, int y, Color c){
		GRect pixel = new GRect(x, y, 1, 1);
		pixel.setFillColor(c);
		add(pixel);	
	}
	
	private void doRadioButtons(){
		this.setSize(300, 300);
		ButtonGroup demoSelect = new ButtonGroup();
		loop = new JRadioButton("The Loop Effect");
		polygons = new JRadioButton("Polygon Drawing");
		demoSelect.add(polygons);
		demoSelect.add(loop);
		this.add(loop, BorderLayout.NORTH);
		this.add(polygons, BorderLayout.NORTH);
	}
	
	public void actionPerformed(ActionEvent e){
		double theta = rotationValue.getValue() * Math.PI/180;
		removeAll();
		draw2DPolygon(rotate2DPolygon(polygon, new GPoint(15, 15), theta));
	}
	
	/*private void doPolygonGUI(){
		JFrame frame = new JFrame("Polygon Drawing");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new )
		frame.setVisible(true);
	}*/
	private JRadioButton loop, polygons;
	private JSlider rotationValue;
	GPoint[] polygon = {new GPoint(80,10), new GPoint(120,15), new GPoint(70, 30), new GPoint(75, 40), new GPoint(85, 45)};
}


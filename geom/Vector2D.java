package geom;

import java.awt.*;

/**
 * Creates and draws vectors onto given graphics adapters.
 * 
 * @author Andrew Vaughan
 * @version August 1, 2006
 */
public class Vector2D {
	
	/**
	 * End-shape declaration for a solid triangle.
	 */
	public static final int SHAPE_TRIANGLE = 0;
	
	/**
	 * End-shape declaration for a hollow triangle.
	 */
	public static final int SHAPE_HOLLOWTRI = 1;
	
	/**
	 * End-shape declaration for a large triangle.
	 */
	public static final int SHAPE_BIGTRI = 2;
	
	public static final int SHAPE_BIGHOLLOWTRI = 3;
	
	/**
	 * End-shape declaration for a solid circle.
	 */
	public static final int SHAPE_CIRCLE = 4;
	
	
//---------------------------------------------------------------------------------------
//	PUBLIC METHODS
	
	
	/**
	 * Creates a new vector with the given ends from the given points.
	 * 
	 * @param g the graphics adapter to draw on to
	 * @param start the starting point of the vector
	 * @param end the ending point of the vector
	 * @param endShape the shape on the end of the line
	 * @param isDashed whether the line is dashed or not
	 */
	public static void drawVector(Graphics2D g, Point start, Point end, int endShape, boolean isDashed) {
		double angle = Math.atan2(start.y - end.y, start.x - end.x);
		double angle_p = Math.atan2(start.y - end.y, start.x - end.x) + (Math.PI / 2);
		
		int height = 5;
		int width = 10;
		
		if (endShape == SHAPE_BIGTRI) {
			height = 7;
			width = 12;
			
		} else if (endShape == SHAPE_CIRCLE) {
			height = 10;
		}
		
		int arrowX = end.x - xCord(height, angle); 
		int arrowY = end.y - yCord(height, angle);
		
		if (isDashed) {
			float dash1[] = {1f};
			g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash1, 0f));
		}
		
		// Draw the line
		if (endShape == SHAPE_TRIANGLE || endShape == SHAPE_HOLLOWTRI)
			g.drawLine(start.x, start.y, arrowX, arrowY);
		else
			g.drawLine(start.x, start.y, end.x, end.y);
		
		
		// Setup the triangle
		Polygon triangle = new Polygon();
		triangle.addPoint(arrowX, arrowY);
		triangle.addPoint(arrowX + xCord(width / 2, angle_p), arrowY + yCord(width / 2, angle_p));
		triangle.addPoint(arrowX + xCord(height, angle), arrowY + yCord(height, angle));
		triangle.addPoint(arrowX + xCord(width / 2, angle_p + Math.PI), arrowY + yCord(width / 2, angle_p + Math.PI));
		triangle.addPoint(arrowX, arrowY);
		
		g.setStroke(new BasicStroke(1f));
		
		// Draw the end shape
		if (endShape == SHAPE_TRIANGLE || endShape == SHAPE_BIGTRI) {
			g.fillPolygon(triangle);
			
		} else if (endShape == SHAPE_HOLLOWTRI || endShape == SHAPE_BIGHOLLOWTRI) {
			g.drawPolygon(triangle);
			
		} else if (endShape == SHAPE_CIRCLE) {
			g.fillOval(end.x - (width / 2), end.y - (height / 2), width, height);
		}
	}
	
	
	/**
	 * Creates a new vector with the given ends from the given points.
	 * 
	 * @param g the graphics adapter to draw on to
	 * @param start the starting point of the vector
	 * @param end the ending point of the vector
	 * @param endShape the shape on the end of the line
	 */
	public static void drawVector(Graphics2D g, Point start, Point end, int endShape) {
		drawVector(g, start, end, endShape, false);
	}
	
	
	/**
	 * Creates a new vector with the given ends from the given points.
	 * 
	 * @param g the graphics adapter to draw on to
	 * @param start the starting point of the vector
	 * @param end the ending point of the vector
	 */
	public static void drawVector(Graphics2D g, Point start, Point end) {
		drawVector(g, start, end, SHAPE_TRIANGLE, false);
	}
	
	
//---------------------------------------------------------------------------------------
//	PRIVATE METHODS
	
	
	/**
	 * Discovers the x-coordinate of a vector given a length and angle.
	 * 
	 * @param len the length of the vector
	 * @param dir the angle of the vector
	 * 
	 * @return the x-coordinate of the vector
	 */
	private static int xCord(int len, double dir) {
		return (int)(-len * Math.cos(dir));
	}
	
	
	/**
	 * Discovers the y-coordinate of a vector given a length and angle.
	 * 
	 * @param len the length of the vector
	 * @param dir the angle of the vector
	 * 
	 * @return the y-coordinate of the vector
	 */
	private static int yCord(int len, double dir) { return (int)(-len * Math.sin(dir)); }
}

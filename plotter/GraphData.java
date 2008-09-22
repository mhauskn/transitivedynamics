package plotter;

import java.util.ArrayList;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Sphere;

import util.Util;

/**
 * Contains all the data a graph3d object needs to
 * display. Useful for passing between graph3d objects 
 * when they are replaced/created/destroyed
 * @author Administrator
 *
 */
public class GraphData 
{
	public int numGridlines = 5;
	
	public ArrayList<int[]> rawData = new ArrayList<int[]>();
			
	public String xLabel = "X-Axis";
	public String yLabel = "Y-Axis";
	public String zLabel = "Z-Axis";
	
	public int xIndex;
	public int yIndex;
	public int zIndex;
	
	public double xMin = 0.0;
	public double xMax = 1.0;
	
	public double yMin = 0.0;
	public double yMax = 1.0;
	
	public double zMin = 0.0;
	public double zMax = 1.0;
		
	public Color3f backgroundColor = Util.WHITE;
	public Color3f axisColor = Util.BLACK;
	public Color3f gridColor = Util.GREY;
	
	public GraphData ()
	{
		
	}
}

/*
 *      @(#)MouseBehaviorApp.java 1.1 00/09/22 16:24
 *
 * Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package plotter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import javax.swing.*;

import util.Util;

import java.util.ArrayList;


public class Graph3d extends JPanel { 
	
	private static final long serialVersionUID = 1L;

	private static final float DEF_DIST = 1.0f;
	
	// Number of gridlines between major axies
	private int numGridlines = 5;
	
	// Holds possible data points and conclusion verb types
	private ArrayList<int[]> rawData = new ArrayList<int[]>();
	
	/**
	 * Holds an arrayList for each verbNumber. This is useful when 
	 * we want to make only one verb transparent.
	 */
	private ArrayList<Sphere>[] spheres = (ArrayList<Sphere>[]) new ArrayList[6];
	
	private int xIndex;
	private int yIndex;
	private int zIndex;
		
	// Labels for each of our Axies
	private String xLabel = "X-Axis";
	private String yLabel = "Y-Axis";
	private String zLabel = "Z-Axis";
	
	// Min and max X Values
	private double xMin = 0.0;
	private double xMax = 1.0;
	
	// Min and max Y Values
	private double yMin = 0.0;
	private double yMax = 1.0;
	
	// Min and max Z Values
	private double zMin = 0.0;
	private double zMax = 1.0;
	
	private static final String LABEL_FONT = "Serif";
	private static final int LABEL_FONT_SIZE = 24;
	
	private static final String TICK_FONT = "Helvetica";
	private static final int TICK_FONT_SIZE = 12;
	
	private static Color3f backgroundColor = Util.WHITE;
	private static Color3f axisColor = Util.BLACK;
	private static Color3f gridColor = Util.GREY;
	
	Canvas3D canvas3D;
	BranchGroup scene;
	SimpleUniverse simpleU;
	BranchGroup objRoot;
	TransformGroup objTransform;
	TransformGroup objRotate;
	
    // Create a simple scene and attach it to the virtual universe
    public Graph3d () 
    {
    	for (int i = 0; i < 6; i++)
    		spheres[i] = new ArrayList<Sphere>();
    	    	
    	//super("Explore Visualization");
    	
    	//setSize(512,512);
    	//this.setMinimumSize(new Dimension(500,500));
    	/*setPreferredSize(new Dimension(512, 512));
        setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(BorderFactory.createEtchedBorder());

    	
        setLayout(new BorderLayout());
        config = SimpleUniverse.getPreferredConfiguration();

        canvas3D = new Canvas3D(config);
        //add("Center", canvas3D);
        //this.getContentPane().add(canvas3D);
        this.add(canvas3D);
        
        scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        setVisible(true);*/
    }
    
    
    public void graph ()
    {  
    	setPreferredSize(new Dimension(512, 512));
        setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(BorderFactory.createEtchedBorder());

    	
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        canvas3D = new Canvas3D(config);
        //add("Center", canvas3D);
        //this.getContentPane().add(canvas3D);
        this.add(canvas3D);
        
        scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        setVisible(true);
    }
    
    public void cleanMemRef ()
    {
    	simpleU.cleanup();
    	spheres = null;
    	canvas3D = null;
    	scene = null;
    	simpleU = null;
    	objRoot = null;
    	objTransform = null;
    	objRotate = null;
    }
    
    public void setXAxisName (String s)
    {
    	xLabel = s;
    }
    
    public void setYAxisName (String s)
    {
    	yLabel = s;
    }
    
    public void setZAxisName (String s)
    {
    	zLabel = s;
    }
    
    public void setNumGridlines (int n)
    {
    	numGridlines = n;
    }
    
    public void setXMinMax (double _xMin, double _xMax)
    {
    	xMin = _xMin;
    	xMax = _xMax;
    }
    
    public void setYMinMax (double _yMin, double _yMax)
    {
    	yMin = _yMin;
    	yMax = _yMax;
    }

    public void setZMinMax (double _zMin, double _zMax)
    {
    	zMin = _zMin;
    	zMax = _zMax;
    }
    
    public void setEMagMinMax (int eMag)
    {
    	xMax = yMax = zMax = Math.abs(eMag);
    	xMin = yMin = zMin = -Math.abs(eMag);
    }
    
    public void setPoints (ArrayList<int[]> points)
    {
    	rawData = points;
    }
    
    /**
     * Sets indicies into the rawData array so that we know 
     * where to look for the data in order to determine each axis.
     * @param _xIndex
     * @param _yIndex
     * @param _zIndex
     */
    public void setIndicies (int _xIndex, int _yIndex, int _zIndex)
    {
    	xIndex = _xIndex;
    	yIndex = _yIndex;
    	zIndex = _zIndex;
    }
    
    public void toggleBackgroundColors ()
    {
    	if (backgroundColor == Util.WHITE)
    	{
    		backgroundColor = Util.BLACK;
    		axisColor = Util.WHITE;
    	}
    	else if (backgroundColor == Util.BLACK)
    	{
    		backgroundColor = Util.WHITE;
    		axisColor = Util.BLACK;
    	}
    }
    
    public GraphData exportGraphData ()
    {
    	GraphData gd = new GraphData();
    	gd.numGridlines = numGridlines;
    	gd.rawData = rawData;
    	gd.xLabel = xLabel;
    	gd.yLabel = yLabel;
    	gd.zLabel = zLabel;
    	gd.xIndex = xIndex;
    	gd.yIndex = yIndex;
    	gd.zIndex = zIndex;
    	gd.xMin = xMin;
    	gd.xMax = xMax;
    	gd.yMin = yMin;
    	gd.yMax = yMax;
    	gd.zMin = zMin;
    	gd.zMax = zMax;
    	gd.backgroundColor = backgroundColor;
    	gd.axisColor = axisColor;
    	gd.gridColor = gridColor;
    	return gd;
    }
    
    public void importGraphData (GraphData gd)
    {
    	numGridlines = gd.numGridlines;
    	rawData = gd.rawData;
    	xLabel = gd.xLabel;
    	yLabel = gd.yLabel;
    	zLabel = gd.zLabel;
    	xIndex = gd.xIndex;
    	yIndex = gd.yIndex;
    	zIndex = gd.zIndex;
    	xMin = gd.xMin;
    	xMax = gd.xMax;
    	yMin = gd.yMin;
    	yMax = gd.yMax;
    	zMin = gd.zMin;
    	zMax = gd.zMax;
    	backgroundColor = gd.backgroundColor;
    	axisColor = gd.axisColor;
    	gridColor = gd.gridColor;
    }
    
//---------------------PRIVATE METHODS-------------------------
    
    /**
     * Creates the scene graph for our grapher
     */
    private BranchGroup createSceneGraph() {
    	// Create the root of the branch graph
    	objRoot = new BranchGroup();

        objTransform = new TransformGroup();
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
        // Move the start position
        Transform3D translate = new Transform3D();
        Transform3D rotate = new Transform3D();
    	Transform3D tempRotate = new Transform3D();
    	translate.setTranslation(new Vector3f (-0.5f,-0.3f,-0.5f));
        rotate.rotX(Math.PI/4.0d);
    	tempRotate.rotY(-Math.PI/4.0d);
        rotate.mul(tempRotate);
        rotate.mul(translate);
    	objRotate = new TransformGroup(rotate);
    	objTransform.addChild(objRotate);
    	
    	// Create background of specified color
    	BoundingSphere boundingSphere = 
            new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    	Background background = new Background(backgroundColor);
        //background.setCapability(Background.ALLOW_COLOR_WRITE);
        background.setApplicationBounds(boundingSphere);
        objRotate.addChild(background);

    	
    	addGridlines(objRotate, numGridlines); // Add the gridlines to the model.
        
        addLabels(objRotate); // Add some axis labels to objRotate
        
        addSpheres(objRotate); // Add the actual data
    	
        
        // a bounding sphere specifies a region a behavior is active
        // create a sphere centered at the origin with radius of 2.5
        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(2.5);

        DirectionalLight lightD = new DirectionalLight();
        lightD.setInfluencingBounds(bounds);
        objTransform.addChild(lightD);

    	objRoot.addChild(objTransform);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objTransform);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);

        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(objTransform);
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(objTransform);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);

        // Let Java 3D perform optimizations on this scene graph.
        //objRoot.compile();

        return objRoot;
    }
    
    /**
     * Creates the appearance for our points
     */
    private Appearance createAppearance(Color3f diffuseColor) {
        Appearance appear = new Appearance();
        //appear.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        Material material = new Material();
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        material.setDiffuseColor(diffuseColor);
        
        material.setShininess(50.0f);
        // make modifications to default material properties
        appear.setMaterial(material);
        
        TransparencyAttributes ta = new TransparencyAttributes(
        		TransparencyAttributes.FASTEST, 0.0f);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        appear.setTransparencyAttributes(ta);

        //ColoringAttributes colorAtt = new ColoringAttributes();
        //colorAtt.setShadeModel(ColoringAttributes.SHADE_FLAT);
        //appear.setColoringAttributes(colorAtt);

        return appear;
    }
    
    /**
     * Adds our points to the graph
     * @param t
     * @param p
     */
    private void addSpheres (TransformGroup t)
    {
    	for (int i = 0; i < rawData.size(); i++)
    	{
    		int[] tuple = rawData.get(i);
    		
    		double xTrans;
    		double yTrans;
    		double zTrans;
    		
    		if (xIndex == -1)
    			xTrans = 0.0;
    		else
    			xTrans = (tuple[xIndex] - xMin) / ((double) (xMax - xMin));
    		
    		if (yIndex == -1)
    			yTrans = 0.0;
    		else
    			yTrans = (tuple[yIndex] - yMin) / ((double) (yMax - yMin));
    		
    		if (zIndex == -1)
    			zTrans = 0.0;
    		else
    			zTrans = (tuple[zIndex] - xMin) / ((double) (zMax - zMin));
    		
    		int verbNum = tuple[tuple.length-1];
    		
    		Sphere s = new Sphere(0.01f, Sphere.GENERATE_NORMALS, 
    				createAppearance(Util.getVerbColor(verbNum)));
    		//s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    		Transform3D translate = new Transform3D();
    		translate.setTranslation(new Vector3d(xTrans, yTrans, zTrans));
    		TransformGroup tg = new TransformGroup(translate);
    		tg.addChild(s);
    		t.addChild(tg);
    		//spheres.add(s);
    		spheres[verbNum].add(s);
    	}
    }
    
    public void makeInvisible (int verbNum)
    {
    	ArrayList<Sphere> a = spheres[verbNum];
    	for (Sphere s : a)	
    	{
    		s.getAppearance().getTransparencyAttributes().setTransparency(0.9f);
    	}
    }
    
    public void makeVisible (int verbNum)
    {
    	ArrayList<Sphere> a = spheres[verbNum];
    	for (Sphere s : a)	
    	{
    		s.getAppearance().getTransparencyAttributes().setTransparency(0.0f);
    	}
    }
    
    public void changeColor (int verbNum)
    {
    	ArrayList<Sphere> a = spheres[verbNum];
    	for (Sphere s : a)	
    	{
    		s.getAppearance().getMaterial().setDiffuseColor(Util.getVerbColor(verbNum));
    	}
    }
        
    /**
     * Adds labels to our axises
     */
    private void addLabels (TransformGroup t)
    {
    	//-------------CREATE FIRST AXIS----------------------
        //Shape3D text2d = new Text2D("2D text in Java 3D", white, "Helvetica", 24, Font.PLAIN);
        Shape3D text2d = new Text2D(xLabel, axisColor, LABEL_FONT, 
        		LABEL_FONT_SIZE, Font.PLAIN);
        text2d.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        
        Appearance textAppear = text2d.getAppearance();
        
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setBackFaceNormalFlip(true);
        textAppear.setPolygonAttributes(polyAttrib);
        
        TransformGroup textTranslationGroup;
        Transform3D translate = new Transform3D();
        Transform3D rotateX = new Transform3D();
    	Transform3D rotateY = new Transform3D();
    	Transform3D rotateZ = new Transform3D();

    	// Translate
        translate.setTranslation(new Vector3f(0f,0f,DEF_DIST + (DEF_DIST/3)));
        // Rotate X
        rotateX.rotX(-Math.PI / 2);
    	
        // Rotate Y
        rotateY.rotY(0);
        
        // Rotate Z
        rotateZ.rotZ(0);
        
        translate.mul(rotateX);
        translate.mul(rotateY);
        translate.mul(rotateZ);     
        
        textTranslationGroup = new TransformGroup(translate);
        textTranslationGroup.addChild(text2d);
        t.addChild(textTranslationGroup);
        
        //--------------CREATE SECOND AXIS--------------------------
        text2d = new Text2D(yLabel, axisColor, LABEL_FONT, LABEL_FONT_SIZE, 
        		Font.PLAIN);
        
        textAppear = text2d.getAppearance();
        
        polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setBackFaceNormalFlip(true);
        textAppear.setPolygonAttributes(polyAttrib);
        
        translate = new Transform3D();
        rotateX = new Transform3D();
    	rotateY = new Transform3D();
    	rotateZ = new Transform3D();

    	// Translate
        translate.setTranslation(new Vector3f(DEF_DIST,0f,0f));
        // Rotate X
        rotateX.rotX(0);
        // Rotate Y
        rotateY.rotY(0);
        // Rotate Z
        rotateZ.rotZ(Math.PI / 2);
        
        translate.mul(rotateX);
        translate.mul(rotateY);
        translate.mul(rotateZ);     
        
        textTranslationGroup = new TransformGroup(translate);
        
        // 2nd Translation
        translate = new Transform3D();
    	rotateY = new Transform3D();

    	// Translate
        translate.setTranslation(new Vector3f(0f,-DEF_DIST/3,0f));

        // Rotate Y
        rotateY.rotX(Math.PI / 4);

        rotateY.mul(translate);
        
        TransformGroup textTranslationGroup2 = new TransformGroup(rotateY);
        textTranslationGroup2.addChild(text2d);
        textTranslationGroup.addChild(textTranslationGroup2);
        
        t.addChild(textTranslationGroup);
        
        //--------------Also 2nd AXIS---------------------------
        text2d = new Text2D(yLabel, axisColor, LABEL_FONT, LABEL_FONT_SIZE, 
        		Font.PLAIN);
        
        textAppear = text2d.getAppearance();
        
        polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setBackFaceNormalFlip(true);
        textAppear.setPolygonAttributes(polyAttrib);

        translate = new Transform3D();
        rotateX = new Transform3D();
    	rotateY = new Transform3D();
    	rotateZ = new Transform3D();
        
    	// Translate
        translate.setTranslation(new Vector3f(0f,DEF_DIST,DEF_DIST));
        // Rotate X
        rotateX.rotX(-Math.PI / 2);
        // Rotate Y
        rotateY.rotY(Math.PI / 2);
        // Rotate Z
        rotateZ.rotZ(0);
        
        translate.mul(rotateX);
        translate.mul(rotateY);
        translate.mul(rotateZ);     
        
        textTranslationGroup = new TransformGroup(translate);
        
        // 2nd Translation
        translate = new Transform3D();
    	rotateY = new Transform3D();

    	// Translate
        translate.setTranslation(new Vector3f(0f,-DEF_DIST/3,0f));

        // Rotate Y
        rotateY.rotX(Math.PI / 4);

        rotateY.mul(translate);
        
        textTranslationGroup2 = new TransformGroup(rotateY);
        textTranslationGroup2.addChild(text2d);
        textTranslationGroup.addChild(textTranslationGroup2);
        
        t.addChild(textTranslationGroup);
        
        //-----------------CREATE THIRD AXIS-----------------------
        text2d = new Text2D(zLabel, axisColor, LABEL_FONT, LABEL_FONT_SIZE, 
        		Font.PLAIN);
        
        textAppear = text2d.getAppearance();
        
        polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setBackFaceNormalFlip(true);
        textAppear.setPolygonAttributes(polyAttrib);

        translate = new Transform3D();
        rotateX = new Transform3D();
    	rotateY = new Transform3D();
    	rotateZ = new Transform3D();
        
    	// Translate
        translate.setTranslation(new Vector3f(DEF_DIST+(DEF_DIST/3),0f,DEF_DIST));
        // Rotate X
        rotateX.rotX(-Math.PI / 2);
        // Rotate Y
        rotateY.rotY(0);
        // Rotate Z
        rotateZ.rotZ(Math.PI / 2);
        
        translate.mul(rotateX);
        translate.mul(rotateY);
        translate.mul(rotateZ);     
        
        textTranslationGroup = new TransformGroup(translate);
        textTranslationGroup.addChild(text2d);
        t.addChild(textTranslationGroup);
    }
    
    /**
     * Adds numbers to our tick marks
     */
    private void numberTicks (TransformGroup t, int numTicks)
    {
    	// Amount to increment by
    	float inc = DEF_DIST / numTicks;
    	
    	double xInc = (xMax - xMin) / numTicks;
    	double yInc = (yMax - yMin) / numTicks;
    	double zInc = (zMax - zMin) / numTicks;

    	
    	for (int i = 0; i <= numTicks; i++)
    	{
    		//--------------ZAXIS TICKS--------------------
    		double tickVal = zInc * i + zMin;
    		String sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		Shape3D text2d = new Text2D(sVal, axisColor, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
            Appearance textAppear = text2d.getAppearance();
            
            PolygonAttributes polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            polyAttrib.setBackFaceNormalFlip(true);
            textAppear.setPolygonAttributes(polyAttrib);
            
            TransformGroup textTranslationGroup;
            Transform3D translate = new Transform3D();
            Transform3D rotateX = new Transform3D();
        	Transform3D rotateY = new Transform3D();
        	Transform3D rotateZ = new Transform3D();

        	// Translate
            translate.setTranslation(new Vector3f(DEF_DIST+DEF_DIST/12,0f,
            		DEF_DIST/50+i*inc));
            // Rotate X
            rotateX.rotX(-Math.PI / 2);
        	
            // Rotate Y
            rotateY.rotY(0);
            
            // Rotate Z
            rotateZ.rotZ(0);
            
            translate.mul(rotateX);
            translate.mul(rotateY);
            translate.mul(rotateZ);     
            
            textTranslationGroup = new TransformGroup(translate);
            textTranslationGroup.addChild(text2d);
            t.addChild(textTranslationGroup);
            
            //-----------------XAXIS TICKS---------------------
            tickVal = xInc * i + xMin;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, axisColor, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
            textAppear = text2d.getAppearance();
            
            polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            polyAttrib.setBackFaceNormalFlip(true);
            textAppear.setPolygonAttributes(polyAttrib);
            
            translate = new Transform3D();
            rotateX = new Transform3D();
        	rotateY = new Transform3D();
        	rotateZ = new Transform3D();

        	// Translate
        	translate.setTranslation(new Vector3f(DEF_DIST/37+i*inc,0f,
        			DEF_DIST+DEF_DIST/5));
            // Rotate X
            rotateX.rotX(-Math.PI / 2);
        	
            // Rotate Y
            rotateY.rotY(0);
            
            // Rotate Z
            rotateZ.rotZ(Math.PI / 2);
            
            translate.mul(rotateX);
            translate.mul(rotateY);
            translate.mul(rotateZ);     
            
            textTranslationGroup = new TransformGroup(translate);
            textTranslationGroup.addChild(text2d);
            t.addChild(textTranslationGroup);
            
            //-------------YAXIS TICKS--------------------
            tickVal = yInc * i + yMin;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, axisColor, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
    		textAppear = text2d.getAppearance();
            
            polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            polyAttrib.setBackFaceNormalFlip(true);
            textAppear.setPolygonAttributes(polyAttrib);

            translate = new Transform3D();
            rotateX = new Transform3D();
        	rotateY = new Transform3D();
        	rotateZ = new Transform3D();
            
        	// Translate
            translate.setTranslation(new Vector3f(0f,-DEF_DIST/37+i*inc,DEF_DIST));
            // Rotate X
            rotateX.rotX(0);
            // Rotate Y
            rotateY.rotY(0);
            // Rotate Z
            rotateZ.rotZ(0);
            
            translate.mul(rotateX);
            translate.mul(rotateY);
            translate.mul(rotateZ);     
            
            textTranslationGroup = new TransformGroup(translate);
            
            // 2nd Translation
            translate = new Transform3D();
        	rotateY = new Transform3D();

        	// Translate
            translate.setTranslation(new Vector3f(-DEF_DIST/5,0f,0f));

            // Rotate Y
            rotateY.rotY(Math.PI/4);

            rotateY.mul(translate);
            
            TransformGroup textTranslationGroup2 = new TransformGroup(rotateY);
            textTranslationGroup2.addChild(text2d);
            textTranslationGroup.addChild(textTranslationGroup2);
            
            t.addChild(textTranslationGroup);
            
            //-------------OTHER YAXIS TICKS--------------------
            tickVal = yInc * i + yMin;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, axisColor, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
    		textAppear = text2d.getAppearance();
            
            polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            polyAttrib.setBackFaceNormalFlip(true);
            textAppear.setPolygonAttributes(polyAttrib);

            translate = new Transform3D();
            rotateX = new Transform3D();
        	rotateY = new Transform3D();
        	rotateZ = new Transform3D();
            
        	// Translate
            translate.setTranslation(new Vector3f(DEF_DIST,-DEF_DIST/37+i*inc,0f));
            // Rotate X
            rotateX.rotX(0);
            // Rotate Y
            rotateY.rotY(0);
            // Rotate Z
            rotateZ.rotZ(0);
            
            translate.mul(rotateX);
            translate.mul(rotateY);
            translate.mul(rotateZ);     
            
            textTranslationGroup = new TransformGroup(translate);
            
            // 2nd Translation
            translate = new Transform3D();
        	rotateY = new Transform3D();

        	// Translate
            translate.setTranslation(new Vector3f(DEF_DIST/12,0f,0f));

            // Rotate Y
            rotateY.rotY(Math.PI/4);

            rotateY.mul(translate);
            
            textTranslationGroup2 = new TransformGroup(rotateY);
            textTranslationGroup2.addChild(text2d);
            textTranslationGroup.addChild(textTranslationGroup2);
            
            t.addChild(textTranslationGroup);
    	}
    }
    
    /**
     * Adds grey gridlines to fill in the diagram
     * @param t
     * @param numLines
     */
    private void addGridlines (TransformGroup t, int numLines)
    {
    	// Amount to increment by
    	float inc = DEF_DIST / numLines;
    	
    	// Creates lines for the XY plane
        for (int i = 0; i < numLines + 1; i++)
        {
	        LineArray a = createGridLine(new Point3f (inc * i, 0.0f, 0.0f),
	        		new Point3f(inc * i, DEF_DIST, 0.0f), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, 0.0f, inc*i),
	        		new Point3f(0.0f, DEF_DIST, inc*i), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        
	        a = createGridLine(new Point3f (0.0f, inc*i, 0.0f),
	        		new Point3f(DEF_DIST, inc*i, 0.0f), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        // Add sloped tick marks
	        a = createGridLine(new Point3f (DEF_DIST, inc*i, 0.0f),
	        		new Point3f(DEF_DIST+(DEF_DIST/30), inc*i, -(DEF_DIST/30)), axisColor);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, 0.0f, inc*i),
	        		new Point3f(DEF_DIST, 0.0f, inc*i), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        // Add straight tick marks
	        a = createGridLine(new Point3f (DEF_DIST, 0.0f, inc*i),
	        		new Point3f(DEF_DIST+(DEF_DIST/25f), 0.0f, inc*i), axisColor);
	        t.addChild(new Shape3D(a));
	        
	        a = createGridLine(new Point3f (inc * i, 0.0f, 0.0f),
	        		new Point3f(inc * i, 0.0f, DEF_DIST), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        // Add straight tick marks
	        a = createGridLine(new Point3f (inc * i, 0.0f, DEF_DIST),
	        		new Point3f(inc * i, 0.0f, DEF_DIST+(DEF_DIST/25)), axisColor);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, inc*i, 0.0f),
	        		new Point3f(0.0f, inc*i, DEF_DIST), i == 0 || i == numLines ? axisColor : gridColor);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, inc*i, DEF_DIST),
	        		new Point3f(-(DEF_DIST/30), inc*i, DEF_DIST+(DEF_DIST/30)), axisColor);
	        t.addChild(new Shape3D(a));
        }
        
        numberTicks(t, numLines);
    }
    
    /**
     * Creates a line from the starting and ending points specified
     * @param start
     * @param end
     * @return
     */
    private LineArray createGridLine (Point3f start, Point3f end, Color3f col)
    {
    	LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la.setCoordinate(0, start);
        la.setCoordinate(1, end);
        la.setColor(0, col);
        la.setColor(1, col);
        
        return la;
    }
} 

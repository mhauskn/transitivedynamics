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

import java.util.ArrayList;


public class Graph3d extends JPanel { 
	
	private static final long serialVersionUID = 1L;

	private static final float DEF_DIST = 1.0f;
	
	// Number of gridlines between major axies
	private int NUM_GRIDLINES = 5;
	
	// ArrayList to hold each of our datapoints
	private ArrayList<Vector3d> dataPoints = new ArrayList<Vector3d>();
	
	// ArrayList to hold the coloring of each verb
	private ArrayList<Color3f> verbColors = new ArrayList<Color3f>();
	
	// Labels for each of our Axies
	private String X_LABEL = "X-Axis";
	private String Y_LABEL = "Y-Axis";
	private String Z_LABEL = "Z-Axis";
	
	// Min and max X Values
	private double X_MIN = 0.0;
	private double X_MAX = 1.0;
	
	// Min and max Y Values
	private double Y_MIN = 0.0;
	private double Y_MAX = 1.0;
	
	// Min and max Z Values
	private double Z_MIN = 0.0;
	private double Z_MAX = 1.0;
	
	private static final String LABEL_FONT = "Serif";
	private static final int LABEL_FONT_SIZE = 24;
	
	private static final String TICK_FONT = "Helvetica";
	private static final int TICK_FONT_SIZE = 12;
	
	private static final Color3f white  = new Color3f(1.0f, 1.0f, 1.0f);
	private static final Color3f black  = new Color3f(0.0f, 0.0f, 0.0f);
	private static final Color3f grey  = new Color3f(0.5f, 0.5f, 0.5f);
	
	private static final Color3f BACKGROUND_COLOR = white;
	private static final Color3f AXIS_COLOR = black;
	private static final Color3f GRID_COLOR = grey;


    Appearance createPointAppearance ()
    {
    	Appearance points_appear = new Appearance();
    	Material material = new Material();
        //material.setDiffuseColor(0.0f, 0.0f, 1.0f);
        material.setShininess(50.0f);
        // make modifications to default material properties
        points_appear.setMaterial(material);
    	
        ColoringAttributes points_coloring = new ColoringAttributes();
        
        points_coloring.setColor(1.0f, 0.0f, 0.0f);
        points_appear.setColoringAttributes(points_coloring);
        
        PointAttributes points_points = new PointAttributes(10.0f, true);
        points_appear.setPointAttributes(points_points);
        
        return points_appear;
    }

    //TODO: Remove duplicate graph function
    // Create a simple scene and attach it to the virtual universe
    public Graph3d () 
    {
    	//super("Explore Visualization");
    	
    	//setSize(512,512);
    	//this.setMinimumSize(new Dimension(500,500));
    	setPreferredSize(new Dimension(512, 512));
        setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(BorderFactory.createEtchedBorder());

    	
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        //add("Center", canvas3D);
        //this.getContentPane().add(canvas3D);
        this.add(canvas3D);
        
        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        setVisible(true);
    }
    
    public void graph ()
    {
    	setSize(512,512);
    	
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        //add("Center", canvas3D);
        //this.getContentPane().add(canvas3D);
        
        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        setVisible(true);
    }
    
    public void setXAxisName (String s)
    {
    	X_LABEL = s;
    }
    
    public void setYAxisName (String s)
    {
    	Y_LABEL = s;
    }
    
    public void setZAxisName (String s)
    {
    	Z_LABEL = s;
    }
    
    public void setNumGridlines (int n)
    {
    	NUM_GRIDLINES = n;
    }
    
    public void setXMinMax (double xMin, double xMax)
    {
    	X_MIN = xMin;
    	X_MAX = xMax;
    }
    
    public void setYMinMax (double yMin, double yMax)
    {
    	Y_MIN = yMin;
    	Y_MAX = yMax;
    }

    public void setZMinMax (double zMin, double zMax)
    {
    	Z_MIN = zMin;
    	Z_MAX = zMax;
    }
    
    /**
     * Sets the graph3d's points equal to the specified points and also 
     * colors the points according to the verb specification given.
     * @param points
     * @param verbs
     */
    public void setPoints (ArrayList<Vector3d> points, 
    		ArrayList<Color3f> concVerbs)
    {
    	dataPoints = points;
    	verbColors = concVerbs;
    }
    
//---------------------PRIVATE METHODS-------------------------
    
    /**
     * Creates the scene graph for our grapher
     */
    private BranchGroup createSceneGraph() {
    	// Create the root of the branch graph
    	BranchGroup objRoot = new BranchGroup();

        TransformGroup objTransform = new TransformGroup();
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
    	TransformGroup objRotate = new TransformGroup(rotate);
    	objTransform.addChild(objRotate);
    	
    	// Create background of specified color
    	BoundingSphere boundingSphere = 
            new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Background background = 
            new Background(BACKGROUND_COLOR);    
        background.setApplicationBounds(boundingSphere);
        objRotate.addChild(background);

    	
    	addGridlines(objRotate, NUM_GRIDLINES); // Add the gridlines to the model.
        
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
        objRoot.compile();

        return objRoot;
    }
    
    /**
     * Creates the appearance for our points
     */
    private Appearance createAppearance(Color3f diffuseColor) {
        Appearance appear = new Appearance();
        Material material = new Material();
        
        material.setDiffuseColor(diffuseColor);
        
        material.setShininess(50.0f);
        // make modifications to default material properties
        appear.setMaterial(material);

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
    	for (int i = 0; i < dataPoints.size(); i++)
    	{
    		Sphere s = new Sphere(0.01f, Sphere.GENERATE_NORMALS, 
    				createAppearance(verbColors.get(i)));
    		Transform3D translate = new Transform3D();
    		translate.setTranslation(dataPoints.get(i));
    		TransformGroup tg = new TransformGroup(translate);
    		tg.addChild(s);
    		t.addChild(tg);
    	}
    }
        
    /**
     * Adds labels to our axises
     */
    private void addLabels (TransformGroup t)
    {
    	//-------------CREATE FIRST AXIS----------------------
        //Shape3D text2d = new Text2D("2D text in Java 3D", white, "Helvetica", 24, Font.PLAIN);
        Shape3D text2d = new Text2D(X_LABEL, AXIS_COLOR, LABEL_FONT, 
        		LABEL_FONT_SIZE, Font.PLAIN);
        
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
        text2d = new Text2D(Y_LABEL, AXIS_COLOR, LABEL_FONT, LABEL_FONT_SIZE, 
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
        text2d = new Text2D(Y_LABEL, AXIS_COLOR, LABEL_FONT, LABEL_FONT_SIZE, 
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
        text2d = new Text2D(Z_LABEL, AXIS_COLOR, LABEL_FONT, LABEL_FONT_SIZE, 
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
    	
    	double xInc = (X_MAX - X_MIN) / numTicks;
    	double yInc = (Y_MAX - Y_MIN) / numTicks;
    	double zInc = (Z_MAX - Z_MIN) / numTicks;

    	
    	for (int i = 0; i <= numTicks; i++)
    	{
    		//--------------ZAXIS TICKS--------------------
    		double tickVal = zInc * i + Z_MIN;
    		String sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		Shape3D text2d = new Text2D(sVal, AXIS_COLOR, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
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
            tickVal = xInc * i + X_MIN;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, AXIS_COLOR, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
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
            tickVal = yInc * i + Y_MIN;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, AXIS_COLOR, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
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
            tickVal = yInc * i + Y_MIN;
    		sVal = Double.toString(tickVal);
    		if (sVal.length() > 4) sVal = sVal.substring(0,5);
    		text2d = new Text2D(sVal, AXIS_COLOR, TICK_FONT, TICK_FONT_SIZE, Font.BOLD);
            
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
	        		new Point3f(inc * i, DEF_DIST, 0.0f), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, 0.0f, inc*i),
	        		new Point3f(0.0f, DEF_DIST, inc*i), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        
	        a = createGridLine(new Point3f (0.0f, inc*i, 0.0f),
	        		new Point3f(DEF_DIST, inc*i, 0.0f), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        // Add sloped tick marks
	        a = createGridLine(new Point3f (DEF_DIST, inc*i, 0.0f),
	        		new Point3f(DEF_DIST+(DEF_DIST/30), inc*i, -(DEF_DIST/30)), AXIS_COLOR);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, 0.0f, inc*i),
	        		new Point3f(DEF_DIST, 0.0f, inc*i), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        // Add straight tick marks
	        a = createGridLine(new Point3f (DEF_DIST, 0.0f, inc*i),
	        		new Point3f(DEF_DIST+(DEF_DIST/25f), 0.0f, inc*i), AXIS_COLOR);
	        t.addChild(new Shape3D(a));
	        
	        a = createGridLine(new Point3f (inc * i, 0.0f, 0.0f),
	        		new Point3f(inc * i, 0.0f, DEF_DIST), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        // Add straight tick marks
	        a = createGridLine(new Point3f (inc * i, 0.0f, DEF_DIST),
	        		new Point3f(inc * i, 0.0f, DEF_DIST+(DEF_DIST/25)), AXIS_COLOR);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, inc*i, 0.0f),
	        		new Point3f(0.0f, inc*i, DEF_DIST), i == 0 || i == numLines ? AXIS_COLOR : GRID_COLOR);
	        t.addChild(new Shape3D(a));
	        a = createGridLine(new Point3f (0.0f, inc*i, DEF_DIST),
	        		new Point3f(-(DEF_DIST/30), inc*i, DEF_DIST+(DEF_DIST/30)), AXIS_COLOR);
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

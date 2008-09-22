package gui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;

import plotter.Graph3d;
import plotter.GraphData;
import plotter.GraphOptions;

public class VisualizeWindow extends JFrame implements ActionListener 
{	
	private static final long serialVersionUID = -4113871651262388325L;

	/**
	 * This menu is disabled by the explorer when the exploration
	 * starts
	 */
	private JMenuBar menuBar;
	
	/**
	 * The main Container Panel holding the panels
	 */
	private ContainerPanel cPanel;
	
	private Container pane;
	
	private JPanel mainPane;
	
	private Graph3d g;
	
	private GraphOptions options;
	
	/**
	 * Creates a new explore window
	 * @param x The XPosition of the window
	 * @param y The YPosition of the window
	 * @param Panel The Container Panel Model
	 * @param menu The menu of the main window
	 */
	public VisualizeWindow(int x, int y, ContainerPanel Panel, JMenuBar menu) {
		super("Visualize Window");
		menuBar = menu;
		cPanel = Panel;
		initializeGUI(x, y);
	}
	
	public void actionPerformed(ActionEvent e) {}
	
	/**
	 * Sets up and initializes the window.
	 */
	private void initializeGUI(int x, int y) {
		pane = getContentPane();
		setLocation(x,y);
		
		mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.LINE_AXIS));
        
        mainPane.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //mainPane.setBorder(BorderFactory.createEtchedBorder());//(1,1,1,1));
       	//mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		g = new Graph3d();
		g.graph();
		
		mainPane.add(g);
		
		// Add separator between graph and controls
		mainPane.add(Box.createRigidArea(new Dimension(5, super.getHeight())));
		
		//mainPane.add(createPlotterOptions());
		options = new GraphOptions(cPanel, g, menuBar, this);
		mainPane.add(options);
		
		mainPane.add(Box.createGlue());
				
		pane.add(mainPane);
		
		pack();
		setVisible(true);
	}
	
	/**
	 * Removes the current graph and creates another one.
	 * We do not modify the current graph because it is 
	 * already compiled and would be very slow if we did 
	 * not compile and allowed all objects to change. 
	 */
	public void resetGraph ()
	{
		GraphData gd = g.exportGraphData();
		mainPane.remove(0);
		
		Graph3d g2 = new Graph3d();
		g2.importGraphData(gd);
		g2.graph();
		
		mainPane.add(g2, 0);
		
		g = g2;
		options.setGraph(g);

		setVisible(true);
	}
	
	public Graph3d getGraph ()
	{
		return g;
	}
}

package gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;

import plotter.Graph3d;
import plotter.GraphData;
import plotter.GraphOptions;

public class VisualizeWindow extends JFrame implements ActionListener,
	WindowListener
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
			
	private JSplitPane splitPane;
	
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
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		Container pane = getContentPane();
		setLocation(x,y);        

		g = new Graph3d();
		g.graph();
		
		options = new GraphOptions(cPanel, g, menuBar, this);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				g, options);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(512);
		splitPane.setPreferredSize(new Dimension(768,512));
		
		pane.add(splitPane);
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
		int dividerLoc =  splitPane.getDividerLocation();
		GraphData gd = g.exportGraphData();
		cleanGraphRef();
		
		g = new Graph3d();
		g.importGraphData(gd);
		g.graph();

		splitPane.setLeftComponent(g);
		splitPane.setDividerLocation(dividerLoc);
						
		options.setGraph(g);

		setVisible(true);
	}
	
	/**
	 * Removes all references to our Graph3d Object
	 */
	private void cleanGraphRef ()
	{
		g.cleanMemRef();
		g.removeAll();
		options.removeGraph();
		splitPane.remove(g);
		g = null;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		cleanGraphRef();
		splitPane.removeAll();
		options.removeAll();
		this.getContentPane().removeAll();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
	
	public Graph3d getGraph ()
	{
		return g;
	}
}

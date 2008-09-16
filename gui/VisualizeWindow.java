package gui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;

import plotter.Graph3d;
import plotter.GraphOptions;

public class VisualizeWindow extends JFrame implements ActionListener 
{	
	/**
	 * This menu is disabled by the explorer when the exploration
	 * starts
	 */
	private JMenuBar menuBar;
	
	/**
	 * The main Container Panel holding the panels
	 */
	private ContainerPanel cPanel;
	
	private JPanel mainPane;
	
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
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	/**
	 * Sets up and initializes the window.
	 */
	private void initializeGUI(int x, int y) {
		Container pane = getContentPane();
		setLocation(x,y);
		
		mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.LINE_AXIS));
        
        mainPane.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //mainPane.setBorder(BorderFactory.createEtchedBorder());//(1,1,1,1));
       	//mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Graph3d g = new Graph3d();
		
		mainPane.add(g);
		
		// Add separator between graph and controls
		mainPane.add(Box.createRigidArea(new Dimension(5, super.getHeight())));
		
		//mainPane.add(createPlotterOptions());
		mainPane.add(new GraphOptions());
		
		mainPane.add(Box.createGlue());
				
		pane.add(mainPane);
		
		pack();
		setVisible(true);
	}
}

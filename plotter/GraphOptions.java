package plotter;

import gui.ContainerPanel;
import gui.Explorer;
import gui.VisualizeWindow;

import javax.swing.*;

import util.Util;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;

/**
 * Graph Options contains the code for all of the individual 
 * controls to the right of the Graph3d model.
 * @author el
 *
 */
public class GraphOptions extends JPanel implements ActionListener, FocusListener
{
	private static final long serialVersionUID = -4765988404040163814L;
	
	public static final String CHANGE_CAUSE_COLOR 		= "CHANGE_CAUSE_COLOR";
	public static final String CHANGE_PREVENTS_COLOR 	= "CHANGE_PREVENTS_COLOR";
	public static final String CHANGE_ALLOWS_COLOR 		= "CHANGE_ALLOWS_COLOR";
	public static final String CHANGE_HELPS_COLOR 		= "CHANGE_HELPS_COLOR";
	public static final String CHANGE_DESPITE_COLOR 	= "CHANGE_DESPITE_COLOR";
	public static final String CHANGE_INVALID_COLOR 	= "CHANGE_INVALID_COLOR";
	public static final String VIZ_COMMAND 				= "VISUALIZE_MODEL";
	public static final String TOGGLE_COLOR_COMMAND 	= "TOGGLE_COLOR";
	
	public static final String CAUSE_SELECTOR 		= "DISPLAY_CAUSE";
	public static final String PREVENTS_SELECTOR 	= "DISPLAY_PREVENTS";
	public static final String ALLOWS_SELECTOR 		= "DISPLAY_ALLOWS";
	public static final String HELPS_SELECTOR 		= "DISPLAY_HELPS";
	public static final String DESPITE_SELECTOR 	= "DISPLAY_DESPITE";
	public static final String INVALID_SELECTOR 	= "DISPLAY_INVALID";

	
	private JButton changeCauseColor;
	private JButton changePreventColor;
	private JButton changeAllowColor;
	private JButton changeHelpColor;
	private JButton changeDespiteColor;
	private JButton changeInvalidColor;
	
	JCheckBox causeSelector;
	JCheckBox allowSelector;
	JCheckBox preventSelector;
	JCheckBox helpSelector;
	JCheckBox despiteSelector;
	JCheckBox invalidSelector;
	
	private JComboBox xCombo;
	private JComboBox yCombo;
	private JComboBox zCombo;
	
	JSlider densitySlider;
	
	private static final int DEN_MIN = 0;
	private static final int DEN_MAX = 100;
	private static final int DEN_INIT = 50;
	
	private static final int eMagMax = 20;
	private static final int eMagMin = 5;
	
	private ContainerPanel cPanel;
	private Graph3d graph;
	private JMenuBar menuBar;
	private VisualizeWindow viz;
	
	public GraphOptions (ContainerPanel _cPanel, Graph3d _graph, 
			JMenuBar _menuBar, VisualizeWindow v)
	{
		cPanel 	= _cPanel;
		graph 	= _graph;
		menuBar = _menuBar;
		viz = v;
		
		setPreferredSize(new Dimension(256, 512));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEtchedBorder());

		/*setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(myTitle),
                BorderFactory.createEmptyBorder(5,5,5,5)));*/
		
		//setBorder(BorderFactory.createTitledBorder(myTitle));
		
		add(addModelOptions());
		add(addResultSelectors());
		add(addAxisOptions());
		add(addColorOptions());
		//add(Box.createRigidArea(new Dimension(10,100)));
	}
	
	public void actionPerformed (ActionEvent e)
	{
		String command = e.getActionCommand();
		
		if (command.equals(VIZ_COMMAND)) {
			Explorer exp = new Explorer(cPanel, menuBar);
			exp.setRetainData(true);
			exp.setEMag(getEMagFromSlider());
			try { exp.explore(); } catch(IOException x) {};
			graph.setPoints(exp.getRawData());
			graph.setEMagMinMax(exp.eZero);
			graph.setXAxisName(xCombo.getSelectedItem().toString());
			graph.setYAxisName(yCombo.getSelectedItem().toString());
			graph.setZAxisName(zCombo.getSelectedItem().toString());
			graph.setIndicies(xCombo.getSelectedIndex()-1, 
					yCombo.getSelectedIndex()-1,
					zCombo.getSelectedIndex()-1);
			viz.resetGraph();
		}
		else if (command.equals(TOGGLE_COLOR_COMMAND)) {
			graph.toggleBackgroundColors();
			viz.resetGraph();
		}
		else if (command.equals(CHANGE_ALLOWS_COLOR)) {
	        new ColorChanger("Choose Allow Color", Util.ALLOWS, this, viz);
		}
		else if (command.equals(CHANGE_CAUSE_COLOR)) {
			new ColorChanger("Choose Cause Color", Util.CAUSES, this, viz);
		}
		else if (command.equals(CHANGE_PREVENTS_COLOR)) {
			new ColorChanger("Choose Prevent Color", Util.PREVENTS, this, viz);
		}
		else if (command.equals(CHANGE_HELPS_COLOR)) {
			new ColorChanger("Choose Help Color", Util.HELPS, this, viz);
		}
		else if (command.equals(CHANGE_DESPITE_COLOR)) {
			new ColorChanger("Choose Despite Color", Util.DESPITE, this, viz);
		}
		else if (command.equals(CHANGE_INVALID_COLOR)) {
			new ColorChanger("Choose Invalid Color", Util.INVALID, this, viz);
		}
		else if (command.equals(CAUSE_SELECTOR)) {
			if (causeSelector.isSelected())
				graph.makeVisible(Util.CAUSES);
			else
				graph.makeInvisible(Util.CAUSES);
		}
		else if (command.equals(PREVENTS_SELECTOR)) {
			if (preventSelector.isSelected())
				graph.makeVisible(Util.PREVENTS);
			else
				graph.makeInvisible(Util.PREVENTS);
		}
		else if (command.equals(ALLOWS_SELECTOR)) {
			if (allowSelector.isSelected())
				graph.makeVisible(Util.ALLOWS);
			else
				graph.makeInvisible(Util.ALLOWS);
		}
		else if (command.equals(HELPS_SELECTOR)) {
			if (helpSelector.isSelected())
				graph.makeVisible(Util.HELPS);
			else
				graph.makeInvisible(Util.HELPS);
		}
		else if (command.equals(DESPITE_SELECTOR)) {
			if (despiteSelector.isSelected())
				graph.makeVisible(Util.DESPITE);
			else
				graph.makeInvisible(Util.DESPITE);
		}
		else if (command.equals(INVALID_SELECTOR)) {
			if (invalidSelector.isSelected())
				graph.makeVisible(Util.INVALID);
			else
				graph.makeInvisible(Util.INVALID);
		}
	}
	
	public void focusGained (FocusEvent e)
	{
		int selX = xCombo.getSelectedIndex();
		int selY = yCombo.getSelectedIndex();
		int selZ = zCombo.getSelectedIndex();
		
		xCombo.removeAllItems();
		yCombo.removeAllItems();
		zCombo.removeAllItems();
		
		String[] vecNames = cPanel.getVecNames();
		for (String s : vecNames)
		{
			xCombo.addItem(s);
			yCombo.addItem(s);
			zCombo.addItem(s);
		}
		xCombo.setSelectedIndex(selX);
		yCombo.setSelectedIndex(selY);
		zCombo.setSelectedIndex(selZ);
	}
	
	public void focusLost (FocusEvent e) {}
		
	public void updateColorKey ()
	{
		changeCauseColor.setBackground(Util.toColor(Util.CAUSES_COLOR));
		changePreventColor.setBackground(Util.toColor(Util.PREVENTS_COLOR));
		changeAllowColor.setBackground(Util.toColor(Util.ALLOWS_COLOR));
		changeHelpColor.setBackground(Util.toColor(Util.HELPS_COLOR));
		changeDespiteColor.setBackground(Util.toColor(Util.DESPITE_COLOR));
		changeInvalidColor.setBackground(Util.toColor(Util.INVALID_COLOR));
	}
	
	public void setGraph (Graph3d _graph)
	{
		graph = _graph;
	}
	
	private int getEMagFromSlider ()
	{
		int mag = densitySlider.getValue();
		double magD = (mag - DEN_MIN) / (double)(DEN_MAX - DEN_MIN);
		double newD = magD * eMagMax + eMagMin;
		
		return (int) Math.round(newD);
	}
	
	private JPanel addModelOptions ()
	{
		String title = "Model";
		
		JPanel modelPane = new JPanel();
		modelPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		//modelPane.setPreferredSize(new Dimension(150,350));
		
		modelPane.setLayout(new BoxLayout(modelPane, BoxLayout.PAGE_AXIS));
		
		JButton vizButton = new JButton("Visualize Current Model");
		vizButton.setActionCommand(VIZ_COMMAND);
		vizButton.addActionListener(this);
		vizButton.setHorizontalAlignment(JLabel.CENTER);
		
		JButton toggleButton = new JButton("Toggle Background Colors");
		toggleButton.setActionCommand(TOGGLE_COLOR_COMMAND);
		toggleButton.addActionListener(this);
		
		densitySlider = new JSlider(JSlider.HORIZONTAL,
				DEN_MIN, DEN_MAX, DEN_INIT);
		
		modelPane.add(vizButton);
		modelPane.add(Box.createRigidArea(new Dimension(10,10)));
		modelPane.add(toggleButton);
		modelPane.add(Box.createRigidArea(new Dimension(10,10)));
		modelPane.add(new JLabel("Density of Model", JLabel.CENTER));
		modelPane.add(densitySlider);
		
		return modelPane;
	}
	
	private JPanel addResultSelectors ()
	{
		String title = "Result Verb Selector";
		
		JPanel selPane = new JPanel();
		selPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		//selPane.setPreferredSize(new Dimension(150,350));
		
		selPane.setLayout(new GridLayout(3,2));
		
		causeSelector = new JCheckBox("Cause");
		causeSelector.setSelected(true);
		causeSelector.setActionCommand(CAUSE_SELECTOR);
		causeSelector.addActionListener(this);
		
		allowSelector = new JCheckBox("Allow");
		allowSelector.setSelected(true);
		allowSelector.setActionCommand(ALLOWS_SELECTOR);
		allowSelector.addActionListener(this);
		
		preventSelector = new JCheckBox("Prevent");
		preventSelector.setSelected(true);
		preventSelector.setActionCommand(PREVENTS_SELECTOR);
		preventSelector.addActionListener(this);
		
		despiteSelector = new JCheckBox("Despite");
		despiteSelector.setSelected(true);
		despiteSelector.setActionCommand(DESPITE_SELECTOR);
		despiteSelector.addActionListener(this);
		
		helpSelector = new JCheckBox("Help");
		helpSelector.setSelected(true);
		helpSelector.setActionCommand(HELPS_SELECTOR);
		helpSelector.addActionListener(this);
		
		invalidSelector = new JCheckBox("Invalid");
		invalidSelector.setSelected(true);
		invalidSelector.setActionCommand(INVALID_SELECTOR);
		invalidSelector.addActionListener(this);
		
		selPane.add(causeSelector);
		selPane.add(allowSelector);
		selPane.add(preventSelector);
		selPane.add(despiteSelector);
		selPane.add(helpSelector);
		selPane.add(invalidSelector);
		
		return selPane;
	}
	
	private JPanel addAxisOptions ()
	{
		String title = "Axis Options";
		
		JPanel axisPane = new JPanel();
		axisPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		
		axisPane.setLayout(new BoxLayout(axisPane, BoxLayout.PAGE_AXIS));
		
		String[] vecs = cPanel.getVecNames();
				
		JLabel xLabel = new JLabel("X-Axis:");
		xCombo = new JComboBox(vecs);
		xCombo.setSelectedIndex(1);
		xCombo.addFocusListener(this);
		
		JLabel yLabel = new JLabel("Y-Axis:");
		yCombo = new JComboBox(vecs);
		yCombo.setSelectedIndex(2);
		yCombo.addFocusListener(this);
		
		JLabel zLabel = new JLabel("Z-Axis:");
		zCombo = new JComboBox(vecs);
		zCombo.setSelectedIndex(4);
		zCombo.addFocusListener(this);
		
		
		JPanel x = new JPanel();
		x.setLayout(new BoxLayout(x, BoxLayout.LINE_AXIS));
		x.add(Box.createRigidArea(new Dimension(10,10)));
		x.add(xLabel);
		x.add(Box.createRigidArea(new Dimension(10,10)));
		x.add(xCombo);
		x.add(Box.createRigidArea(new Dimension(10,10)));
		
		JPanel y = new JPanel();
		y.setLayout(new BoxLayout(y, BoxLayout.LINE_AXIS));
		y.add(Box.createRigidArea(new Dimension(10,10)));
		y.add(yLabel);
		y.add(Box.createRigidArea(new Dimension(10,10)));
		y.add(yCombo);
		y.add(Box.createRigidArea(new Dimension(10,10)));
		
		JPanel z = new JPanel();
		z.setLayout(new BoxLayout(z, BoxLayout.LINE_AXIS));
		z.add(Box.createRigidArea(new Dimension(10,10)));
		z.add(zLabel);
		z.add(Box.createRigidArea(new Dimension(10,10)));
		z.add(zCombo);
		z.add(Box.createRigidArea(new Dimension(10,10)));
		
		axisPane.add(x);
		axisPane.add(Box.createRigidArea(new Dimension(10,10)));
		axisPane.add(y);
		axisPane.add(Box.createRigidArea(new Dimension(10,10)));
		axisPane.add(z);
		
		return axisPane;
	}
	
	
	
	/**
	 * Creates the color key and color chooser buttons
	 * @return
	 */
	public JPanel addColorOptions ()
	{
		String title = "Color Key";
		
		JPanel modelPane = new JPanel();
		modelPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		
		modelPane.setLayout(new GridLayout(3,2));
		
		changeCauseColor = new JButton("Cause");
		changeCauseColor.setBackground(Util.toColor(Util.CAUSES_COLOR));
		changeCauseColor.setActionCommand(CHANGE_CAUSE_COLOR);
		changeCauseColor.setToolTipText("Change Cause Color");
		changeCauseColor.addActionListener(this);
		
		changePreventColor = new JButton("Prevent");
		changePreventColor.setBackground(Util.toColor(Util.PREVENTS_COLOR));
		changePreventColor.setActionCommand(CHANGE_PREVENTS_COLOR);
		changePreventColor.setToolTipText("Change Prevent Color");
		changePreventColor.addActionListener(this);
		
		changeAllowColor = new JButton("Allow");
		changeAllowColor.setBackground(Util.toColor(Util.ALLOWS_COLOR));
		changeAllowColor.setActionCommand(CHANGE_ALLOWS_COLOR);
		changeAllowColor.setToolTipText("Change Allow Color");
		changeAllowColor.addActionListener(this);
		
		changeHelpColor = new JButton("Help");
		changeHelpColor.setBackground(Util.toColor(Util.HELPS_COLOR));
		changeHelpColor.setActionCommand(CHANGE_HELPS_COLOR);
		changeHelpColor.setToolTipText("Change Help Color");
		changeHelpColor.addActionListener(this);
		
		changeDespiteColor = new JButton("Despite");
		changeDespiteColor.setBackground(Util.toColor(Util.DESPITE_COLOR));
		changeDespiteColor.setActionCommand(CHANGE_DESPITE_COLOR);
		changeDespiteColor.setToolTipText("Change Despite Color");
		changeDespiteColor.addActionListener(this);
		
		changeInvalidColor = new JButton("Invalid");
		changeInvalidColor.setBackground(Util.toColor(Util.INVALID_COLOR));
		changeInvalidColor.setActionCommand(CHANGE_INVALID_COLOR);
		changeInvalidColor.setToolTipText("Change Invalid Color");
		changeInvalidColor.addActionListener(this);
		
		modelPane.add(changeCauseColor);
		modelPane.add(changeAllowColor);
		modelPane.add(changePreventColor);
		modelPane.add(changeDespiteColor);
		modelPane.add(changeHelpColor);
		modelPane.add(changeInvalidColor);
		
		return modelPane;
	}
}

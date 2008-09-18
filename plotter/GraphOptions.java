package plotter;

import javax.swing.*;

import util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

public class GraphOptions extends JPanel implements ActionListener
{
	private String myTitle = "Graph Options";
	
	public static final String CHANGE_CAUSE_COLOR = "CHANGE_CAUSE_COLOR";
	public static final String CHANGE_PREVENTS_COLOR = "CHANGE_PREVENTS_COLOR";
	public static final String CHANGE_ALLOWS_COLOR = "CHANGE_ALLOWS_COLOR";
	public static final String CHANGE_HELPS_COLOR = "CHANGE_HELPS_COLOR";
	public static final String CHANGE_DESPITE_COLOR = "CHANGE_DESPITE_COLOR";
	public static final String CHANGE_INVALID_COLOR = "CHANGE_INVALID_COLOR";
	
	private JButton changeCauseColor;
	private JButton changePreventColor;
	private JButton changeAllowColor;
	private JButton changeHelpColor;
	private JButton changeDespiteColor;
	private JButton changeInvalidColor;
	
	private static final int DEN_MIN = 0;
	private static final int DEN_MAX = 100;
	private static final int DEN_INIT = 50;
	
	public GraphOptions ()
	{
		
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
		
		if (command.equals(CHANGE_ALLOWS_COLOR))
		{
			JFrame frame = new JFrame("Choose Allow Color");

	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
	
	        //Create and set up the content pane.
	        JComponent newContentPane = new ColorChanger("Color Palette");
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);
	
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
		}
	}
	
	public void updateColorKey ()
	{
		changeCauseColor.setBackground(Util.toColor(Util.CAUSES_COLOR));
		changePreventColor.setBackground(Util.toColor(Util.PREVENTS_COLOR));
		changeAllowColor.setBackground(Util.toColor(Util.ALLOWS_COLOR));
		changeHelpColor.setBackground(Util.toColor(Util.HELPS_COLOR));
		changeDespiteColor.setBackground(Util.toColor(Util.DESPITE_COLOR));
		changeInvalidColor.setBackground(Util.toColor(Util.INVALID_COLOR));
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
		
		selPane.add(new JCheckBox("Cause"));
		selPane.add(new JCheckBox("Allow"));
		selPane.add(new JCheckBox("Prevent"));
		selPane.add(new JCheckBox("Despite"));
		selPane.add(new JCheckBox("Help"));
		selPane.add(new JCheckBox("Invalid"));
		
		return selPane;
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
		vizButton.setHorizontalAlignment(JLabel.CENTER);
		
		JButton toggleButton = new JButton("Toggle Background Colors");
		
		JSlider densitySlider = new JSlider(JSlider.HORIZONTAL,
				DEN_MIN, DEN_MAX, DEN_INIT);
		
		modelPane.add(vizButton);
		modelPane.add(Box.createRigidArea(new Dimension(10,10)));
		modelPane.add(toggleButton);
		modelPane.add(Box.createRigidArea(new Dimension(10,10)));
		modelPane.add(new JLabel("Density of Model", JLabel.CENTER));
		modelPane.add(densitySlider);
		
		return modelPane;
	}
	
	private JPanel addAxisOptions ()
	{
		String title = "Axis Options";
		
		JPanel axisPane = new JPanel();
		axisPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		
		axisPane.setLayout(new BoxLayout(axisPane, BoxLayout.PAGE_AXIS));
		
		String[] vecs = { "", "A", "B" };
				
		JLabel xLabel = new JLabel("X-Axis:");
		//xLabel.setPreferredSize(new Dimension(50,50));
		JComboBox xCombo = new JComboBox(vecs);
		xCombo.setSelectedIndex(1);
		//xCombo.setPreferredSize(new Dimension((int)(axisPaneSize.getWidth()/2),5));
		
		JLabel yLabel = new JLabel("Y-Axis:");
		//yLabel.setPreferredSize(new Dimension(50,50));
		JComboBox yCombo = new JComboBox(vecs);
		yCombo.setSelectedIndex(1);
		//yCombo.setPreferredSize(new Dimension(10,20));
		
		JLabel zLabel = new JLabel("Z-Axis:");
		//zLabel.setPreferredSize(new Dimension(50,50));
		JComboBox zCombo = new JComboBox(vecs);
		zCombo.setSelectedIndex(1);
		//zCombo.setPreferredSize(new Dimension(30,40));
		
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
		changeCauseColor.addActionListener(this);
		
		changePreventColor = new JButton("Prevent");
		changePreventColor.setBackground(Util.toColor(Util.PREVENTS_COLOR));
		changePreventColor.setActionCommand(CHANGE_PREVENTS_COLOR);
		changePreventColor.addActionListener(this);
		
		changeAllowColor = new JButton("Allow");
		changeAllowColor.setBackground(Util.toColor(Util.ALLOWS_COLOR));
		changeAllowColor.setActionCommand(CHANGE_ALLOWS_COLOR);
		changeAllowColor.addActionListener(this);
		
		changeHelpColor = new JButton("Help");
		changeHelpColor.setBackground(Util.toColor(Util.HELPS_COLOR));
		changeHelpColor.setActionCommand(CHANGE_HELPS_COLOR);
		changeHelpColor.addActionListener(this);
		
		changeDespiteColor = new JButton("Despite");
		changeDespiteColor.setBackground(Util.toColor(Util.DESPITE_COLOR));
		changeDespiteColor.setActionCommand(CHANGE_DESPITE_COLOR);
		changeDespiteColor.addActionListener(this);
		
		changeInvalidColor = new JButton("Invalid");
		changeInvalidColor.setBackground(Util.toColor(Util.INVALID_COLOR));
		changeInvalidColor.setActionCommand(CHANGE_INVALID_COLOR);
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

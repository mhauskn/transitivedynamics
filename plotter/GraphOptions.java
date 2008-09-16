package plotter;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

public class GraphOptions extends JPanel implements ActionListener
{
	private String myTitle = "Graph Options";
	
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
		add(Box.createRigidArea(new Dimension(10,100)));
	}
	
	public void actionPerformed (ActionEvent e)
	{
		
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
		
		JSlider densitySlider = new JSlider(JSlider.HORIZONTAL,
				DEN_MIN, DEN_MAX, DEN_INIT);
		
		modelPane.add(new JButton("Visualize Current Model"));
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
}

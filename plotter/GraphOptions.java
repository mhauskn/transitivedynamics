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
		modelPane.add(new JLabel("Density of Model", JLabel.LEADING));
		modelPane.add(densitySlider);
		
		return modelPane;
	}
}

package plotter;

import gui.VisualizeWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Util;

/**
 * Color Changer Opens up an interactive palette for 
 * the user to select a desired color for the selected 
 * verb.
 * @author Matthew Hausknecht
 *
 */
public class ColorChanger extends JFrame 
	implements ChangeListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private JColorChooser tcc;
	
	public static final String ACTION_OKAY = "okay";
	public static final String ACTION_CANCEL = "cancel";
	
	private JButton okay;
	private JButton cancel;
    
    private Color currentColor = new Color(1,1,1);
    
    private int verbNumber;
    
    private GraphOptions options;
    
    private VisualizeWindow viz;
	
	public ColorChanger (String title, int verbNum, GraphOptions g,
			VisualizeWindow v)
	{
		super(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		//super(new BorderLayout());
		verbNumber = verbNum;
		options = g;
		viz = v;

        //Set up color chooser for setting text color
        tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder("Color Palette"));
        
        okay = new JButton("Apply");
        okay.setActionCommand(ACTION_OKAY);
        
        cancel = new JButton("Cancel");
        cancel.setActionCommand(ACTION_CANCEL);
        
        okay.addActionListener(this);
        cancel.addActionListener(this);
        
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new FlowLayout());
        bannerPanel.add(okay);
        bannerPanel.add(cancel);
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Save Changes"));

        
        mainPanel.add(tcc, BorderLayout.PAGE_START);
        mainPanel.add(bannerPanel, BorderLayout.PAGE_END);
        
        mainPanel.setOpaque(true); //content panes must be opaque
        this.setContentPane(mainPanel);

        //Display the window.
        this.pack();
        this.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent e) {
        currentColor = tcc.getColor(); 
    }
	
	public void actionPerformed (ActionEvent e)
	{
		String command = e.getActionCommand();
		
		if (command.equals(ACTION_OKAY))
		{
			Util.setVerbColor(verbNumber, currentColor);
			options.updateColorKey();
			this.dispose();
			viz.getGraph().changeColor(verbNumber);
		}
		
		if (command.equals(ACTION_CANCEL))
			this.dispose();
	}
}

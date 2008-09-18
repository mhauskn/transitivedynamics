package plotter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChanger extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	protected JColorChooser tcc;
	
	protected JButton okay;
    protected JButton cancel;
	
	public ColorChanger (String title)
	{
		super(new BorderLayout());

        //Set up color chooser for setting text color
        tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(title));
        
        okay = new JButton("Apply");
        cancel = new JButton("Cancel");
        
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new FlowLayout());
        bannerPanel.add(okay);
        bannerPanel.add(cancel);
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Save Changes"));

        
        add(tcc, BorderLayout.PAGE_START);
        add(bannerPanel, BorderLayout.PAGE_END);
	}
	
	public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        
    }
}

package gui;

//import io.ModelIO;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Constrain Panel is placed between two normal panels and can constrain them to a particular verb
 * @author MHAUSKN
 */
public class ConstrainPanel extends Panel implements ItemListener {
	private static final long serialVersionUID = -7335082032087416578L;
	
	protected static final Font DRAW_FONT = new Font("LucidaConsole", Font.PLAIN, 12); 
	
	protected InteractivePanel parent;
	protected InteractivePanel child;
	
	protected JCheckBox constrainButton;
	protected JLabel currVerbLabel;
	
	protected boolean constrained;
	protected String constrainedVerb;
	
	protected int[][] lastGood =  new int[4][2];
	
	
	
	public ConstrainPanel (ContainerPanel cp, InteractivePanel parentPanel, InteractivePanel childPanel) {
		super.setPreferredSize(new Dimension(2,2));

		parent = parentPanel;
		child = childPanel;
		constrained = false;
		
		holding = cp;

		constrainButton = new JCheckBox("");
		constrainButton.setBackground(Color.WHITE);
		constrainButton.addItemListener(this);
		
		currVerbLabel = new JLabel(verb);
		currVerbLabel.setOpaque(true);
		currVerbLabel.setBackground(Color.WHITE);
		currVerbLabel.setForeground(Color.BLACK);
		
        JPanel layout = new JPanel(new BorderLayout());
        layout.add(constrainButton, BorderLayout.WEST);
        layout.add(currVerbLabel, BorderLayout.EAST);
        
		super.setBackground(Color.WHITE);
		super.add(layout);
		
		update();
	}
	
	public void update() {
		if(constrained) {
			if(constrainedVerb.equals(getConclusion())) {
				updateLatest();
			} else {
				restoreLatest();
			}
		}
	}
	
	public void updateLabel() {
		if (constrained)
			currVerbLabel.setText(constrainedVerb);
		else 
			currVerbLabel.setText(getConclusion());
	}
	
	public void updateLatest() {
		lastGood[0][0] = parent.iA;
		lastGood[1][0] = parent.iB;
		lastGood[2][0] = parent.iR;
		lastGood[3][0] = parent.iE;
		lastGood[0][1] = child.iA;
		lastGood[1][1] = child.iB;
		lastGood[2][1] = child.iR;
		lastGood[3][1] = child.iE;
	}
	
	public void restoreLatest() {
		parent.iA = lastGood[0][0];
		parent.iB = lastGood[1][0];
		parent.iR = lastGood[2][0];
		parent.iE = lastGood[3][0];
		parent.updateValues();
		child.iA = lastGood[0][1];
		child.iB = lastGood[1][1];
		child.iR = lastGood[2][1];
		child.iE = lastGood[3][1];
		child.updateValues();
	}
	
	
	public boolean inCompliance() {
		if(!constrained || getConclusion().equals(constrainedVerb)) 
			return true;
		return false;
	}
	
	public boolean resultConcords() {
		if(constrained) {
			int resultant = Math.abs(parent.iB + child.iB + parent.iA);
			/*if(child.child != null && resultant < Math.abs(child.child.iA)) //Bottom Constraint
				return false;*/
			if(parent.parent != null && resultant > Math.abs(parent.parent.iA)) //Top Constraint
				return false;
		}
		return true;
	}
	
	public boolean concordsBelow() {
		if(constrained) {
			int resultant = Math.abs(parent.iB + child.iB + parent.iA);
			if(child.child != null && resultant < Math.abs(child.child.iA))
				return false;
		}
		return true;
	}
	
	public boolean concordsAbove() {
		if(constrained) {
			int resultant = Math.abs(parent.iB + child.iB + parent.iA);
			if(parent.parent != null && resultant > Math.abs(parent.parent.iA))
				return false;
		}
		return true;
	}
	
	public String getConclusion() {
		iA = parent.iA;
		iB = parent.iB + child.iB;
		iR = iA + iB;
		iE = child.iE;
		
		updateValues();
		if(verb.equals("Helps") && parent.verb.equals("Prevents") && child.verb.equals("Prevents")) {
			return "Allows";
		}
		return verb;
	}
	
	
	public void setConstrained(boolean b) {
		constrained = b;
		if(b) constrainButton.setSelected(true);	
		else constrainButton.setSelected(false);	
	}
	
	public void setConstrainedVerb(String verb) {
		constrainedVerb = verb;
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			constrainedVerb = getConclusion();
			constrained = true;
			parent.constrainedBelow = true;
			child.constrainedAbove = true;
			update();
			updateLabel();
		} else if(e.getStateChange() == ItemEvent.DESELECTED){
			constrained = false;
			parent.constrainedBelow = false;
			child.constrainedAbove = false;
			constrainedVerb = getConclusion();
			update();
			updateLabel();
		}
		if(parent.myConstrainBelow == null)
			parent.myConstrainBelow = this;
		if(child.myConstrainAbove == null)
			child.myConstrainAbove = this;
		update();
		repaintAll();
	}
	
	public String toString() {
		//return constrained ? "1" + ModelIO.SEPARATOR + constrainedVerb : "0";	
		return constrained ? "1" : "0";
	}
}

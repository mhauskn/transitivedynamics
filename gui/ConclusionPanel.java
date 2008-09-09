package gui;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ConclusionPanel extends ArrowPanel implements MouseListener, ActionListener {
	
	private static final long serialVersionUID = -7335082032087416578L;

	private JPopupMenu ePopup;
	private boolean reNegate = false;
	
	
	
	private InteractivePanel parent;
		
	public ConclusionPanel(ContainerPanel cp) {
		super("", "");
		
        addMouseListener(this);
		
		parent = null;
		
		holding = cp;
		
		initializeMenus();
	}
	
	public void postPaint(Graphics2D g) {
		g.setColor(COL_FG);
		g.drawLine(0, 0, getWidth(), 0);
	}
	
	public void setParent(InteractivePanel p) {
		parent = p;
	}
	
	/**
	 * Updates our conclusion- Sums the iB vectors of all previous premises and takes iA from the 1st premise
	 */
	public void update() {
		if (parent != null) {
			int patientSum = 0;
			InteractivePanel p = parent;
			
			setBWord(p.getBWord());
			setE(p.getE());
			setENegated(p.getENegated());
			if (reNegate) setENegated(!getENegated());

			while (p != null) {
				//if (!p.reExpressed && !(p.constrainedBelow && p.myConstrain.constrainedVerb.equals("Allows"))) patientSum += p.getB();
				
				if (p.reExpressed || (p.constrainedBelow && p.myConstrainBelow.constrained && p.myConstrainBelow.constrainedVerb.equals("Allows"))) {
					//Do not add to sum
				} else {
					patientSum += p.getB();
				}
				
				if (p.getMyParent() == null) {
					if(p.reExpressed) {
						setA(p.getA());
						setANegated(p.getANegated()); //REmove this in future
						setAWord(NEG_CHAR + p.getBWord());
					} else {
						setA(p.getA());
						setANegated(p.getANegated());
						setAWord(p.getAWord());
					}
				}
				
				p = p.getMyParent();
			}
			
			setB(patientSum);
			updateValues();
			
			if (verb.equals("Helps") || verb.equals("Allows") && parent != null)
			{
				verb = "Allows";
				p = parent;
				while (p != null)
				{
					if (p.verb.equals("Helps"))
					{
						verb = "Helps";
						break;
					}
					p = p.parent;
				}
			}
			
			// If we have a Prevent,Prevent prior to conclusion, we choose Allow
			/*if(verb.equals("Helps") && parent != null) {
				p = parent;
				if(p.verb.equals("Prevents") && p.parent != null) {
					p = p.getMyParent();
					if(p.verb.equals("Prevents")) {
						verb = "Allows";
					}
				}
			}*/
		}
	}
	
	//----- Below is all code for the Negate Popup on the E vector -----//
	
	public void actionPerformed(ActionEvent e) {
		if (parent.getLockDown()) return;
        String source = ((JMenuItem)(e.getSource())).getText();
        if (source.equals("Negate") && reNegate) {
        	reNegate = false;
        	update(); repaint();
        } else if (source.equals("Negate") && !reNegate) {
        	reNegate = true;
        	update(); repaint();
        }
    }
	
	public void mouseReleased(MouseEvent event) {
		if (parent.getLockDown()) return;
		if (event.getButton() == MouseEvent.BUTTON3) {
			if (rE.contains(event.getPoint()) || rNE.contains(event.getPoint())) {
				setEPopup();
				ePopup.show(this, event.getX(), event.getY());
				return;
			}
		}
	}
	
	public void mousePressed(MouseEvent event) {}
	public void mouseDragged(MouseEvent event) {}
	public void mouseMoved(MouseEvent event) {}
	public void mouseClicked(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	
	private void initializeMenus() {
		ePopup = new JPopupMenu();
		ePopup.add(getMenu("Relabel", false));
		ePopup.add(getMenu("Negate", false));
		ePopup.add(getMenu("Reset", true));
	}
	
	private void setEPopup() {
		ePopup.removeAll();
		ePopup.add(getMenu("Negate", true));
	}
	
	private JMenuItem getMenu(String label, boolean enabled) {
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.addActionListener(this);
		menuItem.setEnabled(enabled);
		
		return menuItem;
	}
}

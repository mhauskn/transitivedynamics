package gui;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;


/**
 * An extension of the basic ArrowPanel class that allows for interaction
 * depending on the ruleset defined by the update() method.
 * 
 * @author Andrew Vaughan
 * @version August 1, 2006
 */
public class InteractivePanel extends ArrowPanel implements MouseMotionListener, MouseListener, ActionListener {
	
	/**
	 * Serial identifier for version management.
	 */
	private static final long serialVersionUID = 4392977638167296058L;
	
	/**
	 * Declaration for no button currently being pressed.
	 */
	private static final int MOUSE_NONE = 0;
	
	/**
	 * Declaration for the A button being pressed.
	 */
	private static final int MOUSE_A = 1;
	
	/**
	 * Declaration for the B button being pressed.
	 */
	private static final int MOUSE_B = 2;
	
	/**
	 * Declaration for the E button being pressed.
	 */
	private static final int MOUSE_E = 3;
	
	private static final int MOUSE_NA = 4;
	private static final int MOUSE_NE = 5;
	
	private int lastmove = 0;
	
	/**
	 * Holds the child panel for this premise, if a parent.
	 */
	public InteractivePanel child;
	
	/**
	 * Holds the parent panel for this premise, if a child.
	 */
	public InteractivePanel parent;
	
	/**
	 * Holds the offset for the mouse when a button is pressed so that sliding will be smooth
	 * and exact.
	 */
	private int mouseOffset;
	
	/**
	 * Holds which button is pressed while dragging or interacting with the model
	 */
	private int mouseButton;
	
	/**
	 * The popup menu when the A button is pressed
	 */
	private JPopupMenu aPopup;
	
	/**
	 * The popup menu when the B button is pressed
	 */
	private JPopupMenu bPopup;
	
	/**
	 * The popup menu when the E button is pressed
	 */
	private JPopupMenu ePopup;
	
	/**
	 * The popup menu when the A button is pressed and this is a child premise
	 */
	private JPopupMenu aChildPopup;
	
	/**
	 * The popup menu used when the word is pressed.
	 */
	private JPopupMenu wordPopup;
	
	/**
	 * This object's ID for the container panel.
	 */
	private int id;
	
	/**
	 * Holds whether or not this panel is locked down.
	 */
	private boolean lockDown;
	
	/**
	 * Holds the word displaced by a Self-Targeting
	 */
	private String displacedWord = "";
	
	/**
	 * Lets conclusion know what verb it should adopt
	 */
	public boolean reExpressed = false;
	
	public boolean constrainedAbove = false;
	
	public boolean constrainedBelow = false;
	
	public ConstrainPanel myConstrainAbove;
	
	public ConstrainPanel myConstrainBelow;
	
	
	
//---------------------------------------------------------------------------------------
//	PUBLIC METHODS
	
	
	/**
	 * Default constructor for the interactive panel.
	 * 
	 * @param cp the container panel that holds this interactive panel
	 * @param myid the id in the container panel for this panel
	 * @param aWord the word to use for the affector
	 * @param bWord the word to use for the patient
	 */
	public InteractivePanel(ContainerPanel cp, int myid, String aWord, String bWord) {
		super(aWord, bWord);
		
		setFont(new Font("Courier New", Font.PLAIN, 12));
		
		id = myid;
		holding = cp;
		//panels = cp.panels;
		//constrain = cp.constrain;
		//conclusion = cp.conclusion;
		//lastGood = cp.lastGood;
		
		addMouseMotionListener(this);
        addMouseListener(this);
		
		child = null;
		parent = null;
		
		lockDown = false;
		
		mouseOffset = 0;
		mouseButton = MOUSE_NONE;
		
		initializeMenus();
	}
	
	/*public void setConclusion(ConclusionPanel conc) {
		conclusion = conc;
	}*/
	
	
//---------------------------------------------------------------------------------------
//	ACCESSORS/MUTATORS
	
	/**
	 * Sets the child panel for this interactive panel.
	 * 
	 * @param c the panel to set for the child
	 */
	public void setChild(InteractivePanel c) {
		child = c;
		super.setChild(c);
	}
	
	
	/**
	 * Sets the parent for this interactive panel.
	 * 
	 * @param p the panel to set for the parent
	 */
	public void setParent(InteractivePanel p) {
		parent = p;
		super.setParent(p);
	}
	
	
	/**
	 * Returns the parent to this panel.
	 * 
	 * @return the interactive panel that is the parent of this panel
	 */
	public InteractivePanel getMyParent() {
		return parent;
	}
	
	
	/**
	 * Returns this panel's child.
	 * 
	 * @return the interactive panel that is the child of this panel
	 */
	public InteractivePanel getMyChild() {
		return child;
	}
	
	
	/**
	 * Sets whether or not this panel is currently locked down.
	 * 
	 * @param lock true if this panel is locked, false if not
	 */
	public void setLockDown(boolean lock) {
		lockDown = lock;
	}
	
	public boolean getLockDown()
	{
		return lockDown;
	}
	
	
//---------------------------------------------------------------------------------------
//	EVENT HANDLERS
	
	
	/**
	 * Handles all popup menus whenever the right-mouse button is pressed over an
	 * appropriate button.
	 * 
	 * @param event the mouse event to process
	 */
	public void mouseReleased(MouseEvent event) {
		mouseButton = MOUSE_NONE;
		mouseOffset = 0;
		
		if (lockDown) return;
		
		//Not sure why this is needed and it causes program to lag lots
		//if (event.getX() > 100) setWordMenu();
		
		// If it's a right-click, check for a menu
		if (event.getButton() == MouseEvent.BUTTON3) {
			
			// If they right clicked on the E button
			if (rE.contains(event.getPoint()) || rNE.contains(event.getPoint())) {
				mouseButton = MOUSE_E;
				setEPopup();
				ePopup.show(this, event.getX(), event.getY());
				
				return;
			}
			
			// If they right clicked on the B button
			if (rB.contains(event.getPoint())) {
				mouseButton = MOUSE_B;
				bPopup.show(this, event.getX(), event.getY());
				
				return;
			}
			
			// If they right clicked on the A button
			if (rA.contains(event.getPoint()) || rNA.contains(event.getPoint())) {
				mouseButton = MOUSE_A;
				
				if (parent != null)
					aChildPopup.show(this, event.getX(), event.getY());
				else
					aPopup.show(this, event.getX(), event.getY());
				
				return;
			}
			
			if (event.getX() < 100) {
				setWordMenu();
				wordPopup.show(this, event.getX(), event.getY());
			}
		}
	}
	
	
	/**
	 * Stores the mouse offset and which button was pressed whenever
	 * a user presses a button with the left-mouse button.
	 * 
	 * @param event the mouse event to process
	 */
	public void mousePressed(MouseEvent event) {
		mouseButton = MOUSE_NONE;
		mouseOffset = 0;
		
		if (lockDown) return;
		
		lastmove = event.getX();
		
		// If it's a left-click, check for a button
		if (event.getButton() == MouseEvent.BUTTON1) {
			
			// If they clicked on the negative E button
			if (rNE.contains(event.getPoint()) && !rNELocked)	{
				mouseButton = MOUSE_NE;
			}
			
			// If they clicked on the negative A button
			if (rNA.contains(event.getPoint()) && !rNALocked)	{
				mouseButton = MOUSE_NA;
			}
			
			// If they clicked on the E button
			if (rE.contains(event.getPoint()) && !rELocked) {
				mouseButton = MOUSE_E;
				mouseOffset = event.getX() - (start.x + getE());
			}
			
			// If they clicked on the B button
			if (rB.contains(event.getPoint()) && !rBLocked) {
				mouseButton = MOUSE_B;
				mouseOffset = event.getX() - (start.x + getB());
			}
			
			// If they clicked on the A button and this is a parent
			//if (parent == null && rA.contains(event.getPoint())) {
			if (rA.contains(event.getPoint()) && !rALocked) {
				mouseButton = MOUSE_A;
				mouseOffset = event.getX() - (start.x + getA());
			}
			
		}
	}
	
	
	/**
	 * Handles the movement of the vectors on the model if a mouse button
	 * has been flagged by the mousePressed() function.
	 * 
	 * @param event the mouse event to process
	 */
	public void mouseDragged(MouseEvent event) {
		if (lockDown) return;
		
		switch (mouseButton) {
			case MOUSE_A:	iA = event.getPoint().x - start.x - mouseOffset; break;
			case MOUSE_B:	iB = event.getPoint().x - start.x - mouseOffset; break;
			case MOUSE_E:	iE = event.getPoint().x - start.x - mouseOffset; break;
			
			case MOUSE_NE:	iE += (lastmove - event.getPoint().x); break;
			case MOUSE_NA:	iA += (lastmove - event.getPoint().x); break;
		}
		
		lastmove = event.getPoint().x;
		
		repaint();
	}
	
	
	/**
	 * Handles all popup-menu calls performed by the model.
	 * 
	 * @param e the event to process
	 */
	public void actionPerformed(ActionEvent e) {
		if (lockDown) return;
		
        String source = ((JMenuItem)(e.getSource())).getText();
        
        if (source.equals("Negate")) {
        	if (mouseButton == MOUSE_A) {
        		setANegated(!getANegated());
        		moveANegated();
        	}
        	else if (mouseButton == MOUSE_E) setENegated(!getENegated());
        	
        	mouseButton = MOUSE_NONE;
        	
        	repaint();
        	
        	updateValues();
        	holding.resetMenus();
        	
        } else if (source.equals("Reset")) {
        	if (mouseButton == MOUSE_A) { setA(110); setANegated(false); }
        	if (mouseButton == MOUSE_B) { setB(-50); }
        	if (mouseButton == MOUSE_E) { setE(150); setENegated(false); } 
        	
        	mouseButton = MOUSE_NONE;
        	
        	repaint();
        	
        	updateValues();
        	holding.resetMenus();
        	
        } else if (source.equals("Relabel")) {
        	if (mouseButton == MOUSE_A) {
        		String aVal = (String)JOptionPane.showInputDialog(this, "New Label for \"" + aWord + ":\"", "Relabel Item", JOptionPane.QUESTION_MESSAGE, null, null, aWord);
        		
        		// They hit cancel
        		if (aVal == null) return;
        		
        		// Make sure it was valid
        		if (aVal.length() < 1 || aVal.indexOf("|") != -1) JOptionPane.showMessageDialog(this, "Invalid Label", "Error", JOptionPane.ERROR_MESSAGE);
        		else setAWord(aVal);
        		
        	} else if (mouseButton == MOUSE_B) {
        		String bVal = (String)JOptionPane.showInputDialog(this, "New Label for \"" + bWord + ":\"", "Relabel Item", JOptionPane.QUESTION_MESSAGE, null, null, bWord);
        		
        		// They hit cancel
        		if (bVal == null) return;
        		
        		// Make sure it was valid
        		if (bVal.length() < 1 || bVal.indexOf("|") != -1) JOptionPane.showMessageDialog(this, "Invalid Label", "Error", JOptionPane.ERROR_MESSAGE);
        		else bWord = bVal;
        		
        	}
        	
        	repaintAll();
        	
        	
        } else if (source.equals("Causes")) {
        	holding.setPanel(id, ContainerPanel.CAUSES);
        	holding.resetMenus();
        	
        } else if (source.equals("Helps")) {
        	holding.setPanel(id, ContainerPanel.HELPS);
        	holding.resetMenus();
        	
        } else if (source.equals("Prevents")) { //Should not ever be used as is disabled currently!
        	holding.setPanel(id, ContainerPanel.PREVENTS);
        	holding.resetMenus();
        	
        } else if (source.equals("Despite")) {
        	holding.setPanel(id, ContainerPanel.DESPITE);
        	holding.resetMenus();
        
        } else if (source.equals("Self-Target")) {
        	if(displacedWord.equals("")) {
        		InteractivePanel tmp = this;
        		while(tmp != null) {
        			if(tmp.child == null)
        				displacedWord = tmp.getBWord();
        			tmp.setBWord(tmp.getAWord());
        			if(tmp.parent != null)
        				tmp.setAWord(tmp.parent.getBWord());
        			tmp = tmp.child;
        		}
        	} else {
        		InteractivePanel tmp = this;
        		while(tmp.child != null) {
        			tmp = tmp.child;
        		}
        		
        		tmp.setAWord(tmp.getBWord());
    			tmp.setBWord(displacedWord);
    			displacedWord = "";
    			
        		if(tmp.id != this.id) {
        			tmp = tmp.parent;
        			while(this.id != tmp.id) {
        				tmp.setAWord(tmp.getBWord());
        				tmp.setBWord(tmp.child.getAWord());
        				tmp = tmp.parent;
        			}
        			tmp.setAWord(tmp.getBWord());
    				tmp.setBWord(tmp.child.getAWord());
        		} 
        	}
        	//setWordMenu();
        	//resetAllWordMenus(); Needs Some type of self-target stack to resolve this issue
        
        } else if (source.equals("ReExpress")) {
        	if(parent == null && (verb.equals("Prevents") || (verb.equals("Causes") && eNegated))) {
        		if(holding.conclusion.getAWord().indexOf(NEG_CHAR) == -1) reExpressed = true;
        		else reExpressed = false;
        	} 
        }
        
        holding.repaintAll();
    }
	
	public void mouseMoved(MouseEvent event) {}
	public void mouseClicked(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	
	
//---------------------------------------------------------------------------------------
//	PRIVATE METHODS
	
	
	private void initializeMenus() {
		aPopup = new JPopupMenu();
		aPopup.add(getMenu("Relabel", true));
		//aPopup.add(getMenu("Negate", true));
		aPopup.add(getMenu("Reset", true));
		
		aChildPopup = new JPopupMenu();
		aChildPopup.add(getMenu("Relabel", false));
		//aChildPopup.add(getMenu("Negate", true));
		aChildPopup.add(getMenu("Reset", false));
		
		bPopup = new JPopupMenu();
		bPopup.add(getMenu("Relabel", true));
		//bPopup.add(getMenu("Negate", false));
		bPopup.add(getMenu("Reset", true));
		
		ePopup = new JPopupMenu();
		ePopup.add(getMenu("Relabel", false));
		ePopup.add(getMenu("Negate", false));
		ePopup.add(getMenu("Reset", true));
		
		wordPopup = new JPopupMenu();
		wordPopup.add(getMenu("Causes", false));
		wordPopup.add(getMenu("Helps", true));
		//wordPopup.add(getMenu("Prevents", true));
		wordPopup.add(getMenu("Despite", true));
		
		wordPopup.addSeparator();
		wordPopup.add(getMenu("Self-Target", true));
		wordPopup.add(getMenu("ReExpress", false));
	}
	
	private JMenuItem getMenu(String label, boolean enabled) {
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.addActionListener(this);
		menuItem.setEnabled(enabled);
		
		return menuItem;
	}
	
	private void setEPopup() {
		ePopup.removeAll();
		ePopup.add(getMenu("Relabel", false));
		if((verb.equals("Prevents") && !eNegated) || (eNegated && verb.equals("Causes"))) ePopup.add(getMenu("Negate", true));
		else ePopup.add(getMenu("Negate", false));
		ePopup.add(getMenu("Reset", true));
	}
	
	public void setWordMenu() {
		wordPopup.removeAll();
		//TODO: Investigate why the canPanel call was going into an infinte loop and see if we can use it
		//if (holding.canPanel(id, ContainerPanel.CAUSES) && !verb.equals("Causes")) wordPopup.add(getMenu("Causes", true));
		if(!verb.equals("Causes")) wordPopup.add(getMenu("Causes", true));
		else wordPopup.add(getMenu("Causes", false));
		
		//if (holding.canPanel(id, ContainerPanel.HELPS) && !verb.equals("Helps")) wordPopup.add(getMenu("Helps", true));
		if(!verb.equals("Helps")) wordPopup.add(getMenu("Helps", true));
		else wordPopup.add(getMenu("Helps", false));
		
		//TODO: Re-implement the Prevents choice
		//if (holding.canPanel(id, ContainerPanel.PREVENTS) && !verb.equals("Prevents")) wordPopup.add(getMenu("Prevents", true));
		//else wordPopup.add(getMenu("Prevents", false));
		
		//if (holding.canPanel(id, ContainerPanel.DESPITE) && !verb.equals("Despite")) wordPopup.add(getMenu("Despite", true));
		if(!verb.equals("Despite")) wordPopup.add(getMenu("Despite", true));
		else wordPopup.add(getMenu("Despite", false));
		
		wordPopup.addSeparator();
		
		//if (!displacedWord.equals("") && hasDisplacedChild()) wordPopup.add(getMenu("Self-Target", false));
		wordPopup.add(getMenu("Self-Target", true));
		
		if (parent == null && (verb.equals("Prevents") || (verb.equals("Causes") && eNegated)) 
				&& child != null && (child.verb.equals("Prevents") || (child.verb.equals("Causes") && child.eNegated))) 
			wordPopup.add(getMenu("ReExpress", true));
		else wordPopup.add(getMenu("ReExpress", false));
		
		//update();
		//if (child != null) child.setWordMenu();
	}
	
	/*public boolean hasDisplacedChild() {
		if(child == null) return false;
		InteractivePanel tmp = child;
		while(tmp != null) {
			if(!tmp.displacedWord.equals("")) return true;
			tmp = tmp.child;
		}
		return false;
	}*/
}

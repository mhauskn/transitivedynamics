package gui;

import geom.Vector2D;
import io.ModelIO;

import java.awt.*;

/**
 * The ArrowPanel is an interactive panel with GUI arrows to interact with
 * 
 * @author Andrew Vaughan, Matthew Hausknecht
 */
public class ArrowPanel extends Panel {
	
	private static final long serialVersionUID = -6859995267102467064L;
	
	protected static final Color COL_BG = Color.WHITE;
	protected static final Color COL_BG2 = new Color(240, 240, 240);
	protected static final Color COL_FG = Color.BLACK;
	protected static final Color COL_A = new Color(0, 150, 0);
	protected static final Color COL_Locked = Color.GRAY;
	protected static final Color COL_B = new Color(0, 0, 150);
	protected static final Color COL_R = new Color(255, 0, 0);
	protected static final Color COL_P = new Color(128,0,255);

	protected static final char R_ARROW = '\u2192';
	protected static final Font DRAW_FONT = new Font("LucidaConsole", Font.PLAIN, 12);
	
	/**
	 * Clickable area for the A vector
	 */
	protected Rectangle rA;
	
	/**
	 * Clickable area for the B vector
	 */
	protected Rectangle rB;
	
	/**
	 * Clickable area for the E vector
	 */
	protected Rectangle rE;
	
	/**
	 * Clickable area for the negated-A vector (if applicable)
	 */
	protected Rectangle rNA;
	
	/**
	 * Clickable area for the negated-E vector (if applicable)
	 */
	protected Rectangle rNE;
	
	/**
	 * True if the A vector is currently locked down
	 */
	protected boolean rALocked;
	
	/**
	 * True if the B vector is currently locked down
	 */
	protected boolean rBLocked;
	
	/**
	 * True if the E vector is currently locked down
	 */
	protected boolean rELocked;
	
	/**
	 * True if the negated-A vector is currently locked down
	 */
	protected boolean rNALocked;
	
	/**
	 * True if the negated-E vector is currently locked down
	 */
	protected boolean rNELocked;
	
	/**
	 * The font used to draw letters
	 */
	protected Font fontSmall;
	
	protected int offset;
	
	protected Point start;
	
	protected boolean parentInvalid;
	
	protected InteractivePanel parent;
	protected InteractivePanel child;
	
	
	
	
//---------------------------------------------------------------------------------------
// 	PUBLIC METHODS
	
	/**
	 * Creates a new ArrowPanel
	 * @param A, The letter for the affector vector
	 * @param B, The letter for the patient vector
	 */
	public ArrowPanel(String A, String B) {
		setFont(DRAW_FONT);
		
		iA = 125;
		iB = -50;
		iE = 150;
		
		aNegated = false;
		eNegated = false;
		
		aWord = A;
		bWord = B;
		
		rA = new Rectangle();
		rB = new Rectangle();
		rE = new Rectangle();
		
		rNA = new Rectangle();
		rNE = new Rectangle();
		
		// Nothing locked to start with
		rALocked = rBLocked = rELocked = rNALocked = rNELocked = false;
		
		offset = 50;
				
		fontSmall = new Font(DRAW_FONT.getFamily(), Font.PLAIN, 10);
		
		parentInvalid = false;
		
		updateValues();
	}
	
	/**
	 * Paints the arrow panel component
	 */
	public void paint (Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		
		start = new Point((getWidth() / 2) + offset, getHeight() / 2);
		
		update();
		
		g2d.setBackground(COL_BG);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		drawVectors(g2d);
				
			// Center Dot
			g2d.setColor(COL_FG);
			g2d.fillOval(start.x - 3, start.y - 3, 6, 6);
			
			// Verb
			String toDraw = "Invalid";
			String deepVerb = "Invalid";
			if (!parentInvalid && !verb.equals("Invalid")) {
				if(verb.equals("Despite")) {
					toDraw = (eNegated ? String.valueOf(NEG_CHAR) : "") + bWord + " ";
					toDraw += verb + " ";
					toDraw += (aNegated ? String.valueOf(NEG_CHAR) : "") + aWord;
				} else {
					toDraw  = (aNegated ? String.valueOf(NEG_CHAR) : "") + aWord + " ";
					toDraw += verb + " ";
					toDraw += (eNegated ? String.valueOf(NEG_CHAR) : "") + bWord;
				}	
			}
			
			if (!getDeepVerb().equals("Invalid")) {
				if(getDeepVerb().equals("Despite")) {
					deepVerb = bWord + " " + getDeepVerb() + " " + aWord;
				} else {
					deepVerb = aWord + " " + getDeepVerb() + " " + bWord;
				}
			}			
			
			// Show the TCR if needed
			if (holding.isTCR()) {
				g2d.drawString(deepVerb, 10, (getHeight() / 2) - 2); //Draws deep verb
				g2d.drawString(toDraw, 95, (getHeight() / 2) - 2);
				
				g2d.setFont(fontSmall);
				drawTCR(g2d);
			} else {
				g2d.drawString(toDraw, 10, (getHeight() / 2) + 5);
			}
	}
	
	/**
	 * Returns a string representation of the current object. 
	 * Generally useful for saving the model.
	 */
	public String toString() {
		String retWord = "";
		
		retWord += iA + ModelIO.SEPARATOR;
		retWord += iB + ModelIO.SEPARATOR;
		retWord += iE + ModelIO.SEPARATOR;
		retWord += aWord + ModelIO.SEPARATOR;
		retWord += (aSubscript == null || aSubscript == "" ? " " : aSubscript) + ModelIO.SEPARATOR;
		retWord += bWord + ModelIO.SEPARATOR;
		retWord += (aNegated ? "1" : "0") + ModelIO.SEPARATOR;
		retWord += (eNegated ? "1" : "0") + ModelIO.SEPARATOR;
		
		return retWord;
	}
	
	
//---------------------------------------------------------------------------------------
//	ACCESSORS/MUTATORS
	
	public void setParent(InteractivePanel p) {
		parent = p;
	}
	
	public void setChild(InteractivePanel p) {
		child = p;
	}
	
	public String getDeepVerb() {
		String verb = "Invalid";
		if (TCR.equals("NNY")) verb = "Causes";
		if (TCR.equals("YYY")) verb = "Helps";
		if (TCR.equals("YNN")) verb = "Prevents";
		if (TCR.equals("YNY")) verb = "Despite";
		return verb;
	}
	
	public void setA(int newA) {
		iA = newA;
	}

	
	public int getA() {
		return iA;
	}
	
	
	public void setB(int newB) {
		iB = newB;
	}
	
	
	public int getB() {
		return iB;
	}
	
	
	public void setE(int newE) {
		iE = newE;
	}
	
	
	public int getE() {
		return iE;
	}
	
	
	public void setAWord(String newWord) {
		aWord = newWord;
	}
	
	
	public String getAWord() {
		return aWord;
	}
	
	
	public void setBWord(String newWord) {
		bWord = newWord;
	}
	
	
	public String getBWord() {
		return bWord;
	}
	
	
	public void setANegated(boolean isN) {
		aNegated = isN;
	}
	
	public void moveANegated() {
		if (!getANegated()) {
			iB = -iA;
		} else {
			iE = -iE;
			iA = -iB;
			iB = 0;
		}
	}
	
	public boolean getALocked() {
		return rALocked;
	}
	
	public boolean getBLocked() {
		return rBLocked;
	}
	
	public boolean getANegated() {
		return aNegated;
	}
	
	public void setENegated(boolean isN) {
		eNegated = isN;
	}
	
	public boolean getENegated() {
		return eNegated;
	}
	
	public void setASubscript(String sub) {
		aSubscript = sub;
	}
	
	public String getASubscript() {
		return aSubscript;
	}
	
	public void setBSubscript(String sub) {
		bSubscript = sub;
	}
	
	public String getBSubscript() {
		return bSubscript;
	}
	
	public boolean getSurfaceTendency() {
		int trueE = (eNegated ? -iE : iE);
		return concord(trueE, iB);
	}
	
	public boolean getSurfaceConcordance() {
		int trueA = (aNegated ? -iA : iA);
		return concord(trueA, iB);
	}
	
	public boolean getSurfaceResult() {
		int toCheck = (aNegated ? -iA + iB : iA + iB);
		int trueE = (eNegated ? -iE : iE);
		return concord(toCheck, trueE);
	}
	
//---------------------------------------------------------------------------------------
//	PRIVATE METHODS
	
	private void drawArrow(Graphics2D g2d, Point start, int len, String word, String subword, Rectangle rect, Rectangle n_rect, boolean negate, boolean above, boolean isSmall, Color col, boolean locked) {
		//boolean locked = false;
		
		g2d.setFont(DRAW_FONT);
		
		int strW = g2d.getFontMetrics().stringWidth(word);
		
		// Draw the vector
		
		if (isSmall)
			if (!negate)
				Vector2D.drawVector(g2d, start, new Point(start.x + len, start.y), (len == 0 ? Vector2D.SHAPE_CIRCLE : Vector2D.SHAPE_TRIANGLE), false);
			else
				Vector2D.drawVector(g2d, start, new Point(start.x + len, start.y), (len == 0 ? Vector2D.SHAPE_CIRCLE : Vector2D.SHAPE_HOLLOWTRI), true);
		else
			if (!negate)
				Vector2D.drawVector(g2d, start, new Point(start.x + len, start.y), (len == 0 ? Vector2D.SHAPE_CIRCLE : Vector2D.SHAPE_BIGTRI), false);
			else
				Vector2D.drawVector(g2d, start, new Point(start.x + len, start.y), (len == 0 ? Vector2D.SHAPE_CIRCLE : Vector2D.SHAPE_BIGHOLLOWTRI), true);
			
		
		// Draw the string and set the rectangle
		//if(word.equals(aWord) && rALocked)locked = true; //Locked vectors should be colored gray
		//if(word.equals(bWord) && rBLocked) locked = true;
		if (above) {
			g2d.drawString(word, start.x + len - (strW / 2), start.y - 10);
			if (rect != null) rect.setRect(start.x + len - (strW / 2), start.y - 20, strW, 18);
			
			if (subword != null && subword != "") {
				Font before = g2d.getFont();
				g2d.setFont(fontSmall);
				int strW2 = g2d.getFontMetrics().stringWidth(subword);
				g2d.drawString(subword, start.x + len + strW - 4, start.y - 7);
				g2d.setFont(before);
				
				if (rect != null) rect.setRect(start.x + len - (strW / 2), start.y - 20, strW + strW2, 18);
			}
			
			if (holding.isMagnitude()) {
				g2d.setColor(COL_Locked);
				Font before = g2d.getFont();
				g2d.setFont(fontSmall);
				String mag = "";
				if(col == COL_A) mag = String.valueOf(iA);
				else if(col == COL_B) mag = String.valueOf(iB);
				else if(col == COL_R) mag = String.valueOf(iR);
				else if(col == COL_FG) mag = String.valueOf(iE);
				//g2d.drawString(mag, start.x + len - (strW / 2) + 17, start.y - 10);
				g2d.drawString(mag, start.x + len - (strW / 2) - 10 -3*mag.length(), start.y - 10);
				g2d.setFont(before);
			}
		} else {
			if(!locked) {
				g2d.setColor(col);
				g2d.drawString(word, start.x + len - (strW / 2), start.y + 20);
			} else {
				g2d.setColor(COL_Locked);
				g2d.drawString(word, start.x + len - (strW / 2), start.y + 20);
			}
			
			if (rect != null) rect.setRect(start.x + len - (strW / 2), start.y + 10, strW, 18);
			
			if (subword != null && subword != "") {
				Font before = g2d.getFont();
				g2d.setFont(fontSmall);
				int strW2 = g2d.getFontMetrics().stringWidth(subword);
				g2d.drawString(subword, start.x + len + strW - 4, start.y + 23);
				g2d.setFont(before);
				
				if (rect != null) rect.setRect(start.x + len - (strW / 2), start.y + 10, strW + strW2, 18);
			}
			
			if (holding.isMagnitude()) {
				g2d.setColor(COL_Locked);
				Font before = g2d.getFont();
				g2d.setFont(fontSmall);
				String mag = "";
				if(col == COL_A) mag = String.valueOf(iA);
				else if(col == COL_B) mag = String.valueOf(iB);
				else if(col == COL_R) mag = String.valueOf(iR);
				else if(col == COL_FG) mag = String.valueOf(iE);
				g2d.drawString(mag, start.x + len - (strW / 2) - 10 - 3*mag.length(), start.y + 20);
				g2d.setFont(before);
			}
		}
		
		
		// If negated, draw that information
		if (negate) {
			
			// Draw the vector
			Vector2D.drawVector(g2d, start, new Point(start.x - len, start.y), (len == 0 ? Vector2D.SHAPE_CIRCLE : Vector2D.SHAPE_TRIANGLE), false);
			
			int strW3 = g2d.getFontMetrics().stringWidth(NEG_CHAR + word);
			
			// Draw the string and set the rectangle
			if (above) {
				g2d.drawString(NEG_CHAR + word, start.x - len - (strW / 2), start.y - 10);
				if (n_rect != null) n_rect.setRect(start.x - len - (strW / 2), start.y - 20, strW3, 18);
				
				if (subword != null && subword != "") {
					Font before = g2d.getFont();
					g2d.setFont(fontSmall);
					int strW2 = g2d.getFontMetrics().stringWidth(subword);
					g2d.drawString(subword, start.x - len + strW3 - 4, start.y - 7);
					g2d.setFont(before);
					
					if (n_rect != null) n_rect.setRect(start.x - len - (strW / 2), start.y - 20, strW3 + strW2, 18);
				}
			} else {
				g2d.drawString(NEG_CHAR + word, start.x - len - (strW / 2), start.y + 20);
				if (n_rect != null) n_rect.setRect(start.x - len - (strW / 2), start.y + 10, strW3, 18);
				
				if (subword != null && subword != "") {
					Font before = g2d.getFont();
					g2d.setFont(fontSmall);
					int strW2 = g2d.getFontMetrics().stringWidth(subword);
					g2d.drawString(subword, start.x - len + strW3 - 4, start.y + 23);
					g2d.setFont(before);
					
					if (n_rect != null) n_rect.setRect(start.x - len - (strW / 2), start.y + 10, strW3 + strW2, 18);
				}
			}
		}
	}
	
	/**
	 * Draws the A,E,B,R vectors
	 * @param g2d The graphics 2D object
	 */
	private void drawVectors (Graphics2D g2d)
	{
		// The E Arrow
		g2d.setColor(COL_FG);
		drawArrow(g2d, start, iE, "E", bWord, rE, rNE, eNegated, true, true, COL_FG, false);
		
		// The A Arrow
		g2d.setColor(COL_A);
		drawArrow(g2d, start, iA, aWord, (aSubscript != "" ? aSubscript : null), rA, rNA, aNegated, false, false, COL_A, rALocked);
		
		// The B Arrow
		g2d.setColor(COL_B);
		drawArrow(g2d, start, iB, bWord, (bSubscript != "" ? bSubscript : null), rB, null, false, false, true, COL_B, rBLocked);
		
		// The R Arrow
		g2d.setColor(COL_R);
		if(parent != null && ((iR == parent.iA && !parent.rALocked) || (iR == parent.iB && !parent.rBLocked)))
			g2d.setColor(COL_P);
		if(child != null && ((iR == child.iA && !child.rALocked) || (iR == child.iB && !child.rBLocked)))
			g2d.setColor(COL_P);
		drawArrow(g2d, start, iR, rWord, null, null, null, false, true, true, COL_R, false);
	}
	
	/**
	 * Draws the TCR related text on the model.
	 * @param g2d The graphics 2d object
	 */
	private void drawTCR (Graphics2D g2d)
	{
		if (getTendency()) {
			g2d.drawString("T", 10, (getHeight() / 2) + 12);
			g2d.drawString("(Y-", 10 + g2d.getFontMetrics().stringWidth("TCR  "), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("T", 10, (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("(N-", 10 + g2d.getFontMetrics().stringWidth("TCR  "), (getHeight() / 2) + 12);
		}
		
		if (getConcordance()) {
			g2d.drawString("C", 10 + g2d.getFontMetrics().stringWidth("T"), (getHeight() / 2) + 12);
			g2d.drawString("Y-", 10 + g2d.getFontMetrics().stringWidth("TCR  (Y-"), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("C", 10 + g2d.getFontMetrics().stringWidth("T"), (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("N-", 10 + g2d.getFontMetrics().stringWidth("TCR  (Y-"), (getHeight() / 2) + 12);
		}
		
		if (getResult()) {
			g2d.drawString("R", 10 + g2d.getFontMetrics().stringWidth("TC"), (getHeight() / 2) + 12);
			g2d.drawString("Y)", 10 + g2d.getFontMetrics().stringWidth("TCR  (Y-Y-"), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("R", 10 + g2d.getFontMetrics().stringWidth("TC"), (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("N)", 10 + g2d.getFontMetrics().stringWidth("TCR  (Y-Y-"), (getHeight() / 2) + 12);
		}
		
		// The following code will contain our surface level TCR.
		// We only need to display this if we have something negated.
		g2d.setColor(Color.BLACK); //Draws arrow from deep verb to surface verb
		g2d.drawString(String.valueOf(R_ARROW), 80, (getHeight() / 2) - 2);
		int surfTCRX = 95;
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(String.valueOf(R_ARROW), 80, (getHeight() / 2) + 12);
		
		if (getSurfaceTendency()) {
			g2d.drawString("T", surfTCRX, (getHeight() / 2) + 12);
			g2d.drawString("(Y-", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  "), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("T", surfTCRX, (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("(N-", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  "), (getHeight() / 2) + 12);
		}
		
		if (getSurfaceConcordance()) {
			g2d.drawString("C", surfTCRX + g2d.getFontMetrics().stringWidth("T"), (getHeight() / 2) + 12);
			g2d.drawString("Y-", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  (Y-"), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("C", surfTCRX + g2d.getFontMetrics().stringWidth("T"), (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("N-", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  (Y-"), (getHeight() / 2) + 12);
		}
		
		if (getSurfaceResult()) {
			g2d.drawString("R", surfTCRX + g2d.getFontMetrics().stringWidth("TC"), (getHeight() / 2) + 12);
			g2d.drawString("Y)", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  (Y-Y-"), (getHeight() / 2) + 12);
		}
		else {
			g2d.setColor(new Color(150, 150, 150));
			g2d.drawString("R", surfTCRX + g2d.getFontMetrics().stringWidth("TC"), (getHeight() / 2) + 12);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("N)", surfTCRX + g2d.getFontMetrics().stringWidth("TCR  (Y-Y-"), (getHeight() / 2) + 12);
		}
		
		g2d.setFont(DRAW_FONT);
	}
}

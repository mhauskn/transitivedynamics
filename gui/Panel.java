package gui;

import javax.swing.JPanel;

import util.Util;

/**
 * Generic Panel Class. Extended by ArrowPanel and Constrain Panel.
 * Indirectly extended by ConclusionPanel and InteractivePanel
 * 
 * @author Matthew Hausknecht
 */
public class Panel extends JPanel {
	private static final long serialVersionUID = -6859995267102467064L;
	protected static final char NEG_CHAR = '\u00AC';
	
	public static final String eVEC = "iE";
	public static final String aVEC = "iA";
	public static final String bVEC = "iB";
	
	/**
	 * The container panel that is holding this object.
	 */
	protected ContainerPanel holding;
	
	protected int iA;
	protected int iB;
	protected int iE;
	protected int iR;
	
	protected String TCR;
	protected String subTCR;
	
	protected String aWord;
	protected String aSubscript;
	protected String bSubscript;
	protected String bWord;
	protected String rWord;
	
	protected boolean aNegated;
	protected boolean eNegated;
	
	protected String verb;
	
//---------------------------------------------------------------------------------------
// 	PUBLIC METHODS
	
	public Panel() {
		super();
	}
	
	/**
	 * Updates all of the vectors in the full model
	 */
	protected void update()
	{
		holding.update();
	}
	
	/**
	 * Repaints the full model
	 */
	protected void repaintAll()
	{
		holding.repaintAll();
	}
	
	/**
	 * Updates the individual vector without updating the whole model
	 */
	protected void updateValues() {
		
		rWord = getRWord();
		
		updateR();
		
		// Calculate tendency, concordance, and result
		TCR = (getTendency() ? "Y" : "N") + (getConcordance() ? "Y" : "N") + (getResult() ? "Y" : "N");
		subTCR = "";
		
		// Calculate the verb choice
		verb = "Invalid";
				
		if (aNegated) {
			if (eNegated) {
				subTCR = (!getTendency() ? "Y" : "N") + (!getConcordance() ? "Y" : "N") + 
					(!getSubResult() ? "Y" : "N");
				if (TCR.equals("YYY") && subTCR.equals("NNY")) verb = Util.CAUSES_WORD;
				if ((TCR.equals("NNN") || TCR.equals("NNY"))) verb = Util.HELPS_WORD;
				if (TCR.equals("NYN") && subTCR.equals("YNN")) verb = Util.PREVENTS_WORD;
				if (TCR.equals("NYN") && subTCR.equals("YNY")) verb = Util.DESPITE_WORD;
				
			} else {
				subTCR = (getTendency() ? "Y" : "N") + (!getConcordance() ? "Y" : "N") + 
					(getSubResult() ? "Y" : "N");
				if (TCR.equals("NYN") && subTCR.equals("NNY") || (TCR.equals("NNN") && iB == 0)) verb = Util.CAUSES_WORD;
				if ((TCR.equals("YNY") || TCR.equals("YNN"))) verb= Util.HELPS_WORD;
				if (TCR.equals("YYY") && subTCR.equals("YNN")) verb = Util.PREVENTS_WORD;
				if (TCR.equals("YYY") && subTCR.equals("YNY")) verb = Util.DESPITE_WORD;
			}
			
		} else {
			if (eNegated) {
				if (TCR.equals("YNN")) verb = Util.CAUSES_WORD;
				if (TCR.equals("NYN")) verb = Util.HELPS_WORD;
				if (TCR.equals("NNY")) verb = Util.PREVENTS_WORD;
				if (TCR.equals("NNN")) verb = Util.CAUSES_WORD;
				
			} else {
				if (TCR.equals("NNY")) verb = Util.CAUSES_WORD;
				if (TCR.equals("YYY")) verb = Util.HELPS_WORD;
				if (TCR.equals("YNN")) verb = Util.PREVENTS_WORD;
				if (TCR.equals("YNY")) verb = Util.DESPITE_WORD;
			}
		}
	}
	
	/**
	 * Updates the resultant vector.
	 */
	public void updateR () {
		iR = getR();
	}
	
	/**
	 * True if the patient vector has same sign as the endstate
	 */
	public boolean getTendency() {
		return concord(iB, iE);
	}
	
	/**
	 * True if the affector and patient vectors point in same direction
	 */
	public boolean getConcordance() {
		if (iA == iB) return true;
		if (iA > 0 && iB > 0) return true;
		if (iA < 0 && iB < 0) return true;
		
		return false;
	}
	
	/**
	 * True if the result points in the same direction as the endstate
	 */
	public boolean getResult() {
		int toCheck = (aNegated ? iA + iB : iR);
		
		if (toCheck == iE) return true;
		if (toCheck > 0 && iE > 0) return true;
		if (toCheck < 0 && iE < 0) return true;
		
		return false;
	}
	
	/**
	 * True if vectors vec1 and vec2 point in same direction
	 */
	public boolean concord(int vec1, int vec2) {
		if (vec1 > 0 && vec2 > 0) return true;
		if (vec1 < 0 && vec2 < 0) return true;
		if (vec1 == vec2) return true;
		return false;
	}
	
	/**
	 * Gets the verb of the panel
	 */
	public String getVerb () {
		return verb;
	}
	
//---------------------------------------------------------------------------------------
// 	PRIVATE METHODS
	
	/**
	 * True if the endstate is in the same direction as the result
	 */
	private boolean getSubResult() {
		int toCheck = iR;
		
		if (toCheck == iE) return true;
		if (toCheck > 0 && iE > 0) return true;
		if (toCheck < 0 && iE < 0) return true;
		
		return false;
	}
	
	private String getRWord() {
		return bWord + (aNegated ? String.valueOf(NEG_CHAR) : "") + aWord;
	}
	
	private int getR() {
		if (aNegated) return iB - iA;
		return iA + iB;
	}
}

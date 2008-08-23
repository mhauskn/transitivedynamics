package gui;

import javax.swing.JPanel;

/**
 * Generic Panel Class. Extended by ArrowPanel and Constrain Panel.
 * Indirectly extended by ConclusionPanel and InteractivePanel
 * 
 * @author Matthew Hausknecht
 */
public class Panel extends JPanel {
	private static final long serialVersionUID = -6859995267102467064L;
	protected static final char NEG_CHAR = '\u00AC';
	
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
		
		iR = getR();
		
		// Calculate tendency, concordance, and result
		TCR = (getTendency() ? "Y" : "N") + (getConcordance() ? "Y" : "N") + (getResult() ? "Y" : "N");
		subTCR = "";
		
		// Calculate the verb choice
		verb = "Invalid";
				
		if (aNegated) {
			if (eNegated) {
				subTCR = (!getTendency() ? "Y" : "N") + (!getConcordance() ? "Y" : "N") + 
					(!getSubResult() ? "Y" : "N");
				if (TCR.equals("YYY") && subTCR.equals("NNY")) verb = "Causes";
				if ((TCR.equals("NNN") || TCR.equals("NNY"))) verb = "Helps";
				if (TCR.equals("NYN") && subTCR.equals("YNN")) verb = "Prevents";
				if (TCR.equals("NYN") && subTCR.equals("YNY")) verb = "Despite";
				
			} else {
				subTCR = (getTendency() ? "Y" : "N") + (!getConcordance() ? "Y" : "N") + 
					(getSubResult() ? "Y" : "N");
				if (TCR.equals("NYN") && subTCR.equals("NNY") || (TCR.equals("NNN") && iB == 0)) verb = "Causes";
				if ((TCR.equals("YNY") || TCR.equals("YNN"))) verb= "Helps";
				if (TCR.equals("YYY") && subTCR.equals("YNN")) verb = "Prevents";
				if (TCR.equals("YYY") && subTCR.equals("YNY")) verb = "Despite";
			}
			
		} else {
			if (eNegated) {
				if (TCR.equals("YNN")) verb = "Causes";
				if (TCR.equals("NYN")) verb = "Helps";
				if (TCR.equals("NNY")) verb = "Prevents";
				if (TCR.equals("NNN")) verb = "Causes";
				
			} else {
				if (TCR.equals("NNY")) verb = "Causes";
				if (TCR.equals("YYY")) verb = "Helps";
				if (TCR.equals("YNN")) verb = "Prevents";
				if (TCR.equals("YNY")) verb = "Despite";
			}
		}
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

package gui;

import java.util.ArrayList;
import java.util.Random;

import util.Util;

public class Simulator {
	long timeStart;
	
	String retval = "";
	
	long numCauses = 0;
	long numAllows = 0;
	long numHelps = 0;
	long numPrevents = 0;
	long numDespites = 0;
	long numInvalids = 0;
	
	long divisor = 0;
	long attempts = 0;
	
	boolean valid = true; // If true we will Accept the trial
	
	int totalAccepted = 0; // Count of all accepted trials
	
	int cntPanels;
	
	int eZero;
	
	double[][] totals;
	
	// [iA][iB] <-- panel 0
	int[][] initVals;
	
	// [direction iA][direction iB]  <--- panel 0
	int[][] initDirs;
	
	// [iA Locked?][iB Locked?] <--- panel 0
	boolean[][] initLocked;
	
	private ContainerPanel cPanel;
	private ExploreWindow expWin;
	
	/**
	 * Holds the vectors of our accepted configurations
	 */
	ArrayList<Integer[][]> acceptedConf = new ArrayList<Integer[][]>();
	
	public Simulator (ContainerPanel _cPanel, ExploreWindow _expWin)
	{
		cPanel = _cPanel;
		expWin = _expWin;
		cntPanels = cPanel.panels.length;
		eZero = Math.abs(cPanel.panels[0].iE);
		totals = new double[8][cntPanels + 1];
		initVals = new int[2][cntPanels];
		initDirs = new int[2][cntPanels];
		initLocked = new boolean[2][cntPanels];
	}
	
	public String simulate(int numPatients)
	{
		timeStart = System.currentTimeMillis();
		String premise = "";
		String overallAffector = cPanel.conclusion.getAWord();
		String overallPatient = cPanel.conclusion.eNegated ? '\u00AC' + cPanel.conclusion.getBWord() : cPanel.conclusion.getBWord();
		retval += "\n";
		
		String aWord = cPanel.panels[0].aWord.length() > 5 ? cPanel.panels[0].aWord.substring(0, 5) : cPanel.panels[0].aWord;
		String bWord = cPanel.panels[0].bWord.length() > 5 ? cPanel.panels[0].bWord.substring(0, 5) : cPanel.panels[0].bWord;
		aWord = aWord.length() > 1 ? "" + aWord :  " " + aWord;
		retval += aWord;
		for (int i = aWord.length(); i < 6; i++)
			retval += " ";
		retval += bWord;
		for (int i = bWord.length(); i < 6; i++)
			retval += " ";
		for (int t = 0; t < cntPanels; t++)
		{
			if (t != 0)
			{
				aWord = cPanel.panels[t].aWord.length() > 5 ? cPanel.panels[t].aWord.substring(0, 5) : cPanel.panels[t].aWord;
				bWord = cPanel.panels[t].bWord.length() > 5 ? cPanel.panels[t].bWord.substring(0, 5) : cPanel.panels[t].bWord;
				retval += aWord;
				for (int i = aWord.length(); i < 6; i++)
					retval += " ";
				retval += bWord;
				for (int i = bWord.length(); i < 6; i++)
					retval += " ";
			}
			
			if (cPanel.panels[t].aNegated && !cPanel.panels[t].verb.equals("Invalid"))
				premise += "N_";
			if (cPanel.panels[t].verb.equals("Allows"))
				premise += "ALLOW";
			else if (cPanel.panels[t].verb.equals("Helps"))
				premise += "HELP";
			else if (cPanel.panels[t].verb.equals("Causes"))
				premise += "CAUSE";
			else if (cPanel.panels[t].verb.equals("Despite"))
				premise += "DESPITE";
			else if (cPanel.panels[t].verb.equals("Prevents"))
				premise += "PREVENT";
			if (cPanel.panels[t].eNegated && !cPanel.panels[t].verb.equals("Invalid"))
				premise += "_N";
			if (t < cntPanels - 1) {
				if(cPanel.constrain[t].constrained)
					premise += " -"+cPanel.constrain[t].verb.toLowerCase().substring(0,1)+"- ";
				else
					premise += " / ";
			}
		}
		
		retval += "\n";
		
		// Reset the totals for everything
		for (int i = 0; i < 8; i++)
			for (int j = 0; j <= cntPanels; j++)
				totals[i][j] = 0.0;
		
		// Lock the panels and store the panel's words and values
		String[] oWords = new String[cntPanels];
		int oEValues[] = new int[cntPanels];
		
		int aValue = cPanel.panels[0].getA();
		for (int i = 0; i < cntPanels; i++) {
			cPanel.panels[i].setLockDown(true);
			oWords[i] = cPanel.panels[i].verb;
			oEValues[i] = cPanel.panels[i].getE();
			initDirs[0][i] = cPanel.panels[i].getA();
			initDirs[1][i] = cPanel.panels[i].getB();
			initLocked[0][i] = cPanel.panels[i].rALocked;
			initLocked[1][i] = cPanel.panels[i].rBLocked;
			initVals[0][i] = cPanel.panels[i].getA();
			initVals[1][i] = cPanel.panels[i].getB();
			
			if (oWords[i].equals("Invalid")) {
				cPanel.panels[0].setA(aValue);
				for (int bb = 0; bb < cntPanels; bb++) {
					cPanel.panels[bb].setLockDown(false);
					
					cPanel.update();
					cPanel.repaint();
				}
				
				return "  ERROR: Invalid verb encountered.\n";
			}
		}
		
		while (totalAccepted < numPatients)
		{
			//Check to make sure we have not accepted this conf already
			
			attempts++;
			for (int i = 0; i < cntPanels; i++)
			{
				if (initDirs[0][i] > 0) cPanel.panels[i].setA(getRandom(0, eZero));
				else cPanel.panels[i].setA(getRandom(-eZero, 0)); 
				
				if (initDirs[1][i] > 0) cPanel.panels[i].setB(getRandom(0, eZero));
				else cPanel.panels[i].setB(getRandom(-eZero, 0));
				
				cPanel.panels[i].iR = cPanel.panels[i].iA + cPanel.panels[i].iB;
			}
			
			for (int i = 0; i < cntPanels; i++)
				cPanel.panels[i].updateValues();
			
			cPanel.update();
			
			valid = true; // Innocent until proven guilty!
			
			for (int t = 0; t < oWords.length; t++)
				if (!oWords[t].equals(cPanel.panels[t].verb))
					valid = false;
			
			if (valid)
			{
				if (!uniqueConfig())
					continue;
				addAcceptedConfig();
				cPanel.conclusion.update();
				
				if(cPanel.conclusion.verb.equals("Causes")) numCauses++;
				else if(cPanel.conclusion.verb.equals("Allows")) numAllows++;
				else if(cPanel.conclusion.verb.equals("Helps")) numHelps++;
				else if(cPanel.conclusion.verb.equals("Prevents")) numPrevents++;
				else if(cPanel.conclusion.verb.equals("Despite")) numDespites++;
				else if(cPanel.conclusion.verb.equals("Invalid")) numInvalids++;
				divisor++;
				
				for (int i = 0; i < cntPanels; i++) {
					String aVal = String.valueOf(cPanel.panels[i].getA());
					aVal += ",";
					for (int j = aVal.length(); j < 6; j++)
						aVal += " ";
	
					String bVal = String.valueOf(cPanel.panels[i].getB());
					if (i != (cntPanels - 1))
					{
						bVal += ",";
						for (int j = bVal.length(); j < 6; j++)
							bVal += " ";
					}
					retval += aVal;
					retval += bVal;
				}
				retval += "\n";
				totalAccepted++;
			}
		}
		
		for (int i = 0; i < cntPanels; i++) {
			cPanel.panels[i].setLockDown(false);
			cPanel.panels[i].setA(initVals[0][i]);
			cPanel.panels[i].setB(initVals[1][i]);
			cPanel.panels[i].setE(oEValues[i]);
		}
		
		cPanel.update();
		cPanel.repaintAll();
		
		String elapsed = Util.parseTime(System.currentTimeMillis() - timeStart);
	
		expWin.update(numCauses, numAllows, numHelps, numPrevents, numDespites, numInvalids, totalAccepted, attempts, elapsed, premise, overallAffector, overallPatient, eZero);
		return retval;
	}
	
	private boolean uniqueConfig ()
	{
		boolean unique = true;
		
		for (Integer[][] conf : acceptedConf)
		{
			if (matchesCurConfig(conf))
				unique = false;
		}
		
		return unique;
	}
	
	private boolean matchesCurConfig (Integer[][] conf)
	{
		for (int i = 0; i < cntPanels; i++)
		{
			if (cPanel.panels[i].iA != conf[0][i])
				return false;
			if (cPanel.panels[i].iB != conf[1][i])
				return false;
		}
		return true;
	}
	
	private void addAcceptedConfig ()
	{
		Integer[][] conf = new Integer[2][cntPanels];
		for (int i = 0; i < cntPanels; i++)
		{
			conf[0][i] = cPanel.panels[i].iA;
			conf[1][i] = cPanel.panels[i].iB;
		}
		acceptedConf.add(conf);
	}

	private int getRandom(int a, int b) {
		Random rnd = new Random();
		int retval = 0;
		
		// If they're equal... that's an easy random number
		if (a == b)
			return a;
		
		while (retval == 0) {
			
			// If they're crossing 0, that poses a problem (we shouldn't see any of those
			if (a < 0 && b > 0 || a > 0 && b < 0) {
				System.out.println("ERROR IN getRandom");
				return 0;
			}
			
			// Otherwise, we pick a random number and slap on the polarity
			if (a > b) {
				retval = rnd.nextInt(a - b);
				if ((a - b) > 0) retval += b;
				else retval -= b;
				
			} else if (a < b) {
				retval = rnd.nextInt(b - a);
				if ((b - a) > 0) retval += a;
				else retval -= a;
			}
		}
		
		return retval;
	}
}

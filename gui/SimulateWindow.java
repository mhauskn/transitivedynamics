package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import util.util;

import javax.swing.*;

public class SimulateWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 8986946014398992725L;
		
	ContainerPanel cPanel;
	ExploreWindow exWin;
	
	JScrollPane areaScrollPane;
	JPanel setupPanel;
	//JPanel resultPanel;
	JTextField txtPatients;
	JTextArea txtResults;
	
	
	public SimulateWindow(int x, int y, ContainerPanel cp, ExploreWindow ex) {
		super("Simulation Experimentation");
		
		cPanel = cp;
		exWin = ex;
		initializeGUI(x, y);
	}
	
	
	/**
	 * Sets up and initializes the window.
	 */
	private void initializeGUI(int x, int y) {
		setSize(500, 250);
		setLocation(x, y);
		
		setBackground(Color.gray);
        setLayout(new BorderLayout());
        
        // The configuration panel
        GridLayout cfgLO = new GridLayout(2, 1);
        setupPanel = new JPanel(cfgLO);
        
        // Add the instructions
        JMultilineLabel instructions = new JMultilineLabel();
        instructions.setText("Before beginning the simulation, please enter the following information regarding what constraints the simulation must follow:");
        instructions.setJustified(true);
        instructions.setMaxWidth(400);
        setupPanel.add(instructions);
        
        // Add the option for the number of patients
        SpringLayout a_layout = new SpringLayout();
        JPanel setupPanel_A = new JPanel(a_layout);
        
        JLabel lblPatients = new JLabel("Number of Participants:");
        txtPatients = new JTextField("30", 3);
        
        setupPanel_A.add(lblPatients);
        setupPanel_A.add(txtPatients);
        a_layout.putConstraint(SpringLayout.WEST, lblPatients, 5, SpringLayout.WEST, setupPanel_A);
        a_layout.putConstraint(SpringLayout.NORTH, lblPatients, 5, SpringLayout.NORTH, setupPanel_A);
        a_layout.putConstraint(SpringLayout.WEST, txtPatients, 5, SpringLayout.EAST, lblPatients);
        a_layout.putConstraint(SpringLayout.NORTH, txtPatients, 5, SpringLayout.NORTH, setupPanel_A);
        
        JButton btnOK = new JButton("Continue");
        JButton btnCancel = new JButton("Cancel");
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        
        setupPanel_A.add(btnOK);
        setupPanel_A.add(btnCancel);
        
        a_layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.SOUTH, txtPatients);
        a_layout.putConstraint(SpringLayout.NORTH, btnCancel, 10, SpringLayout.SOUTH, txtPatients);
        a_layout.putConstraint(SpringLayout.WEST, btnCancel, 5, SpringLayout.EAST, btnOK);
        
        setupPanel.add(setupPanel_A);
        
        // The results panel
        //resultPanel = new JPanel(new GridLayout(1, 1));
        txtResults = new JTextArea();
        txtResults.setBorder(BorderFactory.createEtchedBorder(1));
        txtResults.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        //resultPanel.add(txtResults);
        
        areaScrollPane = new JScrollPane(setupPanel);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
            		BorderFactory.createTitledBorder("Configure Simulation"),
                    BorderFactory.createEmptyBorder(2,2,2,2)));
        
        add(areaScrollPane, BorderLayout.CENTER);
        
        // Show the created window
        setVisible(true);
	}


	public void actionPerformed(ActionEvent e) {
		String txt = ((JButton)e.getSource()).getText();
		
		if (txt.equals("Continue")) {
			int patients = Integer.parseInt(txtPatients.getText());
			
			if (patients <= 0) {
				txtResults.setText("ERROR: Invalid number of participants.");
				return;
			}
			
			/*areaScrollPane.setViewportView(resultPanel);
			areaScrollPane.setBorder(
		            BorderFactory.createCompoundBorder(
		            		BorderFactory.createTitledBorder("Simulation Results"),
		                    BorderFactory.createEmptyBorder(2,2,2,2)));*/
			
			String outputHeader = "--------------------\n";
			outputHeader += "Simulation Date: " + (DateFormat.getDateInstance(DateFormat.LONG)).format(new Date()) + "\n";
			outputHeader += "Number of Participants: " + patients + "\n";
			
			
			exWin.txtBox.append(outputHeader);
			
			String outputText = runSimulation(patients);
			
			exWin.txtBox.append(outputText);
			
			txtResults.setText(outputText);
			txtResults.setCaretPosition(0);
			
			setSize(500, 510);
			
			validate();
			repaint();
			
			
			txtPatients.setText("30");
			setVisible(false);
			
		} else if (txt.equals("Cancel")) {
			txtPatients.setText("30");
			setVisible(false);
		}
	}
	
	
	
	private String runSimulation(int patients) {
		
		long timeStart = System.currentTimeMillis();
		
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
		
		int cntPanels = cPanel.panels.length;
		
		int eZero = Math.abs(cPanel.panels[0].iE);
		
		double totals[][] = new double[8][cntPanels + 1];
		
		// [iA][iB] <-- panel 0
		int[][] initVals = new int[2][cntPanels];
		
		// [direction iA][direction iB]  <--- panel 0
		int[][] initDirs = new int[2][cntPanels];
		
		// [iA Locked?][iB Locked?] <--- panel 0
		boolean[][] initLocked = new boolean[2][cntPanels];
		
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
		
		while (totalAccepted < patients)
		{
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
			{
				if (initLocked[0][i]) // if iA Locked set equal to result above
					cPanel.panels[i].setA(cPanel.panels[i-1].iR);
				if (initLocked[1][i])
					cPanel.panels[i].setB(cPanel.panels[i+1].iR);
			}
			
			for (int i = 0; i < cntPanels; i++)
				cPanel.panels[i].updateValues();
			
			valid = true; // Innocent until proven guilty!
			
			for (int t = 0; t < oWords.length; t++)
				if (!oWords[t].equals(cPanel.panels[t].verb))
					valid = false;
			
			if (valid)
			{
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
		
		for (int i = 0; i < cntPanels; i++) {
			cPanel.update();
			//TODO: LOOk at this versus the above equivalent
			cPanel.panels[i].repaint();
		}
		
		String elapsed = util.parseTime(System.currentTimeMillis() - timeStart);

		exWin.update(numCauses, numAllows, numHelps, numPrevents, numDespites, numInvalids, totalAccepted, attempts, elapsed, premise, overallAffector, overallPatient, eZero);
		return retval;
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

package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.*;

public class SimulateWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 8986946014398992725L;
	
	static final String continue_cmd = "continue";
	static final String cancel_cmd = "cancel";
	static final String output_cmd = "output";
	static final String normalize_cmd = "normalize";
		
	ContainerPanel cPanel;
	ExploreWindow exWin;
	
	JScrollPane areaScrollPane;
	JPanel setupPanel;
	//JPanel resultPanel;
	JTextField txtPatients, ztxt;
	JTextArea txtResults;
	
	/**
	 * Specifies if the individual patient vectors should be included 
	 * in output
	 */
	private boolean outputMagnitudes = false;
	
	/**
	 * Draws new vectors from a normal distribution
	 */
	private boolean useNormalDist = false;
	
	
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
		setSize(500, 330);
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
        btnOK.setActionCommand(continue_cmd);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setActionCommand(cancel_cmd);
        
        JCheckBox chkOutput = new JCheckBox("Output Individual Responses");
        chkOutput.setActionCommand(output_cmd);
        JCheckBox normalDist = new JCheckBox("Sample from Normal Distribution");
        normalDist.setActionCommand(normalize_cmd);
        
        chkOutput.addActionListener(this);
        normalDist.addActionListener(this);
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        
        JLabel zscore = new JLabel("Endpoint Z-Score:");
        ztxt = new JTextField("3.0", 3);
        ztxt.setEnabled(false);
        
        setupPanel_A.add(chkOutput);
        setupPanel_A.add(normalDist);
        setupPanel_A.add(zscore);
        setupPanel_A.add(ztxt);
        setupPanel_A.add(btnOK);
        setupPanel_A.add(btnCancel);
        
        a_layout.putConstraint(SpringLayout.NORTH, chkOutput, 1, SpringLayout.SOUTH, txtPatients);
        a_layout.putConstraint(SpringLayout.NORTH, normalDist, 1, SpringLayout.SOUTH, chkOutput);
        a_layout.putConstraint(SpringLayout.NORTH, zscore, 1, SpringLayout.SOUTH, normalDist);
        a_layout.putConstraint(SpringLayout.WEST, zscore, 5, SpringLayout.WEST, setupPanel_A);
        a_layout.putConstraint(SpringLayout.NORTH, ztxt, 1, SpringLayout.SOUTH, normalDist);
        a_layout.putConstraint(SpringLayout.WEST, ztxt, 5, SpringLayout.EAST, zscore);

        a_layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.SOUTH, ztxt);
        a_layout.putConstraint(SpringLayout.NORTH, btnCancel, 10, SpringLayout.SOUTH, ztxt);
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
		String txt = e.getActionCommand();
		
		if (txt.equals(continue_cmd)) {
			int patients = Integer.parseInt(txtPatients.getText());
			double zscore = Math.abs(Double.parseDouble(ztxt.getText()));
			if (!useNormalDist) zscore = -1;
			
			if (patients <= 0 || zscore == 0) {
				txtResults.setText("ERROR: Invalid number of participants.");
				return;
			}
			
			String outputHeader = "--------------------\n";
			outputHeader += "Simulation Date: " + (DateFormat.getDateInstance(DateFormat.LONG)).format(new Date()) + "\n";
			outputHeader += "Number of Participants: " + patients + "\n";
			
			
			exWin.txtBox.append(outputHeader);
			
			String outputText = runSimulation(patients, zscore);
			
			if (outputMagnitudes)
				exWin.txtBox.append(outputText);
			
			txtResults.setText(outputText);
			txtResults.setCaretPosition(0);
			
			setSize(500, 510);
			
			validate();
			repaint();
			
			
			txtPatients.setText("30");
			setVisible(false);
			
		} else if (txt.equals(cancel_cmd)) {
			txtPatients.setText("30");
			setVisible(false);
		} else if (txt.equals(output_cmd)) {
			outputMagnitudes = !outputMagnitudes;
		} else if (txt.equals(normalize_cmd)) {
			useNormalDist = !useNormalDist;
			ztxt.setEnabled(useNormalDist);
		}
	}
	
	private String runSimulation(int patients, double zscore) 
	{
		Simulator s = new Simulator(cPanel, exWin);
		return s.simulate(patients, zscore);
	}
}

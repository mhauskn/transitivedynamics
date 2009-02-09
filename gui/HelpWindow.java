package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HelpWindow extends JFrame {
	
	private static final long serialVersionUID = -2474120333070019286L;
	
	public HelpWindow() {
		super("Force Composition Modeler Help");
        
		setSize(500, 400);
		setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        InternetPane help = new InternetPane();
		help.setEditable(false);
		help.setContentType("text/html");
		
		JScrollPane areaScrollPane = new JScrollPane(help);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(areaScrollPane, BorderLayout.CENTER);
        
        createHelp(help);
        
        help.setCaretPosition(0);
        areaScrollPane.getVerticalScrollBar().setValue(0);
        
        // Show the created window
        setVisible(true);
	}
	
	
	
	public void createHelp(InternetPane help) {
		String hText;
		
		hText  = "<HTML>\n";
		
		hText += "  <HEAD>\n";
		hText += "    <STYLE TYPE=\"text/css\">\n";
		hText += "      body {\n";
		hText += "        font-family: Trebuchet MS, Verdana, Arial;\n";
		hText += "        font-size: 12pt;\n";
		hText += "      }\n";
		hText += "      .title {\n";
		hText += "        font-size: 20pt;\n";
		hText += "        font-weight: bold;\n";
		hText += "      }\n";
		hText += "      .subtitle {\n";
		hText += "        font-size: 14pt;\n";
		hText += "        font-weight: bold;\n";
		hText += "      }\n";
		hText += "      .small {\n";
		hText += "        font-size: 10pt;\n";
		hText += "        color: gray;\n";
		hText += "      }\n";
		hText += "    </STYLE>\n";
		hText += "  </HEAD>\n";
		
		hText += "  <BODY>\n";
		hText += "    <CENTER><SPAN CLASS=\"title\">Force Composition Modeler</SPAN><BR><BR><SPAN CLASS=\"small\">Emory University<BR>Atlanta, GA</SPAN>\n";
		hText += "      <HR WIDTH=\"200\" COLOR=\"red\" noshade>\n";
		hText += "    </CENTER><BR><BR>\n";
		
		hText += "    <SPAN CLASS=\"subtitle\">Introduction</SPAN><BR>\n";
		hText += "    <BLOCKQUOTE>Welcome!  The Force Composition Modeler is a java-based tool that allows researchers to visually display transative dynamics of causality in a graphical, vector-based form.  By using features included such as the \"Explore\" method, researchers can pursue this involvement even further by accessing important ratios of predicted results between participant responses.</BLOCKQUOTE><BR>\n";
		
		hText += "    <SPAN CLASS=\"subtitle\">Getting Started</SPAN><BR>\n";
		hText += "    <BLOCKQUOTE>This Force Composition Modeling software incorporated multiple, selectable algorithms to create a conclusive result based off of multiple premises involving participant and affecting tendencies and results.  By modifying any one premise, it is very likely that both the conclusion and other premises will be modified as well.<BR><BR>";
		hText += "                <B>Starting a New Premise</B><BR>";
		hText += "                When the program begins, two premises and a conclusion are presented by default.  The number of premises, as well as other options, can be changed using the various options in the <B>Models</B> menu.<BR><BR>";
		hText += "                <B>Modifying a Premise</B><BR>";
		hText += "                For each premise, there exists two letters.  In the first premise, as an example, the letter <B>A</B> is considered the affecting vector, while the letter <B>B</B> is considered the participant vector.  These vectors can be moved by clicking and dragging on their letters.  Right clicking any letter will bring a menu of options, allowing for negations and the ability to change vector names.<BR><BR>";
		hText += "                <B>Changing Premises</B><BR>";
		hText += "                The relationship of each premise is listed to the left of the vectors. Right-clicking on the premise will bring up a menu of possible relationships. Selecting any of these will modify the model.<BR><BR>";
		hText += "                <B>Saving and Loading</B><BR>";
		hText += "                The Force Composition Modeling software allows for saving and loading all settings of the software as a \".tdm\" file.  These can be distributed as long as the .TDM version it was saved as is of equal or lower version as the Force Composition Modeling software that is opening it.  Images can also be produced from the vector plots in the .png format used by various operating systems.<BR><BR>";
		hText += "                <B>Running Explore</B><BR>";
		hText += "                One of the most popular features of the Force Composition Modeling software is its ability to explore and formulate ratios of conclusions based on all possibilities of a given premise.  By running explore, the software will run, tally, and calculate percentages of conclusions based on every possible vector value, without changing premise verbs.  This results in a table with the following information for every possible conclusion:<BR>";
		hText += "                <UL><LI><B>Category</B> - The type of verb that was associated with the conclusion.<BR>";
		hText += "                <LI><B>T-C-R</B> - Boolean values that show the tendency, concordance, and result values of the conclusion.<BR>";
		hText += "                <LI><B>Percent</B> - The percent of the results that concluded in that particular category.<BR>";
		hText += "                <LI><B>Avg #</B> - The average value for the letter when the given category was the conclusion.<BR>";
		hText += "                <LI><B>Trials</B> - The total number of combinations used that concluded with the given category.<BR>";
		hText += "                </UL><B>Visualizing</B><BR>";
		hText += "                Visualization allows output of an exploration to be seen in 3-D space. To utilize this capability it is necessary to have Java3D installed. Java3D can be found at <href>http://java.sun.com/javase/technologies/desktop/java3d/</href>. Visualized model can be rotated by holding left mouse and moving. Translation is achived by holding down right mouse and moving. Zooming is achived throug holding middle mouse and moving up and down.<BR><BR>";
		hText += "                </BLOCKQUOTE><BR>";
		
		hText += "    <SPAN CLASS=\"subtitle\">License</SPAN><BR>\n";
		hText += "    <BLOCKQUOTE>All Force Composition Modeling applications are free to use and distribute under the conditions that all author information, this help file, and the underlying code are not modified or corrupted in any way.</BLOCKQUOTE><BR>";
		
		hText += "    <SPAN CLASS=\"subtitle\">Author Information</SPAN><BR>\n";
		hText += "    <BLOCKQUOTE>Software created by Andrew Vaughan.<BR>Modified by Matthew Hausknecht.<BR>Project supervised and controlled by Phillip Wolff, PhD.</BLOCKQUOTE><BR>";
		
		hText += "  </BODY>\n";
		
		hText += "</HTML>";
		
		help.setText(hText);
	}
}

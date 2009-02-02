package util;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import javax.vecmath.*; // Color3f

/**
 * Stores commonly used utility methods
 * 
 * @author Matthew Hausknecht
 * @version August 6, 2008
 */
public class Util {
	
	public static final char NEG_CHAR = '\u00AC';
		
	public static final int CAUSES 		= 0;
	public static final int PREVENTS 	= 1;
	public static final int HELPS 		= 2;
	public static final int DESPITE 	= 3;
	public static final int ALLOWS 		= 4;
	public static final int INVALID		= 5;
	
	public static final String CAUSES_WORD 		= "Causes";
	public static final String PREVENTS_WORD 	= "Prevents";
	public static final String HELPS_WORD 		= "Helps";
	public static final String DESPITE_WORD 	= "Despite";
	//public static final String ALLOWS_WORD 		= "Allows"; //Removed by request of el jefe
	public static final String INVALID_WORD 	= "Invalid";
	
	public static final String[] WORDS = new String[] { CAUSES_WORD, PREVENTS_WORD,
		HELPS_WORD, DESPITE_WORD, INVALID_WORD};
	
	public static final int[] WORD_NUMS = new int[] { CAUSES, PREVENTS, HELPS,
		DESPITE, ALLOWS, INVALID };
	
	/**
	 * Defines colors which are used to represent the different
	 * verbs.
	 */
	public static Color3f CAUSES_COLOR =  // Pale Green
		new Color3f (152/255f, 251/255f, 152/255f);
	public static Color3f PREVENTS_COLOR =  // Light Sky Blue
		new Color3f (135/255f, 206/255f, 250/255f);
	public static Color3f HELPS_COLOR = // Light Salmon
		new Color3f (255/255f, 160/255f, 122/255f);
	public static Color3f DESPITE_COLOR = // Khaki
		new Color3f (240/255f, 230/255f, 140/255f);	
	public static Color3f ALLOWS_COLOR = // Light Coral
		new Color3f (240/255f, 128/255f, 128/255f);
	public static Color3f INVALID_COLOR = // Light Gray
		new Color3f (211/255f, 211/255f, 211/255f);
		
	public static final Color3f WHITE  = new Color3f(1.0f, 1.0f, 1.0f);
	public static final Color3f BLACK  = new Color3f(0.0f, 0.0f, 0.0f);
	public static final Color3f GREY   = new Color3f(0.5f, 0.5f, 0.5f);
	
	
	
	
//-------------------------METHODS--------------------------//
	
	/**
	 * Returns the color associated with a verbnum or black if 
	 * an invalid number was specified.
	 * @param verbNum
	 * @return
	 */
	public static Color3f getVerbColor (int verbNum)
	{
		switch (verbNum)
		{
			case 0: return CAUSES_COLOR;
			case 1: return PREVENTS_COLOR;
			case 2: return HELPS_COLOR;
			case 3: return DESPITE_COLOR;
			case 4: return ALLOWS_COLOR;
			case 5: return INVALID_COLOR;
		}
		return BLACK;
	}
	
	public static void setVerbColor (int verbNum, Color newColor)
	{
		Color3f newColor3f = new Color3f(newColor.getRed()/255f,
				newColor.getGreen()/255f, newColor.getBlue()/255f);
		
		setVerbColor(verbNum, newColor3f);
	}
	
	public static void setVerbColor (int verbNum, Color3f newColor)
	{
		switch (verbNum)
		{
		case 0: CAUSES_COLOR = newColor; break;
		case 1: PREVENTS_COLOR = newColor; break;
		case 2: HELPS_COLOR = newColor; break;
		case 3: DESPITE_COLOR = newColor; break;
		case 4: ALLOWS_COLOR = newColor; break;
		case 5: INVALID_COLOR = newColor; break;
		}
	}
	
	/**
	 * Returns the 
	 * @return
	 */
	public static int getVerbNum (String verb)
	{
		if (verb.equals(CAUSES_WORD)) {
			return CAUSES;
			
		} else if (verb.equals(PREVENTS_WORD)) {
			return PREVENTS;
			
		} else if (verb.equals(HELPS_WORD)) {
			return HELPS;
			
		} else if (verb.equals(DESPITE_WORD)) {
			return DESPITE;
		} 
		return INVALID;
	}
		
	/**
	 * Interpret a given number of milliseconds
	 * @param milli The given number of milliseconds
	 * @return Interpreted information
	 */
	public static String parseTime(long milli) {
		long sec = milli / 1000;
		long min = sec / 60;
		sec -= min * 60;
		
		long hr = min / 60;
		min -= hr * 60;
		
		long day = hr / 24;
		hr -= day * 24;
		
		return day + "d " + hr + "h " + min + "m " + sec + "s";
	}
	
	public static Color toColor (Color3f color)
	{
		return new Color(color.x,color.y,color.z);
	}
	
	/**
	 * Allows the user to choose a file with extension 'extension'.
	 * 
	 * @param extension Select only files with given extension
	 * @param description Description of file with given extension
	 * @return The file chooser object
	 */
	public static JFileChooser chooseFile (final String extension, final String description)
	{
		JFileChooser chooser = new JFileChooser();
    	
    	// Setup the chooser
    	chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	chooser.setFileFilter(new FileFilter() {
    		public boolean accept(File f) {
    			return f.getName().toLowerCase().endsWith(extension) || f.isDirectory();
    		}
    		public String getDescription() {
    			return description;
       		}
    	});
    	return chooser;
	}
}

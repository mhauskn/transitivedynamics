package util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Stores commonly used utility methods
 * 
 * @author Matthew Hausknecht
 * @version August 6, 2008
 */
public class util {
	
	public static final char NEG_CHAR = '\u00AC';

	
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

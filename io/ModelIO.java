package io;

import gui.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;


/**
 * Handles all file input and output for TDM files and image mapping.
 * 
 * @author Andrew Vaughan
 * @version August 1, 2006
 */
public class ModelIO {
	
	/**
	 * The separator used in the TDM file-structure.
	 */
	public final static String SEPARATOR = "|";
	
	
//---------------------------------------------------------------------------------------
// 	PUBLIC METHODS
	
	
	/**
	 * Saves the given container panel information as an image with the given filename.
	 * 
	 * @param cp the container panel to save
	 * @param imageName the string representation of the file to save the image as
	 */
	public static void saveImage(ContainerPanel cp, String imageName) {
        int width = cp.getWidth();
        int height = cp.getHeight();
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        cp.paint(g2);
        g2.dispose();
        
        try {
        	ImageIO.write(image, "png", new File(imageName));
        	
    	} catch(IOException ioe) {
    		System.out.println(ioe.getMessage());
    	}
	}
	
	
	/**
	 * Saves the given transative window as a TDM file.
	 * 
	 * @param cp the container panel to save
	 * @param toSave the file to save the container panel to
	 */
	public static void saveFile(TransativeWindow t, File toSave) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(toSave));
			
			output.write(t.window.getWidth() + SEPARATOR);					// Width
			output.write(t.window.getHeight() + SEPARATOR);					// Height
			output.write((t.isMagnitude() ? "1" : "0") + SEPARATOR);		// Magnitude
			output.write((t.isTCR() ? "1" : "0") + SEPARATOR);				// TCR
			output.write(t.cPanel.toString());								// Panels
			output.close();
			
		} catch (IOException e) {
			System.out.println("Unknown Error During Model Save");
		}
	}
	
	
	/**
	 * Loads a given TDM file to the given transative window.
	 * 
	 * @param cp the container panel to load to
	 * @param toLoad the file to load from
	 */
	public static void loadFile(TransativeWindow t, File toLoad) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(toLoad));
			
			String record = "";
			
			// Make sure the file is not null
			if ((record = input.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(record, SEPARATOR);
				
				int count = tok.countTokens();
				
				if (count >= 21) {
					// Load the primary information from the file
					int frameWidth = Integer.parseInt(tok.nextToken());
					int frameHeight = Integer.parseInt(tok.nextToken());
					boolean isMag = tok.nextToken().equals("1");
					boolean isTCR = tok.nextToken().equals("1");
					
					int numPanels = Integer.parseInt(tok.nextToken());
					
					// Make sure we have enough information left in the file to support the number of panels
					if (tok.countTokens() >= numPanels * 8) {
						t.cPanel.createModel(numPanels);
						
						for (int i = 0; i < numPanels; i++) {
							t.cPanel.panels[i].setA(Integer.parseInt(tok.nextToken()));
							t.cPanel.panels[i].setB(Integer.parseInt(tok.nextToken()));
							t.cPanel.panels[i].setE(Integer.parseInt(tok.nextToken()));
							t.cPanel.panels[i].setAWord(tok.nextToken());
							t.cPanel.panels[i].setASubscript(tok.nextToken().trim());
							t.cPanel.panels[i].setBWord(tok.nextToken().trim());
							t.cPanel.panels[i].setANegated(tok.nextToken().equals("1"));
							t.cPanel.panels[i].setENegated(tok.nextToken().equals("1"));
						}
						
						t.cPanel.update();
						t.cPanel.repaint();
						
						for (int i=0; i<numPanels-1; i++) {
							//t.cPanel.constrain[i].getConclusion();
							if(tok.nextToken().equals("1")) {
								t.cPanel.constrain[i].setConstrained(true);
								t.cPanel.constrain[i].setConstrainedVerb(t.cPanel.constrain[i].getConclusion());
								t.cPanel.constrain[i].update();
							} else {
								t.cPanel.constrain[i].setConstrained(false);
							}
						}
						
						t.setTCR(isTCR);
						t.setMagnitudes(isMag);
						
						t.window.setSize(frameWidth, frameHeight);
						t.window.validate();
						
						return;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Unknown Error During Model Load");
		}
		
		// We would have returned if it was valid
		System.out.println("Invalid TDM File");
	}
}

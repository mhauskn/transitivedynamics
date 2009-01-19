package gui;

import java.awt.*;
import javax.swing.JEditorPane;

/**
 * Used with the Help window.
 *
 */
public class InternetPane extends JEditorPane {
	private static final long serialVersionUID = -7110776245407164749L;
	
	public InternetPane() {
		super();
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		super.paintComponent(g2d);
	}
}

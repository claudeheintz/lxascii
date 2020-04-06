/** 
 * LXAsciiApp.java
 *
 * Title:			LX USITT ASCII tool
 * Description:		simple app that performs operations on USITT ASCII text
Copyright 2007-2020 Claude Heintz Design
 * @author			Claude Heintz
 * @version			
 * originally ASCIIApplet.java changed to LXAsciiApp.java 4/6/20
 */


import javax.swing.*;

public class LXAsciiApp {
	
	public LXAsciiApp() {
	
	}
	
	public static void Run (String clarg) {
		try {
			//comment out this to remove native look and feel
			try {
				//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} 
			catch (Exception e) { 
			}
			@SuppressWarnings("unused")			
			LXAsciiFrame frame;
			
			if ( clarg == null ) {
				frame = new LXAsciiFrame();
			} else {
				frame = new LXAsciiFrame(clarg);
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An app error occured:\n" + e );
		}
	}

	// Main entry point
	static public void main(String[] args) {
		if ( args.length > 0 ) {
			Run(args[0]);
		} else {
			Run(null);
		}
	}
	
}	


package launcher;

import gui.LauncherFrame;

import java.awt.EventQueue;

import utils.DirectoryHelper;
import utils.OperatingSystemHelper;

/**
 * Launcher starts the Terrae Rasa launcher application and stores a few different variables that are "global".
 * These mostly relate to forced installs and the different dialogs being open, to prevent duplicates.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Launcher {
	/** If true, an update will be forced when the play button is clicked. */
	public static boolean forceUpdate;
	/** If true, a forced update will also reinstall resources such as sound. */
	public static boolean reinstallResources;	
	/** Indicates whether or not the credits dialog is open. Used to prevent multiple dialogs open at once.*/
	public static boolean creditsDialogOpen;	
	/** Indicates whether or not the repair dialog is open. Used to prevent multiple dialogs open at once.*/
	public static boolean repairDialogOpen;	
	/** Indicates whether or not the download dialog is open. Used to prevent multiple dialogs open at once.*/
	public static boolean downloadDialogOpen;
	
	/**
	 * Launches the application. Before the frame is created, the OperatingSystemHelper and DirectoryHelper are initialized.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperatingSystemHelper.init();
					DirectoryHelper.init();					
					LauncherFrame frame = new LauncherFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

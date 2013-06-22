package gui;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * LauncherFrame implements the JFrame the launcher uses. The look and feel is set to be OS specified 
 * and the frame is by default centered, but resizable. Most of the actual form content is kept in 
 * this frame's contentPane, which is an instance of MainPanel.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class LauncherFrame extends JFrame {
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public LauncherFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}	
		final Image icon = Toolkit.getDefaultToolkit().getImage(LauncherFrame.class.getResource("/resources/iconV2.png"));
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 500);
		setTitle("Terrae Rasa Launcher");
		setLocationRelativeTo(null);		
		contentPane = new MainPanel();		
		setContentPane(contentPane);
	}
}

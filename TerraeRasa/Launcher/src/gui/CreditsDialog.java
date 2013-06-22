package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import launcher.Launcher;
import utils.ImageLoader;
/**
 * CreditsDialog is a pop-up window from the launcher that shows any applicable credits. Each credit is shown with a 
 * JLabel at this time due to there not being many. Version 1 credits only two people - Matt and Alec.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class CreditsDialog extends JDialog {
	private final BufferedImage background = ImageLoader.loadImage("resources/newsbackground.png");
	private final JPanel contentPanel = new JPanel() {
		protected void paintComponent(Graphics g)	{
			super.paintComponent(g);
			int x = 2048;
			int y = 2048;
			g.drawImage(background, 0, 0, x, y, null);
		}
	};

	/**
	 * Create the dialog
	 */
	public CreditsDialog() {		
		setBounds(100, 100, 450, 115);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setTitle("Terrae Rasa Credits");
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 450, 140);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
	
		//Create the label for the first person being credited
		JLabel firstCreditLabel = new JLabel("Alec Sobeck: Art and Code");
		firstCreditLabel.setBounds(20, 10, 440, 50);
		contentPanel.add(firstCreditLabel);
		firstCreditLabel.setForeground(Color.WHITE);
		firstCreditLabel.setFont(new Font("Cambria Math", Font.PLAIN, 24));
		//Create the label for the second person being credited
		JLabel secondCreditLabel = new JLabel("Matthew Robertson: Art and Code");
		secondCreditLabel.setBounds(20, 55, 440, 50);
		contentPanel.add(secondCreditLabel);
		secondCreditLabel.setForeground(Color.WHITE);
		secondCreditLabel.setFont(new Font("Cambria Math", Font.PLAIN, 24));
		
		//This window listener indicates when the credits dialog is closed. This
		//is used to prevent multiple of the same dialogs from being open at the
		//same time
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosed(WindowEvent e) {
		    	Launcher.creditsDialogOpen = false;
		    }
		});
	}
}

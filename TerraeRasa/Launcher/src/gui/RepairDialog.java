package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import utils.ImageLoader;
import java.awt.Component;
import java.awt.Color;

import launcher.Launcher;

/**
 * RepairDialog is a small pop-up window that allows the user to re-install applicable 
 * launcher or game resources to fix a broken install. By default only the "/bin" folder 
 * is reinstalled, but the user can choose to reinstall the significantly larger resources
 * folder as well.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class RepairDialog extends JDialog {
	private JButton okButton;
	private final BufferedImage background = ImageLoader.loadImage("resources/newsbackground.png");
	private final JPanel contentPanel = new JPanel() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int x = 2048;
			int y = 2048;
			g.drawImage(background, 0, 0, x, y, null);
		}
	};
	
	/**
	 * Create the dialog.
	 */
	public RepairDialog() {
		
		setResizable(false);
		setBounds(100, 100, 450, 180);
		setLocationRelativeTo(null);
		setModal(true);
		setTitle("Repair Terrae Rasa");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		//The text area that describes this feature
		final JTextArea descriptionTextArea = new JTextArea();
		descriptionTextArea.setForeground(Color.BLACK);
		descriptionTextArea.setAlignmentY(Component.TOP_ALIGNMENT);
		descriptionTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		descriptionTextArea.setEditable(false);
		descriptionTextArea.setBounds(10, 11, 424, 107);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setRows(10);
		descriptionTextArea.setText("The Terrae Rasa launcher can attempt to repair the " + '\n' + 
									"game if there are issues. This might be useful if " + '\n' + 
									"the game resources are corrupt or there are other " + '\n' + 
									"issues with the game install. Repairing will install" + '\n' + 
									"a clean version of the latest update.");
		//The button that activates repair functionality
		JButton repairButton = new JButton("Repair");
		repairButton.setBounds(25, 124, 111, 32);
		repairButton.setOpaque(false);
		repairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Sets the flag for forceUpdate to true, and indicates that to the user.
				Launcher.forceUpdate = true;
				descriptionTextArea.setText("Terrae Rasa will be repaired when the play button " + '\n' + "is clicked.");
			}
		});
		contentPanel.setLayout(null);
		contentPanel.add(descriptionTextArea);
		contentPanel.add(repairButton);
		//The ok button, which will close the dialog
		okButton = new JButton("OK");
		okButton.setBounds(335, 124, 99, 32);
		contentPanel.add(okButton);
		okButton.setOpaque(false);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Closes the dialog and actives the windowclosed listener
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);
		//The checkbox to indicate whether or not the user wants to redownload the 
		//resources that are saved to disk. This is used when exiting the form
		final JCheckBox reinstallCheckBox = new JCheckBox("Reinstall Resources");
		reinstallCheckBox.setForeground(Color.WHITE);
		reinstallCheckBox.setBounds(142, 129, 165, 23);
		reinstallCheckBox.setOpaque(false);
		contentPanel.add(reinstallCheckBox);
		
		//Called when this dialog is closed. Allows this dialog to be re-opened and also records
		//the value of the reinstallCheckbox incase the user wanted to reinstall
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosed(WindowEvent e) {
		    	Launcher.reinstallResources = reinstallCheckBox.isSelected();
		    	Launcher.repairDialogOpen = false;
		    }
		});
	}
}

package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import launcher.Launcher;
import utils.DirectoryHelper;
import utils.FileHelper;
import utils.GameLauncher;
import web.WebDownloader;

/**
 * DownloadDialog handles what happens after the user wants to play. If they have a forced-reinstall then that
 * takes priority. Otherwise, the game will attempt to look up the version of the game installed and current
 * version online and react accordingly - no installed version is a forced install; no online version
 * available means the user can simply play without any update; and a newer version online asks if the user
 * wants to update. Anything else means the user simply gets to start playing.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DownloadDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private JLabel informationLabel;
	private JButton confirmButton;
	private JButton denyButton;
	private boolean canPlayOffline = false;
	private boolean buttonLock = false;
	private boolean newVersionAvailable = false;
	
	public DownloadDialog() {
		setBounds(100, 100, 400, 175);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setTitle("Terrae Rasa");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		//Progress bar
		progressBar = new JProgressBar();
		progressBar.setBounds(15, 16, 370, 22);
		//Information Label - shows the user a short message
		informationLabel = new JLabel("");
		informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationLabel.setBounds(15, 44, 370, 47);
		informationLabel.setFont(new Font("Cambria Math", Font.PLAIN, 22));
		informationLabel.setVisible(false);
		//Confirm button
		confirmButton = new JButton("Yes");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(buttonLock) {
					return;
				}
				//If the online version is not available, offer this option
				if(canPlayOffline) {
					GameLauncher.play();
					buttonLock = true;
				}				
				//if a new version is available, offer this option
				if(newVersionAvailable) {
					buttonLock = true;
					install(true, true);					
				}
			}
		});
		confirmButton.setBounds(15, 90, 120, 33);
		confirmButton.setVisible(false);
		//Deny Button
		denyButton = new JButton("No");
		denyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(buttonLock) {
					return;
				}
				//If the online version is not available, offer this option
				if(canPlayOffline)
				{
					buttonLock = true;
					dispose();
				}
				//if a new version is available, offer this option
				if(newVersionAvailable)
				{
					buttonLock = true;
					GameLauncher.play();
				}
			}
		});
		denyButton.setBounds(265, 90, 120, 33);
		denyButton.setVisible(false);
		contentPanel.setLayout(null);
		contentPanel.add(confirmButton);
		contentPanel.add(denyButton);
		contentPanel.add(informationLabel);
		contentPanel.add(progressBar);

		//When the window is closed, allow it to be reopened 
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosed(WindowEvent e) {
		    	Launcher.downloadDialogOpen = false;
		    }
		});
		
		//When the dialog is shown, then compare the versions and do something appropriate
		this.addComponentListener(new ComponentAdapter() {
		    public void componentShown(ComponentEvent e) {
		    	compareVersions();
		    }
		});		
	}

	/**
	 * Attempts to install the game from the web server.
	 * @param includeSound true if sound resources should be downloaded, otherwise false
	 */
	private void install(boolean includeSound, boolean includeOnlyNeededResources) {
		WebDownloader downloader = new WebDownloader();
		downloader.downloadAllRequiredFiles(progressBar, includeSound, includeOnlyNeededResources);
	}
	
	/**
	 * Attempts to get the latest game version. If there is none available then it gives a value of "-1"
	 * @return the latest game version; or -1 if the version is not available
	 */
	private String getLatestVersion()
	{
		WebDownloader downloader = new WebDownloader();
		try {
			return downloader.getLatestVersion();
		} catch (IOException e) {
			return "-1";
		}
	}
	
	/**
	 * Attempts to get the installed version. If there is none gives a value of "-1"
	 * @return the installed version; or -1 if no version installed
	 */
	private String getInstalledVersion()
	{
		try {
			return FileHelper.getInstalledVersion();
		} catch (IOException e) {
			return "-1";
		}
	}
	
	/**
	 * Compares the currently installed version and most recently available version, and offers
	 * the user an appropriate choice or response to them.
	 */
	private void compareVersions()
	{
		DirectoryHelper.createDirectories();
		String latestVersion = getLatestVersion();
		String installedVersion = getInstalledVersion();
		
		if(Launcher.forceUpdate) {
			//Forces an update then begins to play, if possible.
			buttonLock = false;
			informationLabel.setVisible(true);
			informationLabel.setText("Attempting to Repair...");
			install(true, false);
		}
		else if(installedVersion.equals("-1")) {
			//Attempts a full install because no version is present. This will fail if no new version 
			//is actually available though...
			buttonLock = false;
			informationLabel.setVisible(true);
			if(latestVersion.equals("-1")) {
				//This indicates there is no install, and no connection available.
				informationLabel.setText("Failed Download.");
				JOptionPane.showMessageDialog(null, 
						"Terrae Rasa requires an internet connection for the " + '\n' +
				 		"initial download. Please connect to the internet to " + '\n' + 
				 		"install the game", 
				 		"Failed Download",
				 		JOptionPane.OK_OPTION);
			}
			else {
				informationLabel.setText("Downloading...");
				install(true, false);
			}
		}
		else if(latestVersion.equals("-1")) {
			//Indicates the web server is not available, but there is a game installed.
			//Offer to play that.
			buttonLock = false;
			informationLabel.setVisible(true);
			confirmButton.setVisible(true);
			denyButton.setVisible(true);
			informationLabel.setText("Update Server not available. Play anyway?");
			canPlayOffline = true;
		}
		else if(!latestVersion.equals(installedVersion)) {
			//Indicates there is both a version installed and a different (newer) online version. 
			//Ask the user if they want to download the newer version.
			buttonLock = false;
			informationLabel.setVisible(true);
			confirmButton.setVisible(true);
			denyButton.setVisible(true);
			informationLabel.setText("New Version Available. Update?");
			newVersionAvailable = true;
		}
		else
		{
			//Game is up to date with no issues - time to play!
			GameLauncher.play();
		}
	}
}

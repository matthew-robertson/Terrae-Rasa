package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import launcher.Launcher;

import utils.ImageLoader;

/**
 * MainPanel is an extension of JPanel that implements most of the launcher's layout and design. MainPanel contains
 * the the logoPanel, the sidebarPanel, the newsPanel and all of the components therein. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JEditorPane newsDisplayEditorPane;
	private final BufferedImage background = ImageLoader.loadImage("resources/sidebar.png");
	/** The main logo font is Segoe Print Bold */
	private final BufferedImage logo = ImageLoader.loadImage("resources/logo.png");
	private final JPanel logoPanel;
	private final JPanel sidebarPanel;
	private final JPanel newsPanel;
	
	/**
	 * Creates the panel.
	 */
	public MainPanel() {
		//LogoPanel initialization
		logoPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(logo, 0, 0, logo.getWidth(), logo.getHeight(), null);
			}	
		};
		logoPanel.setOpaque(false);		
		//SidebarPanel initialization
		sidebarPanel = new JPanel();
		sidebarPanel.setOpaque(false);		
		//NewsPanel initialization
		newsPanel = new JPanel();
		newsPanel.setOpaque(false);
		//Repair Button
		JButton repairButton = new JButton("Repair");
		repairButton.setFont(new Font("Cambria Math", Font.PLAIN, 24));
		repairButton.setOpaque(false);
		repairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!Launcher.repairDialogOpen) {
					//Creates the repair dialog if it isnt already open
					RepairDialog dialog = new RepairDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					Launcher.repairDialogOpen = true;
				}
			}
		});
		//Play Button
		JButton playButton = new JButton("Play");
		playButton.setFont(new Font("Cambria Math", Font.PLAIN, 24));
		playButton.setOpaque(false);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!Launcher.downloadDialogOpen) {
					//Creates the play dialog if it isnt already open
					DownloadDialog dialog = new DownloadDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					Launcher.downloadDialogOpen = true;
				}
			}
		});
		//Credits Button
		JButton creditsButton = new JButton("Credits");
		creditsButton.setFont(new Font("Cambria Math", Font.PLAIN, 24));
		creditsButton.setOpaque(false);
		creditsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!Launcher.creditsDialogOpen) {
					//Creates the credits dialog if it isnt already open
					CreditsDialog dialog = new CreditsDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					Launcher.creditsDialogOpen = true;
				}
			}
		});
		//The JScrollPane and JEditorPane for the HTML news contained within it
		//The JScrollPane
		JScrollPane scrollPane = new JScrollPane();		
		//The JEditorPane with the news in it. If the catch clause of this is triggered
		//due to a failed download, a generic failednews.html page is used.
		try {
			newsDisplayEditorPane = new JEditorPane("http://dl.dropbox.com/u/11203435/TerraeRasa/News.html");
		} catch (IOException e) {
			try {
				newsDisplayEditorPane = new JEditorPane(MainPanel.class.getClassLoader().getResource("resources/failednews.html"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}	
		newsDisplayEditorPane.setEditable(false);
		newsDisplayEditorPane.setBorder(null);
		scrollPane.setViewportView(newsDisplayEditorPane);
		
		//Main Form Layout
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(newsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(1))
						.addComponent(logoPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sidebarPanel, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(logoPanel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(newsPanel, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
				.addComponent(sidebarPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
		);
		
		//Sidebar Panel layout
		GroupLayout gl_sidebarPanel = new GroupLayout(sidebarPanel);
		gl_sidebarPanel.setHorizontalGroup(
			gl_sidebarPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sidebarPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sidebarPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(playButton, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
						.addComponent(creditsButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
						.addComponent(repairButton, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_sidebarPanel.setVerticalGroup(
			gl_sidebarPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_sidebarPanel.createSequentialGroup()
					.addContainerGap(193, Short.MAX_VALUE)
					.addComponent(repairButton, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(playButton, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(creditsButton, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		sidebarPanel.setLayout(gl_sidebarPanel);
				
		//News panel layout
		GroupLayout gl_newsPanel = new GroupLayout(newsPanel);
		gl_newsPanel.setHorizontalGroup(
			gl_newsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
		);
		gl_newsPanel.setVerticalGroup(
			gl_newsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
		);
		newsPanel.setLayout(gl_newsPanel);
		setLayout(groupLayout);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width < 2048) ? 2048 : Toolkit.getDefaultToolkit().getScreenSize().width;
		int y = (Toolkit.getDefaultToolkit().getScreenSize().height < 2048) ? 2048 : Toolkit.getDefaultToolkit().getScreenSize().height;
		g.drawImage(background, 0, 0, x, y, null);
	}
}

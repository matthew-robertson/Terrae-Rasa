package net.dimensia.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class GuiMainMenu extends JFrame
{
	public static boolean isDownloadOpen;
	public static GuiDownload downloadWindow;
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JPanel loginFields;
	private JEditorPane editorPane;
	private JButton updateButton;
	private JButton	loginButton;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JPanel contentPane;
	private JPanel loginPane;
	private JPanel imagePanel;
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	public static BufferedImage loginBarBackground;
	public static BufferedImage logo;

	public GuiMainMenu()
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
			setTitle("Dimensia");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			getContentPane().setLayout(new BorderLayout());
			setBounds(0, 0, 846, 456); 
			setLocationRelativeTo(null); //Center the frame
			
			//news Content pane
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			
			//HTML news
			try
			{
				editorPane = new JEditorPane("http://dl.dropbox.com/u/11203435/Dimensia/News.html");
			}
			catch (IOException e)
			{
				System.err.println("Error Loading News");
				editorPane = new JEditorPane(GuiMainMenu.class.getClassLoader().getResource("Resources/Test.html"));
			}
			editorPane.setEditable(false);
			editorPane.setBorder(null);
  			scrollPane = new JScrollPane(editorPane);
  			contentPane.add(scrollPane);
		
  			//Login Panel (the thing at the bottom)
  			loginBarBackground = loadImage("Resources/background.png");
  			logo = loadImage("Resources/logo.png");
  			
  			loginPane = new JPanel()
  			{
  				protected void paintComponent(Graphics g) //override paint to set the background image
  				{
  					super.paintComponent(g);
  					g.drawImage(loginBarBackground, 0,0, Toolkit.getDefaultToolkit().getScreenSize().width ,Toolkit.getDefaultToolkit().getScreenSize().height, null);
  				}
  			};
  			loginPane.setBorder(null);
  			loginPane.setPreferredSize(new Dimension(1200, 100));
  			loginPane.setLayout(new GridLayout(0, 2, 0, 0));
  			getContentPane().add(loginPane, BorderLayout.PAGE_END);
  			
  			//Password, Login, Update, button panel					
			loginFields = new JPanel()
			{
				protected void paintComponent(Graphics g) //override paint to set the background image
				{
					super.paintComponent(g);
					g.drawImage(loginBarBackground, 0,0, Toolkit.getDefaultToolkit().getScreenSize().width ,Toolkit.getDefaultToolkit().getScreenSize().height, null);
				}
			};
			loginFields.setAlignmentX(Component.RIGHT_ALIGNMENT);
			loginPane.add(loginFields);
			
			//Things that go inside loginFields (JPanel)
			//Update Button
			updateButton = new JButton("Update");
			updateButton.setPreferredSize(new Dimension(100, 40));
			updateButton.setFocusable(false);
			updateButton.setFont(new Font("Arial", Font.PLAIN, 11));
	
			//Play(Login) Button
  			loginButton	= new JButton("Login");
			loginButton.setFont(new Font("Arial", Font.PLAIN, 11));
			loginButton.setFocusable(false);
			loginButton.setPreferredSize(new Dimension(100, 40));

			//Username label
			usernameLabel = new JLabel("Username: ");
			usernameLabel.setForeground(Color.white);
			
			//Password label
			passwordLabel = new JLabel("Password:");
			passwordLabel.setForeground(Color.white);
			
			//Player Name TextField
			usernameTextField = new JTextField();
			usernameTextField.setMaximumSize(new Dimension(100, 35));
			usernameTextField.setColumns(10);
			
			//Password Field
			passwordField = new JPasswordField();
			GroupLayout gl_loginFields = new GroupLayout(loginFields);
			gl_loginFields.setHorizontalGroup(
				gl_loginFields.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_loginFields.createSequentialGroup()
						.addGap(15)
						.addGroup(gl_loginFields.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_loginFields.createSequentialGroup()
								.addComponent(updateButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(16)
								.addComponent(usernameLabel)
								.addGap(15)
								.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_loginFields.createSequentialGroup()
								.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(passwordLabel)
								.addGap(18)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))))
			);
			gl_loginFields.setVerticalGroup(
				gl_loginFields.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_loginFields.createSequentialGroup()
						.addGap(14)
						.addGroup(gl_loginFields.createParallelGroup(Alignment.LEADING)
							.addComponent(updateButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_loginFields.createSequentialGroup()
								.addGap(9)
								.addComponent(usernameLabel))
							.addGroup(gl_loginFields.createSequentialGroup()
								.addGap(6)
								.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(5)
						.addGroup(gl_loginFields.createParallelGroup(Alignment.LEADING)
							.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_loginFields.createSequentialGroup()
								.addGap(10)
								.addComponent(passwordLabel))
							.addGroup(gl_loginFields.createSequentialGroup()
								.addGap(7)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
			);
			loginFields.setLayout(gl_loginFields);

			//Panel that has the logo painted on it
			imagePanel = new JPanel()
			{
				protected void paintComponent(Graphics g) //override paint to set the background image
				{
					super.paintComponent(g);
					g.drawImage(loginBarBackground, 0,0, Toolkit.getDefaultToolkit().getScreenSize().width ,Toolkit.getDefaultToolkit().getScreenSize().height, null);
					g.drawImage(logo, 0, 0, null);
				}
			};
			imagePanel.setPreferredSize(new Dimension(300, 100));
			imagePanel.setLayout(new GridLayout(1, 0, 0, 0));
			loginPane.add(imagePanel);
			
			initializeButtonListeners();
			
			setVisible(true);
		}
		catch(Exception e) /*Mostly refers the the 5+ exceptions that can occur from setNativeLookAndFeel()*/
		{
			e.printStackTrace();
		} 
	}
	
	private BufferedImage loadImage(String ref)
    {
    	try
    	{
	        URL url = GuiMainMenu.class.getClassLoader().getResource(ref);
	
	        if (url == null) 
	        {
	            throw new IOException("Cannot find: " + ref);
	        }
	
	        Image img = new ImageIcon(url).getImage();
	        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB/*It's crucial this is _ARGB for alpha*/);
	        Graphics g = bufferedImage.getGraphics();
	        g.drawImage(img, 0, 0, null);
	        g.dispose();
	
	        return bufferedImage;
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	return null;
    }
		
	private void initializeButtonListeners()
	{
		loginButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
            {
				if(!play())
				{
					JOptionPane.showMessageDialog(null, "Missing Files. Please reinstall.");
				}
            }
        });      
 
		updateButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
            {
				
                openDownloadWindow();
            }
        });
	}

	private void openDownloadWindow()
	{
		if(!isDownloadOpen)
		{
			isDownloadOpen = true;
			EventQueue.invokeLater(new Runnable() 
			{
				public void run() 
				{
					downloadWindow = new GuiDownload();
				}
			});
		}
	}
	
	private boolean play()
	{
		FileManager fileManager = new FileManager();
		String path = fileManager.getBasePath();
		
		if(fileManager.canLaunch())
		{
			try 
			{
				if(fileManager.getOsName().toLowerCase().contains("mac"))
				{
					Runtime runtime = Runtime.getRuntime();
					String applescriptCommand = "do shell script \"" + new StringBuilder().append("java -jar /Users/"+ System.getProperty("os.name") + "/Library/Application").append((char)(92)).append((char)(92)).append(" Support/Dimensia/bin/Dimensia.jar").toString() + "\" \n";
					String[] args1 = { "osascript", "-e", applescriptCommand };
					Process process = runtime.exec(args1);
				}
				else
				{
					System.out.println("java -jar " + path + "/bin/Dimensia.jar");
					System.out.println( new File( path + "/bin/Dimensia.jar").exists());
					Runtime.getRuntime().exec("java -jar " + path + "/bin/Dimensia.jar");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			} 
			
			System.exit(1);
			return true;
		}
		return false;
	}
}
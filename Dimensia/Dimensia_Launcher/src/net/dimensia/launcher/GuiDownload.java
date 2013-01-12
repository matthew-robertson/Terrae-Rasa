package net.dimensia.launcher;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

public class GuiDownload extends JFrame implements ActionListener, PropertyChangeListener, WindowListener
{	
	private FileManager fileManager;
	private static final long serialVersionUID = 1L;	
	private JPanel contentPane;
	private static JScrollPane messageLogScroll;
	private static JTextPane messageLog;
	private static JProgressBar progressBar;
	private JButton downloadButton;
	private JButton quitButton;
	private JLabel logo;
		
	public GuiDownload()
	{
		fileManager = new FileManager();
		
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) /*Mostly refers the the 5+ exceptions that can occur from setNativeLookAndFeel()*/
		{
			e.printStackTrace();
		}
		
		contentPane = new JPanel()
		{
			protected void paintComponent(Graphics g) //override paint to set the background image
			{
				super.paintComponent(g);
				g.drawImage(GuiMainMenu.loginBarBackground, 0,0, Toolkit.getDefaultToolkit().getScreenSize().width ,Toolkit.getDefaultToolkit().getScreenSize().height, null);
			}
		};
		setTitle("Dimensia Install");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setBounds(0, 0, 846, 456); 
		setLocationRelativeTo(null); //Center the frame
		setContentPane(contentPane);
		
		//Download Progress Bar
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setForeground(Color.green);
		
		//Message Log and scroller
		messageLog = new JTextPane();
		messageLog.setBorder(null);
		messageLog.setEditable(false);
		messageLogScroll = new JScrollPane(messageLog);
		
		//Download Button
		downloadButton = new JButton("Start Download");
		downloadButton.setSize(new Dimension(150, 80));
		downloadButton.setFocusable(false);
		downloadButton.setPreferredSize(new Dimension(150, 40));
		
		//Quit Button
		quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(150, 80));
		quitButton.setFocusable(false);
		quitButton.setSize(new Dimension(150, 80));
		
		//The logo at the top (fixed size)
		logo = new JLabel(new ImageIcon(loadImage("Resources/logo.png")));
		
		//layout 
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(166)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(60)
							.addComponent(downloadButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
							.addComponent(quitButton, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
							.addGap(238))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(progressBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
								.addComponent(logo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
								.addComponent(messageLogScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
							.addGap(196))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(logo, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(messageLogScroll, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(downloadButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addComponent(quitButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
					.addGap(24))
		);
		contentPane.setLayout(gl_contentPane);

		initializeButtonListeners();		
		addWindowListener(this);	
	    setVisible(true);
	}
	
	private void initializeButtonListeners()
	{
		quitButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
            {
                dispose(); //destroy the frame
            }
        });      
 
		downloadButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent evt)
            {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		    	fileManager = new FileManager();
		    	fileManager.addPropertyChangeListener(GuiMainMenu.downloadWindow); //let the progress bar update when the thread offers a new completion %
		    	fileManager.execute(); //Start download
			}
        });
	}
	
	private BufferedImage loadImage(String ref)
    {
    	try
    	{
	        URL url = GuiDownload.class.getClassLoader().getResource(ref);
	
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
		
	/**
	 * Add a message to the end of the text area. Automatically applies \n
	 * @param message the text to add to the end of the text area
	 */
	public synchronized static void log(String message)
	{
		if(message == null)
		{
			messageLog.setText("");
		}
		else
		{
			messageLog.setText(messageLog.getText() + message + '\n');
		}
	}
	
	public synchronized static void updateProgressBar(int completePercent)
	{
		progressBar.setValue(completePercent);
	}
	
    public void propertyChange(PropertyChangeEvent evt) 
    {
    	if ("progress" == evt.getPropertyName()) 
    	{
    		int progress = (Integer) evt.getNewValue();
    		progressBar.setValue(progress);
    	} 
        
    	if(fileManager.isDone())
    	{
    		Toolkit.getDefaultToolkit().beep();
    		GuiMainMenu.isDownloadOpen = false;
    		setCursor(null); //turn off the wait cursor
    	}
    }

	public void windowClosing(WindowEvent e) 
	{
		GuiMainMenu.isDownloadOpen = false;
	}
    
	public void actionPerformed(ActionEvent e) {}	
	public void windowClosed(WindowEvent arg0) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}	
}
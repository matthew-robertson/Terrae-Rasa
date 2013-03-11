package debug;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DebugFrame extends JPanel
{
	public DebugFrame(BufferedImage image)
	{
		this.img = image;
		repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawImage(img, 100, 100, 356, 612, this);
		
		
	}
	
	BufferedImage img;
	
}

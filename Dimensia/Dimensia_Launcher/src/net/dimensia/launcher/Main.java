package net.dimensia.launcher;
import java.awt.EventQueue;

public class Main 
{
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				GuiMainMenu gui = new GuiMainMenu();
			}
		});
	}	
}

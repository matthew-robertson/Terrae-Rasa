package server;

import java.util.Scanner;

public class ConsoleInputThread extends Thread
{
	private boolean done;
	
	public ConsoleInputThread()
	{
		setDaemon(true);
		done = false;
	}
	
	public void run()
	{
		while(!done)
		{
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			TerraeRasa.addServerIssuedCommand(input);
		}
	}
	
	public void close()
	{
		done = true;
		//This will likely throw something.
		interrupt();
	}
}

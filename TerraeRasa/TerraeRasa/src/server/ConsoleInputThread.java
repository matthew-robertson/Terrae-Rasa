package server;

import java.util.Scanner;

import entry.MPGameEngine;

public class ConsoleInputThread extends Thread
{
	private boolean done;
	private Scanner scanner;
	
	public ConsoleInputThread()
	{
		setName("ConsoleInputThread");
		setDaemon(true);
		done = false;
		scanner = new Scanner(System.in);
		Log.log("Starting the console input thread");
	}

	public void run()
	{
		while(!done)
		{
			String input = scanner.nextLine();
			MPGameEngine.addServerIssuedCommand(input);
		}
		System.out.println("Terminating the console input thread.");
	}
}

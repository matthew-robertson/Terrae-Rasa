package server;

import java.net.Socket;

public class ClientConnectionThread extends Thread
{
	private Socket socket;
	
	public ClientConnectionThread(Socket socket)
	{
		setDaemon(true);
		this.socket = socket;
	}
	
	public void run()
	{
		//This would be where the actual important stuff happens, if it were applicable.
		System.out.println("Useful client thread started.");
		
		while(true)
		{
			System.out.println("Still being useful.");
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

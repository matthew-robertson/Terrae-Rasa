package server;

import java.net.Socket;

public class ServerConnectionThread extends Thread
{
	private Socket socket;
	private WorldLock worldLock;
	
	public ServerConnectionThread(WorldLock lock, Socket socket)
	{
		this.socket = socket;
		this.worldLock = lock;
		setDaemon(true);
	}
	
	public void run()
	{
		System.out.println("Really useful thread is really useful.");
		
		while(true)
		{
			System.out.println("Doing a thing...");
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		ObjectOutputStream os = null;
		ObjectInputStream is = null;
		try {
			 os = new ObjectOutputStream(socket.getOutputStream());
			 is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			os.writeUTF("Test");
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
}

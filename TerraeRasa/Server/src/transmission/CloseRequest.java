package transmission;

import server.ServerConnectionThread;
import entities.EntityPlayer;

public class CloseRequest {
	public ServerConnectionThread thread;
	public EntityPlayer player;
	
	public CloseRequest(ServerConnectionThread thread, EntityPlayer player)
	{
		this.thread = thread;
		this.player = player;
	}
}

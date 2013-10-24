package server;

import server.entities.EntityPlayer;

public class PlayerIssuedCommand 
{
	private EntityPlayer player;
	private String actualCommand;
	
	public PlayerIssuedCommand(EntityPlayer associatedPlayer, String actualCommand)
	{
		this.player = associatedPlayer;
		this.actualCommand = actualCommand;
	}
	
	public EntityPlayer getAssociatedPlayer()
	{
		return player;
	}

	public String getActualCommand()
	{
		return actualCommand;
	}
}

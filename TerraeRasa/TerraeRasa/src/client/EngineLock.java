package client;

import java.util.Vector;

import transmission.CompressedClientUpdate;
import transmission.CompressedPlayer;
import transmission.CompressedServerUpdate;
import world.World;
import entities.EntityPlayer;
import enums.EnumHardwareInput;

public class EngineLock 
{
	private Vector<CompressedClientUpdate> clientUpdates = new Vector<CompressedClientUpdate>();
	private GameEngine engine;
	private Vector<CompressedServerUpdate> serverUpdates = new Vector<CompressedServerUpdate>();
//	private EntityPlayer relevantPlayer;
	
	public EngineLock(GameEngine engine)
	{
		this.engine = engine;
	}
	
	public synchronized void addClientUpdate(CompressedClientUpdate update)
	{
		clientUpdates.add(update);
	}
	
	//Deletes too!
	public synchronized CompressedClientUpdate[] yieldClientUpdates()
	{
		CompressedClientUpdate[] updates = new CompressedClientUpdate[clientUpdates.size()];
		clientUpdates.copyInto(updates);
		clientUpdates.clear();
		return updates;
	}
	
	public synchronized void addUpdate(CompressedServerUpdate update)
	{
		serverUpdates.add(update);
	}
	
	//Deletes too
	public synchronized CompressedServerUpdate[] yieldServerUpdates()
	{
		CompressedServerUpdate[] updates = new CompressedServerUpdate[serverUpdates.size()];
		serverUpdates.copyInto(updates);
		serverUpdates.clear();
		return updates;
	}
	
	public synchronized boolean hasUpdates()
	{
		return serverUpdates.size() > 0;
	}
	
	public synchronized boolean hasClientUpdates()
	{
		return clientUpdates.size() > 0;
	}
		
	public synchronized void setPlayer(EntityPlayer player)
	{
		engine.setActivePlayerID(player.entityID);
	}
	
	public synchronized void updatePlayer(CompressedPlayer player)
	{
		throw new RuntimeException("NYI");
		//TODO: EngineLock.updatePlayer() does not work at all
	//	engine.getPlayer().mergeOnto(player);
	}
	
	public synchronized EntityPlayer getSentPlayer()
	{
		return engine.getSentPlayer();
	}
	
	public synchronized void setWorld(World world)
	{
		engine.setWorld(world);
		GameEngine.flagAsMPPlayable();
	}
	
	public synchronized void setActivePlayerID(int id)
	{
		engine.setActivePlayerID(id);
	}
}

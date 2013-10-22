package client;

import java.util.Vector;

import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.SuperCompressedChunk;
import client.entities.EntityPlayer;
import client.world.WorldClientEarth;
import entry.SPGameLoop;

public class EngineLock 
{
	private Vector<CompressedClientUpdate> clientUpdates = new Vector<CompressedClientUpdate>();
	private SPGameLoop engine;
	private Vector<CompressedServerUpdate> serverUpdates = new Vector<CompressedServerUpdate>();
//	private EntityPlayer relevantPlayer;
	
	public EngineLock(SPGameLoop engine)
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
	
	public synchronized EntityPlayer getSentPlayer()
	{
		return engine.getActivePlayer();
	}
	
	public synchronized void setWorld(WorldClientEarth world)
	{
		engine.setWorld(world);
		SPGameLoop.flagAsMPPlayable();
	}
	
	public synchronized void setActivePlayerID(int id)
	{
		engine.setActivePlayerID(id);
	}

	public String getActivePlayerName() {
		return engine.getActivePlayerName();
	}
	
	public void expandChunk(SuperCompressedChunk chunk)
	{
		engine.registerChunkExpand(chunk);
	}
}

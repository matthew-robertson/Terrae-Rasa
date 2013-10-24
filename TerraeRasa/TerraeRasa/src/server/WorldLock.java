package server;



import java.util.Vector;

import blocks.Chunk;

import server.entities.EntityPlayer;
import transmission.CompressedClientUpdate;
import transmission.ServerUpdate;
import transmission.TransmittablePlayer;
import transmission.WorldData;
import entry.MPGameEngine;
import entry.MPGameLoop;

public class WorldLock 
{
	private MPGameLoop engine;
	private Vector<ServerUpdate> serverUpdates = new Vector<ServerUpdate>();
	private EntityPlayer relevantPlayer;

	public WorldLock(MPGameLoop engine)
	{
		this.engine = engine;
	}
	
	//This can be null. Indicates chunk load failure.
	public Chunk requestChunk(int x)
	{
		return engine.requestChunk(x);
	}
	
	public WorldData getWorldData()
	{
		return engine.getWorld().getWorldData();
	}
	
	public void addPlayerToWorld(EntityPlayer player)
	{		
		engine.getWorld().addPlayerToWorld(MPGameEngine.terraeRasa.getSettings(), player);
		engine.registerPlayer(player);
		relevantPlayer = player;
	}
	
	public Chunk[] getChunks(int[] req)
	{
		Chunk[] chunks = new Chunk[req.length];
		for(int i = 0; i < chunks.length; i++)
		{
			chunks[i] = engine.getWorld().getChunk(req[i]);
		}
		return chunks;
	}
	
	public Chunk[] getInitialChunks()
	{
		ServerSettings settings = MPGameEngine.terraeRasa.getSettings();
		final int loadDistanceHorizontally = (settings.loadDistance * Chunk.getChunkWidth() >= 200) ? settings.loadDistance * Chunk.getChunkWidth() : 200;
		
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftOff = (engine.getWorld().getWorldCenterBlock() - loadDistanceHorizontally) / Chunk.getChunkWidth();
		int rightOff = (engine.getWorld().getWorldCenterBlock() + loadDistanceHorizontally) / Chunk.getChunkWidth();
		
		Vector<Chunk> chunks = new Vector<Chunk>();
		for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
		{
			chunks.add(engine.requestChunk(i));
		}
		Chunk[] c = new Chunk[chunks.size()];
		chunks.copyInto(c);
		return c;
	}

	//This is from the server
	public synchronized void addUpdate(ServerUpdate update)
	{
		if(getRelevantPlayer() != null)
		{
			serverUpdates.add(update);
		}
	}
	
	//Deletes too
	public ServerUpdate[] yieldServerUpdates()
	{
		ServerUpdate[] updates = new ServerUpdate[serverUpdates.size()];
		serverUpdates.copyInto(updates);
		serverUpdates.clear();
		return updates;
	}
	
	public EntityPlayer getRelevantPlayer()
	{
		return relevantPlayer;
	}
	
	public void registerPlayerUpdate(CompressedClientUpdate[] updates)
	{
		for(CompressedClientUpdate update : updates)
		{
			PlayerInput input = new PlayerInput(relevantPlayer, update.clientInput);
			engine.getWorld().registerPlayerMovement(input);
			engine.registerClientUpdate(update);
		}
	}

	public TransmittablePlayer[] requestOtherPlayers()
	{
		EntityPlayer[] players = engine.getPlayersArray();
		TransmittablePlayer[] compPlayers = new TransmittablePlayer[players.length];
		for(int i = 0; i < compPlayers.length; i++)
		{
			compPlayers[i] = players[i].getTransmittable();
		}		
		return compPlayers;
	}

}
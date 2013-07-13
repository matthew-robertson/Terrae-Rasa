package server;

import io.Chunk;

import java.util.Vector;

import transmission.CompressedPlayer;
import transmission.CompressedServerUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import entities.EntityPlayer;
import enums.EnumHardwareInput;

public class WorldLock 
{
	private GameEngine engine;
	private Vector<CompressedServerUpdate> serverUpdates = new Vector<CompressedServerUpdate>();
	private EntityPlayer relevantPlayer;
	
	public WorldLock(GameEngine engine)
	{
		this.engine = engine;
	}
	
	public synchronized void issueConsoleCommand(EntityPlayer associatedPlayer, String command)
	{
		//TODO: console commands
	}
	
	//This can be null. Indicates chunk load failure.
	public synchronized Chunk requestChunk(int x)
	{
		return engine.requestChunk(x);
	}
	
	public synchronized WorldData getWorldData()
	{
		return engine.getWorld().getWorldData();
	}
	
	public synchronized void addPlayerToWorld(EntityPlayer player)
	{
		engine.getWorld().addPlayerToWorld(TerraeRasa.terraeRasa.getSettings(), player);
		engine.registerPlayer(player);
		relevantPlayer = player;
	}
	
	public synchronized Chunk[] getChunks(int[] req)
	{
		Chunk[] chunks = new Chunk[req.length];
		for(int i = 0; i < chunks.length; i++)
		{
			chunks[i] = engine.getWorld().getChunk(req[i]);
		}
		return chunks;
	}
	
	public synchronized Chunk[] getInitialChunks()
	{
		ServerSettings settings = TerraeRasa.terraeRasa.getSettings();
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
	
	public synchronized void addUpdate(ServerUpdate update)
	{
		if(getRelevantPlayer() != null)
		{
			CompressedServerUpdate compressedUpdate = new CompressedServerUpdate();
			compressedUpdate.values = update.getValues();
			compressedUpdate.chunks = new SuperCompressedChunk[]{ };
		//	compressedUpdate.player = null;
			compressedUpdate.entityUpdates = update.getUpdates();
			compressedUpdate.positionUpdates = update.getPositionUpdates();
		//	compressedUpdate.update = new PositionUpdate(getRelevantPlayer().entityID, getRelevantPlayer().x, getRelevantPlayer().y);
			serverUpdates.add(compressedUpdate);
		}
	}
	
	//Deletes too
	public synchronized CompressedServerUpdate[] yieldServerUpdates()
	{
		CompressedServerUpdate[] updates = new CompressedServerUpdate[serverUpdates.size()];
		serverUpdates.copyInto(updates);
		serverUpdates.clear();
		return updates;
	}
	
	public synchronized EntityPlayer getRelevantPlayer()
	{
		return relevantPlayer;
	}
	
	public synchronized void registerPlayerInput(EnumHardwareInput[] clientInput)
	{
		PlayerInput input = new PlayerInput(relevantPlayer, clientInput);
		engine.getWorld().registerPlayerMovement(input);
	}

	public synchronized CompressedPlayer[] requestOtherPlayers()
	{
		EntityPlayer[] players = engine.getPlayersArray();
		CompressedPlayer[] compPlayers = new CompressedPlayer[players.length];
		for(int i = 0; i < compPlayers.length; i++)
		{
			compPlayers[i] = EntityPlayer.compress(players[i]);
		}		
		return compPlayers;
	}

}
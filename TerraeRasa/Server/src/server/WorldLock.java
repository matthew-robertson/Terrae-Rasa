package server;

import io.Chunk;

import java.util.Vector;

import transmission.ChunkCompressor;
import transmission.CompressedClientUpdate;
import transmission.CompressedPlayer;
import transmission.CompressedServerUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedChunk;
import transmission.UpdateWithObject;
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

	//This is from the server
	public synchronized void addUpdate(ServerUpdate update)
	{
		if(getRelevantPlayer() != null)
		{
			CompressedServerUpdate compressedUpdate = new CompressedServerUpdate();
			compressedUpdate.values = update.getValues();
			Chunk[] chunks = update.getChunks();
			SuperCompressedChunk[] compressedChunks = new SuperCompressedChunk[chunks.length];
			for(int i = 0; i < chunks.length; i++)
			{
				compressedChunks[i] = ChunkCompressor.compressChunk(chunks[i]);
			}
			compressedUpdate.chunks = compressedChunks;
			compressedUpdate.blockUpdates = update.getBlockUpdates();
			compressedUpdate.entityUpdates = update.getUpdates();
			compressedUpdate.positionUpdates = update.getPositionUpdates();
			compressedUpdate.statUpdates = update.getStatUpdates();
			compressedUpdate.objectUpdates = update.getObjectUpdates();
			
//			System.out.println("---------Debug Compressed Update start--------");
//			
//			for(String str : compressedUpdate.values)
//			{
//				System.out.println("String: " + str);
//			}
//			for(UpdateWithObject str : compressedUpdate.objectUpdates)
//			{
//				System.out.println("String_withobj_: " + str.command);
//			}
//			
//			System.out.println("---------Debug Compressed Update end----------");
//			
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
	
	public synchronized void registerPlayerUpdate(CompressedClientUpdate[] updates)
	{
		for(CompressedClientUpdate update : updates)
		{
			PlayerInput input = new PlayerInput(relevantPlayer, update.clientInput);
			engine.getWorld().registerPlayerMovement(input);
			engine.registerClientUpdate(update);
		}
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
package server;

import io.Chunk;

import java.util.Vector;

import transmission.WorldData;
import entities.EntityPlayer;

public class WorldLock 
{
	private GameEngine engine;
	
	public WorldLock(GameEngine engine)
	{
		this.engine = engine;
	}
	
	public void issueConsoleCommand(EntityPlayer associatedPlayer, String command)
	{
		//TODO: console commands
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
		engine.getWorld().addPlayerToWorld(TerraeRasa.terraeRasa.getSettings(), player);
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
}

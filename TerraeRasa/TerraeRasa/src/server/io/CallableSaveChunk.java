package server.io;

import java.util.concurrent.Callable;

import savable.SavableBlock;
import savable.SavableChunk;
import server.Log;
import server.utils.SaveHelper;
import blocks.Chunk;
import blocks.MinimalBlock;

public class CallableSaveChunk implements Callable<Boolean>
{
	private boolean removeChunk;
	private String basepath;
	private Chunk chunk;
	private int x;
	private ChunkManager manager;
	
	public CallableSaveChunk(ChunkManager manager, Chunk chunk, int x, String basepath, String worldName, boolean removeChunk)
	{
		this.chunk = chunk;
		this.x = x;
		this.basepath = basepath;
		this.manager = manager;
		this.removeChunk = removeChunk;
	}
	
	public Boolean call() throws Exception 
	{
		SavableChunk savable = new SavableChunk();
		savable.biomeID = chunk.getBiome().getBiomeID();
		savable.backWalls = convertToSavable(chunk.backWalls);
		savable.blocks = convertToSavable(chunk.blocks);
		savable.x = chunk.getX();
		savable.height = chunk.getHeight();
		savable.lightPositions = chunk.getLightPositions();
		
		SaveHelper smanager = new SaveHelper();
		smanager.saveCompressedFile(basepath + "/" + x + ".trc", savable);
		Log.log("Chunk Saved to: " + basepath + "/" + x + ".trc");
		manager.unlockChunk(chunk.getX());
		if(removeChunk)
		{
			this.chunk = null;
		}
		savable = null;
		return true;
	}
	
	private SavableBlock[][] convertToSavable(MinimalBlock[][] blocks)
	{
		SavableBlock[][] savables = new SavableBlock[blocks.length][blocks[0].length];
		for(int i = 0; i < savables.length; i++)
		{
			for(int k = 0; k < savables[0].length; k++)
			{
				SavableBlock sblock = new SavableBlock();
				sblock.bitMap = blocks[i][k].bitMap;
				sblock.id = blocks[i][k].id;
				sblock.metaData = blocks[i][k].metaData;
				sblock.mainInventory = blocks[i][k].getMainInventory();
						//(blocks[i][k] instanceof BlockChest) ? ((BlockChest)(blocks[i][k])).getMainInventory() : new ItemStack[0];
				savables[i][k] = sblock;			
			}
		}
		return savables;
	}
}

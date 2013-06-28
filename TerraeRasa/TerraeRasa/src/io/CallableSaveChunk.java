package io;
import java.util.concurrent.Callable;

import savable.SavableBlock;
import savable.SavableChunk;
import savable.SaveManager;
import utils.ItemStack;
import blocks.Block;
import blocks.BlockChest;

public class CallableSaveChunk implements Callable<Boolean>
{
	private String basepath;
	private Chunk chunk;
	private int x;
	private ChunkManager manager;
	
	public CallableSaveChunk(ChunkManager manager, Chunk chunk, int x, String basepath, String worldName)
	{
		this.chunk = chunk;
		this.x = x;
		this.basepath = basepath;
		this.manager = manager;
	}
	
	public Boolean call() throws Exception 
	{
		SavableChunk savable = new SavableChunk();
		savable.biomeID = chunk.getBiome().getBiomeID();
		savable.light = chunk.getLight();
		savable.diffuseLight = chunk.diffuseLight;
		savable.ambientLight = chunk.ambientLight;
		savable.backWalls = convertToSavable(chunk.backWalls);
		savable.blocks = convertToSavable(chunk.blocks);
		savable.x = chunk.getX();
		savable.wasChanged = chunk.getChanged();
		savable.height = chunk.getHeight();
		savable.lightUpdated = true;
		savable.flaggedForLightingUpdate = true;
			
		SaveManager smanager = new SaveManager();
		smanager.saveCompressedFile(basepath + "/" + x + ".trc", savable);
		System.out.println("Chunk Saved to: " + basepath + "/" + x + ".trc");
		manager.unlockChunk(chunk.getX());
		return true;
	}
	
	private SavableBlock[][] convertToSavable(Block[][] blocks)
	{
		SavableBlock[][] savables = new SavableBlock[blocks.length][blocks[0].length];
		for(int i = 0; i < savables.length; i++)
		{
			for(int k = 0; k < savables[0].length; k++)
			{
				SavableBlock sblock = new SavableBlock();
				sblock.bitMap = blocks[i][k].getBitMap();
				sblock.id = blocks[i][k].getID();
				sblock.metaData = blocks[i][k].metaData;
				sblock.mainInventory = (blocks[i][k] instanceof BlockChest) ? ((BlockChest)(blocks[i][k])).getMainInventory() : new ItemStack[0];
				savables[i][k] = sblock;			
			}
		}
		return savables;
	}
}

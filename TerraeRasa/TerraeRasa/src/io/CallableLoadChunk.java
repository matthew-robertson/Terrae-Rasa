package io;
import java.util.concurrent.Callable;

import savable.SavableBlock;
import savable.SavableChunk;
import savable.SaveManager;
import world.Biome;
import blocks.Block;
import blocks.BlockChest;

public class CallableLoadChunk implements Callable<Chunk>
{
	private String basepath;
	private int x;
	private ChunkManager manager;
	
	public CallableLoadChunk(ChunkManager manager, int x, String basepath, String worldName)
	{
		this.x = x;
		this.basepath = basepath;
		this.manager = manager;
	}
	
	public Chunk call() throws Exception
	{
		SavableChunk savable = (SavableChunk)new SaveManager().loadCompressedFile(basepath + "/" + x + ".trc");
		Chunk chunk = new Chunk(Biome.getBiomeFromBiomeList(savable.biomeID), savable.x, savable.height);
		chunk.light = savable.light;
		chunk.diffuseLight = savable.diffuseLight;
		chunk.ambientLight = savable.ambientLight;
		chunk.backWalls = convertFromSavable(savable.backWalls);
		chunk.blocks = convertFromSavable(savable.blocks);
		chunk.setChanged(savable.wasChanged);
		chunk.setFlaggedForLightingUpdate(true);
		System.out.println("Chunk Loaded From File Path : " + basepath + "/" + x + ".trc");
		manager.unlockChunk(chunk.getX());
		return chunk;
	}
	
	private Block[][] convertFromSavable(SavableBlock[][] savables)
	{
		Block[][] blocks = new Block[savables.length][savables[0].length];
		for(int i = 0; i < savables.length; i++)
		{
			for(int k = 0; k < savables[0].length; k++)
			{
				Block block = Block.blocksList[savables[i][k].id].clone();
				block.setBitMap(savables[i][k].bitMap);
				block.metaData = savables[i][k].metaData;
				if(block instanceof BlockChest)
				{
					((BlockChest)(block)).setInventory(savables[i][k].mainInventory);
				}
				blocks[i][k] = block;			
			}
		}
		return blocks;
	}
}

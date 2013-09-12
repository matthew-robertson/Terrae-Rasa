package transmission;

import java.io.Serializable;

import utils.DisplayableItemStack;
import blocks.Block;
import blocks.BlockChest;
import blocks.MinimalBlock;


public class SuperCompressedBlock 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DisplayableItemStack[] mainInventory;
	public short id;
	public byte metaData;
	public byte bitMap;
	
	public SuperCompressedBlock()
	{
		
	}	
	
	public SuperCompressedBlock(Block block)
	{
		this.id = (short) block.id;
		this.metaData = 1;
		this.bitMap = 0;
		mainInventory = (block instanceof BlockChest) ? ((BlockChest)(block)).getDisplayableInventory() : new DisplayableItemStack[0];
	}

	public SuperCompressedBlock(MinimalBlock block) 
	{
		this.id = (short) block.id;
		this.metaData = (byte) block.metaData;
		this.bitMap = (byte) block.getBitMap();
		mainInventory = block.getDisplayableInventory();
	}
}

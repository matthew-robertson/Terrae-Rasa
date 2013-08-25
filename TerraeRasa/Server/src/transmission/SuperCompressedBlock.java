package transmission;

import java.io.Serializable;

import utils.DisplayableItemStack;
import blocks.Block;
import blocks.BlockChest;


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
		this.metaData = (byte) block.metaData;
		this.bitMap = (byte) block.getBitMap();
		mainInventory = (block instanceof BlockChest) ? ((BlockChest)(block)).getDisplayableInventory() : new DisplayableItemStack[0];
	}
}

package blocks;

import savable.SavableBlock;
import utils.ItemStack;

/**
 * A MinimalBlock is a highly cropped version of Block, which holds minimal amounts of data required for rendering and operations. Other information can
 * still be requested from the full Block version of this MinimalBlock which will share the same block ID.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MinimalBlock 
{
	public ItemStack[] mainInventory;
	public short id;
	public byte metaData;
	public byte bitMap;
	public boolean hasMetaData;
	public boolean isSolid;
	
	/**
	 * Constructs a new MinimalBlock with the given Block
	 * @param block the Block to convert to a MinimalBlock
	 */
	public MinimalBlock(Block block)
	{
		this.id = (short) block.getID();
		this.metaData = (byte) block.metaData;
		this.mainInventory = (block instanceof BlockChest) ? ((BlockChest)(block)).getMainInventory() : new ItemStack[0];
		this.setBitMap(block.getBitMap());
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
	}
	
	/**
	 * Constructs a new MinimalBlock using a SavableBlock
	 * @param savedBlock the SavableBlock to convert to a MinimalBlock
	 */
	public MinimalBlock(SavableBlock savedBlock)
	{
		Block block = Block.blocksList[savedBlock.id].clone();
		this.id = (short) block.getID();
		this.metaData = (byte) savedBlock.metaData;
		this.mainInventory = savedBlock.mainInventory; //??????
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
		this.setBitMap(savedBlock.bitMap);
	}
	
//	public MinimalBlock(SuperCompressedBlock compressedBlock)
//	{
//		Block block = Block.blocksList[compressedBlock.id].clone();
//		this.id = (short) block.getID();
//		this.metaData = (byte) compressedBlock.metaData;
//		this.mainInventory = compressedBlock.mainInventory; //??????
//		this.hasMetaData = block.hasMetaData;
//		this.isSolid = block.isSolid;
//		this.setBitMap(compressedBlock.bitMap);
//	}
	
	public int getID()
	{
		return id;
	}
	
	/**
	 * Sets the bitmap of this minimal block, using the same procedure as Block.setBitMap(int)
	 * @param i the new bitmap value
	 * @return a reference to this MinimalBlock
	 */
	public MinimalBlock setBitMap(int i) 
	{
		bitMap = (byte)i;		
		return this;
	}
}

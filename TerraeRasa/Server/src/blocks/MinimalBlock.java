package blocks;

import savable.SavableBlock;
import utils.DisplayableItemStack;
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
		this.metaData = 1;
		this.mainInventory = (block instanceof BlockChest) ? ((BlockChest)(block)).getMainInventory() : new ItemStack[0];
		this.setBitMap((byte)0);
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
	}
	
	/**
	 * Constructs a new MinimalBlock using a SavableBlock
	 * @param savedBlock the SavableBlock to convert to a MinimalBlock
	 */
	public MinimalBlock(SavableBlock savedBlock)
	{
		Block block = Block.blocksList[savedBlock.id];
		this.id = (short) block.getID();
		this.metaData = (byte) savedBlock.metaData;
		this.mainInventory = savedBlock.mainInventory; 
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
		this.setBitMap(savedBlock.bitMap);
	}
		
	/**
	 * Gets the Block ID associated with this minimum block.
	 * @return the Block ID associated with this minimum block
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Sets the bitmap of this minimal block
	 * @param bit the new bitmap value
	 * @return a reference to this MinimalBlock
	 */
	public MinimalBlock setBitMap(byte bit) 
	{
		bitMap = bit;		
		return this;
	}

	/**
	 * Gets whether or not this block is solid - if it's solid it cant be walked through.
	 * @return true if this block cannot be passed, otherwise false
	 */
	public boolean isSolid() 
	{
		return isSolid;
	}

	/**
	 * Gets this block's bitmap.
	 * @return this block's bitmap
	 */
	public int getBitMap() 
	{
		return bitMap;
	}
	
	/**
	 * Converts the mainInventory[] from a ItemStack[] to a DisplayableItemStack[] and then returns the newly created DisplayableItemStack[].
	 * @return a reference safe, conversion of mainInventory[] from ItemStack[] to DisplayableItemStack[]
	 */
	public DisplayableItemStack[] getDisplayableInventory()
	{
		DisplayableItemStack[] displayables = new DisplayableItemStack[mainInventory.length];
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(this.mainInventory[i] != null)
			{
				displayables[i] = new DisplayableItemStack(mainInventory[i]);
			}
		}
		return displayables;
	}
}

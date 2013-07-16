package blocks;

import savable.SavableBlock;
import transmission.SuperCompressedBlock;
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
	public short iconX;
	public short iconY;
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
		this.iconX = (short) block.iconX;
		this.iconY = (short) block.iconY;
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
		this.iconX = (short) block.iconX;
		this.iconY = (short) block.iconY;
		this.setBitMap(savedBlock.bitMap);
	}
	
	public MinimalBlock(SuperCompressedBlock compressedBlock)
	{
		Block block = Block.blocksList[compressedBlock.id].clone();
		this.id = (short) block.getID();
		this.metaData = (byte) compressedBlock.metaData;
		this.mainInventory = compressedBlock.mainInventory; //??????
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
		this.iconX = (short) block.iconX;
		this.iconY = (short) block.iconY;
		this.setBitMap(compressedBlock.bitMap);
	}
	
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
		char tilemap = Block.blocksList[id].getTileMap(); 
		bitMap = (byte)i;
		// If the block is a general case
		if (tilemap == 'g') {
			if (i <= 15) {
				this.setIconIndex(i, this.iconY);
			} else {
				this.setIconIndex(i - 16, this.iconY + 1);
			}
		}
		// If the block is a pillar
		else if (tilemap == 'p') {
			this.setIconIndex(i, this.iconY);
		}
		// If the block is a tree
		else if (tilemap == 't') {
			this.setIconIndex(i, this.iconY);
		}
		// If the block is a branch
		else if (tilemap == 'b') {
			// If the branch is a regular branch
			if (i <= 11) {
				this.setIconIndex(4 + i, this.iconY);
			}
			// If the branch is covered in snow
			else {
				this.setIconIndex(4 + (i - 12), this.iconY + 1);
			}
		}
		// If the block is a treetop
		else if (tilemap == 'T') {
			this.setIconIndex(this.iconX + 3 * i, this.iconY);
		}
		return this;
	}
	
	protected MinimalBlock setIconIndex(int x, int y) 
	{
		iconY = (short)y;
		iconX = (short)x;
		return this;
	}
}

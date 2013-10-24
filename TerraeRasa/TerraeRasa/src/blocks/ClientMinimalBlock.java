package blocks;

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
public class ClientMinimalBlock extends MinimalBlock
{
	public short iconX;
	public short iconY;
	
	/**
	 * Constructs a new MinimalBlock MinimumBlock with the properties of air
	 * @param isFront true if the block is front air, otherwise false (backair)
	 */ 
	public ClientMinimalBlock(boolean isFront)
	{
		Block block = null;
		if(isFront)
			block = Block.air;
		else
			block = Block.backAir;
		this.id = (short) block.getID();
		this.metaData = (byte) 1;
		this.mainInventory = (ItemStack[]) ((block instanceof BlockChest) ? ((BlockChest)(block)).getMainInventory() : new ItemStack[0]);
		this.iconX = (short) block.iconX;
		this.iconY = (short) block.iconY;
		this.setBitMap((byte)0);
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
	}
	
	public ClientMinimalBlock(SuperCompressedBlock compressedBlock)
	{
		Block block = Block.blocksList[compressedBlock.id];
		this.id = (short) block.getID();
		this.metaData = (byte) compressedBlock.metaData;
		if(compressedBlock.mainInventory == null)
		{
			this.mainInventory = new ItemStack[0];
		}
		else
		{
			this.mainInventory = compressedBlock.mainInventory; //??????
		}
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
		this.iconX = (short) block.iconX;
		this.iconY = (short) block.iconY;
		this.setBitMap(compressedBlock.bitMap);
	}
	
	/**
	 * Sets the bitmap of this minimal block, using the same procedure as Block.setBitMap(int)
	 * @param i the new bitmap value
	 * @return a reference to this MinimalBlock
	 */
	public void setBitMap(byte bit) 
	{
		byte tilemap = Block.blocksList[id].getTileMap(); 
		bitMap = bit;
		// If the block is a general case
		if (tilemap == Block.TILEMAP_GENERAL) {
			if (bit <= 15) {
				this.setIconIndex(bit, this.iconY);
			} else {
				this.setIconIndex(bit - 16, this.iconY + 1);
			}
		}
		// If the block is a pillar
		else if (tilemap == Block.TILEMAP_PILLAR) {
			this.setIconIndex(bit, this.iconY);
		}
		// If the block is a tree
		else if (tilemap == Block.TILEMAP_TREE) {
			this.setIconIndex(bit, this.iconY);
		}
		// If the block is a branch
		else if (tilemap == Block.TILEMAP_TREE_BRANCH) {
			// If the branch is a regular branch
			if (bit <= 11) {
				this.setIconIndex(4 + bit, this.iconY);
			}
			// If the branch is covered in snow
			else {
				this.setIconIndex(4 + (bit - 12), this.iconY + 1);
			}
		}
		// If the block is a treetop
		else if (tilemap == Block.TILEMAP_TREETOP) {
			this.setIconIndex(this.iconX + 3 * bit, this.iconY);
		}
	}
	
	protected ClientMinimalBlock setIconIndex(int x, int y) 
	{
		iconY = (short)y;
		iconX = (short)x;
		return this;
	}

}

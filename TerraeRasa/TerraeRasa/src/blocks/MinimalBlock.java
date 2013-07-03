package blocks;

import utils.ItemStack;

public class MinimalBlock 
{
	public ItemStack[] mainInventory;
	public int id;
	public int metaData;
	public int bitMap;
	public float blockWidth; 
	/** The render height of a texture (in the world) */
	public float blockHeight; 
	/** The pixel width of a Block's texture (on a spritesheet) */
	public float textureWidth; 
	/** The pixel height of a Block's texture (on a spritesheet) */
	public float textureHeight; 
	public int iconX;
	public int iconY;
	public boolean hasMetaData;
	public boolean isSolid;
	
	public MinimalBlock(Block block)
	{
		this.bitMap = block.getBitMap();
		this.id = block.getID();
		this.metaData = block.metaData;
		this.mainInventory = (block instanceof BlockChest) ? ((BlockChest)(block)).getMainInventory() : new ItemStack[0];
		this.blockWidth = (float) block.blockWidth;
		this.blockHeight = (float) block.blockHeight;
		this.textureHeight = (float) block.textureHeight;
		this.textureWidth = (float) block.textureWidth;
		this.iconX = (int) block.iconX;
		this.iconY = (int) block.iconY;
		this.hasMetaData = block.hasMetaData;
		this.isSolid = block.isSolid;
	}

	public int getID()
	{
		return id;
	}
	
	public MinimalBlock setBitMap(int i) 
	{
		char tilemap = Block.blocksList[id].getTileMap(); 
		bitMap = i;
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
		iconY = y;
		iconX = x;
		return this;
	}
}

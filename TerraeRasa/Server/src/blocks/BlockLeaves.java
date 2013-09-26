package blocks;

import utils.ItemStack;

/**
 * BlockLeaves is an extension of Block that represents some sort of tree leaf block. This will always convert to a sapling at 5% rate when broken.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class BlockLeaves extends Block 
{
	/**
	 * Constructs a new BlockLeaf
	 * @param i the unique ID of this BlockLeaf
	 */
	protected BlockLeaves(int i)
	{
		super(i);
	}
	
	/**
	 * Rolls for a chance (5%) of a sapling.
	 */
	public ItemStack getDroppedItem()
	{
		return (random.nextInt(20) == 0) ? new ItemStack(Block.sapling, 1) : null;
	}
}

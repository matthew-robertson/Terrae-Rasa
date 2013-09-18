package blocks;

import utils.ItemStack;

/**
 * BlockWood is an extension of Block that represents some sort of tree block. This will always convert to a plank when broken.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class BlockWood extends Block
{
	/**
	 * Constructs a new BlockWood
	 * @param i the unique ID of this BlockWood
	 */
	public BlockWood(int i)
	{
		super(i);
	}
	
	/**
	 * BlockWood will always drop 1 plank.
	 */
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.plank, 1);
	}
}

package blocks;

import utils.ItemStack;

/**
 * BlockGrass is an extension of Block that represents some sort of grass block. Grass will always convert to a dirt block when broken.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class BlockGrass extends Block
{
	/**
	 * Constructs a new BlockGrass
	 * @param i the unique ID of this BlockGrass
	 */
	protected BlockGrass(int i)
	{
		super(i);
	}
	
	/**
	 * Blocks of grass have a 100% chance to drop a stack of dirt.
	 */
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.dirt, 1);
	}
}

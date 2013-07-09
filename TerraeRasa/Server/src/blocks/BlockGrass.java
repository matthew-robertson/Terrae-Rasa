package blocks;

import utils.ItemStack;


public class BlockGrass extends Block
{
	public BlockGrass(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.dirt, 1);
	}
}

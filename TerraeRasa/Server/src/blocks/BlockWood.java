package blocks;

import utils.ItemStack;

public class BlockWood extends Block
{
	public BlockWood(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.plank, 1);
	}
}

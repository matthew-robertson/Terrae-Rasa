package blocks;

import utils.ItemStack;


public class BlockLeaves extends Block 
{
	
	public BlockLeaves(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return (random.nextInt(20) == 0) ? new ItemStack(Block.sapling, 1) : null;
	}
}

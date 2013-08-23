package blocks;

import utils.DisplayableItemStack;


public class BlockLeaves extends Block 
{
	
	public BlockLeaves(int i)
	{
		super(i);
	}
	
	public DisplayableItemStack getDroppedItem()
	{
		return (random.nextInt(20) == 0) ? new DisplayableItemStack(Block.sapling, 1) : null;
	}
}

package blocks;

import utils.DisplayableItemStack;

public class BlockWood extends Block
{
	public BlockWood(int i)
	{
		super(i);
	}
	
	public DisplayableItemStack getDroppedItem()
	{
		return new DisplayableItemStack(Block.plank, 1);
	}
}

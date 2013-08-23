package blocks;

import utils.DisplayableItemStack;


public class BlockGrass extends Block
{
	public BlockGrass(int i)
	{
		super(i);
	}
	
	public DisplayableItemStack getDroppedItem()
	{
		return new DisplayableItemStack(Block.dirt, 1);
	}
}

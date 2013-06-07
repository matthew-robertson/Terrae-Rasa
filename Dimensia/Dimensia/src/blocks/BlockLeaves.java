package blocks;

import java.io.Serializable;

import utils.ItemStack;


public class BlockLeaves extends Block implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public BlockLeaves(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return (random.nextInt(20) == 0) ? new ItemStack(Block.sapling, 1) : null;
	}
}

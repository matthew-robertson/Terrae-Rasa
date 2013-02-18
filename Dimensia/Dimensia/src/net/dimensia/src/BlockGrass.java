package net.dimensia.src;

import java.io.Serializable;

public class BlockGrass extends Block implements Serializable
{
	private static final long serialVersionUID = 1L;

	public BlockGrass(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.dirt, 1);
	}
}

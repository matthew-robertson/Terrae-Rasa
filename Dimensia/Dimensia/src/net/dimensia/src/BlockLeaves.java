package net.dimensia.src;
import java.io.Serializable;


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

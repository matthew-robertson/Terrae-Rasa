package net.dimensia.src;
import java.io.Serializable;


public class BlockWood extends Block implements Serializable
{
	private static final long serialVersionUID = 1L;

	public BlockWood(int i)
	{
		super(i);
	}
	
	public ItemStack getDroppedItem()
	{
		return new ItemStack(Block.plank, 1);
	}
	
}

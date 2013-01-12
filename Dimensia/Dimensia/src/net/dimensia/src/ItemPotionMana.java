package net.dimensia.src;


public class ItemPotionMana extends Item
{
	public ItemPotionMana(int i, int m)
	{
		super(i);
		manaRestored = m;
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		boolean success = world.player.restorePlayerMana(world, manaRestored);

		if(success)
		{
			world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
		}
	}
	
	public int getManaRestored()
	{
		return manaRestored;
	}
	
	protected int manaRestored;
}

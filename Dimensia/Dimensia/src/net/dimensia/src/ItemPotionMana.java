package net.dimensia.src;

public class ItemPotionMana extends Item
{
	protected int manaRestored;
	
	public ItemPotionMana(int i, int m)
	{
		super(i);
		manaRestored = m;
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		boolean success = player.restorePlayerMana(world, manaRestored);

		if(success)
		{
			player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
		}
	}
	
	public int getManaRestored()
	{
		return manaRestored;
	}
}

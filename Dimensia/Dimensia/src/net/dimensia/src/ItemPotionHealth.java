package net.dimensia.src;

public class ItemPotionHealth extends Item
{
	protected int healthRestored;

	public ItemPotionHealth(int i, int h)
	{
		super(i);
		healthRestored = h;
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		boolean success = player.healPlayer(world, healthRestored);
		
		if(success)
		{
			player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
}

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
		if(!player.isOnCooldown(itemID))
		{
			boolean success = player.healPlayer(world, healthRestored, true);
			if(success)
			{
				player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
				player.putOnCooldown(itemID, 60);
			}
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
}

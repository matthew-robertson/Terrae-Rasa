package net.dimensia.src;


public class ItemPotionHealth extends Item
{
	public ItemPotionHealth(int i, int h)
	{
		super(i);
		healthRestored = h;
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		boolean success = world.player.healPlayer(world, healthRestored);
		
		if(success)
		{
			world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
	
	protected int healthRestored;
}

package net.dimensia.src;


public class ItemHeartCrystal extends Item
{
	public ItemHeartCrystal(int i)
	{
		super(i);
		maxStackSize = 1;
		HEALTH_INCREASE = 20;
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		boolean success = world.player.boostMaxHealth(HEALTH_INCREASE);
		
		if(success)
		{
			world.player.inventory.removeEntireStackFromInventory(world.player.selectedSlot);
		}
	}
	
	private final int HEALTH_INCREASE;
}

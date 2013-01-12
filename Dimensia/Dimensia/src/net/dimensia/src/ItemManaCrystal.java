package net.dimensia.src;


public class ItemManaCrystal extends Item
{
	public ItemManaCrystal(int i)
	{
		super(i);
		maxStackSize = 1;
		MANA_INCREASE = 20;
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		boolean success = world.player.boostMaxMana(MANA_INCREASE);
		
		if(success)
		{
			world.player.inventory.removeEntireStackFromInventory(world.player.selectedSlot);
		}		
	}
	
	private final int MANA_INCREASE;
}

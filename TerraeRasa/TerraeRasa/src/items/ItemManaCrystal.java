package items;

import entities.EntityPlayer;
import world.World;

public class ItemManaCrystal extends Item
{
	private final int MANA_INCREASE;

	public ItemManaCrystal(int i)
	{
		super(i);
		maxStackSize = 1;
		MANA_INCREASE = 20;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		boolean success = player.boostMaxMana(MANA_INCREASE);
		
		if(success)
		{
			player.inventory.removeEntireStackFromInventory(world, player, player.selectedSlot);
			world.soundEngine.playSoundEffect(onUseSound);
		}		
	}
}

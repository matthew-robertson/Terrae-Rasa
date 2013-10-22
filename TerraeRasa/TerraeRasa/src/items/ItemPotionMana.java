package items;

import server.entities.EntityPlayer;
import world.World;
import entry.MPGameEngine;

public class ItemPotionMana extends Item
{
	protected int manaRestored;
	
	public ItemPotionMana(int i, int m)
	{
		super(i);
		manaRestored = m;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		boolean success = player.restoreMana(world, manaRestored, true);

		if(success)
		{
			player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
			MPGameEngine.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
		}
	}
	
	public int getManaRestored()
	{
		return manaRestored;
	}
}

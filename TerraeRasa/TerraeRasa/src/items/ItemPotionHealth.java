package items;

import server.entities.EntityPlayer;
import world.World;
import entry.MPGameEngine;

public class ItemPotionHealth extends Item
{
	protected int healthRestored;

	public ItemPotionHealth(int i, int h)
	{
		super(i);
		healthRestored = h;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		if(!player.isOnCooldown(id))
		{
			boolean success = player.heal(world, healthRestored, true);
			if(success)
			{
				player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
				player.putOnCooldown(id, 60);
				MPGameEngine.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
			}
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
}

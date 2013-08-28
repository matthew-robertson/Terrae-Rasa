package items;

import server.TerraeRasa;
import entities.EntityPlayer;
import world.World;

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
				TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
			}
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
}

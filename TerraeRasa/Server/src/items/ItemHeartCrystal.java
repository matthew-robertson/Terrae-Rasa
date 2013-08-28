package items;

import server.TerraeRasa;
import entities.EntityPlayer;
import world.World;

public class ItemHeartCrystal extends Item
{
	private final int HEALTH_INCREASE;
	
	public ItemHeartCrystal(int i)
	{
		super(i);
		maxStackSize = 1;
		HEALTH_INCREASE = 20;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		boolean success = player.boostMaxHealth(HEALTH_INCREASE);
		
		if(success)
		{
			player.inventory.removeEntireStackFromInventory(world, player, player.selectedSlot);
			TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
		}
	}
}

package items;

import server.entities.EntityPlayer;
import statuseffects.StatusEffectSteelSkin;
import world.World;
import entry.MPGameEngine;

public class ItemPotionSteelSkin extends ItemPotion
{
	public ItemPotionSteelSkin(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectSteelSkin(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
		MPGameEngine.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
	}
}

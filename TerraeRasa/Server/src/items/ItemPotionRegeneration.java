package items;

import entities.EntityPlayer;
import server.TerraeRasa;
import statuseffects.StatusEffectRegeneration;
import world.World;

public class ItemPotionRegeneration extends ItemPotion
{
	public ItemPotionRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectRegeneration(durationSeconds, tier, power, ticksBetweenEffect, false));
		player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
		TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
	}
}

package items;

import entities.EntityPlayer;
import server.TerraeRasa;
import statuseffects.StatusEffectManaRegeneration;
import world.World;

public class ItemPotionManaRegeneration extends ItemPotion
{
	public ItemPotionManaRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectManaRegeneration(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
		TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
	}
}

package items;

import entities.EntityPlayer;
import server.TerraeRasa;
import statuseffects.StatusEffectDodgeBuff;
import world.World;

public class ItemPotionDodge extends ItemPotion
{
	public ItemPotionDodge(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectDodgeBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
		TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/soundeffect " + onUseSound);
	}
}

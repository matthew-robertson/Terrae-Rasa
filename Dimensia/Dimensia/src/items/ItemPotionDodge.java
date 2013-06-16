package items;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectDodgeBuff;
import world.World;

public class ItemPotionDodge extends ItemPotion
{
	public ItemPotionDodge(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectDodgeBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

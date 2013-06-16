package items;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectCriticalBuff;
import world.World;

public class ItemPotionCriticalBuff extends ItemPotion
{
	public ItemPotionCriticalBuff(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectCriticalBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

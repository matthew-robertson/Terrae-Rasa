package items;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectRegeneration;
import world.World;

public class ItemPotionRegeneration extends ItemPotion
{
	public ItemPotionRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectRegeneration(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

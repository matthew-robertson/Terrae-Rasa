package items;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectManaRegeneration;
import world.World;

public class ItemPotionManaRegeneration extends ItemPotion
{
	public ItemPotionManaRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectManaRegeneration(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

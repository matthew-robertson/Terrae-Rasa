package items;

import entities.EntityLivingPlayer;
import statuseffects.StatusEffectSwiftness;
import world.World;

public class ItemPotionSwiftness extends ItemPotion
{
	public ItemPotionSwiftness(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectSwiftness(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

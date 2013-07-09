package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectDamageBuff;
import world.World;

public class ItemPotionDamageBuff extends ItemPotion
{
	public ItemPotionDamageBuff(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectDamageBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

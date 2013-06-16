package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectAttackSpeedBuff;
import world.World;

public class ItemPotionAttackSpeed extends ItemPotion
{
	public ItemPotionAttackSpeed(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(new StatusEffectAttackSpeedBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

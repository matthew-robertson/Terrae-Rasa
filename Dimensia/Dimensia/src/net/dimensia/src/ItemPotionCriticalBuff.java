package net.dimensia.src;

public class ItemPotionCriticalBuff extends ItemPotion
{
	public ItemPotionCriticalBuff(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectCriticalBuff(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

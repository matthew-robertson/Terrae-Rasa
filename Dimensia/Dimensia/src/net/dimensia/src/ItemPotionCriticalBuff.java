package net.dimensia.src;

public class ItemPotionCriticalBuff extends ItemPotion
{
	public ItemPotionCriticalBuff(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectCriticalBuff(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

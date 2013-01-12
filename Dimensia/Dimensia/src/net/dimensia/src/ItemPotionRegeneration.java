package net.dimensia.src;

public class ItemPotionRegeneration extends ItemPotion
{
	public ItemPotionRegeneration(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectRegeneration(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

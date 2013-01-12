package net.dimensia.src;

public class ItemPotionManaRegeneration extends ItemPotion
{
	public ItemPotionManaRegeneration(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectManaRegeneration(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

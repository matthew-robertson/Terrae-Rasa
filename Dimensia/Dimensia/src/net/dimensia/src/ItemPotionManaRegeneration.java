package net.dimensia.src;

public class ItemPotionManaRegeneration extends ItemPotion
{
	public ItemPotionManaRegeneration(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectManaRegeneration(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

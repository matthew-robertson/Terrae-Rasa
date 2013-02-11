package net.dimensia.src;

public class ItemPotionSwiftness extends ItemPotion
{
	public ItemPotionSwiftness(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectSwiftness(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

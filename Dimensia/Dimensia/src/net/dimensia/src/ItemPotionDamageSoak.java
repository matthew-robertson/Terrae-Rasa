package net.dimensia.src;

public class ItemPotionDamageSoak extends ItemPotion
{
	public ItemPotionDamageSoak(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectDamageSoak(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

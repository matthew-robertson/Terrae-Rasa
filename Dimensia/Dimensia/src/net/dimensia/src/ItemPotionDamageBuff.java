package net.dimensia.src;

public class ItemPotionDamageBuff extends ItemPotion
{
	public ItemPotionDamageBuff(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectDamageBuff(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

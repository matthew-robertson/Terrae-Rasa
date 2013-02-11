package net.dimensia.src;

public class ItemPotionAttackSpeed extends ItemPotion
{
	public ItemPotionAttackSpeed(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectAttackSpeedBuff(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

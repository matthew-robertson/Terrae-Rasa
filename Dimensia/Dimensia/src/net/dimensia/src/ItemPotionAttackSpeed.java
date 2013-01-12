package net.dimensia.src;

public class ItemPotionAttackSpeed extends ItemPotion
{
	public ItemPotionAttackSpeed(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectAttackSpeedBuff(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

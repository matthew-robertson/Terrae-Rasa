package net.dimensia.src;

public class ItemPotionDamageSoak extends ItemPotion
{
	public ItemPotionDamageSoak(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectDamageSoak(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

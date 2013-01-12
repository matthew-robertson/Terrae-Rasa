package net.dimensia.src;

public class ItemPotionDodge extends ItemPotion
{
	public ItemPotionDodge(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectDodgeBuff(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

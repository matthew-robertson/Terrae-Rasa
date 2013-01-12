package net.dimensia.src;

public class ItemPotionSwiftness extends ItemPotion
{
	public ItemPotionSwiftness(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectSwiftness(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

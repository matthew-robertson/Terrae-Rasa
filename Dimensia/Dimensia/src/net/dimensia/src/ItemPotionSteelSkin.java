package net.dimensia.src;

public class ItemPotionSteelSkin extends ItemPotion
{
	public ItemPotionSteelSkin(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityPlayer entity)
	{
		entity.registerStatusEffect(new StatusEffectSteelSkin(durationSeconds, tier));
		world.player.inventory.removeItemsFromInventoryStack(1, world.player.selectedSlot);
	}
}

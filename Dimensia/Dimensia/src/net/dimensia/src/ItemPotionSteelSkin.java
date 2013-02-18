package net.dimensia.src;

public class ItemPotionSteelSkin extends ItemPotion
{
	public ItemPotionSteelSkin(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectSteelSkin(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

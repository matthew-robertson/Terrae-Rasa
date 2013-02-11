package net.dimensia.src;

public class ItemPotionDodge extends ItemPotion
{
	public ItemPotionDodge(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		player.registerStatusEffect(new StatusEffectDodgeBuff(durationSeconds, tier));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

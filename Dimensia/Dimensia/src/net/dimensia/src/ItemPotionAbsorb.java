package net.dimensia.src;

public class ItemPotionAbsorb extends ItemPotion
{
	public ItemPotionAbsorb(int i, int duration, int tier) 
	{
		super(i, duration, tier);
	}
	
	public void onRightClick(World world, EntityLivingPlayer player)
	{
		if(!player.isOnCooldown(itemID))
		{
			player.registerStatusEffect(new StatusEffectAbsorb(durationSeconds, tier, 50));
			player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);		
			player.putOnCooldown(itemID, 600);
		}		
	}
}

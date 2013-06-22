package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectRegeneration;
import world.World;

public class ItemPotionRegeneration extends ItemPotion
{
	public ItemPotionRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(new StatusEffectRegeneration(durationSeconds, tier, power, ticksBetweenEffect, false));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
		world.soundEngine.playSoundEffect(onUseSound);
	}
}

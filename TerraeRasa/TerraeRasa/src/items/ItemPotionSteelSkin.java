package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectSteelSkin;
import world.World;

public class ItemPotionSteelSkin extends ItemPotion
{
	public ItemPotionSteelSkin(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(new StatusEffectSteelSkin(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
	}
}

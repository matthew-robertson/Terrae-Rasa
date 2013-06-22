package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectDodgeBuff;
import world.World;

public class ItemPotionDodge extends ItemPotion
{
	public ItemPotionDodge(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(new StatusEffectDodgeBuff(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
		world.soundEngine.playSoundEffect(onUseSound);
	}
}

package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectSwiftness;
import world.World;

public class ItemPotionSwiftness extends ItemPotion
{
	public ItemPotionSwiftness(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		player.registerStatusEffect(world, new StatusEffectSwiftness(durationSeconds, tier, power, ticksBetweenEffect));
		player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
		world.soundEngine.playSoundEffect(onUseSound);
	}
}

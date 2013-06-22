package items;

import entities.EntityPlayer;
import statuseffects.StatusEffectAbsorb;
import world.World;

public class ItemPotionAbsorb extends ItemPotion
{
	public ItemPotionAbsorb(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		if(!player.isOnCooldown(id))
		{
			player.registerStatusEffect(new StatusEffectAbsorb(durationSeconds, tier, (int)power));
			player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);		
			player.putOnCooldown(id, 600);
			world.soundEngine.playSoundEffect(onUseSound);
		}		
	}
}

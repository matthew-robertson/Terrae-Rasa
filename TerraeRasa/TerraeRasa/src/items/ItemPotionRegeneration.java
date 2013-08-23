package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionRegeneration extends ItemPotion
{
	public ItemPotionRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		SoundEngine.playSoundEffect(onUseSound);
	}
}

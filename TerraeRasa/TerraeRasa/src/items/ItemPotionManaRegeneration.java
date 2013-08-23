package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionManaRegeneration extends ItemPotion
{
	public ItemPotionManaRegeneration(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		SoundEngine.playSoundEffect(onUseSound);
	}
}

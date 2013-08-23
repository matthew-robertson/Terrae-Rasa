package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionAttackSpeed extends ItemPotion
{
	public ItemPotionAttackSpeed(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		SoundEngine.playSoundEffect(onUseSound);
	}
}

package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionDamageBuff extends ItemPotion
{
	public ItemPotionDamageBuff(int i, int duration, int tier, double power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		SoundEngine.playSoundEffect(onUseSound);
	}
}

package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionHealth extends Item
{
	protected int healthRestored;

	public ItemPotionHealth(int i, int h)
	{
		super(i);
		healthRestored = h;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		if(!player.isOnCooldown(id))
		{
			SoundEngine.playSoundEffect(onUseSound);
		}
	}
	
	public int getHealthRestored()
	{
		return healthRestored;
	}
}

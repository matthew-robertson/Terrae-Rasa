package items;

import world.World;
import audio.SoundEngine;
import entities.EntityPlayer;

public class ItemPotionMana extends Item
{
	protected int manaRestored;
	
	public ItemPotionMana(int i, int m)
	{
		super(i);
		manaRestored = m;
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{
		boolean success = player.restoreMana(world, manaRestored, true);

		if(success)
		{
			player.inventory.removeItemsFromInventoryStack(player, 1, player.selectedSlot);
			SoundEngine.playSoundEffect(onUseSound);
		}
	}
	
	public int getManaRestored()
	{
		return manaRestored;
	}
}

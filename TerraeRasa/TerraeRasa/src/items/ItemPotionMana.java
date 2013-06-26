package items;

import entities.EntityPlayer;
import world.World;

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
			player.inventory.removeItemsFromInventoryStack(1, player.selectedSlot);
			world.soundEngine.playSoundEffect(onUseSound);
		}
	}
	
	public int getManaRestored()
	{
		return manaRestored;
	}
}
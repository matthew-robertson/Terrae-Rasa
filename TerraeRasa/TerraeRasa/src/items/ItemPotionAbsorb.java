package items;

import world.World;
import entities.EntityPlayer;

public class ItemPotionAbsorb extends ItemPotion
{
	public ItemPotionAbsorb(int i, int duration, int tier, int power, int ticksBetweenEffect) 
	{
		super(i, duration, tier, power, ticksBetweenEffect);
	}
	
	public void onRightClick(World world, EntityPlayer player)
	{	
	}
}

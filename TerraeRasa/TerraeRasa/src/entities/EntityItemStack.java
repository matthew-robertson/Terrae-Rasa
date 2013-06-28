package entities;

import items.Item;
import items.ItemArmor;
import items.ItemTool;
import utils.ActionbarItem;
import utils.ItemStack;

/**
 * <code>EntityItemStack</code> implements a basic ItemStack entity that the world is able
 * to maintain, display, or possibly destroy. By default the fall speed is 1.8f, and the EntityItemStack
 * has only 1 (max)Health. EntityItemStacks can, in theory, die due to any damage at all, but in 
 * practice, this may never be implemented (31/5/12 time of writing this).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class EntityItemStack extends EntityParticle
{
	private int ticksBeforePickup;
	private ItemStack stack;
	
	public EntityItemStack(double x, double y, ItemStack stack)
	{
		if(stack.getItemID() < ActionbarItem.itemIndex) //Blocks are 6x6 render size
		{
			width = 6;
			height = 6;
		}
		else if(stack.getItemID() >= ActionbarItem.spellIndex)
		{
			width = 16;
			height = 16;
		}
		else if(Item.itemsList[stack.getItemID()] instanceof ItemTool) //Tools are 16x16 render size
		{
			width = 16;
			height = 16;
		}
		else if(Item.itemsList[stack.getItemID()] instanceof ItemArmor) //Armor is 14x14 render size
		{
			width = 14;
			height = 14;
		}
		else //everything else is 10x10 render size
		{
			width = 10;
			height = 10;
		}	
		
		this.x = x;
		this.y = y;
		blockWidth = (double)(width) / 6;
		blockHeight = (double)(height) / 6;
		ticksBeforePickup = 20;
		this.setStack(stack);
	}
		
	/**
	 * Gets whether or not the EntityItemStack can be picked up. 
	 * @return true if it can be picked up, otherwise false
	 */
	public boolean canBePickedUp()
	{
		return ticksBeforePickup <= 0;
	}
	
	/**
	 * Updates the EntityItemStack, decreasing time before it can be picked up if applicable.
	 */
	public void update()
	{
		ticksBeforePickup--;
	}
	
	public ItemStack getStack() 
	{
		return stack;
	}

	public void setStack(ItemStack stack) 
	{
		this.stack = stack;
	}
}

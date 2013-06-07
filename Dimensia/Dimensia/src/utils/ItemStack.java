package utils;

import items.Item;

import java.io.Serializable;

import spells.Spell;

import blocks.Block;

/**
 * <code>ItemStack implements Serializable</code>
 * <br><br>
 * ItemStack contains information about an item stored in an inventory of some sort, 
 * generally a: Chest, InventoryPlayer, or EntityItemStack. An ItemStack contains fields for 
 * the name of the Item/Block being stored, the current and maximum stack size of that Item/Block, 
 * and the unique id of that Item/Block. 
 * <br><br>
 * ItemStack implements numerous constructors for nearly every possible information combo. 
 * The ItemID, max stack size, and item name can only be set in the constructor, to prevent the 
 * ItemStack becoming corrupt or changing item type.
 * <br><br>
 * Additional methods of interest are {@link #addToStack(int)} and {@link #removeFromStack(int)} 
 * which can add to or remove from the Itemstacks's current stack size. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ItemStack 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String itemName;
	private int stackSize;
	private int maxStackSize;
	private int itemID;
	//private ItemGem[] sockets = { (ItemGem) Item.gemDefense1 };
	
	public ItemStack(ActionbarItem item)
	{
		itemID = item.id;
		maxStackSize = item.maxStackSize;
		stackSize = 1;
		itemName = item.name;
	}
	
	public ItemStack(ActionbarItem item, int stackSize)
	{
		itemID = item.id;
		maxStackSize = item.maxStackSize;
		this.stackSize = stackSize; 
		if(stackSize > maxStackSize) 
		{
			stackSize = maxStackSize;
		}
		itemName = item.name;
	}
		
	public ItemStack(int id, int stackSize)
	{
		itemID = id;
		this.stackSize = stackSize;
		
		if(itemID < ActionbarItem.itemIndex)
		{
			itemName = Block.blocksList[id].name;
			maxStackSize = Block.blocksList[id].maxStackSize;
		}
		else if(itemID >= ActionbarItem.itemIndex && itemID < ActionbarItem.spellIndex)
		{
			itemName = Item.itemsList[id].name;
			maxStackSize = Item.itemsList[id].maxStackSize;			
		}		
		else
		{
			itemName = Spell.spellList[id].name;
			maxStackSize = Spell.spellList[id].maxStackSize;
		}
	}
	
	public ItemStack(int id)
	{
		itemID = id;
		stackSize = 1;
		
		if(itemID < ActionbarItem.itemIndex)
		{
			itemName = Block.blocksList[id].name;
			maxStackSize = Block.blocksList[id].maxStackSize;
		}
		else if(itemID >= ActionbarItem.itemIndex && itemID < ActionbarItem.spellIndex)
		{
			itemName = Item.itemsList[id].name;
			maxStackSize = Item.itemsList[id].maxStackSize;			
		}		
		else
		{
			itemName = Spell.spellList[id].name;
			maxStackSize = Spell.spellList[id].maxStackSize;
		}
	}
	
	public ItemStack(ItemStack stack)
	{
		this.itemName = stack.getItemName();
		this.stackSize = stack.getStackSize();
		this.itemID = stack.getItemID();
		this.maxStackSize = stack.getMaxStackSize();
	}
	
	/**
	 * Adds the specified value to the ItemStack stack size. If this value exceeds the maxStackSize of the ItemStack then
	 * the stacksize is set to the maximum allowed value.
	 * @param amount
	 */
	public void addToStack(int amount)
	{
		int i = new Integer(amount);
		stackSize += i;
		if(stackSize > maxStackSize) 
		{
			stackSize = maxStackSize;
		}
	}
	
	/**
	 * Removes the specified amount from the ItemStack's stack size. This can create a negative value.
	 * @param amount the amount to remove from this ItemStack's stack size
	 */
	public void removeFromStack(int amount)
	{
		stackSize -= amount;
	}
	
	/**
	 * Sets the ItemStack's stack size to the given integer value.
	 * @param i the new stack size of the ItemStack
	 */
	public void setStackSize(int i)
	{
		stackSize = i;
	}
	
	/**
	 * Gets the name of the ItemStack's contents
	 * @return the name of the ItemStack's contents
	 */
	public final String getItemName()
	{
		return new String(itemName);
	}
	
	/**
	 * Gets the max stack size of this ItemStack, based on the values in Item.java and Block.java
	 * @return the max stack size of this ItemStack
	 */
	public final int getMaxStackSize()
	{
		return new Integer(maxStackSize);
	}
	
	/**
	 * Gets the stack size of this instance of ItemStack.
	 * @return the stack size of this ItemStack
	 */ 
	public final int getStackSize()
	{
		return new Integer(stackSize);
	}
	
	/**
	 * Gets the unique Item or Block ID of this instance of ItemStack.
	 * @return the ID of this ItemStack
	 */
	public final int getItemID()
	{
		return new Integer(itemID);
	}
}

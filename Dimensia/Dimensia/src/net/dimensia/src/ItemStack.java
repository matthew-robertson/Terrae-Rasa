package net.dimensia.src;

import java.io.Serializable;

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
	
	public ItemStack(Item item)
	{
		itemID = item.itemID;
		maxStackSize = item.maxStackSize;
		stackSize = 1;
		itemName = item.itemName;
	}
	
	public ItemStack(Item item, int stackSize)
	{
		itemID = item.itemID;
		maxStackSize = item.maxStackSize;
		this.stackSize = stackSize; if(stackSize > maxStackSize) stackSize = maxStackSize;
		itemName = item.itemName;
	}
	
	public ItemStack(Block block)
	{
		itemID = block.blockID;
		maxStackSize = block.maxStackSize; 
		stackSize = 1;
		itemName = block.blockName;
	}
	
	public ItemStack(Block block, int stackSize)
	{
		itemID = block.blockID;
		maxStackSize = block.maxStackSize; 
		this.stackSize = stackSize; if(stackSize > maxStackSize) stackSize = maxStackSize;
		itemName = block.blockName;
	}
	
	public ItemStack(int id, int stackSize)
	{
		itemID = id;
		this.stackSize = stackSize;
		
		if(itemID < 2048)
		{
			itemName = Block.blocksList[id].blockName;
			maxStackSize = Block.blocksList[id].maxStackSize;
		}
		else
		{
			itemName = Item.itemsList[id].itemName;
			maxStackSize = Item.itemsList[id].maxStackSize;			
		}		
	}
	
	public ItemStack(int id)
	{
		itemID = id;
		stackSize = 1;
		
		if(itemID < 2048)
		{
			itemName = Block.blocksList[id].blockName;
			maxStackSize = Block.blocksList[id].maxStackSize;
		}
		else
		{
			itemName = Item.itemsList[id].itemName;
			maxStackSize = Item.itemsList[id].maxStackSize;			
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

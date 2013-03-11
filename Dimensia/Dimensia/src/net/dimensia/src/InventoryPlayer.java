package net.dimensia.src;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * <code>InventoryPlayer implements Serializable</code> <br> 
 * <br>
 * <code>InventoryPlayer</code> is responsible for tracking all the items the player is holding or has equipped.
 * A new instance of <code>InventoryPlayer</code> creates a mainInventory of size 48, an armour inventory of size 
 * 10 (which is not fully used), and a garbage can of size 1 (see {@link #InventoryPlayer()} for any other 
 * uses of the Constructor).
 * <br><br>
 * 
 * /home/alec/Desktop/Dimensia/Dimensia/src/net/dimensia/client/
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class InventoryPlayer 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Integer> inventoryTotals;
	private ItemStack[] trash;
	private ItemStack[] mainInventory;
	private ItemStack[] armorInventory;		
	
	/**
	 * Only constructor for InventoryPlayer. <br>
	 * Initializes the inventory to contain: <br>
	 *  		1. A mainInventory[] of size 48 <br>
	 *  		2. An armorInventory[] of size 10 <br>
	 *  		3. A trash[] of size 1. <br>
	 *   		4. An inventoryTotals Hashtable of all Items/Blocks, with a starting quantity of 0 each <br>
	 */
	public InventoryPlayer()
	{
		mainInventory = new ItemStack[48];
		armorInventory = new ItemStack[10];
		trash = new ItemStack[1];
		initializeInventoryTotals(false);	
	}
	
	/**
	 * Populates the inventoryTotals Hashtable with every possible item/block that exists (each with a value of 0)
	 */
	public void initializeInventoryTotals(boolean isReloaded)
	{
		if(isReloaded)
		{
			for(int i = 0; i < Item.itemsList.length; i++) //Items
			{
				if(Item.itemsList[i] != null && inventoryTotals.get(Item.itemsList[i].getItemName()) == null) //A new Item needs added 
				{
					inventoryTotals.put(Item.itemsList[i].itemName, 0);
				}
			}
			for(int i = 0; i < Block.blocksList.length; i++) //Blocks
			{	
				if(Block.blocksList[i] != null  && inventoryTotals.get(Block.blocksList[i].getBlockName()) == null) //A new Block needs added
				{
					inventoryTotals.put(Block.blocksList[i].blockName, 0);					
				}
			}
		}
		else
		{
			inventoryTotals = new Hashtable<String, Integer>();
			
			for(int i = 0; i < Item.itemsList.length; i++) //Items
			{
				if(Item.itemsList[i] != null && inventoryTotals.get(Item.itemsList[i].getItemName()) != null) //If the Item exists and is already in inventoryTotals
				{
					if(Item.itemsList[i].getItemName().toLowerCase() != "unnamed") //And it isnt unnamed
					{
						throw new RuntimeException("Item name already exists : " + Item.itemsList[i].getItemName()); //There is a name conflict, throw an exception
					}
				}
				else if(Item.itemsList[i] != null) //Otherwise if the item exists, add it with a starting value of 0
				{
					inventoryTotals.put(Item.itemsList[i].itemName, 0);
				}
			}
			for(int i = 0; i < Block.blocksList.length; i++) //Blocks
			{	
				if(Block.blocksList[i] != null  && inventoryTotals.get(Block.blocksList[i].getBlockName()) != null) //If the block exists
				{
					if(Block.blocksList[i].getBlockName().toLowerCase() != "unnamed") //And it isnt unnamed
					{
						throw new RuntimeException("Block name already exists : " + Block.blocksList[i].getBlockName()); //There is a name conflict, throw an exception
					}
				}
				else if(Block.blocksList[i] != null) //Otherwise if the block exists, add it with a starting value of 0
				{
					inventoryTotals.put(Block.blocksList[i].blockName, 0);				
				}
			}
		}
	}
	
	/**
	 * Checks for the first null slot in mainInventory[]
	 * @return the slot in mainInventory[] that's null, or -1 for a failure
	 */
	public int getFirstEmptySlot()
	{
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] == null) //Is the slot a value null? if so return the index
			{
				return i;
			}
		}				
		return -1;
	}
	
	/**
	 * Checks for a partial ItemStack in the inventory
	 * @param stack the stack to check for
	 * @return the mainInventory[] slot containing the item, or -1 if the check failed
	 */
	public int doesPartialStackExist(ItemStack stack)
	{
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] == null) 
			{
				continue;
			}
			if(mainInventory[i].getItemID() == stack.getItemID() && mainInventory[i].getStackSize() < stack.getMaxStackSize()) 
			{ //If the itemID matches and there's less than the max stacksize; success
				return i;
			}
		}		
		return -1;
	}
	
	/**
	 * Gets the size of the mainInventory
	 * @return the length of the mainInventory[] array
	 */
	public int getMainInventoryLength()
	{
		return mainInventory.length;
	}
	
	/**
	 * Attempts to pick up an ItemStack
	 * @param stack Stack to pick up
	 * @return whatever's left, or null for a successful operation
	 */
	public ItemStack pickUpItemStack(World world, EntityLivingPlayer player, ItemStack stack)
	{
		int slot;
		int size = stack.getStackSize();
		try		
		{
			player.onInventoryChange();
		}
		catch (Exception e)	//Try/catch- fixing lazy programming for many years!
		{	
		}
		
		if((slot = doesPartialStackExist(stack)) != -1) //Is there already a partial stack of the item?
		{
			if(mainInventory[slot].getStackSize() + size > mainInventory[slot].getMaxStackSize()) //Is there more than enough to fill that stack? is so fill up that stack and continue;
			{
				int t = stack.getMaxStackSize() - mainInventory[slot].getStackSize();
				size -= t;
				inventoryTotals.put(mainInventory[slot].getItemName(), inventoryTotals.get(mainInventory[slot].getItemName()) + t);
				mainInventory[slot].addToStack(t);				
			}
			else //If there isn't, add the stacks and return
			{
				mainInventory[slot].addToStack(size);
				inventoryTotals.put(mainInventory[slot].getItemName(), inventoryTotals.get(mainInventory[slot].getItemName()) + size);
				return null;
			}
		}
				
		if((slot = getFirstEmptySlot()) == -1) //Is there A free inventory space? If not return what's left of the itemStack 
		{
			stack.setStackSize(size);
			return stack;
		}
		else //If there is space, Start adding items to the inventory
		{
			for(int i = 0; i < size; )
			{
				slot = getFirstEmptySlot(); //Get first empty slot
				if(slot == -1) break;
				else
				{
					if(size > stack.getMaxStackSize()) //Is there more than a stack of the item? if so loop again
					{
						mainInventory[slot] = new ItemStack(stack.getItemID(), stack.getStackSize());
						mainInventory[slot].setStackSize(stack.getMaxStackSize());
						inventoryTotals.put(mainInventory[slot].getItemName(), inventoryTotals.get(mainInventory[slot].getItemName()) + mainInventory[slot].getStackSize());						
						size -= stack.getMaxStackSize();
						continue;
					}
					else //If not, put what's left of the stack in the inventory and return
					{
						mainInventory[slot] = new ItemStack(stack.getItemID(), stack.getStackSize());
						mainInventory[slot].setStackSize(size);
						inventoryTotals.put(mainInventory[slot].getItemName(), inventoryTotals.get(mainInventory[slot].getItemName()) + size);
						size = 0;
						return null;
					}
				}	
			}
		}
		
		stack.setStackSize(size);
		return stack;
	}
	
	/**
	 * Attempts to remove items from the inventory, nothing is removed if there isnt enough.
	 * @param stack stack to try to remove (including # to remove)
	 * @return success of the removal
	 */
	public boolean removeItemsFromInventory(World world, EntityLivingPlayer player, ItemStack stack)
	{
		int quantity = stack.getStackSize();
		boolean[] nullSlots = new boolean[mainInventory.length];
		boolean removeItems = false;
		player.onInventoryChange();
		
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] == null)
			{
				continue;			
			}
			if(mainInventory[i].getItemID() == stack.getItemID()) //The stack is a match.
			{
				quantity -= mainInventory[i].getStackSize();
				
				if(quantity > 0) //If there's still more to remove, go to the next stack and mark the current one to be nullified
				{
					nullSlots[i] = true;
					continue;
				}				
				else if(quantity == 0) //There isnt anything left in the current stack, and it's possible to remove items now
				{
					nullSlots[i] = true;
					removeItems = true;
					break;
				}
				else //There was a partial itemstack left, so a bit of additional math is needed
				{
					mainInventory[i].setStackSize(Math.abs(quantity));	
					removeItems = true;
					break;
				}				
			}
		}		
		
		if(removeItems) //Items can be removed
		{ 
			for(int i = 0; i < mainInventory.length; i++)
			{
				if(nullSlots[i]) //if the slot needs removed, remove it
					mainInventory[i] = null;
			}
			inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) - stack.getStackSize()); //adjust totals
			return true;
		}					
		
		return false;
	}

	/**
	 * Removes items from mainInventory[] at the specified index
	 * @param howMany how many items to remove
	 * @param index slot in mainInventory[]
	 * @return whether the operation succeeded or not (fails due to not enough items)
	 */
	public boolean removeItemsFromInventoryStack(int howMany, int index)
	{
		if(howMany < mainInventory[index].getStackSize())
		{
			mainInventory[index].removeFromStack(howMany);
			inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - howMany); //adjust totals
			return true;
		}
		else if(howMany == mainInventory[index].getStackSize())
		{
			mainInventory[index].removeFromStack(howMany);
			inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - howMany); //adjust totals
			mainInventory[index] = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an entire stack of the mainInventory[].
	 * @param index index in mainInventory[] to remove
	 */
	public void removeEntireStackFromInventory(World world, EntityLivingPlayer player, int index)
	{
		player.onInventoryChange();
		inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - mainInventory[index].getStackSize()); 
		mainInventory[index] = null;
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the mainInventory[]
	 * @param stack stack to place in mainInventory[]
	 * @param index where to place the stack in mainInventory[]
	 * @return success of the operation
	 */
	public boolean putItemStackInSlot(World world, EntityLivingPlayer player, ItemStack stack, int index)
	{
		player.onInventoryChange();
		
		if(stack == null) //the stack to be placed is null, so clear the slot in mainInventory[] (this has to get a bit hacky for control reasons)
		{
			inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - mainInventory[index].getStackSize());		
			mainInventory[index] = null;
		}
		else //otherwise put the stack in the inventory
		{
			inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) + stack.getStackSize());		
			mainInventory[index] = new ItemStack(stack);	
		}

		return true;
	}
	
	/**
	 * Attempts to combine two ItemStack in the mainInventory
	 * @param stack stack to combine with the current one
	 * @param index where to combine stacks in mainInventory[]
	 * @return success of operation
	 */
	public boolean combineItemStacksInSlot(World world, EntityLivingPlayer player, ItemStack stack, int index)
	{
		player.onInventoryChange();
		if(mainInventory[index] == null || mainInventory[index].getItemID() != stack.getItemID())
		{	
			return false;
		}
		
		int size = stack.getStackSize();
		mainInventory[index].addToStack(size);
		inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) + stack.getStackSize());		
		return true;
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the armorInventory[]
	 * @param stack stack to place in armorInventory[]
	 * @param index where to place the stack in armorInventory[]
	 * @return success of the operation
	 */
	public boolean setArmorInventoryStack(ItemStack stack, int index)
	{
		if(stack == null)
		{
			armorInventory[index] = null;
		}
		else
		{
			armorInventory[index] = new ItemStack(stack);
		}
		return true;
	}
	
	/**
	 * Checks to see if the player has anything in their (main)inventory
	 * @return true if the mainInventory is empty, otherwise false
	 */
	public boolean isEmpty()
	{
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(mainInventory[i] != null)
			{
				return false;
			}
		}		
		return true;
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the trashInventory[]
	 * @param stack stack to place in trashInventory[]
	 * @param index where to place the stack in trashInventory[]
	 * @return success of the operation
	 */
	public boolean setTrashStack(ItemStack stack, int index)
	{		
		if(stack == null)
		{
			trash[index] = null;
		}
		else
		{
			trash[index] = new ItemStack(stack);
		}
		return true;
	}
	
	/**
	 * Volatile function to return the mainInventory[]. Very vulnerable to pointer issues if modification to mainInventory[]
	 * is attempted using this method
	 * @return the mainInventory[] array
	 */
	public final ItemStack[] getMainInventory()
	{
		return mainInventory;
	}
	
	/**
	 * Volatile function to return the armorInventory[]. Very vulnerable to pointer issues if modification to armorInventory[]
	 * is attempted using this method
	 * @return the armorInventory[] array
	 */
	public final ItemStack[] getArmorInventory()
	{
		return armorInventory;
	}

	/**
	 * Volatile function to return the trash[]. Very vulnerable to pointer issues if modification to trash[]
	 * is attempted using this method. Currently this should return an ItemStack[] of size 1 all the time.
	 * @return the trash[] array
	 */
	public final ItemStack[] getTrash()
	{
		return trash;
	}
	
	/**
	 * Gets a reference safe ItemStack from the mainInventory[] 
	 * @param index the slot of mainInventory[] to return.
	 * @return a pointer(reference) safe ItemStack at the specified index of mainInventory[] or null if nothing is there
	 */
	public final ItemStack getMainInventoryStack(int index)
	{
		return  (mainInventory[index] != null) ? new ItemStack(mainInventory[index]) : null;
	}
		
	/**
	 * Gets a reference safe ItemStack from the armorInventory[] 
	 * @param index the slot of armorInventory[] to return.
	 * @return a pointer(reference) safe ItemStack at the specified index of armorInventory[] or null if nothing is there
	 */
	public final ItemStack getArmorInventoryStack(int index)
	{
		return  (armorInventory[index] != null) ? new ItemStack(armorInventory[index]) : null;
	}
		
	/**
	 * Gets a reference safe ItemStack from the trash[]
	 * @param index the slot of trash[] to return.
	 * @return a pointer(reference) safe ItemStack at the specified index trash[] or null if nothing is there
	 */
	public final ItemStack getTrashStack(int index)
	{
		return  (trash[index] != null) ? new ItemStack(trash[index]) : null;
	}
	
	/**
	 * Gets the quantity of an Item/Block in the mainInventory[] 
	 * @param name the Item/Block name to check. It should be given by using Item.getItemName() or Block.getBlockName()
	 * @return the quantity of that item in the mainInventory[]
	 */
	public final int getTotalInInventory(String name)
	{
		return inventoryTotals.get(name);
	}
		
	/**
	 * Gets the size of the armorInventory
	 * @return the length of the armorInventory[] array
	 */
	public int getArmorInventoryLength() 
	{
		return armorInventory.length;
	}
	
	/**
	 * Iterates through the armorInventory[], destroying the first item with a saving relic
	 * attribute (to prevent death).
	 */
	public void removeSavingRelic()
	{
		for(int i = 0; i < armorInventory.length; i++)
		{
			if(armorInventory[i] != null)
			{
				if(((ItemArmor)(Item.itemsList[armorInventory[i].getItemID()])).getIsSavingRelic())
				{
					armorInventory[i] = null;
					return;
				}
			}
		}
	}
}

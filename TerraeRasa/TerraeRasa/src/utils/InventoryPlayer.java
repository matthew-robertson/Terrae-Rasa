package utils;

import items.Item;
import items.ItemArmor;
import items.ItemArmorAccessory;
import items.ItemArmorBelt;
import items.ItemArmorBody;
import items.ItemArmorBoots;
import items.ItemArmorGloves;
import items.ItemArmorHelmet;
import items.ItemArmorPants;

import java.util.Hashtable;

import blocks.Block;
import entities.EntityPlayerBase;

/**
 * <br>
 * <code>InventoryPlayer</code> is responsible for tracking all the items the player is holding or has equipped.
 * A new instance of <code>InventoryPlayer</code> creates a mainInventory of size 48, an armour inventory of size 
 * 10 (which is not fully used), and a garbage can of size 1 (see {@link #InventoryPlayer()} for any other 
 * uses of the Constructor).
 * 
 * <br><br>
 * 
 * V1.1: All changes to the armour inventory should go through {@link #setArmorInventoryStack(EntityPlayerBase, ItemStack, int)} now. 
 * This is to ensure that player stats get updated appropriately.
 * 
 * <br><br>
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class InventoryPlayer 
{
	public final static int MAIN_INVENTORY_SIZE = 48;
	public final static int ARMOR_INVENTORY_SIZE = 10;
	public final static int QUIVER_SIZE = 4;
	private Hashtable<String, Integer> inventoryTotals;
	private ItemStack[] trash;
	private ItemStack[] mainInventory;
	/**
	 * Slots are in order (the actual index can be found by subtracting 1 from the number next to that piece of
	 * armor): <br>
	 * <ol>
	 * <li> Helmet </li>
	 * <li> Body </li>
	 * <li> Belt </li>
	 * <li> Pants </li>
	 * <li> Boots </li>
	 * <li> Gloves </li>
	 * <li> Accessories </li>
	 * <li> Accessories </li>
	 * <li> Accessories </li>
	 * <li> Accessories </li>
	 * </ol>
	 */
	private ItemStack[] armorInventory;		
	private ItemStack[] quiver; 
	public static final int HELMET_INDEX = 0;
	public static final int BODY_INDEX = 1;
	public static final int BELT_INDEX = 2;
	public static final int PANTS_INDEX = 3;
	public static final int BOOTS_INDEX = 4;
	public static final int GLOVES_INDEX = 5;
	public static final int ARMOR_INVENTORY_LENGTH = 10;
	
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
		mainInventory = new ItemStack[MAIN_INVENTORY_SIZE];
		armorInventory = new ItemStack[ARMOR_INVENTORY_SIZE];
		trash = new ItemStack[1];
		quiver = new ItemStack[QUIVER_SIZE];
		initializeInventoryTotals();	
	}
	
	/**
	 * Constructs an InventoryPlayer with the given DisplayableItemStack[] for main, armor, and quiver inventories
	 * @param mainInventory an DisplayableItemStack[] of length 48 representing the mainInventory
	 * @param armorInventory an DisplayableItemStack[] of length 10 representing the armorInventory
	 * @param quiver an DisplayableItemStack[] of length 4 representing the quiver
	 */
	public InventoryPlayer(ItemStack[] mainInventory, ItemStack[] armorInventory, ItemStack[] quiver)
	{
		//Initialize the main inventory
		this.mainInventory = new ItemStack[MAIN_INVENTORY_SIZE];
		if(mainInventory.length < MAIN_INVENTORY_SIZE)
		{
			for(int i = 0; i < mainInventory.length; i++)
			{
				if(mainInventory[i] != null)
					this.mainInventory[i] = new ItemStack(mainInventory[i]);
			}
		}
		else
		{
			for(int i = 0; i < MAIN_INVENTORY_SIZE; i++)
			{
				if(mainInventory[i] != null)
					this.mainInventory[i] = new ItemStack(mainInventory[i]);
			}
		}
		
		//Initialize the armor inventory
		this.armorInventory = new ItemStack[ARMOR_INVENTORY_SIZE];
		if(armorInventory.length < ARMOR_INVENTORY_SIZE)
		{
			for(int i = 0; i < armorInventory.length; i++)
			{
				if(armorInventory[i] != null)
					this.armorInventory[i] = new ItemStack(armorInventory[i]);
			}
		}
		else
		{
			for(int i = 0; i < ARMOR_INVENTORY_SIZE; i++)
			{
				if(armorInventory[i] != null)
					this.armorInventory[i] = new ItemStack(armorInventory[i]);
			}
		}
		
		//Initialize the quiver
		this.quiver = new ItemStack[QUIVER_SIZE];
		if(quiver.length < QUIVER_SIZE)
		{
			for(int i = 0; i < quiver.length; i++)
			{
				if(quiver[i] != null)
					this.quiver[i] = new ItemStack(quiver[i]);
			}
		}
		else
		{
			for(int i = 0; i < QUIVER_SIZE; i++)
			{
				if(quiver[i] != null)
					this.quiver[i] = new ItemStack(quiver[i]);
			}
		}
		
		trash = new ItemStack[1];
		initializeInventoryTotals();
	}
	
	/**
	 * Populates the inventoryTotals Hashtable with every possible item/block that exists (each with a value of 0)
	 */
	private void initializeInventoryTotals()
	{		
		inventoryTotals = new Hashtable<String, Integer>();
		
		//Put Every existing block and item in the list with a default value of 0.
		//TODO change name -> id
		for(int i = 0; i < Item.itemsList.length; i++) //Items
		{
			if(Item.itemsList[i] != null && inventoryTotals.get(Item.itemsList[i].getName()) != null) //If the Item exists and is already in inventoryTotals
			{
				if(Item.itemsList[i].getName().toLowerCase() != "unnamed") //And it isnt unnamed
				{
					throw new RuntimeException("Item name already exists : " + Item.itemsList[i].getName()); //There is a name conflict, throw an exception
				}
			}
			else if(Item.itemsList[i] != null) //Otherwise if the item exists, add it with a starting value of 0
			{
				inventoryTotals.put(Item.itemsList[i].getName(), 0);
			}
		}
		for(int i = 0; i < Block.blocksList.length; i++) //Blocks
		{	
			if(Block.blocksList[i] != null && inventoryTotals.get(Block.blocksList[i].getName()) != null) //If the block exists
			{
				if(Block.blocksList[i].getName().toLowerCase() != "unnamed") //And it isnt unnamed
				{
					throw new RuntimeException("Block name already exists : " + Block.blocksList[i].getName()); //There is a name conflict, throw an exception
				}
			}
			else if(Block.blocksList[i] != null) //Otherwise if the block exists, add it with a starting value of 0
			{
				inventoryTotals.put(Block.blocksList[i].getName(), 0);				
			}
		}
		
		for(ItemStack stack : mainInventory)
		{
			if(stack != null && stack.getItemID() < ActionbarItem.spellIndex){
				inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) + stack.getStackSize());
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
	 * Regenerates the passives and auras on an item based on their given affixes.
	 */
	public void forceAffixRegeneration()
	{
		for(ItemStack stack : mainInventory)
			if(stack != null)
				stack.regenerateBonuses();
		for(ItemStack stack : armorInventory)
			if(stack != null)
				stack.regenerateBonuses();
		for(ItemStack stack : quiver)
			if(stack != null)
				stack.regenerateBonuses();
		for(ItemStack stack : trash)
			if(stack != null)
				stack.regenerateBonuses();
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
	public ItemStack pickUpItemStack(EntityPlayerBase player, ItemStack stack)
	{
		int slot;
		int size = stack.getStackSize();
		
		player.onInventoryChange();		
		
		if((slot = doesPartialStackExist(stack)) != -1) //Is there already a partial stack of the item?
		{
			player.changedInventorySlots.add("i " + slot);
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
				if(slot == -1)
					break;
				else
				{
					player.changedInventorySlots.add("i " + slot);
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
						mainInventory[slot] = new ItemStack(stack);
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
	 * Checks for a partial ItemStack in the quiver
	 * @param stack the stack to check for
	 * @return the quiver[] slot containing the item, or -1 if the check failed
	 */
	public int doesPartialQuiverStackExist(ItemStack stack)
	{
		for(int i = 0; i < quiver.length; i++)
		{
			if(quiver[i] == null) 
			{
				continue;
			}
			if(quiver[i].getItemID() == stack.getItemID() && quiver[i].getStackSize() < stack.getMaxStackSize()) 
			{ //If the itemID matches and there's less than the max stacksize; success
				return i;
			}
		}		
		return -1;
	}
	
	/**
	 * Checks for the first null slot in the quiver[]
	 * @return the slot in the quiver[] that's null, or -1 for a failure
	 */
	public int getFirstEmptyQuiverSlot()
	{
		for(int i = 0; i < quiver.length; i++)
		{
			if(quiver[i] == null) //Is the slot a value null? if so return the index
			{
				return i;
			}
		}				
		return -1;
	}

	/**
	 * Attempts to place an ItemStack it into the appropriate armour slot.
	 * @param stack the ItemStack to pick up
	 * @return whatever's left, or null for a successful operation
	 */
	public ItemStack pickUpArmorItemStack(EntityPlayerBase player, ItemStack stack)
	{
		Item item = (ItemArmor)(Item.itemsList[stack.getItemID()]);
		player.onArmorChange();
		
		if(item instanceof ItemArmorHelmet && armorInventory[0] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[0], 0);
			player.changedInventorySlots.add("a 0");
			return null;			
		}
		else if(item instanceof ItemArmorBody && armorInventory[1] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[1], 1);
			player.changedInventorySlots.add("a 1");
			return null;
		}
		else if(item instanceof ItemArmorBelt && armorInventory[2] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[2], 2);
			player.changedInventorySlots.add("a 2");
			return null;
		}
		else if(item instanceof ItemArmorPants && armorInventory[3] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[3], 3);
			player.changedInventorySlots.add("a 3");
			return null;
		}
		else if(item instanceof ItemArmorBoots && armorInventory[4] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[4], 4);
			player.changedInventorySlots.add("a 4");
			return null;
		}
		else if(item instanceof ItemArmorGloves && armorInventory[5] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[5], 5);
			player.changedInventorySlots.add("a 5");
			return null;
		}
		else if(item instanceof ItemArmorAccessory && armorInventory[6] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[6], 6);
			player.changedInventorySlots.add("a 6");
			return null;
		}
		else if(item instanceof ItemArmorAccessory && armorInventory[7] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[7], 7);
			player.changedInventorySlots.add("a 7");
			return null;
		}
		else if(item instanceof ItemArmorAccessory && armorInventory[8] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[8], 8);
			player.changedInventorySlots.add("a 8");
			return null;			
		}
		else if(item instanceof ItemArmorAccessory && armorInventory[9] == null)
		{
			setArmorInventoryStack(player, stack, armorInventory[9], 9);
			player.changedInventorySlots.add("a 9");
			return null;
		}
		
		return stack;
	}

	/**
	 * Attempts to place an ItemStack into the quiver.
	 * @param stack Stack to pick up
	 * @return whatever's left, or null for a successful operation
	 */
	public ItemStack pickUpQuiverItemStack(EntityPlayerBase player, ItemStack stack)
	{
		int slot;
		int size = stack.getStackSize();		
		player.onInventoryChange();
			
		if((slot = doesPartialQuiverStackExist(stack)) != -1) //Is there already a partial stack of the item?
		{
			player.changedInventorySlots.add("q " + slot);
			if(quiver[slot].getStackSize() + size > quiver[slot].getMaxStackSize()) //Is there more than enough to fill that stack? is so fill up that stack and continue;
			{
				int t = stack.getMaxStackSize() - quiver[slot].getStackSize();
				size -= t;
				quiver[slot].addToStack(t);				
			}
			else //If there isn't, add the stacks and return
			{
				quiver[slot].addToStack(size);
				return null;
			}
		}
				
		if((slot = getFirstEmptyQuiverSlot()) == -1) //Is there A free inventory space? If not return what's left of the itemStack 
		{
			stack.setStackSize(size);
			return stack;
		}
		else //If there is space, Start adding items to the inventory
		{
			for(int i = 0; i < size; )
			{
				slot = getFirstEmptyQuiverSlot(); //Get first empty slot
				if(slot == -1) break;
				else
				{
					player.changedInventorySlots.add("q " + slot);
					if(size > stack.getMaxStackSize()) //Is there more than a stack of the item? if so loop again
					{
						quiver[slot] = new ItemStack(stack.getItemID(), stack.getStackSize());
						quiver[slot].setStackSize(stack.getMaxStackSize());
						size -= stack.getMaxStackSize();
						continue;
					}
					else //If not, put what's left of the stack in the inventory and return
					{
						quiver[slot] = new ItemStack(stack.getItemID(), stack.getStackSize());
						quiver[slot].setStackSize(size);
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
	public boolean removeItemsFromInventory(EntityPlayerBase player, ItemStack stack)
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
					player.changedInventorySlots.add("i " + i);
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
				{
					mainInventory[i] = null;
					player.changedInventorySlots.add("i " + i);
				}
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
	public boolean removeItemsFromInventoryStack(EntityPlayerBase player, int howMany, int index)
	{
		if(mainInventory[index] == null)
		{
			System.out.println("Null ItemStack passed to InventoryPlayer.removeItemsFromInventory@slot=" + index);
			return false;
		}
		if(howMany < mainInventory[index].getStackSize())
		{
			player.changedInventorySlots.add("i " + index);
			mainInventory[index].removeFromStack(howMany);
			if(mainInventory[index].getItemID() < ActionbarItem.spellIndex)
			{
				inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - howMany); //adjust totals
			}
			return true;
		}
		else if(howMany == mainInventory[index].getStackSize())
		{
			player.changedInventorySlots.add("i " + index);
			mainInventory[index].removeFromStack(howMany);
			if(mainInventory[index].getItemID() < ActionbarItem.spellIndex)
			{
				inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - howMany); //adjust totals
			}
			mainInventory[index] = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an entire stack of the mainInventory[].
	 * @param index index in mainInventory[] to remove
	 */
	public void removeEntireStackFromInventory(EntityPlayerBase player, int index)
	{
		player.onInventoryChange();
		inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - mainInventory[index].getStackSize()); 
		mainInventory[index] = null;
		player.changedInventorySlots.add("i " + index);
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the mainInventory[]
	 * @param stack stack to place in mainInventory[]
	 * @param index where to place the stack in mainInventory[]
	 * @return success of the operation
	 */
	public boolean putItemStackInSlot(EntityPlayerBase player, ItemStack stack, int index)
	{
		player.onInventoryChange();

		//the stack to be placed is null, so clear the slot in mainInventory[] (this has to get a bit hacky for control reasons)
		if(stack == null && mainInventory[index] != null) 
		{
			player.changedInventorySlots.add("i " + index);
			inventoryTotals.put(mainInventory[index].getItemName(), inventoryTotals.get(mainInventory[index].getItemName()) - mainInventory[index].getStackSize());		
			mainInventory[index] = null;
		}
		else if(stack != null)//otherwise put the stack in the inventory
		{
			player.changedInventorySlots.add("i " + index);
			if(stack.getItemID() < ActionbarItem.spellIndex)
			{
				inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) + stack.getStackSize());		
			}
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
	public boolean combineItemStacksInSlot(EntityPlayerBase player, ItemStack stack, int index)
	{
		player.onInventoryChange();
		if(mainInventory[index] == null || mainInventory[index].getItemID() != stack.getItemID())
		{	
			return false;
		}
		
		player.changedInventorySlots.add("i " + index);
		int size = stack.getStackSize();
		mainInventory[index].addToStack(size);
		inventoryTotals.put(stack.getItemName(), inventoryTotals.get(stack.getItemName()) + stack.getStackSize());		
		return true;
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the armorInventory[]. If null is given as an itemstack
	 * then whatever is in that itemstack will be removed. Use this to remove or add armour from the armorInventory[]
	 * and update player stats.
	 * @param stack stack to place in armorInventory[]
	 * @param index where to place the stack in armorInventory[]
	 * @return success of the operation
	 */
	public boolean setArmorInventoryStack(EntityPlayerBase player, ItemStack newStack, ItemStack oldStack, int index)
	{
		player.changedInventorySlots.add("a " + index);
		if(newStack == null)
		{
			//If a piece of armor is being removed, then ensure its stats are appropriately neutralized
			if(armorInventory[index] != null)
			{
				player.removeSingleArmorItem((ItemArmor)(Item.itemsList[armorInventory[index].getItemID()]), oldStack, index);
			}
			armorInventory[index] = null;
		}
		else
		{
			if(armorInventory[index] != null)
			{
				player.removeSingleArmorItem((ItemArmor)(Item.itemsList[armorInventory[index].getItemID()]), oldStack, index);
			}
			armorInventory[index] = new ItemStack(newStack);
			//Apply a newly added armour piece's stats
			player.applySingleArmorItem((ItemArmor)(Item.itemsList[newStack.getItemID()]), newStack, index);
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
	public boolean setTrashStack(EntityPlayerBase player, ItemStack stack, int index)
	{	
		player.changedInventorySlots.add("t " + index);
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
	public void removeSavingRelic(EntityPlayerBase player)
	{
		for(int i = 0; i < armorInventory.length; i++)
		{
			if(armorInventory[i] != null)
			{
				if(((ItemArmor)(Item.itemsList[armorInventory[i].getItemID()])).getIsSavingRelic())
				{
					player.changedInventorySlots.add("a " + i);
					setArmorInventoryStack(player, null, armorInventory[i], i);
					return;
				}
			}
		}
	}
	
	/**
	 * Volatile function to return the quiver[]. Very vulnerable to pointer issues if modification to quiver[]
	 * is attempted using this method
	 * @return the quiver[] array
	 */
	public final ItemStack[] getQuiver()
	{
		return quiver;
	}
	
	/**
	 * Gets the size of the quiver
	 * @return the length of the quiver[] array
	 */
	public int getQuiverLength() 
	{
		return quiver.length;
	}
	
	/**
	 * Tries to place an ItemStack in the selected index of the quiver[]. Passing a null ItemStack will clear that slot
	 * of the quiver[].
	 * @param stack stack to place in quiver[]
	 * @param index where to place the stack in quiver[]
	 * @return success of the operation
	 */
	public boolean setQuiverStack(EntityPlayerBase player, ItemStack stack, int index)
	{
		player.changedInventorySlots.add("q " + index);
		if(stack == null)
		{
			quiver[index] = null;
		}
		else
		{
			quiver[index] = new ItemStack(stack);
		}
		return true;
	}
	
	/**
	 * Adjusts the stacksize of an Itemstack in the mainInventory[]. This will do nothing if that stack is null.
	 * @param index the index of the ItemStack in the mainInventory[]
	 * @param newStackSize the new stacksize of the ItemStack
	 */
	public void adjustMainInventoryStackSize(EntityPlayerBase player, int index, int newStackSize)
	{
		if(mainInventory[index] != null && mainInventory[index].getStackSize() != newStackSize)
		{
			//The change in stack size. A positive number is an increase, negative number a 
			//decrease overall.
			player.changedInventorySlots.add("i " + index);
			int differenceInStackSize = newStackSize - mainInventory[index].getStackSize();			
			inventoryTotals.put(mainInventory[index].getItemName(), 
					inventoryTotals.get(mainInventory[index].getItemName()) + differenceInStackSize);				
			mainInventory[index].setStackSize(newStackSize);
		}
	}
	
	/**
	 * Gets a reference safe ItemStack from the quiver[] 
	 * @param index the slot of quiver[] to return.
	 * @return a pointer(reference) safe ItemStack at the specified index of quiver[] or null if nothing is there
	 */
	public final ItemStack getQuiverStack(int index)
	{
		return  (quiver[index] != null) ? new ItemStack(quiver[index]) : null;
	}
	
	
	
	
	/**
	 * Attempts to combine two ItemStack in the specified quiver slot
	 * @param stack stack to combine with the current one
	 * @param index where to combine stacks in quiver[]
	 * @return success of operation
	 */
	public boolean combineItemStacksInQuiverSlot(EntityPlayerBase player, ItemStack stack, int index)
	{
		player.onInventoryChange();
		if(quiver[index] == null || quiver[index].getItemID() != stack.getItemID())
		{	
			return false;
		}
		player.changedInventorySlots.add("q " + index);
		quiver[index].addToStack(stack.getStackSize());
		return true;
	}
	

	/**
	 * Adjusts the stacksize of an Itemstack in the quiver[]. This will do nothing if that stack is null.
	 * @param index the index of the ItemStack in the quiver[]
	 * @param newStackSize the new stacksize of the ItemStack
	 */
	public void adjustQuiverStackSize(EntityPlayerBase player, int index, int newStackSize)
	{
		if(quiver[index] != null && quiver[index].getStackSize() != newStackSize)
		{
			player.changedInventorySlots.add("q " + index);
			quiver[index].setStackSize(newStackSize);
		}
	}
	
	/**
	 * Removes items from the quiver[] at the specified index
	 * @param howMany how many items to remove
	 * @param index slot in quiver[]
	 * @return whether the operation succeeded or not (fails due to not enough items)
	 */
	public boolean removeItemsFromQuiverStack(EntityPlayerBase player, int howMany, int index)
	{
		if(howMany < quiver[index].getStackSize())
		{
			player.changedInventorySlots.add("q " + index);
			quiver[index].removeFromStack(howMany);
			return true;
		}
		else if(howMany == quiver[index].getStackSize())
		{
			player.changedInventorySlots.add("q " + index);
			quiver[index].removeFromStack(howMany);
			quiver[index] = null;
			return true;
		}
		return false;
	}
	

	/**
	 * Removes items from trash[] at the specified index
	 * @param howMany how many items to remove
	 * @param index slot in trash[]
	 * @return whether the operation succeeded or not (fails due to not enough items)
	 */
	public boolean removeItemsFromTrashStack(EntityPlayerBase player, int howMany, int index)
	{
		if(howMany < trash[index].getStackSize())
		{
			player.changedInventorySlots.add("t " + index);
			trash[index].removeFromStack(howMany);
			return true;
		}
		else if(howMany == trash[index].getStackSize())
		{
			player.changedInventorySlots.add("t " + index);
			trash[index].removeFromStack(howMany);
			trash[index] = null;
			return true;
		}
		return false;
	}
	
}
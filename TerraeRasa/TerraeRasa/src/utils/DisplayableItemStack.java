package utils;

import items.Item;

import java.io.Serializable;

import passivebonuses.DisplayablePassiveBonus;
import spells.Spell;
import affix.DisplayableAffix;
import auras.DisplayableAura;
import blocks.Block;

/**
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
 * <br>
 * Version 1.1 adds GemSocket Support
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class DisplayableItemStack 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String itemName;
	private int stackSize;
	private int maxStackSize;
	private int itemID;
	private DisplayableGemSocket[] gemSockets;
	private String renderedName;
	private DisplayablePassiveBonus[] bonuses;
	private DisplayableAura[] auras;
	private String craftingID;		
	
	public DisplayableItemStack(ActionbarItem item) {
		itemID = item.id;
		maxStackSize = item.maxStackSize;
		stackSize = 1;
		itemName = item.name;
		gemSockets = new DisplayableGemSocket[item.getTotalSockets()];
		for (int i = 0; i < gemSockets.length; i++) {
			gemSockets[i] = new DisplayableGemSocket();
		}
		bonuses = new DisplayablePassiveBonus[0];
		auras = new DisplayableAura[0];
		renderedName = itemName;
		craftingID = ""+itemID;
	}

	public DisplayableItemStack(ActionbarItem item, int stackSize) {
		itemID = item.id;
		maxStackSize = item.maxStackSize;
		this.stackSize = stackSize;
		if (stackSize > maxStackSize) {
			stackSize = maxStackSize;
		}
		itemName = item.name;
		gemSockets = new DisplayableGemSocket[item.getTotalSockets()];
		for (int i = 0; i < gemSockets.length; i++) {
			gemSockets[i] = new DisplayableGemSocket();
		}
		bonuses = new DisplayablePassiveBonus[0];
		auras = new DisplayableAura[0];
		renderedName = itemName;
		craftingID = ""+itemID;
	}

	public DisplayableItemStack(int id, int stackSize) {
		itemID = id;
		this.stackSize = stackSize;

		if (itemID < ActionbarItem.itemIndex) {
			itemName = Block.blocksList[id].name;
			maxStackSize = Block.blocksList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Block.blocksList[id].getTotalSockets()];
		} else if (itemID >= ActionbarItem.itemIndex
				&& itemID < ActionbarItem.spellIndex) {
			itemName = Item.itemsList[id].name;
			maxStackSize = Item.itemsList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Item.itemsList[id].getTotalSockets()];
		} else {
			itemName = Spell.spellList[id].name;
			maxStackSize = Spell.spellList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Spell.spellList[id].getTotalSockets()];
		}
		for (int i = 0; i < gemSockets.length; i++) {
			gemSockets[i] = new DisplayableGemSocket();
		}
		bonuses = new DisplayablePassiveBonus[0];
		auras = new DisplayableAura[0];
		renderedName = itemName;
		craftingID = ""+itemID;
	}

	public DisplayableItemStack(int id) {
		itemID = id;
		stackSize = 1;

		if (itemID < ActionbarItem.itemIndex) {
			itemName = Block.blocksList[id].name;
			maxStackSize = Block.blocksList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Block.blocksList[id].getTotalSockets()];
		} else if (itemID >= ActionbarItem.itemIndex
				&& itemID < ActionbarItem.spellIndex) {
			itemName = Item.itemsList[id].name;
			maxStackSize = Item.itemsList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Item.itemsList[id].getTotalSockets()];
		} else {
			itemName = Spell.spellList[id].name;
			maxStackSize = Spell.spellList[id].maxStackSize;
			gemSockets = new DisplayableGemSocket[Spell.spellList[id].getTotalSockets()];
		}
		for (int i = 0; i < gemSockets.length; i++) {
			gemSockets[i] = new DisplayableGemSocket();
		}
		bonuses = new DisplayablePassiveBonus[0];
		auras = new DisplayableAura[0];
		renderedName = itemName;
		craftingID = ""+itemID;
	}

	public DisplayableItemStack(DisplayableItemStack stack) {
		if (stack == null) {
			System.err.println("DO NOT COPY A NULL ITEMSTACK.");
			throw new RuntimeException(
					"Dont do this. It's physically painful to copy a null ItemStack.");
		}
		this.itemName = stack.getItemName();
		this.stackSize = stack.getStackSize();
		this.itemID = stack.getItemID();
		this.maxStackSize = stack.getMaxStackSize();
		this.gemSockets = stack.getGemSockets();
		this.bonuses = stack.getBonuses();
		this.auras = stack.getAuras();
		this.renderedName = stack.getRenderedName();
		this.craftingID = ""+itemID;
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
	public DisplayableItemStack setStackSize(int i)
	{
		stackSize = i;
		return this;
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

	/**
	 * Gets all the GemSockets for this ItemStack
	 * @return all the GemSockets for this ItemStack
	 */
	public DisplayableGemSocket[] getGemSockets()
	{
		return gemSockets;
	}
	
	/**
	 * Gets the GemSocket for this ItemStack at the given index
	 * @param index the index of the GemSocket
	 * @return the GemSocket at the specified index
	 */
	public DisplayableGemSocket getSocket(int index)
	{
		return gemSockets[index];
	}
	
	/**
	 * Indicates if the ItemStack has any gem sockets
	 * @return true if the ItemStack has 1 or more gem sockets; otherwise false
	 */
	public boolean hasSockets()
	{
		return gemSockets.length > 0;
	}
	
	/**
	 * Gives this ItemStack some specific PassiveBonuses.
	 * @param bonuses the bonuses to give this ItemStack
	 */
	public void PassiveBonuses(DisplayablePassiveBonus[] bonuses)
	{
		this.bonuses = bonuses;
	}
	
	public DisplayableItemStack setAffix(DisplayableAffix affix){
		System.err.println("Affix-Set failure with affix=" + affix.toString());
		return this;
	}
	
	/**
	 * Gives this Itemstack some specific auras.
	 * @param auras the auras to give this ItemStack
	 */
	public void setAuras(DisplayableAura[] auras)
	{
		this.auras = auras;
	}

	/**
	 * Sets the rendered name of this ItemStack, which is what will be displayed in the tooltip
	 * @param name the name to be displayed in the tooltip
	 */
	public void setRenderedName(String name)
	{
		this.renderedName = name;
	}
	
	/**
	 * Gets any special PassiveBonuses assigned to this specific ItemStack
	 * @return any PassiveBonuses assigned to this ItemStack
	 */
	public DisplayablePassiveBonus[] getBonuses()
	{
		return bonuses;
	}
	
	/**
	 * Gets any special Auras for this specific ItemStack
	 * @return any Auras assigned to this specific ItemStack
	 */
	public DisplayableAura[] getAuras()
	{
		return auras;
	}
	
	/**
	 * Gets the rendered name for this ItemStack, which will be used in the UI
	 * @return the renderedName of this ItemStack
	 */
	public String getRenderedName()
	{
		return renderedName;
	}

	/**
	 * Converts all the passive bonuses and then auras into a string 
	 * array. Useful for tooltips and visualization.
	 * @return this ItemStack's PassiveBonuses, and Auras as a String[]
	 */
	public String[] getStringBonuses()
	{
		String[] allBonuses = new String[bonuses.length + auras.length];
		int i = 0;
		for(i = 0; i < bonuses.length; i++)
		{
			if(bonuses[i] != null)
			{
//				System.out.println("null bonus @" + renderedName);
				allBonuses[i] = bonuses[i].toString();
			}
		}
		for(i = 0; i < auras.length; i++)
		{
			allBonuses[bonuses.length + i] = auras[i].toString();
		}
		return allBonuses;
	}
	
	/**
	 * Yields a string in the form of <b> "ItemStack of " + QUANTITY + " X " + ITEM_NAME </b>
	 */
	public String toString()
	{
		return "ItemStack of " + stackSize + " X " + itemName;
	}
	
	/**
	 * Yields a fairly unique ID in the form of: <b> ITEM_ID + " " + AFFIX_CRAFTING_ID </b>. The AFFIX_CRAFTING_ID is something
	 * fairly unique to each affix.
	 * @return a String representing this Itemstack's crafting ID
	 */
	public String getCraftingID()
	{
		return craftingID;
	}
}

package blocks;

import utils.ItemStack;

/**
 * BlockChest is an extension of Block that allows items to be stored in it. Items can be placed in 
 * the chest with {@link #placeItemStack(ItemStack, int)} and removed using {@link #takeItemStack(int)}. 
 * Most methods which request an ItemStack make a deep copy to prevent reference errors, but this is not the 
 * case with {@link #getMainInventory()}, therefore caution should be used to prevent corrupting the chest's 
 * contents. 
 * <br><br>
 * A new Chest can be created using the only constructor, {@link #BlockChest(int, int)}.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class BlockChest extends Block
{
	/** The contents of the chest. */
	private ItemStack[] mainInventory;
	/** The size of the mainInventory[], preferably a multiple of 5. */
	private int inventorySize;
	
	/**
	 * Creates a chest of the specified size and ID. Block(int) is called, and handed the ID of the 
	 * block to create it and add it to Block.blockList[]. An inventorySize that is not divisible by 5 will 
	 * create unexpected results in the UI.
	 * @param i the ID of the block (handed to the Block constructor)
	 * @param inventorySize the size of the chest's storage (inventory)
	 */
	protected BlockChest(int i, int inventorySize)
	{
		super(i);
		mainInventory = new ItemStack[inventorySize];
		this.inventorySize = inventorySize;
	}	
	
	/**
	 * Implements a further version of Block(Block). In addition to Block(Block) a deep copy of all variables
	 * is performed and a new mainInventory[] is created.
	 * @param block the block to create a deep copy of
	 */
	public BlockChest(BlockChest block)
	{
		super(block);
		this.mainInventory = new ItemStack[block.inventorySize];
		this.inventorySize = block.inventorySize;
	}
	
	/**
	 * Attempts to place the itemstack in the chest.
	 * @param stack
	 * @param index
	 * @return
	 */
	public ItemStack placeItemStack(ItemStack stack, int index)
	{
		if(mainInventory[index] == null)
		{
			mainInventory[index] = stack;
			return null;
		}
		else if(stack.getItemID() == mainInventory[index].getItemID())
		{
			if(stack.getStackSize() +  mainInventory[index].getStackSize() <= mainInventory[index].getMaxStackSize())
			{
				mainInventory[index].addToStack(stack.getStackSize());
				return null;
			}
			else 
			{
				stack.removeFromStack(mainInventory[index].getMaxStackSize() - mainInventory[index].getStackSize());
				mainInventory[index].addToStack(mainInventory[index].getMaxStackSize() - mainInventory[index].getStackSize());
			}
		}
		return stack;
	}
	
	/**
	 * Gets the ItemStack at the specified index and then removes that ItemStack from the chest. This will return null if 
	 * there is no item at the given ItemStack. Additionally, the ItemStack returned will be a deep copy.
	 * @param index the ItemStack to remove from the chest
	 * @return the ItemStack at given index from the chest
	 */
	public ItemStack takeItemStack(int index)
	{
		ItemStack stack = (mainInventory[index] != null) ? new ItemStack(mainInventory[index]) : null;
		mainInventory[index] = null;
		return stack;
	}
	
	/**
	 * Gets whether or not the slot at the specified index in empty. An empty slot is one that is not filled by any 
	 * item and is therefore null. Throws an out of bounds exception if the given index is not within the 
	 * Chest's mainInventory[] size.
	 * @param index the slot of the chest to check. This value should be in bounds.
	 * @return true if the specified slot is empty, otherwise false
	 */
	public boolean isStackEmpty(int index) 
	{
		return mainInventory[index] == null;
	}
	
	/**
	 * Returns a deep copy of the ItemStack at the given index. This will instead be null if there is no 
	 * ItemStack at the given index. An out of bounds exception is thrown if the given index is not within the 
	 * Chest's mainInventory[] size.
	 * @param index the slot of the chest to copy
	 * @return a deep copy of the ItemStack at the given index, or null if it's empty
	 */
	public ItemStack getItemStack(int index)
	{
		return (mainInventory[index] != null) ? new ItemStack(mainInventory[index]) : null;
	}
	
	/**
	 * Removes the ItemStack at the given index from the chest's inventory.
	 * @param index the slot of the chest to empty.
	 */
	public void removeItemStack(int index)
	{
		mainInventory[index] = null;
	}
		
	/**
	 * Returns a reference to the chest's mainInventory[].
	 * @return a reference to the chest's mainInventory[]
	 */
	public ItemStack[] getMainInventory()
	{
		return mainInventory;
	}
	
	/**
	 * Returns the size of the chest's inventory. This should be equal to the mainInventory's length, but is a separate value
	 * stored within the chest.
	 * @return
	 */
	public int getInventorySize()
	{
		return inventorySize;
	}
	
	/**
	 * Sets the mainInventory[] to the given ItemStacks
	 * @param stacks the new contains of the mainInventory[]
	 */
	public void setInventory(ItemStack[] stacks)
	{
		this.mainInventory = stacks;
	}
	
	/**
	 * Destroys the current mainInventory[], and recreates it with the given inventory size. <b> All Chest contents
	 * are destroyed so this should be used sparingly. </b>
	 * @param i the size of the new mainInventory[]
	 */
	public void setInventorySize(int newsize)
	{
		this.inventorySize = newsize;
		mainInventory = new ItemStack[inventorySize];
	}
}
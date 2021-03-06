package blocks;

import utils.ItemStack;

/**
 * BlockChestServer is an extension of Block that allows items to be stored in it. Items can be placed in 
 * the chest with {@link #placeItemStack(ItemStack, int)} and removed using {@link #takeItemStack(int)}. 
 * Most methods which request an ItemStack make a deep copy to prevent reference errors. 
 * <br><br>
 * A new BlockChestServer copy can be created using the only publicly exposed constructor, {@link #BlockChestServer(BlockChestServer)}.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class BlockChest extends Block
{
	/** The contents of the chest. */
	private ItemStack[] mainInventory;
	
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
	}	
	
	/**
	 * Implements a further version of Block(Block). In addition to Block(Block) a fresh mainInventory[] is created.
	 * @param block the block to create a deep copy of
	 */
	public BlockChest(BlockChest block)
	{
		super(block);
		this.mainInventory = new ItemStack[block.getInventorySize()];
	}
	
	/**
	 * Attempts to place the itemstack in the chest.
	 * @param stack the stack to place into a slot
	 * @param index the index of the slot in the mainInventory[] to place the new stack
	 * @return
	 */
	public ItemStack placeItemStack(ItemStack stack, int index)
	{
		if(mainInventory[index] == null)
		{
			mainInventory[index] = new ItemStack(stack);
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
	 * Returns a deep copy of the chest's mainInventory[].
	 * @return a deep copy to the chest's mainInventory[]
	 */
	public ItemStack[] getMainInventory()
	{
		ItemStack[] stacks = new ItemStack[mainInventory.length];
		for(int i = 0; i < stacks.length; i++)
		{
			if(this.mainInventory[i] != null)
			{
				stacks[i] = new ItemStack(this.mainInventory[i]);
			}
		}		
		return stacks;
	}
	
	/**
	 * Returns the size (length) of the chest's inventory. 
	 * @return the length of the chest's inventory
	 */
	public int getInventorySize()
	{
		return mainInventory.length;
	}
	
	/**
	 * Sets the mainInventory[] to the given ItemStacks. This is bounds safe, and reference safe. Excess items will simply be ignored.
	 * @param stacks the new contains of the mainInventory[]
	 */
	public void setInventory(ItemStack[] stacks)
	{
		for(int i = 0; i < mainInventory.length; i++)
		{
			if(i < stacks.length)
			{
				if(stacks[i] != null)
				{
					mainInventory[i] = new ItemStack(stacks[i]);
				}
			}
		}
	}
	
	/**
	 * Destroys the current mainInventory[], and recreates it with the given inventory size. <b> All Chest contents
	 * are destroyed so this should be used sparingly. </b>
	 * @param i the size of the new mainInventory[]
	 */
	public void setInventorySize(int newsize)
	{
		mainInventory = new ItemStack[newsize];
	}
	
	/**
	 * Removes a specified number of items from a stack in the inventory.
	 * @param howMany the number of items to remove
	 * @param index the index in the mainInvenotry by which to remove items.
	 * @return
	 */
	public boolean removeItemsFromInventoryStack(int howMany, int index)
	{
		if(mainInventory[index] == null)
		{
			return false;
		}
		
		if(howMany < mainInventory[index].getStackSize())
		{
			mainInventory[index].removeFromStack(howMany);
			return true;
		}
		else if(howMany == mainInventory[index].getStackSize())
		{
			mainInventory[index].removeFromStack(howMany);
			mainInventory[index] = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Creates a full BlockChestServer based on a given MinimalBlock
	 * @param block the MinimalBlock to fully expand to a BlockChestServer
	 * @return a BlockChestServer, expanded from the MinimalBlock
	 */
	public BlockChest mergeOnto(MinimalBlock block)
	{
		this.id = block.id;
		this.hasMetaData = block.hasMetaData;
		this.setInventory(block.mainInventory);
 		return this;
	}
	
	public BlockChest mergeOnto(ClientMinimalBlock block)
	{
		this.id = block.id;
		this.hasMetaData = block.hasMetaData;
		this.setInventory(block.mainInventory);
		return this;
	}
}
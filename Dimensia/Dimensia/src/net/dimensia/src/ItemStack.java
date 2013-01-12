package net.dimensia.src;
import java.io.Serializable;


public class ItemStack implements Serializable
{
	private static final long serialVersionUID = 1L;

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
	
	public void addToStack(int amount)
	{
		int i = new Integer(amount);
		stackSize += i;
		if(stackSize > maxStackSize) 
		{
			stackSize = maxStackSize;
		}
	}
	
	public void removeFromStack(int amount)
	{
		stackSize -= amount;
	}
	
	public void setStackSize(int i)
	{
		stackSize = i;
	}
	
	public final String getItemName()
	{
		return new String(itemName);
	}
	
	public final int getMaxStackSize()
	{
		return new Integer(maxStackSize);
	}
	
	public final int getStackSize()
	{
		return new Integer(stackSize);
	}
	
	public final int getItemID()
	{
		return new Integer(itemID);
	}
	
	private String itemName;
	private int stackSize;
	private int maxStackSize;
	private int itemID;
}

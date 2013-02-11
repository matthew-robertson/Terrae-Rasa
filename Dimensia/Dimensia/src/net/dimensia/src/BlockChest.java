package net.dimensia.src;

/**
 * Currently assumes attachment left first, then if that fails attempts to attach right
 * @author Alec
 *
 */

public class BlockChest extends Block
{
	private static final long serialVersionUID = 1L;
	private boolean isAttachedRight;
	private boolean isAttachedLeft;
	private ItemStack[] mainInventory;
	
	protected BlockChest(int i)
	{
		super(i);
		mainInventory = new ItemStack[20];
	}	
	
	public BlockChest(BlockChest block)
	{
		//super();
		this.droppedItem = block.droppedItem;
		this.maximumDropAmount = block.maximumDropAmount;
		this.minimumDropAmount = block.minimumDropAmount;
		this.hasMetaData = block.hasMetaData;
		this.blockWidth = block.blockWidth; 
		this.blockHeight = block.blockHeight; 
		this.maxStackSize = block.maxStackSize;
		this.textureWidth = block.textureWidth; 
		this.textureHeight = block.textureHeight; 
		this.iconIndex = block.iconIndex;
		this.gradeOfToolRequired = block.gradeOfToolRequired;
		this.blockType = block.blockType;
		this.blockTier = block.blockTier;
		this.material = block.material;
		this.breakable = block.breakable;
		this.hardness = block.hardness;
		this.blockID = block.blockID;
		this.blockName = block.blockName;
		this.passable = block.passable;
		this.isOveridable = block.isOveridable;
		this.isSolid = block.isSolid;
		this.mainInventory = new ItemStack[20];
		this.isAttachedLeft = false;
		this.isAttachedRight = false;
	}
	
	/**
	 * @deprecated
	 * --restructure
	 */
	public BlockChest clone()
	{
		BlockChest chest = (BlockChest) super.clone();
		if(chest == Block.chest)
		{
			throw new RuntimeException("Cloning has failed. This is considered critical. If you aren't Alec (hopefully you are), tell him");
		}		
		chest = new BlockChest(chest);		
		return chest;
	}
	
	/*
	public ItemStack addItemStack(ItemStack stack, int index)
	{
		if(mainInventory[index] == null)
		{
			mainInventory[index] = stack;
			return null;
		}
		if(mainInventory[index].getItemID() == stack.getItemID())
		{
			/**
			 * 
			 * This is very broken after ItemStack conversion
			 * 
			 *
			 //
			if(mainInventory[index].getMaxStackSize() - mainInventory[index].getStackSize() > stack.getStackSize())
			{
			//	int i = mainInventory[index].getMaxStackSize() - mainInventory[index].getStackSize();
			//	stack.stackSize -= i;
			//	mainInventory[index].stackSize = mainInventory[index].getMaxStackSize();
				return stack;
			}
			else
			{	
			//	mainInventory[index].stackSize += stack.stackSize;
				return null;
			}			
		}
		
		return stack;
	}	
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
	
	public ItemStack takeItemStack(int index)
	{
		ItemStack stack = (mainInventory[index] != null) ? new ItemStack(mainInventory[index]) : null;
		mainInventory[index] = null;
		return stack;
	}
	
	public boolean isStackEmpty(int index)
	{
		return mainInventory[index] == null;
	}
	
	public ItemStack getItemStack(int index)
	{
		return (mainInventory[index] != null) ? new ItemStack(mainInventory[index]) : null;
	}
	
	public void attachRight()
	{
		isAttachedRight = true;
	}
	
	public void attachLeft()
	{
		isAttachedLeft = true;
	}
	
	public boolean isAttachedLeft()
	{
		return isAttachedLeft;
	}
	
	public void removeItemStack(int index)
	{
		mainInventory[index] = null;
	}
	
	public boolean isAttachedRight()
	{
		return isAttachedRight;
	}
	
	public void removeAttachment()
	{
		isAttachedLeft = false;
		isAttachedRight = false;
	}
	
	public ItemStack[] getMainInventory()
	{
		return mainInventory;
	}
	
	public boolean canAttach()
	{
		return !(isAttachedLeft || isAttachedRight);
	}
	
	public boolean isAttached()
	{
		return (isAttachedRight || isAttachedLeft);
	}
}
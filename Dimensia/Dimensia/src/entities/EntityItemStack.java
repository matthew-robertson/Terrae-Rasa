package entities;

import utils.ItemStack;
import world.World;
import items.Item;
import items.ItemArmor;
import items.ItemTool;
import blocks.Block;

/**
 * <code>EntityItemStack extends EntityLiving</code> and <code>implements Serializable</code>
 * <br>
 * <code>EntityItemStack</code> implements a basic ItemStack entity that the world is able
 * to maintain, display, or possibly destroy. By default the fall speed is 1.8f, and the EntityItemStack
 * has only 1 (max)Health. EntityItemStacks can, in theory, die due to any damage at all, but in 
 * practice, this may never be implemented (31/5/12 time of writing this).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class EntityItemStack extends EntityLiving
{
	private static final long serialVersionUID = 1L;
	
	public EntityItemStack(float x, float y, ItemStack stack)
	{
		super();
		if(stack.getItemID() < 2048) //Blocks are 6x6 render size
		{
			width = 6;
			height = 6;
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
		fallSpeed = 1.8f; 
		maxHealth = 1;
		health = 1;
		blockWidth = (float)(width) / 6;
		blockHeight = (float)(height) / 6;
		ticksBeforePickup = 20;
		this.stack = stack;
	}
	
	/**
	 * Overrides EntityLiving's applyGravity because itemstacks cant jump. Moves the itemstack down if applicable.
	 */
	public void applyGravity(World world)
	{
		if(!isOnGround(world)) //if the entity is in the air, make them fall
		{
			moveEntityDown(world);			
		}		
	}
	
	/**
	 * Overrides EntityLiving's moveEntityDown because EntityItemStack shouldnt typically behave as though it's alive.
	 */
	public void moveEntityDown(World world) 	 
	{		
		int offset = (int) Math.ceil((y % 6 == 0) ? blockHeight : (blockHeight + 1)); 
		
		if((int)((y + fallSpeed) / 6) + offset + 1 > world.getHeight() - 1) //bounds check
		{
			y = world.getHeight() * 6 - height + 6;
			return;
		}
		
		Block[] blocks = new Block[(int) Math.ceil(blockWidth + ((x % 6 == 0) ? 0 : 1))]; //blocks to check
		boolean flag = true;
		
		for(int i = 0; i < blocks.length; i++) //get blocks to check
		{
			blocks[i] = world.getBlock((x / 6) + i, ((y + fallSpeed) / 6) + offset); 
		}		

		for(int i = 0; i < blocks.length; i++)
		{
			if(!blocks[i].isPassable()) //hittest has failed
			{
				flag = false;
				break;
			}
			else
			{
				flag = true;
			}
		}
		
		if(flag && y + fallSpeed < world.getHeight() * 6) //Normal Gravity
		{
			y += fallSpeed;
		}
		else if(y % 6 != 0) //Near ground partial gravity 
		{
			y += (6 - (y % 6));
		}		
	}
	
	public int ticksBeforePickup;
	public ItemStack stack;
}

package net.dimensia.src;
import org.lwjgl.input.Mouse;

public class MouseInput 
{	
	public static void mouse(World world, EntityPlayer player)
	{
		if(player.isStunned)
		{
			return;
		}
		
		int active = player.selectedSlot;
		int mouseBX = ((Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
		int mouseBY = ((Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);
		
		if(Mouse.isButtonDown(0) && world.player.inventory.getMainInventoryStack(active) != null) //Left Mouse Down && Actionbar slot isnt empty
		{			
			if(player.isInventoryOpen)
			{
			}
			else
			{
				if(!player.hasSwungTool) //If the player isn't swinging a tool, start swinging
				{
					if(player.isFacingRight) 
					{
						player.isSwingingRight = true;
					}
					else
					{
						player.isSwingingRight = false;
					}
					player.hasSwungTool = true;
				}	
				
				//Attempt to launch a projectile
				player.launchProjectile(world, (float)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()), (float)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()), Item.itemsList[world.player.inventory.getMainInventoryStack(active).getItemID()]);
				
				//Try to mine a block
				player.breakBlock(world, mouseBX, mouseBY, Item.itemsList[world.player.inventory.getMainInventoryStack(active).getItemID()]); 
			}
		}
		else if (Mouse.isButtonDown(1)) //Right Mouse down
		{			
			if(world.getBlock(mouseBX, mouseBY) instanceof BlockChest && world.player.isInventoryOpen) //If the block is a chest
			{
				world.player.setViewedChest(mouseBX, mouseBY);
			}
			else
			{
				if((world.player.viewedChestX != mouseBX || world.player.viewedChestY != mouseBY) && world.getBlock(mouseBX, mouseBY) instanceof BlockChest && world.player.isInventoryOpen)
				{
				}
				else
				{
					world.player.isViewingChest = false;
				}
			}
			
			if(world.player.inventory.getMainInventoryStack(active) != null)
			{
				if (!player.isInventoryOpen && world.player.inventory.getMainInventoryStack(active).getItemID() <= 2048) //If player is holding a block
				{
					mouseBX = (int) ((float)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
					mouseBY = (int) ((float)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);				
					
					double d = Math.sqrt(( 
						(Math.pow(((MathHelper.getCorrectMouseXPosition() + Render.getCameraX()) - (player.x + ((world.player.isFacingRight) ? 9 : 3))), 2)) +
						(Math.pow(((MathHelper.getCorrectMouseYPosition() + Render.getCameraY()) - (player.y + 9)), 2))  
					)); //Find the distance from the appropriate hand
					
					if(d <= player.getMaximumBlockPlaceDistance()) //if the click was close enough to place a block, try to place one
					{
						world.placeBlock(mouseBX, mouseBY, Block.blocksList[world.player.inventory.getMainInventoryStack(active).getItemID()].clone());
					}
				}
				else if(world.player.inventory.getMainInventoryStack(active) != null && !world.player.isInventoryOpen) //otherwise, if it's an item
				{
					Item.itemsList[world.player.inventory.getMainInventoryStack(active).getItemID()].onRightClick(world, world.player);
				}
			}
		}		
		
		//Check for mouse wheel movement if the inventory isnt open
		if(!world.player.isInventoryOpen)
		{
			int wheelMovement = Mouse.getDWheel();
			
	        if (wheelMovement > 0) //If mouse scrolled up
	        {
	        	world.player.selectedSlot--; //Decrease selected slot (actionbar)
	        }
	        else if(wheelMovement < 0) //If mouse scrolled down
	        {
	        	world.player.selectedSlot++; //Increase selected slot (actionbar)
	        }
	        
	        //Bounds checking
	        if(world.player.selectedSlot < 0)
	        {
	        	world.player.selectedSlot = 11;
	        }
	        if(world.player.selectedSlot > 11)
	        {
	        	world.player.selectedSlot = 0;
	        }
		}
	}
	private static int correctedDirection;
}

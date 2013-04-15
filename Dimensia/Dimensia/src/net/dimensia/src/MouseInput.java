package net.dimensia.src;

import org.lwjgl.input.Mouse;

public class MouseInput 
{	
	private static boolean mouseLock = false;
	
	public static void mouse(World world, EntityLivingPlayer player)
	{
		if(player.isStunned)
		{
			return;
		}
		
		int active = player.selectedSlot;
		int mouseBX = ((Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
		int mouseBY = ((Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);
		
		if(Mouse.isButtonDown(0) && player.inventory.getMainInventoryStack(active) != null) //Left Mouse Down && Actionbar slot isnt empty
		{			
			if(player.isInventoryOpen)
			{
			}
			else
			{
				if(!player.isSwingingTool()) //If the player isn't swinging a tool, start swinging
				{
					player.startSwingingTool(player.isFacingRight);
				}	
				
				//Attempt to launch a projectile
				int selectedItemID = player.inventory.getMainInventoryStack(active).getItemID();
				if(selectedItemID >= ActionbarItem.itemIndex && selectedItemID < ActionbarItem.spellIndex)
				{
					Item item = Item.itemsList[selectedItemID];
					if (item instanceof ItemMagic){
						ItemMagic spell = (ItemMagic) item;
						player.launchProjectileMagic(world, 
								(float)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()), 
								(float)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()), 
								spell);
					}
					else if (item instanceof ItemRanged){
						ItemRanged weapon = (ItemRanged) item;
						player.launchProjectileWeapon(world, 
								(float)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()), 
								(float)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()), 
								weapon);
					}
					//Try to mine a block
					player.breakBlock(world, mouseBX, mouseBY, Item.itemsList[player.inventory.getMainInventoryStack(active).getItemID()]); 
				}
			}
		}
		else if (Mouse.isButtonDown(1)) //Right Mouse down
		{			
			if(world.getBlock(mouseBX, mouseBY) instanceof BlockChest && player.isInventoryOpen) //If the block is a chest
			{
				player.setViewedChest(mouseBX, mouseBY);
			}
			else
			{
				if((player.viewedChestX != mouseBX || 
						player.viewedChestY != mouseBY) && 
						world.getBlock(mouseBX, mouseBY) instanceof BlockChest && player.isInventoryOpen)
				{
				}
				else
				{
					player.isViewingChest = false;
				}
			}
			
			if(player.inventory.getMainInventoryStack(active) != null)
			{
				//If player is holding a block
				if (!player.isInventoryOpen && player.inventory.getMainInventoryStack(active).getItemID() < ActionbarItem.itemIndex) 
				{
					mouseBX = (int) ((float)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
					mouseBY = (int) ((float)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);				
					
					double d = Math.sqrt(( 
						(Math.pow(((MathHelper.getCorrectMouseXPosition() + Render.getCameraX()) - (player.x + ((player.isFacingRight) ? 9 : 3))), 2)) +
						(Math.pow(((MathHelper.getCorrectMouseYPosition() + Render.getCameraY()) - (player.y + 9)), 2))  
					)); //Find the distance from the appropriate hand
					
					if(d <= player.getMaximumBlockPlaceDistance()) //if the click was close enough to place a block, try to place one
					{
						world.placeBlock(player, mouseBX, mouseBY, Block.blocksList[player.inventory.getMainInventoryStack(active).getItemID()].clone());
					}
				}
				else if(player.inventory.getMainInventoryStack(active) != null && !player.isInventoryOpen && 
						player.inventory.getMainInventoryStack(active).getItemID() >= ActionbarItem.itemIndex && 
						player.inventory.getMainInventoryStack(active).getItemID() < ActionbarItem.spellIndex) //otherwise, if it's an item
				{
					if(!mouseLock)
					{
						Item.itemsList[player.inventory.getMainInventoryStack(active).getItemID()].onRightClick(world, player);
						mouseLock = true;
					}
				}
				else if(player.inventory.getMainInventoryStack(active) != null && !player.isInventoryOpen) //Spell
				{
					if(!mouseLock)
					{
						Spell.spellList[player.inventory.getMainInventoryStack(active).getItemID()].onRightClick(world, player);
						mouseLock = true;
					}
				}
			}
		}		
		else
		{
			mouseLock = false;
		}
		
		//Check for mouse wheel movement if the inventory isnt open
		if(!player.isInventoryOpen)
		{
			int wheelMovement = Mouse.getDWheel();
			
	        if (wheelMovement > 0) //If mouse scrolled up
	        {
	        	player.selectedSlot--; //Decrease selected slot (actionbar)
	        	player.clearSwing();
	        }
	        else if(wheelMovement < 0) //If mouse scrolled down
	        {
	        	player.selectedSlot++; //Increase selected slot (actionbar)
	        	player.clearSwing();
	        }
	        
	        //Bounds checking
	        if(player.selectedSlot < 0)
	        {
	        	player.selectedSlot = 11;
	        }
	        if(player.selectedSlot > 11)
	        {
	        	player.selectedSlot = 0;
	        }
		}
	}
}

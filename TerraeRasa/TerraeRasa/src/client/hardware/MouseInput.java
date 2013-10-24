package client.hardware;

import items.Item;
import items.ItemTool;

import java.util.Vector;

import math.MathHelper;

import org.lwjgl.input.Mouse;

import utils.ActionbarItem;
import world.World;
import blocks.Block;
import blocks.BlockBackWall;
import blocks.BlockChest;
import client.audio.SoundEngine;
import client.entities.EntityPlayer;
import client.render.Render;
import enums.EnumHardwareInput;

public class MouseInput 
{	
	private static boolean mouseLock = false;
	
	public static void mouse(World world, EntityPlayer player, Vector<String> clientCommands, Vector<EnumHardwareInput> hardwareInput)
	{
		try {
			int active = player.selectedSlot;
			int mouseBX = ((Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
			int mouseBY = ((Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);
				
			if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && player.getIsMining()){
				String command = "/mine stop " + player.entityID;
				clientCommands.add(command);
			}
			if(Mouse.isButtonDown(0) && player.inventory.getMainInventoryStack(active) != null) //Left Mouse Down && Actionbar slot isnt empty
			{			
				if(!player.isInventoryOpen)
				{
					int selectedItemID = player.inventory.getMainInventoryStack(active).getItemID();
					if(selectedItemID < ActionbarItem.spellIndex)
					{
						Item item = Item.itemsList[selectedItemID];
						//Try to mine a block
						String command = "/mine frontcontinue " + player.entityID + " " + mouseBX + " " + mouseBY + " " + active;
						clientCommands.add(command);
						
						if(!player.isSwingingTool() && item instanceof ItemTool) //If the player isn't swinging a tool, start swinging
						{
							ItemTool tool = (ItemTool) item;
							command = "/player " + player.entityID + " startswing " + player.selectedSlot;
							clientCommands.add(command);
							if (player.getIsMining()){
								SoundEngine.playSoundEffect(tool.hitSound);
							}				
						}
						//Attempt to launch a projectile				
						if(selectedItemID >= ActionbarItem.itemIndex)
						{
							double xPos = Render.getCameraX() + MathHelper.getCorrectMouseXPosition(); 
							double yPos = Render.getCameraY() + MathHelper.getCorrectMouseYPosition();
							command = "/projectile launch " + player.entityID + " " + active + " " + xPos + " " + yPos;
							clientCommands.add(command);							
						}
					}
				}
			}
			else if (Mouse.isButtonDown(1)) //Right Mouse down
			{			
				//If the block is a chest
				if(world.getAssociatedBlock(mouseBX, mouseBY) instanceof BlockChest && player.isInventoryOpen) {
					player.setViewedChest(mouseBX, mouseBY);
				}
				else {
					if(!((player.viewedChestX != mouseBX || player.viewedChestY != mouseBY) && 
						world.getAssociatedBlock(mouseBX, mouseBY) instanceof BlockChest && 
						player.isInventoryOpen)) {
						player.isViewingChest = false;
					}
				}
				
				if(player.inventory.getMainInventoryStack(active) != null)
				{
					int selectedItemID = player.inventory.getMainInventoryStack(active).getItemID();	
					
					//If player is holding a block
					if (!player.isInventoryOpen && selectedItemID < ActionbarItem.itemIndex) 
					{
						mouseBX = (int) ((double)(Render.getCameraX() + MathHelper.getCorrectMouseXPosition()) / 6);
						mouseBY = (int) ((double)(Render.getCameraY() + MathHelper.getCorrectMouseYPosition()) / 6);				
						
						//Find the distance from the appropriate hand
						double d = Math.sqrt(( 
							(Math.pow(((MathHelper.getCorrectMouseXPosition() + Render.getCameraX()) - (player.x + ((player.isFacingRight) ? 9 : 3))), 2)) +
							(Math.pow(((MathHelper.getCorrectMouseYPosition() + Render.getCameraY()) - (player.y + 9)), 2))  
						)); 
						//if the click was close enough to place a block, try to place one
						if(d <= player.getMaximumBlockPlaceDistance()) {
							if (Block.blocksList[selectedItemID] instanceof BlockBackWall) {
								String command = "/placebackblock " + mouseBX + " " + mouseBY + " " + player.entityID + " " + player.inventory.getMainInventoryStack(active).getItemID() + " " + active;
								clientCommands.add(command);
							}
							else {
								String command = "/placefrontblock " + mouseBX + " " + mouseBY + " " + player.entityID + " " + player.inventory.getMainInventoryStack(active).getItemID() + " " + active;
								clientCommands.add(command);
							}
						}
					}
					else if(player.inventory.getMainInventoryStack(active) != null && 
							!player.isInventoryOpen && 
							player.inventory.getMainInventoryStack(active).getItemID() >= ActionbarItem.itemIndex && 
							player.inventory.getMainInventoryStack(active).getItemID() < ActionbarItem.spellIndex) //otherwise, if it's an item
					{
						if(!mouseLock)
						{
							clientCommands.add("/player " + player.entityID + " use " + active);
							//Item.itemsList[player.inventory.getMainInventoryStack(active).getItemID()].onRightClick(world, player);
							mouseLock = true;
						}
					}
					else if(player.inventory.getMainInventoryStack(active) != null && !player.isInventoryOpen) //Spell
					{
						//TODO spells disabled? (probably not, but maybe)
//						if(!mouseLock) {
//							Spell.spellList[player.inventory.getMainInventoryStack(active).getItemID()].onRightClick(world, player);
//							mouseLock = true;
//						}
					}
					if (player.inventory.getMainInventoryStack(active) != null && selectedItemID < ActionbarItem.spellIndex) {
						Item item = Item.itemsList[selectedItemID];
						
						//Mine the backwall
						String command = "/mine backcontinue " + player.entityID + " " + mouseBX + " " + mouseBY + " " + active;
						clientCommands.add(command);
						
						if(!player.isSwingingTool() && item instanceof ItemTool) //If the player isn't swinging a tool, start swinging
						{
							ItemTool tool = (ItemTool) item;
							
							command = "/player " + player.entityID + " startswing " + player.selectedSlot;
							clientCommands.add(command);
							
						//	player.startSwingingTool(player.isFacingRight);
							if (player.getIsMining()){
								SoundEngine.playSoundEffect(tool.hitSound);
							}				
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
		        	clientCommands.add("/player " + player.entityID + " setactionbarslot " + ((player.selectedSlot > 0) ? player.selectedSlot : 0));
		        	player.clearSwing();
		        	clientCommands.add("/player " + player.entityID + " cancelswing");
		        }
		        else if(wheelMovement < 0) //If mouse scrolled down
		        {
		        	player.selectedSlot++; //Increase selected slot (actionbar)
		        	clientCommands.add("/player " + player.entityID + " setactionbarslot " + ((player.selectedSlot > 0) ? player.selectedSlot : 0));
		        	player.clearSwing();
		        	clientCommands.add("/player " + player.entityID + " cancelswing");
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
		} catch (NullPointerException e) {
			e.printStackTrace();
		}		
			
	}
}

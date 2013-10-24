package client.ui;

import items.Item;
import items.ItemGem;
import math.MathHelper;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import transmission.ClientUpdate;
import utils.ActionbarItem;
import utils.ItemStack;
import utils.MetaDataHelper;
import blocks.Block;
import blocks.BlockChest;
import blocks.ClientMinimalBlock;
import client.entities.EntityPlayer;
import client.hardware.Keys;
import client.world.WorldClientEarth;

/**
 * Refactor not entirely done
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class UIMouse extends UIBase
{
	/**
	 * Updates all mouse events on call. Including: chests, the mainInventory, the garbage, 
	 * and the recipe scroller 
	 */
	protected static void mouse(ClientUpdate update, WorldClientEarth world, EntityPlayer player)
	{
		mouseEventLeftClick(update, world, player);	
		mouseEventRightClick(update, world, player);
	}

	/**
	 * Does something appropriate if the user right clicks with the inventory open. The functionality of this is limited.
	 * @param world
	 * @param player
	 */
	protected static void mouseEventRightClick(ClientUpdate update, WorldClientEarth world, EntityPlayer player)
	{
		//If the mouse isnt down, there's really no reason to run the rest of this function
		if(!Mouse.isButtonDown(1)) 
		{
			return;
		}		
		
		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();		
		
		try 
		{
			//Band-aid fix for mouse clicks. Get the position before calling this though of it'll break on windows.
			Mouse.destroy(); 
			Mouse.create();
		} 	
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		if(Keys.lshiftDown)
		{
			mouseEventRightClickShift(world, player, x, y);
		}
		else
		{
			mouseEventRightClickNoModifier(update, world, player, x, y);
		}
	}
	
	protected static void mouseEventRightClickShift(WorldClientEarth world, EntityPlayer player, int mouseX, int mouseY)
	{
		boolean[] equipped = { false };
		int[] positions = { -1, -1 };
		ItemStack clickedItem = getClickedStack(world, player, mouseX, mouseY, equipped, positions);
		socketItemEquipped = equipped[0];
		if(clickedItem != null && clickedItem.hasSockets())
		{					
			isSocketWindowOpen = true;
			inventoryID = positions[0];
			inventoryIndex = positions[1];
			player.clearViewedChest();
		}
		else
		{
			UISocketMenu.closeSocketWindow();
		}
	}
	
	protected static void mouseEventRightClickNoModifier(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int x, int y)
	{
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				//The mouse doesn't have something picked up 
				//Pick up 1/2 stack of whatever is in the inventory. 
				if(player.inventory.getMainInventoryStack(i) != null && player.heldMouseItem == null)
				{
					pickUpHalfMouseItem(update, world, player, 1, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(update, world, player, 1, i);
				}
			}		
		}
		
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) //Quiver
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				//The mouse doesn't have something picked up 
				//Pick up 1/2 stack of whatever is in the inventory. 
				if(player.inventory.getQuiverStack(i) != null && player.heldMouseItem == null) 
				{
					pickUpHalfMouseItem(update, world, player, 3, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(update, world, player, 3, i);
				}
			}		
		}
		
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) //Armour and Accessories
		{
			int x1 = (int) ((Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y1 = armorOffset + ((i % 5) * (size + 2));	
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				//The mouse doesn't have something picked up 
				//Pick up 1/2 stack of whatever is in the inventory. 
				if(player.inventory.getArmorInventoryStack(i) != null && player.heldMouseItem == null) 
				{
					pickUpHalfMouseItem(update, world, player, 2, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(update, world, player, 2, i);
				}
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20) //Garbage
		{
			if(player.inventory.getTrashStack(0) != null && player.heldMouseItem == null)  //The mouse doesn't have something picked up
			{
				pickUpHalfMouseItem(update, world, player, 4, 0, x - x1, y - y1, 16);
			}
			else if(player.heldMouseItem != null) //The mouse has something picked up
			{
				UIInventory.placeOneItemIntoInventory(update, world, player, 4, 0);
			}
		}	
		
		if(player.isViewingChest)
		{
			chestMouseEvents(update, world, player, x, y, false);
		}
	}
	
	/**
	 * Handles Mouse Events for everything in the inventory.
	 */
	protected static void mouseEventLeftClick(ClientUpdate update, WorldClientEarth world, EntityPlayer player)
	{
		//If the mouse isnt down, there's really no reason to run the rest of this function
		if(!Mouse.isButtonDown(0)) 
		{
			return;
		}		
		
		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();		
		
		try {
			//Band-aid fix for mouse clicks. Get the position before calling this though of it'll break on windows.
			Mouse.destroy(); 
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		if(Keys.lshiftDown)
		{
			mouseClickShiftModifier(update, world, player, x, y);
		}
		else
		{
			mouseClickNoModifier(update, world, player, x, y);
		}		
	}
	
	/**
	 * Handles mouse input given that no modifier is applied. This will cause the default behaviour, which is to 
	 * pick things up using the mouse, or s.
	 * @param world the world in use
	 * @param player the player in use
	 */
	protected static void mouseClickNoModifier(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int x, int y)
	{
		shouldDropItem = true;
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				if(player.inventory.getMainInventoryStack(i) != null && player.heldMouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(update, world, player, 1, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(update, world, player, 1, i);
				}
			}		
		}
		
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) //Quiver
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * (size)) - (size + 22f));
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				if(player.inventory.getQuiverStack(i) != null && player.heldMouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(update, world, player, 3, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(update, world, player, 3, i);
				}
			}		
		}
		
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) //Armour and Accessories
		{
			int x1 = (int) ((Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y1 = armorOffset + ((i % 5) * (size + 2));	
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				if(player.inventory.getArmorInventoryStack(i) != null && player.heldMouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(update, world, player, 2, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(player.heldMouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(update, world, player, 2, i);
				}
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		if(x >= x1 && x <= x1 + 20 && y >= y1 && y <= y1 + 20) //Garbage
		{
			if(player.inventory.getTrashStack(0) != null && player.heldMouseItem == null)  //The mouse doesn't have something picked up
			{
				pickUpMouseItem(update, world, player, 4, 0, x - x1, y - y1, 16);
			}
			else if(player.heldMouseItem != null) //The mouse has something picked up
			{
				UIInventory.placeItemIntoInventory(update, world, player, 4, 0);
			}
		}			
		
		//Recipe Slots:
		int xoff = (int) (Display.getWidth() * 0.25f) - 62;
		int yoff = 10;
		size = 20;
			
		x1 = xoff;
		y1 = yoff; 
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.selectedRecipe >= 2) //Left
		{
			shouldDropItem = false;
			UI.adjustSliderPosition(world, player, -1);
		}
		
		x1 = xoff + 24;
		y1 = yoff;
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.selectedRecipe >= 1) //Mid-Left
		{
		
			shouldDropItem = false;
			UI.adjustSliderPosition(world, player, -1);
		}
	
		size = 24; 
		x1 = xoff + 49;
		y1 = yoff - 2; 
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Middle
		{
			
			shouldDropItem = false;
			UI.craftRecipe(update, world, player, player.selectedRecipe, -2, -2, 16);
		}

		size = 20; 
		x1 = xoff + 76;
		y1 = yoff;
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Mid-right
		{
		
			shouldDropItem = false;
			UI.adjustSliderPosition(world, player, 1);
		}
		
		x1 = xoff + 100;
		y1 = yoff;	
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Right
		{
			shouldDropItem = false;
			UI.adjustSliderPosition(world, player, 1);
		}
		
		if(player.isViewingChest)
		{
			chestMouseEvents(update, world, player, x, y, true);
		}
		
		if(isSocketWindowOpen)
		{			
			int tooltipWidth = 8 * 20;
			int tooltipHeight = 100;
			int frameX = (int) (Display.getWidth() * 0.25) - 80;  
			int frameY = (int) (Display.getHeight() * 0.5) - 106; 
			
			ItemStack socketedItem = null;
			if(inventoryID == 1)
			{
				socketedItem = player.inventory.getMainInventoryStack(inventoryIndex);
			}
			else if(inventoryID == 2)
			{
				socketedItem = player.inventory.getArmorInventoryStack(inventoryIndex);
			}
			else if(inventoryID == 3)
			{
				socketedItem = player.inventory.getQuiverStack(inventoryIndex);
			}
			else if(inventoryID == 4)
			{
				socketedItem = player.inventory.getTrashStack(inventoryIndex);
			}
			String itemName = socketedItem.getItemName();
			String[] stats = { };        
			if(socketedItem.getItemID() >= Item.itemIndex && socketedItem.getItemID() < ActionbarItem.spellIndex)
			{
				stats = Item.itemsList[socketedItem.getItemID()].getStats();
			}
			
			//% of total text size to render, in this case scale to 1/2 size.
			float xScale = 0.25F;
			
			//Find out how long does the tooltip actually has to be
			double requiredHeight = 10 + 
					tooltipFont.getHeight(itemName) * xScale * 1.5F + 				
					(tooltipFont.getHeight(itemName) * xScale * stats.length) 
					+ 30;
			
			tooltipHeight = (int) requiredHeight;
			frameY -= tooltipHeight;
			
			//Check if a gem should be socketted
			float yOffset = frameY + boldTooltip.getHeight(itemName) + 5 * (stats.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));	
			
			//Check if a gem could be socketed
			if(player.getHeldItem() != null)
			{
				if(player.getHeldItem().getItemID() < ActionbarItem.spellIndex && 
						player.getHeldItem().getItemID() >= ActionbarItem.blockIndex && 
						Item.itemsList[player.getHeldItem().getItemID()] instanceof ItemGem)
				{
					
					int numberOfSockets = socketedItem.getGemSockets().length;
					for(int i = 0; i < numberOfSockets; i++)
					{
						size = 30;
						double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
						double xOffset = frameX + offsetByTotal + (i * (size + 7.5));
						if(y > yOffset && y < yOffset + size && x > xOffset && x < xOffset + size)
						{	
	//						/player <id> socketgem <inventory_id> <index> <gem_socket_index>
							String command = "/player " + player.entityID + " socketgem " + inventoryID + " " + inventoryIndex + " " + i;
							update.addCommand(command);
							shouldDropItem = false;
						}
					}
				}
			}			
			
			double width = 12;
			double height = 12;
			double xBound = frameX + tooltipWidth - width - 4;
			double yBound = frameY + 4;
			if(x >= xBound && x <= xBound + width && y >= yBound && y <= yBound + height)
			{
				clearSocketVariables();
			}	
		}		
		
		//if the player didnt click something, drop their mouseitem
		if(shouldDropItem)
		{
			dropMouseItem(update, world, player);
		}
	}
	
	/**
	 * Picks up an item from the inventory to the mouse's temperary 'itemstack'. This function is by itself not very safe.
	 * Don't call it without ensuring that the mouseItem Itemstack is null.
	 * @param whichInventory An integer value determining which inventory to pick up the item from. 1-main, 2-coin, 3-ammo, 4-armor, 5-social armor, 6-trash
	 * @param index The slot of the selected inventory in which an item is being removed from.
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize how big is the mouse item being rendered
	 */
	private static void pickUpMouseItem(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		
		String command = "/player " + player.entityID + " mousepickup " + whichInventory + " " + index + " all";
		update.addCommand(command);
	}
	
	/**
	 * Picks up an item from the inventory to the mouse's temporary 'itemstack'. This function is by itself not very safe.
	 * Don't call it without ensuring that the mouseItem Itemstack is null. This version only picks up 1 item, not the entire
	 * stack
	 * @param whichInventory An integer value determining which inventory to pick up the item from. 1-main, 2-coin, 3-ammo, 4-armor, 5-social armor, 6-trash
	 * @param index The slot of the selected inventory in which an item is being removed from.
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize how big is the mouse item being rendered
	 */
	private static void pickUpHalfMouseItem(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		
		String command = "/player " + player.entityID + " mousepickup " + whichInventory + " " + index + " 1/2";
		update.addCommand(command);
	}
	
	/**
	 * Handles mouse interaction with the inventory, and other stuff, given that the shift modifier is applied. This will
	 * change the behaviour of certain clicks and cause stuff to be moved around, not picked up, most of the time.
	 * @param world the world in use
	 * @param player the player in use
	 */
	private static void mouseClickShiftModifier(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int x, int y)
	{
		//Inventory
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				String command = "/player " + player.entityID + " shiftclick 1 " + i;
				update.addCommand(command);
			}		
		}
		
		//Quiver
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * (size)) - (size + 22f));
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) 
			{
				String command = "/player " + player.entityID + " shiftclick 3 " + i;
				update.addCommand(command);
			}		
		}
		
		//Armour and Accessories
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) 
		{
			int x1 = (int) ((Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y1 = armorOffset + ((i % 5) * (size + 2));	
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) 
			{
				String command = "/player " + player.entityID + " shiftclick 2 " + i;
				update.addCommand(command);
			}			
		}
		
		//Garbage
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		if(x >= x1 && x <= x1 + 20 && y >= y1 && y <= y1 + 20) 
		{
			String command = "/player " + player.entityID + " shiftclick 4 0";
			update.addCommand(command);
		}			
		
		//Recipe Slots:
		int xoff = (int) (Display.getWidth() * 0.25f) - 62;
		int yoff = 10;
		size = 20;
			
		x1 = xoff;
		y1 = yoff; 
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.selectedRecipe >= 2) //Left
		{
			UI.adjustSliderPosition(world, player, -1);
		}
		
		x1 = xoff + 24;
		y1 = yoff;
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.selectedRecipe >= 1) //Mid-Left
		{
		
			UI.adjustSliderPosition(world, player, -1);
		}
	
		size = 24; 
		x1 = xoff + 49;
		y1 = yoff - 2; 
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Middle
		{
			
			UI.craftRecipe(update, world, player, player.selectedRecipe, -2, -2, 16);
		}

		size = 20; 
		x1 = xoff + 76;
		y1 = yoff;
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Mid-right
		{
		
			UI.adjustSliderPosition(world, player, 1);
		}
		
		x1 = xoff + 100;
		y1 = yoff;	
		if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Right
		{
			UI.adjustSliderPosition(world, player, 1);
		}
		
		if(player.isViewingChest)
		{
			chestMouseEvents(update, world, player, x, y, true);
		}
	}
		
	/**
	 * Handles chest mouse events, based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have
	 * @param x the x position of the mouse (not including getCameraX())
	 * @param y the y position of the mouse (not including getCameraY())
	 */
	protected static void chestMouseEvents(ClientUpdate update, WorldClientEarth world, EntityPlayer player, int mouseX, int mouseY, boolean leftClick)
	{
		if(leftClick)
		{
			//Get the initial block the player is viewing
			Block block = world.getAssociatedBlock(player.viewedChestX, player.viewedChestY);
			if(!(block instanceof BlockChest))
			{
				player.clearViewedChest();
				return;
			}
			BlockChest chest = (BlockChest)block;
			ClientMinimalBlock minimalBlock = world.getBlock(player.viewedChestX, player.viewedChestY);
			
			int xOffset = 0;
			int yOffset = 0;		
			if(minimalBlock.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockWidth / 6), 
						(int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockHeight / 6)); //metadata used by the block of size (x,y)
				int metaWidth = metadata.length; 
				int metaHeight = metadata[0].length;	
				//Loop until a the current chest's metadata value is found
				//This provides the offset to find the 'real' chest, with the actual items in it
				for(int i = 0; i < metaWidth; i++) 
				{
					for(int j = 0; j < metaHeight; j++)
					{
						if(metadata[i][j] == world.getBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset).metaData)
						{
							xOffset = i; 
							yOffset = j;
							break;
						}
					}
				}			
				//Update the chest
				minimalBlock = world.getBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset);
				chest = (BlockChest)(world.getAssociatedBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset));
			}	
			
			int totalRows = chest.getInventorySize() / 4;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each ItemStack in the chest
			{
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				if(mouseX >= x1 && mouseX <= x1 + size && mouseY >= y1 && mouseY <= y1 + size) //Is the click in bounds?
				{
//					/player <id> chestevent <x> <y> <index> <qty>
					pickUpMouseItemChest(i, mouseX - x1 - 2, mouseY - y1 - 2, 16);
					String command = "/player " + player.entityID + " chestevent " + (player.viewedChestX - xOffset) + " " + (player.viewedChestY - yOffset) + " " + i + " all";
					update.addCommand(command);
					shouldDropItem = false;
				}		
			}
		}
		else
		{
			//Get the initial block the player is viewing
			Block block = world.getAssociatedBlock(player.viewedChestX, player.viewedChestY);
			if(!(block instanceof BlockChest))
			{
				player.clearViewedChest();
				return;
			}
			BlockChest chest = (BlockChest)block;
			ClientMinimalBlock minimalBlock = world.getBlock(player.viewedChestX, player.viewedChestY);
			
			int xOffset = 0;
			int yOffset = 0;		
			if(minimalBlock.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockWidth / 6), 
						(int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockHeight / 6)); //metadata used by the block of size (x,y)
				int metaWidth = metadata.length; 
				int metaHeight = metadata[0].length;	
										
				//Loop until a the current chest's metadata value is found
				//This provides the offset to find the 'real' chest, with the actual items in it
				for(int i = 0; i < metaWidth; i++) 
				{
					for(int j = 0; j < metaHeight; j++)
					{
						if(metadata[i][j] == world.getBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset).metaData)
						{
							xOffset = i; 
							yOffset = j;
							break;
						}
					}
				}			
				//Update the chest
				minimalBlock = world.getBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset);
				chest = (BlockChest)(world.getAssociatedBlock(player.viewedChestX - xOffset, player.viewedChestY - yOffset));
			}	
			
			//for each ItemStack in the chest check if there's a mouse command interacting with it
			int totalRows = chest.getInventorySize() / 4;
			for(int i = 0; i < chest.getInventorySize(); i++) 
			{
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				if(mouseX > x1 && mouseX < x1 + size && mouseY > y1 && mouseY < y1 + size) //Is the click in bounds?
				{
//					/player <id> chestevent <x> <y> <index> <qty>
					pickUpHalfMouseItemChest(i, mouseX - x1 - 2, mouseY - y1 - 2, 16);
					String command = "/player " + player.entityID + " chestevent " + (player.viewedChestX - xOffset) + " " + (player.viewedChestY - yOffset) + " " + i + " 1/2";
					update.addCommand(command);
				}		
			}	
		}
	}
	
	/**
	 * Hands some render stuff regarding a mouse item pickup. This does not issue a command to the server.
	 * @param index the slow of the chest that is being removed
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize the size of the mouseItem being rendered
	 */
	protected static void pickUpMouseItemChest(int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
	}

	/**
	 * Hands some render stuff regarding a mouse item pickup. This does not issue a command to the server.
	 * @param index the slow of the chest that is being removed
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize the size of the mouseItem being rendered
	 */
	protected static void pickUpHalfMouseItemChest(int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
	}
	
	/**
	 * Issues a command to drop the mouseItem into the world
	 */
	protected static void dropMouseItem(ClientUpdate update, WorldClientEarth world, EntityPlayer player)
	{
		String command = "/player " + player.entityID + " mousethrow " + " all";
		update.addCommand(command);
	}

	/**
	 * Checks to see if a mouse click is over something in one of the player's inventories.
	 * This will return null if nothing appropriate is found.
	 * @param positions 
	 * @return the ItemStack the player is clicking on, or null if none is selected
	 */
	protected static ItemStack getClickedStack(WorldClientEarth world, EntityPlayer player, int x, int y, boolean[] equipped, int[] positions)
	{		
		//Inventory
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				equipped[0] = false;
				positions[0] = 1;
				positions[1] = i;
				return player.inventory.getMainInventoryStack(i);
			}		
		}
		
		//Quiver
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * size) - (size + 22f));
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				equipped[0] = true;
				positions[0] = 3;
				positions[1] = i;
				return player.inventory.getQuiverStack(i);
			}		
		}
		
		//Armour and Accessories
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) //Armour and Accessories
		{
			int x1 = (int) ((Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y1 = armorOffset + ((i % 5) * (size + 2));	
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				equipped[0] = true; 
				positions[0] = 2;
				positions[1] = i;
				return player.inventory.getArmorInventoryStack(i);
			}			
		}
		
		//Garbage
		size = 20;
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) 
		{
			equipped[0] = false;
			positions[0] = 4;
			positions[1] = 0;
			return player.inventory.getTrashStack(0);
		}			
		
		//Middle Recipe Slot:
		size = 24;
		x1 = (int) (Display.getWidth() * 0.25f) - 13;
		y1 = 8; 
	
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.getAllPossibleRecipes().length > 0) //Middle
		{
			equipped[0] = false;
			return player.getAllPossibleRecipes()[player.selectedRecipe].getResult();
		}
				
		//Chests
		if(player.isViewingChest)
		{
			//Get the initial block the player is viewing
			BlockChest chest = (BlockChest)world.getAssociatedBlock(player.viewedChestX, player.viewedChestY);
			ClientMinimalBlock minimalBlock = world.getBlock(player.viewedChestX, player.viewedChestY);
			
			if(minimalBlock.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockWidth / 6), 
						(int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockHeight / 6)); //metadata used by the block of size (x,y)
				int metaWidth = metadata.length; 
				int metaHeight = metadata[0].length;	
				x1 = 0;
				y1 = 0;				
				//Loop until a the current chest's metadata value is found
				//This provides the offset to find the 'real' chest, with the actual items in it
				for(int i = 0; i < metaWidth; i++) 
				{
					for(int j = 0; j < metaHeight; j++)
					{
						if(metadata[i][j] == world.getBlock(player.viewedChestX - x1, player.viewedChestY - y1).metaData)
						{
							x1 = i; 
							y1 = j;
							break;
						}
					}
				}			
				//Update the chest
				minimalBlock = world.getBlock(player.viewedChestX - x1, player.viewedChestY - y1);
				chest = (BlockChest)(world.getAssociatedBlock(player.viewedChestX - x1, player.viewedChestY - y1));
			}	
			
			int totalRows = chest.getInventorySize() / 4;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each ItemStack in the chest
			{
				size = 20;
				x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					equipped[0] = false;
					return chest.getItemStack(i);
				}		
			}
		}
		return null;
	}	

}

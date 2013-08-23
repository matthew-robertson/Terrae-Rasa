package ui;

import hardware.Keys;
import items.Item;
import items.ItemAmmo;
import items.ItemArmor;
import items.ItemGem;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import transmission.ClientUpdate;
import transmission.UpdateWithObject;
import utils.ActionbarItem;
import utils.DisplayableItemStack;
import utils.MathHelper;
import utils.MetaDataHelper;
import world.World;
import blocks.Block;
import blocks.BlockChest;
import entities.EntityPlayer;

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
	protected static void mouse(ClientUpdate update, World world, EntityPlayer player)
	{
		mouseEventLeftClick(update, world, player);	
		mouseEventRightClick(world, player);
	}

	/**
	 * Does something appropriate if the user right clicks with the inventory open. The functionality of this is limited.
	 * @param world
	 * @param player
	 */
	protected static void mouseEventRightClick(World world, EntityPlayer player)
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
			mouseEventRightClickNoModifier(world, player, x, y);
		}
	}
	
	protected static void mouseEventRightClickShift(World world, EntityPlayer player, int mouseX, int mouseY)
	{
		boolean[] equipped = { false };
		DisplayableItemStack clickedItem = getClickedStack(world, player, mouseX, mouseY, equipped);
		socketItemEquipped = equipped[0];
		if(clickedItem != null && clickedItem.hasSockets())
		{					
			isSocketWindowOpen = true;
			player.clearViewedChest();
			socketedItem = clickedItem;
		}
		else
		{
			UISocketMenu.closeSocketWindow();
		}
	}
	
	protected static void mouseEventRightClickNoModifier(World world, EntityPlayer player, int x, int y)
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
				if(player.inventory.getMainInventoryStack(i) != null && mouseItem == null)
				{
					pickUpHalfMouseItem(world, player, 1, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(world, player, 1, i);
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
				if(player.inventory.getQuiverStack(i) != null && mouseItem == null) 
				{
					pickUpHalfMouseItem(world, player, 4, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(world, player, 4, i);
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
				if(player.inventory.getArmorInventoryStack(i) != null && mouseItem == null) 
				{
					pickUpHalfMouseItem(world, player, 2, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeOneItemIntoInventory(world, player, 2, i);
				}
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20) //Garbage
		{
			if(player.inventory.getTrashStack(0) != null && mouseItem == null)  //The mouse doesn't have something picked up
			{
				pickUpHalfMouseItem(world, player, 3, 0, x - x1, y - y1, 16);
			}
			else if(mouseItem != null) //The mouse has something picked up
			{
				UIInventory.placeOneItemIntoInventory(world, player, 3, 0);
			}
		}	

		if(player.isViewingChest)
		{
			BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY).clone();
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
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
				chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x1, player.viewedChestY - y1));
			}	
			
			int totalRows = chest.getInventorySize() / 4;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each DisplayableItemStack in the chest
			{
				size = 20;
				x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					if(chest.getDisplayableItemStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up, so this is straightforward
					{
						pickUpHalfMouseItemChest(chest, i, x - x1 - 2, y - y1 - 2, 16);
					}
					else if(mouseItem != null) //The mouse has something picked up, things need swapped
					{
						//Reference safe swap
						DisplayableItemStack stack = new DisplayableItemStack(mouseItem);
						mouseItem = chest.takeDisplayableItemStack(i);
						chest.placeDisplayableItemStack(stack, i);
						shouldDropItem = false;
					}
				}		
			}	
		}
	}
	
	/**
	 * Handles Mouse Events for everything in the inventory.
	 */
	protected static void mouseEventLeftClick(ClientUpdate update, World world, EntityPlayer player)
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
			mouseClickShiftModifier(world, player, x, y);
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
	protected static void mouseClickNoModifier(ClientUpdate update, World world, EntityPlayer player, int x, int y)
	{
		shouldDropItem = true;
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				if(player.inventory.getMainInventoryStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 1, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(world, player, 1, i);
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
				if(player.inventory.getQuiverStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 4, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(world, player, 4, i);
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
				if(player.inventory.getArmorInventoryStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 2, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					UIInventory.placeItemIntoInventory(world, player, 2, i);
				}
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x >= x1 && x <= x1 + 20 && y >= y1 && y <= y1 + 20) //Garbage
		{
			if(player.inventory.getTrashStack(0) != null && mouseItem == null)  //The mouse doesn't have something picked up
			{
				pickUpMouseItem(world, player, 3, 0, x - x1, y - y1, 16);
			}
			else if(mouseItem != null) //The mouse has something picked up
			{
				UIInventory.placeItemIntoInventory(world, player, 3, 0);
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
			UI.craftRecipe(world, player, player.selectedRecipe, -2, -2, 16);
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
			chestMouseEvents(world, player, x, y);
		}
		
		//Socket a gem, if possible.
		if(isSocketWindowOpen && mouseItem != null)
		{
			if(mouseItem.getItemID() < ActionbarItem.spellIndex && 
				mouseItem.getItemID() >= ActionbarItem.blockIndex && 
				Item.itemsList[mouseItem.getItemID()] instanceof ItemGem)
			{
				int tooltipHeight = 100;
				int frameX = (int) (Display.getWidth() * 0.25) - 80;  
				int frameY = (int) (Display.getHeight() * 0.5) - 106; 
				
				DisplayableItemStack stack = socketedItem;
				String itemName = socketedItem.getItemName();
				String[] stats = { };        
				if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
				{
					stats = Item.itemsList[stack.getItemID()].getStats();
				}
				
				frameY -= (int) (boldTooltip.getHeight(itemName) + (boldTooltip.getHeight(itemName) * 0.75 * stats.length) + 30);							
				float yOffset = frameY + boldTooltip.getHeight(itemName) + 5 * (stats.length) + 
						(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
				
				int numberOfSockets = stack.getGemSockets().length;
				for(int i = 0; i < numberOfSockets; i++)
				{
					size = 30;
					double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
					double xOffset = frameX + offsetByTotal + (i * (size + 7.5));
					if(y > yOffset && y < yOffset + size && x > xOffset && x < xOffset + size)
					{	
						if(socketItemEquipped)
						{
							player.inventory.setArmorInventoryStack(player, null, socketedItem, socketItemIndex);
						}
						stack.getGemSockets()[i].socket(new DisplayableItemStack(mouseItem));
						if(socketItemEquipped)
						{
							player.inventory.setArmorInventoryStack(player, socketedItem, null, socketItemIndex);
						}
					
						mouseItem.removeFromStack(1);
						if(mouseItem.getStackSize() <= 0)
						{
							mouseItem = null;
						}
						shouldDropItem = false;
					}
				}
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
	private static void pickUpMouseItem(World world, EntityPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		
		if(whichInventory == 1) //Main Inventory
		{
			mouseItem = player.inventory.getMainInventoryStack(index);
			player.inventory.putDisplayableItemStackInSlot(world, player, null, index);
		}
		else if(whichInventory == 2) //Armor Inventory
		{
			mouseItem = player.inventory.getArmorInventoryStack(index);
			player.inventory.setArmorInventoryStack(player, null, player.inventory.getArmorInventoryStack(index), index);
		}
		else if(whichInventory == 3) //Trash
		{
			mouseItem = player.inventory.getTrashStack(index);
			player.inventory.setTrashStack(null, index);
		}
		else if(whichInventory == 4) //Quiver
		{
			mouseItem = player.inventory.getQuiverStack(index);
			player.inventory.setQuiverStack(player, null, index);
		}			
	}
	
	/**
	 * Picks up an item from the inventory to the mouse's temperary 'itemstack'. This function is by itself not very safe.
	 * Don't call it without ensuring that the mouseItem Itemstack is null. This version only picks up 1 item, not the entire
	 * stack
	 * @param whichInventory An integer value determining which inventory to pick up the item from. 1-main, 2-coin, 3-ammo, 4-armor, 5-social armor, 6-trash
	 * @param index The slot of the selected inventory in which an item is being removed from.
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize how big is the mouse item being rendered
	 */
	private static void pickUpHalfMouseItem(World world, EntityPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		
		if(whichInventory == 1) //Main Inventory
		{
			mouseItem = new DisplayableItemStack(player.inventory.getMainInventoryStack(index));
			mouseItem.setStackSize((int)(MathHelper.floorOne(mouseItem.getStackSize() / 2)));
			player.inventory.removeItemsFromInventoryStack(player, mouseItem.getStackSize(), index); 
		}
		else if(whichInventory == 2) //Armor Inventory
		{
			mouseItem = player.inventory.getArmorInventoryStack(index);
			player.inventory.setArmorInventoryStack(player, null, player.inventory.getArmorInventoryStack(index), index);
		}
		else if(whichInventory == 3) //Trash
		{
			mouseItem = new DisplayableItemStack(player.inventory.getTrashStack(index));
			mouseItem.setStackSize((int)(MathHelper.floorOne(mouseItem.getStackSize() / 2)));
			player.inventory.removeItemsFromTrashStack(mouseItem.getStackSize(), index);
		}
		else if(whichInventory == 4) //Quiver
		{
			mouseItem = new DisplayableItemStack(player.inventory.getQuiverStack(index));
			mouseItem.setStackSize((int)(MathHelper.floorOne(mouseItem.getStackSize() / 2)));
			player.inventory.removeItemsFromQuiverStack(player, mouseItem.getStackSize(), index);
		}			
	}
	
	/**
	 * Handles mouse interaction with the inventory, and other stuff, given that the shift modifier is applied. This will
	 * change the behaviour of certain clicks and cause stuff to be moved around, not picked up, most of the time.
	 * @param world the world in use
	 * @param player the player in use
	 */
	private static void mouseClickShiftModifier(World world, EntityPlayer player, int x, int y)
	{
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				//Ensure it's actually an Item
				if (player.inventory.getMainInventoryStack(i) != null && 
				    player.inventory.getMainInventoryStack(i).getItemID() < ActionbarItem.spellIndex)
				{
					Item selectedItem = (Item)(Item.itemsList[player.inventory.getMainInventoryStack(i).getItemID()]);
					if(selectedItem instanceof ItemArmor)
					{
						DisplayableItemStack stack = player.inventory.getMainInventoryStack(i);
						player.inventory.removeEntireStackFromInventory(world, player, i);
						stack = player.inventory.pickUpArmorDisplayableItemStack(world, player, stack);
						player.inventory.putDisplayableItemStackInSlot(world, player, stack, i);
					}
					else if(selectedItem instanceof ItemAmmo)
					{
						DisplayableItemStack stack = player.inventory.getMainInventoryStack(i);
						player.inventory.removeEntireStackFromInventory(world, player, i);
						stack = player.inventory.pickUpQuiverDisplayableItemStack(world, player, stack);
						player.inventory.putDisplayableItemStackInSlot(world, player, stack, i);
					}					
				}
			}		
		}
		
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) //Quiver
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * (size)) - (size + 22f));
			
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.inventory.getQuiverStack(i) != null) 
			{
				//Try to place that stack in the inventory - leaving what's left in the quiver (if any)
				player.inventory.setQuiverStack(player, player.inventory.pickUpItemStack(world, 
						player, 
						player.inventory.getQuiverStack(i)), 
						i);
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
			
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size && player.inventory.getArmorInventoryStack(i) != null) 
			{
				player.inventory.setArmorInventoryStack(player, player.inventory.pickUpItemStack(world, 
							player, 
							player.inventory.getArmorInventoryStack(i)),
						player.inventory.getArmorInventoryStack(i),
						i);
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x >= x1 && x <= x1 + 20 && y >= y1 && y <= y1 + 20 && player.inventory.getTrashStack(0) != null) //Garbage
		{
			player.inventory.setTrashStack(player.inventory.pickUpItemStack(world, 
					player, 
					player.inventory.getTrashStack(0)), 
					0);
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
			
			UI.craftRecipe(world, player, player.selectedRecipe, -2, -2, 16);
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
			chestMouseEvents(world, player, x, y);
		}
	}
		
	/**
	 * Handles chest mouse events, based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have
	 * @param x the x position of the mouse (not including getCameraX())
	 * @param y the y position of the mouse (not including getCameraY())
	 */
	protected static void chestMouseEvents(World world, EntityPlayer player, int x, int y)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY).clone();
		
		if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
		{
			//Get the metadata for the block's size
			int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockWidth / 6), 
					(int)(Block.blocksList[world.getBlock(player.viewedChestX, player.viewedChestY).id].blockHeight / 6)); //metadata used by the block of size (x,y)
			int metaWidth = metadata.length; 
			int metaHeight = metadata[0].length;	
			int x1 = 0;
			int y1 = 0;				
			
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
			chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x1, player.viewedChestY - y1));
		}	
		
		int totalRows = chest.getInventorySize() / 4;
		for(int i = 0; i < chest.getInventorySize(); i++) //for each DisplayableItemStack in the chest
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
			
			if(x >= x1 && x <= x1 + size && y >= y1 && y <= y1 + size) //Is the click in bounds?
			{
				if(chest.getDisplayableItemStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up, so this is straightforward
				{
					pickUpMouseItemChest(chest, i, x - x1 - 2, y - y1 - 2, 16);
					shouldDropItem = false;
				}
				else if(mouseItem != null) //The mouse has something picked up, things need swapped
				{
					//Reference safe swap
					DisplayableItemStack stack = new DisplayableItemStack(mouseItem);
					mouseItem = chest.takeDisplayableItemStack(i);
					chest.placeDisplayableItemStack(stack, i);
					shouldDropItem = false;
				}
			}		
		}		
	}
	
	/**
	 * Picks up an item from the specified chest to the mouse's temporary 'itemstack'. 
	 * This function is by itself not very safe. Don't call it without ensuring that the 
	 * mouseItem DisplayableItemStack is null.
	 * @param chest the chest that is have an item taken from it
	 * @param index the slow of the chest that is being removed
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize the size of the mouseItem being rendered
	 */
	protected static void pickUpMouseItemChest(BlockChest chest, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		mouseItem = chest.getDisplayableItemStack(index);
		chest.removeDisplayableItemStack(index);
	}
	
	protected static void pickUpHalfMouseItemChest(BlockChest chest, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		mouseItem = new DisplayableItemStack(chest.getDisplayableItemStack(index));
		mouseItem.setStackSize((int)(MathHelper.floorOne(mouseItem.getStackSize() / 2)));
		chest.removeItemsFromInventoryStack((mouseItem.getStackSize()), index);
	}
	
	/**
	 * Drops the mouseItem into the world
	 */
	protected static void dropMouseItem(ClientUpdate update, World world, EntityPlayer player)
	{
		if(mouseItem != null)
		{
			//TODO: throw stacks.
			UpdateWithObject objUpdate = new UpdateWithObject();
			String command = "/player " + player.entityID + " throw " + " " + player.x + " " + player.y;
			objUpdate.command = command;
			objUpdate.object = new DisplayableItemStack(mouseItem);
			update.addObjectUpdate(objUpdate);
			//world.addDisplayableItemStackToItemList(new EntityDisplayableItemStack(player.x, player.y, mouseItem));
			mouseItem = null;
		}
	}

	/**
	 * Checks to see if a mouse click is over something in one of the player's inventories.
	 * This will return null if nothing appropriate is found.
	 * @return the DisplayableItemStack the player is clicking on, or null if none is selected
	 */
	protected static DisplayableItemStack getClickedStack(World world, EntityPlayer player, int x, int y, boolean[] equipped)
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
				socketItemIndex = i;
				return player.inventory.getArmorInventoryStack(i);
			}			
		}
		
		size = 20;
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		//Garbage
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) 
		{
			equipped[0] = false;
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
				
		if(player.isViewingChest)
		{
			//Get the initial block the player is viewing
			BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY);
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
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
				chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x1, player.viewedChestY - y1));
			}	
			
			int totalRows = chest.getInventorySize() / 4;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each DisplayableItemStack in the chest
			{
				size = 20;
				x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					equipped[0] = false;
					return chest.getDisplayableItemStack(i);
				}		
			}
		}
		return null;
	}	
}

package ui;

import items.Item;
import items.ItemAmmo;
import items.ItemArmorAccessory;
import items.ItemArmorBelt;
import items.ItemArmorBody;
import items.ItemArmorBoots;
import items.ItemArmorGloves;
import items.ItemArmorHelmet;
import items.ItemArmorPants;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import blocks.BlockChest;

import utils.InventoryPlayer;
import utils.ItemStack;
import utils.MetaDataHelper;
import world.World;
import entities.EntityPlayer;

/**
 * Refactor not entirely done
 * TODO: refactor more
 */
public class UIInventory extends UIBase 
{
	/**
	 * Gets the slot of the main inventory the player is mousing over, or -1 if they arent mousing over a particular slot.
	 * @param player
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	protected static int getMouseoverSlotInventory(EntityPlayer player, int mouseX, int mouseY)
	{
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) 
		{
			int size = 20;
			int x = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) //Is the click in bounds?
			{
				return i;
			}		
		}
		return -1;
	}
	
	protected static int getMouseoverSlotQuiver(EntityPlayer player, int mouseX, int mouseY)
	{
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			int size = 20;
			int x = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y = (int) ((Display.getHeight() * 0.5f) - (i * size) - (size + 22f));
			
			if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) //Is the click in bounds?
			{
				return i;
			}		
		}
		return -1;
	}
	
	protected static int getMouseoverSlotArmor(EntityPlayer player, int mouseX, int mouseY)
	{
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) //Armour and Accessories
		{
			int x = (int) ((Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y = armorOffset + ((i % 5) * (size + 2));	
			
			if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) //Is the click in bounds?
			{
				return i;
			}			
		}
		return -1;
	}
	
	protected static int getMouseoverSlotTrash(EntityPlayer player, int mouseX, int mouseY)
	{
		int size = 20;
		int x = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) 
		{
			return 0;
		}	
		return -1;
	}
	
	protected static ItemStack getMouseoverRecipeSliderStack(EntityPlayer player, int mouseX, int mouseY)
	{
		int size = 24;
		int x = (int) (Display.getWidth() * 0.25f) - 13;
		int y = 8; 
	
		//Middle slot
		if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size && player.getAllPossibleRecipes().length > 0) 
		{
			return player.getAllPossibleRecipes()[player.selectedRecipe].getResult();
		}
		

		x = (int) (Display.getWidth() * 0.25f) - 13;
		y = 10 - 2; 
		if(player.getAllPossibleRecipes() != null && player.getAllPossibleRecipes().length != 0) //Currently selected recipe (pane)
		{				
			for(int i = 0; i < player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe().length; i++) //Panes for the recipe ingredients
			{
				double newSize = 20;
				double newX = x + 2 + ((newSize + 2) * i);
				double newY = y + size + 2;
				
				if(mouseX > newX && mouseX < newX + newSize && mouseY > newY && mouseY < newY + newSize)
				{
					return player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe()[i];
				}		
			}
		}
		
		
		return null;
	}
	
	protected static ItemStack getMouseoverChestStack(World world, EntityPlayer player, int mouseX, int mouseY)
	{
		if(player.isViewingChest)
		{
			//Get the initial block the player is viewing
			BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY);
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6), (int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6)); //metadata used by the block of size (x,y)
				int metaWidth = metadata.length; 
				int metaHeight = metadata[0].length;	
				int x = 0;
				int y = 0;				
				
				//Loop until a the current chest's metadata value is found
				//This provides the offset to find the 'real' chest, with the actual items in it
				for(int i = 0; i < metaWidth; i++) 
				{
					for(int j = 0; j < metaHeight; j++)
					{
						if(metadata[i][j] == world.getBlock(player.viewedChestX - x, player.viewedChestY - y).metaData)
						{
							x = i; 
							y = j;
							break;
						}
					}
				}			
				
				//Update the chest
				chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x, player.viewedChestY - y));
			}	
			
			int totalRows = chest.getInventorySize() / 5;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each ItemStack in the chest
			{
				int size = 20;
				int x = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				int y = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				
				if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) //Is the click in bounds?
				{
					return chest.getItemStack(i);
				}		
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	/**
	 * Renders the selected chest(s), based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have.
	 */
	protected static void renderChest(World world, EntityPlayer player)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY).clone();
		
		if(chest.metaData != 1)
		{
			//Get the metadata for the block's size
			int[][] metadata = MetaDataHelper.getMetaDataArray((int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6), (int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6)); //metadata used by the block of size (x,y)
			int metaWidth = metadata.length; 
			int metaHeight = metadata[0].length;	
			int x = 0;
			int y = 0;				
			
			//Loop until a the current chest's metadata value is found
			//This provides the offset to find the 'real' chest, with the actual items in it
			for(int i = 0; i < metaWidth; i++) 
			{
				for(int j = 0; j < metaHeight; j++)
				{
					if(metadata[i][j] == world.getBlock(player.viewedChestX - x, player.viewedChestY - y).metaData)
					{
						x = i; 
						y = j;
						break;
					}
				}
			}
			
			//Update the chest
			chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x, player.viewedChestY - y));
			player.viewedChestX -= x;
			player.viewedChestY -= y;
		}	
		
		t.startDrawingQuads(); 
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
		int totalRows = (chest.getInventorySize() / 5);
		for(int i = 0; i < chest.getInventorySize(); i++) //Draw all the background slots
		{			
			int size = 20;
			actionbarSlot.bind();
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
		
		GL11.glColor4f(1, 1, 1, 1);
		
		for(int i = 0; i < chest.getInventorySize(); i++) //Draw all the items, in the slots (with text)
		{
			if(chest.getItemStack(i) != null)
			{
				textures[chest.getItemStack(i).getItemID()].bind();
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size + 4))) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size + 4)) - (size + 22f) - 2);

				t.startDrawingQuads();
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
				
				if(chest.getItemStack(i).getMaxStackSize() > 1) //If maxStackSize > 1, draw the stackSize
				{
					GL11.glColor4f(0, 1, 0, 1);
					trueTypeFont.drawString(x - 2, y + 18, new StringBuilder().append(chest.getItemStack(i).getStackSize()).toString(), 0.25f, -0.25f);
					GL11.glColor4f(1, 1, 1, 1);
				}	
			}
		}			
		
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	
	
	
	
	/**
	 * Renders the actionbar (only if the inventory is closed)
	 */
	protected static void renderActionBar(World world, EntityPlayer player) 
	{		
		GL11.glColor4f(1, 1, 1, 0.6f); //the slots are partially transparent full colour
			
		for(int i = 0; i < 12; i++) //Frames of the actionbar
		{
			actionbarSlot.bind();
			int xsize = 18;
			int ysize = 18;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((i - 6) * (xsize + 3)));
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (xsize + 22f));
			
			if(i == player.selectedSlot) //If the slot is the one selected, make it slightly larger and fancier
			{
				y -= 2;
				ysize += 4;
				x -= 2;
				xsize += 4;
				actionbarOutline.bind();
			}
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + ysize, 0, 0, 1);
			t.addVertexWithUV(x + xsize, y + ysize, 0, 1, 1);
			t.addVertexWithUV(x + xsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();	
			
			if(player.inventory.getMainInventoryStack(i) != null &&
			   player.isOnCooldown(player.inventory.getMainInventoryStack(i).getItemID()))
			{
				ICONS.bind();
				t.startDrawingQuads();
				t.addVertexWithUV(x, y + ysize, 0, 1F/16F, 4F/16F);
				t.addVertexWithUV(x + xsize, y + ysize, 0, 1F/16F, 5F/16F);
				t.addVertexWithUV(x + xsize, y, 0, 0, 5F/16F);
				t.addVertexWithUV(x, y, 0, 0, 4F/16F);
				t.draw();
			}
		}		

		GL11.glColor4f(1, 1, 1, 1);
		
		for(int i = 0; i < 12; i++) //Images of the Items on the hotbar
		{			
			if(player.inventory.getMainInventoryStack(i) == null) //Make sure a nullpointer isnt thrown
			{
				continue;
			}
			else
			{
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((i - 6) * (21)) + 1);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - 39);
				if(player.inventory.getMainInventoryStack(i).getItemID() < 2048) //blocks need slightly different dimensions
				{
					size = 14;
					y += 1;
					x += 1;
				}
				textures[player.inventory.getMainInventoryStack(i).getItemID()].bind();
				t.startDrawingQuads();
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
				
				if(player.inventory.getMainInventoryStack(i).getMaxStackSize() == 1) //if the stacksize is one, dont render stacksize
				{
					continue;
				}
				
				//otherwise render the stacksize in green text
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 18, new StringBuilder().append(player.inventory.getMainInventoryStack(i).getStackSize()).toString(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}		
	}	

	/**
	 * Renders the full inventory - recipes, armour, all 40 slots, coins, ammo...
	 */
	protected static void renderInventory(World world, EntityPlayer player) 
	{
		GL11.glColor4f(1, 1, 1, 0.6f);

		//Inventory Frames
		actionbarSlot.bind();
		t.startDrawingQuads();
		for(int i = 0; i < player.inventory.getMainInventory().length; i++) 
		{
			int size = 20;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
		
		//Quiver Frames
		t.startDrawingQuads();
		t.setColorRGBA_F(0.666f, 0.666f, 0.666f, 0.6f);
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			int size = 20;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (-7 * size));
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (i * size) - (size + 22f));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
		
		//Armour and Accessories Frames
		GL11.glColor4f(0.603f, 1.0f, 0.466f, 0.6f);
		t.startDrawingQuads();		
		int armorOffset = 80;
		int size = 20;
		if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
		}
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) 
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((size + 2) * (1.5 + (i / 5))));
			int y = getCameraY() + armorOffset + ((i % 5) * (size + 2));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();	
		
		//Garbage Slot Frame
		t.startDrawingQuads(); 
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
		size = 20;
		int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((-6 * (size))));
		int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (4 * (size)) - (size + 22f));
		t.addVertexWithUV(x, y + size, 0, 0, 1);
		t.addVertexWithUV(x + size, y + size, 0, 1, 1);
		t.addVertexWithUV(x + size, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		
		if(player.inventory.getTrashStack(0) == null) //Garbage Slot Image If nothing's there
		{
			ICONS.bind(); 
			GL11.glColor4f(0.6f, 0.6f, 0.6f, 1);
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + size, 0, 0, 64.0F / ICONS_SHEET_HEIGHT);
			t.addVertexWithUV(x + size, y + size, 0, 64.0F / ICONS_SHEET_WIDTH, 64.0F / ICONS_SHEET_HEIGHT);
			t.addVertexWithUV(x + size, y, 0, 64.0F / ICONS_SHEET_WIDTH, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
		}
		
		populateInventorySlots(world, player); 
		renderScrollableCraftingRecipeWheel(world, player);
	}		

	/**
	 * Fills all the inventory frames rendered with items if the slot isnt null
	 */
	protected static void populateInventorySlots(World world, EntityPlayer player) 
	{
		GL11.glColor4f(1, 1, 1, 1);
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Main Inventory
		{
			if(player.inventory.getMainInventoryStack(i) == null) 
			{
				continue;
			}
			else
			{
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size + 4)) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * (size + 4)) - (size + 24f));
				
				if(player.inventory.getMainInventoryStack(i).getItemID() < 2048)
				{
					size = 14;
					y += 1;
					x += 1;
				}
				textures[player.inventory.getMainInventoryStack(i).getItemID()].bind();
				t.startDrawingQuads();
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			
				if(player.isOnCooldown(player.inventory.getMainInventoryStack(i).getItemID()))
				{
					ICONS.bind();
					t.startDrawingQuads();
					t.addVertexWithUV(x, y + size, 0, 1F/16F, 4F/16F);
					t.addVertexWithUV(x + size, y + size, 0, 1F/16F, 5F/16F);
					t.addVertexWithUV(x + size, y, 0, 0, 5F/16F);
					t.addVertexWithUV(x, y, 0, 0, 4F/16F);
					t.draw();
				}
			}
		}	
		
		//Quiver Inventory
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			if(player.inventory.getQuiverStack(i) == null) 
			{
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (-7 * (size + 4)) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (i * (size + 4)) - (size + 24f));
				icons[7].bind();
				t.startDrawingQuads(); 
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			}
			else
			{
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (-7 * (size + 4)) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (i * (size + 4)) - (size + 24f));
				
				textures[player.inventory.getQuiverStack(i).getItemID()].bind();
				t.startDrawingQuads();
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			}
		}	
				
		//
		int armorOffset = 80;
		int size = 16;
		
		if(armorOffset + (9 * (size + 5)) > Display.getHeight() * 0.5F)
		{
			armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 5))));
		}
		armorOffset += 2;
		
		for(int i = 0; i < player.inventory.getArmorInventoryLength(); i++) //Armor Inventory
		{
			if(player.inventory.getArmorInventoryStack(i) == null) 
			{	
				if(i < 6)
				{
					size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((size + 6) * (1.5f + (i / 5))) + 2);
					int y = getCameraY() + armorOffset + ((i % 5) * (size + 6));
					icons[i].bind();
					t.startDrawingQuads();					
					t.addVertexWithUV(x, y + size, 0, 0, 1);
					t.addVertexWithUV(x + size, y + size, 0, 1, 1);
					t.addVertexWithUV(x + size, y, 0, 1, 0);
					t.addVertexWithUV(x, y, 0, 0, 0);
					t.draw();
				}
			}
			else
			{					
				textures[player.inventory.getArmorInventoryStack(i).getItemID()].bind();
				t.startDrawingQuads();
				size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((size + 6) * (1.5f + (i / 5))) + 2);
				int y = getCameraY() + armorOffset + ((i % 5) * (size + 6));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			}
		}			
		if(player.inventory.getTrashStack(0) != null) //Garbage Slot 
		{
			textures[player.inventory.getTrashStack(0).getItemID()].bind();
			t.startDrawingQuads(); 
			size = 16;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((-6 * (size + 4)))) + 2;
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (4 * (size + 4)) - (size + 26)) + 2;
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
		}
		
		//Stack Sizes:
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Main Inventory
		{
			if(player.inventory.getMainInventoryStack(i) == null || player.inventory.getMainInventoryStack(i).getMaxStackSize() == 1) 
			{
				continue;
			}
			else
			{
				GL11.glColor4f(0, 1, 0, 1);
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * 20) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * 20) - 40);
				trueTypeFont.drawString(x - 2, 
						y + 18, 
						new StringBuilder().append(player.inventory.getMainInventoryStack(i).getStackSize()).toString(), 
						0.25f, 
						-0.25f);
			}
		}	

		//Quiver
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) 
		{
			if(player.inventory.getQuiverStack(i) == null || player.inventory.getQuiverStack(i).getMaxStackSize() == 1) 
			{
				continue;
			}
			else
			{
				GL11.glColor4f(0, 1, 0, 1);
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (-7 * 20) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (i * 20) - 40);
				trueTypeFont.drawString(x - 2, 
						y + 18, 
						new StringBuilder().append(player.inventory.getQuiverStack(i).getStackSize()).toString(), 
						0.25f, 
						-0.25f);
			}
		}	
	
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	/**
	 * Places an item into any of the standard inventories
	 * @param whichInventory the number value of the inventory the item is to be placed in. Main-1, Armor-2, Trash-3
	 * @param index slot of the selected inventory to place the item.
	 */
	protected static void placeItemIntoInventory(World world, EntityPlayer player, int whichInventory, int index)
	{
		shouldDropItem = false;
		try
		{
			if(whichInventory == 1) //Main Inventory
			{
				if(player.inventory.getMainInventoryStack(index) == null) //There's nothing there, so the mouse doesnt have to pickup something
				{
					player.inventory.putItemStackInSlot(world, player, mouseItem, index);
					mouseItem = null;
				}
				else if(player.inventory.getMainInventoryStack(index).getItemID() == mouseItem.getItemID())
				{
					if(player.inventory.getMainInventoryStack(index).getStackSize() + mouseItem.getStackSize() 
							<= player.inventory.getMainInventoryStack(index).getMaxStackSize())
					{
						player.inventory.combineItemStacksInSlot(world, player, mouseItem, index);
						mouseItem = null;
					}
					else
					{
						mouseItem.removeFromStack((player.inventory.getMainInventoryStack(index).getMaxStackSize() - 
								player.inventory.getMainInventoryStack(index).getStackSize()));
						player.inventory.adjustMainInventoryStackSize(index, player.inventory.getMainInventoryStack(index).getMaxStackSize());
					}
				}
				else //If there is an item there, swap that slot's item and the mouse's item.
				{
					ItemStack stack = new ItemStack(player.inventory.getMainInventoryStack(index));
					player.inventory.putItemStackInSlot(world, player, mouseItem, index);
					mouseItem = stack;
				}
			}
			else if(whichInventory == 2) //Armor && Accessories
			{
				Item item = Item.itemsList[mouseItem.getItemID()];			
				//Check if the item is actually valid for the selected slot:
				if(index == InventoryPlayer.HELMET_INDEX) //Helmet
				{
					if(!(item != null) || !(item instanceof ItemArmorHelmet))
					{
						return;
					}	
				}
				else if(index == InventoryPlayer.BODY_INDEX) //Body
				{
					if(!(item != null) || !(item instanceof ItemArmorBody))
					{
						return;
					}	
				}
				else if(index == InventoryPlayer.BELT_INDEX) //Belt
				{
					if(!(item != null) || !(item instanceof ItemArmorBelt))
					{
						return;
					}	
				}			
				else if(index == InventoryPlayer.PANTS_INDEX) //Pants
				{
					if(!(item != null) || !(item instanceof ItemArmorPants))
					{
						return;
					}	
				}
				else if(index == InventoryPlayer.BOOTS_INDEX) //Boots
				{
					if(!(item != null) || !(item instanceof ItemArmorBoots))
					{
						return;
					}	
				}
				else if(index == InventoryPlayer.GLOVES_INDEX) //Gloves
				{
					if(!(item != null) || !(item instanceof ItemArmorGloves))
					{
						return;
					}	
				}
				else //Accessory
				{
					if(!(item != null) || !(item instanceof ItemArmorAccessory))
					{
						return;
					}	
				}
				
				if(player.inventory.getArmorInventoryStack(index) == null) //There's nothing there, so the mouse doesnt have to pickup something
				{
					player.inventory.setArmorInventoryStack(player, mouseItem, player.inventory.getArmorInventoryStack(index), index);
					mouseItem = null;
				}
				else //If there is an item there, swap that slot's item and the mouse's item.
				{
					ItemStack stack = player.inventory.getArmorInventoryStack(index);
					player.inventory.setArmorInventoryStack(player, mouseItem, player.inventory.getArmorInventoryStack(index), index);
					mouseItem = stack;
				}
			}
			else if(whichInventory == 3) //Trash
			{
				//The mouse doesnt swap items in this instance, so just place the item there
				player.inventory.setTrashStack(mouseItem, index);
				mouseItem = null;
			}
			else if(whichInventory == 4) //Quiver
			{								
				Item item = Item.itemsList[mouseItem.getItemID()];
				if(!(item instanceof ItemAmmo))
				{
					return;
				}
				
				if(player.inventory.getQuiverStack(index) == null) //There's nothing there, so the mouse doesnt have to pickup something
				{
					player.inventory.setQuiverStack(mouseItem, index);
					mouseItem = null;
				}
				else if(player.inventory.getQuiverStack(index).getItemID() == mouseItem.getItemID())
				{
				
					if(player.inventory.getQuiverStack(index).getStackSize() + mouseItem.getStackSize() 
							<= player.inventory.getQuiverStack(index).getMaxStackSize())
					{
						player.inventory.combineItemStacksInQuiverSlot(world, player, mouseItem, index);
						mouseItem = null;
					}
					else
					{
						mouseItem.removeFromStack((player.inventory.getQuiverStack(index).getMaxStackSize() - 
								player.inventory.getQuiverStack(index).getStackSize()));
						player.inventory.adjustQuiverStackSize(index, player.inventory.getQuiverStack(index).getMaxStackSize());
					}
				}
				
				
				else //If there is an item there, swap that slot's item and the mouse's item.
				{
					ItemStack stack = player.inventory.getQuiverStack(index);
					player.inventory.setQuiverStack(mouseItem, index);
					mouseItem = stack;
				}
			}
			else //If something's added to no inventory, then obviously something's wrong.
			{
				throw new RuntimeException("Tried to place item into inventory " + whichInventory + " but failed");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Renders the crafting 'wheel' that lists all possible recipes when near the appropriate furniture.
	 * 2 small images on either side that move it and one central image which can be crafted
	 */
	protected static void renderScrollableCraftingRecipeWheel(World world, EntityPlayer player)
	{
		actionbarSlot.bind();
		GL11.glColor4f(1, 1, 1, 0.6f);
		int size;
		int x;
		int y;
		int xoff = (int) (Display.getWidth() * 0.25f) - 62 + getCameraX();
		int yoff = 10 + getCameraY();
		
		//Panes of scrolling bar:
		if(player.selectedRecipe >= 2 && player.getAllPossibleRecipes().length > 0) //Top slot, if applicable
		{
			size = 20; 
			x = xoff;
			y = yoff;
			t.startDrawingQuads();  
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();		
		}
		if(player.selectedRecipe >= 1 && player.getAllPossibleRecipes().length > 0) //Mid-Top slot, if applicable (pane)
		{
			size = 20; 
			x = xoff + 24;
			y = yoff;
			t.startDrawingQuads();  
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();		
		}
		if(player.getAllPossibleRecipes().length != 0) //Currently selected recipe (pane)
		{
			size = 24; 
			x = xoff + 49;
			y = yoff - 2; 
			t.startDrawingQuads();  
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();		

			GL11.glColor4f(1.0f, 0.466f, 0.466f, 0.6f);	
			t.startDrawingQuads();  
			for(int i = 0; i < player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe().length; i++) //Panes for the recipe ingredients
			{
				double newSize = 20;
				double newX = x + 2 + ((newSize + 2) * i);
				double newY = y + size + 2;
				t.addVertexWithUV(newX, newY + newSize, 0, 0, 1);
				t.addVertexWithUV(newX + newSize, newY + newSize, 0, 1, 1);
				t.addVertexWithUV(newX + newSize, newY, 0, 1, 0);
				t.addVertexWithUV(newX, newY, 0, 0, 0);
			}
			t.draw();		
			GL11.glColor4f(1, 1, 1, 0.6f);
		}
		if(player.selectedRecipe + 1 < player.getAllPossibleRecipes().length) //Mid-Bottom slot, if applicable (pane)
		{
			size = 20; //Mid Bottom
			x = xoff + 76;
			y = yoff;
			t.startDrawingQuads();  
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();		
		}
		if(player.selectedRecipe + 2 < player.getAllPossibleRecipes().length) //Bottom slot, if applicable (pane)
		{	
			size = 20; 
			x = xoff + 100;
			y = yoff;
			t.startDrawingQuads();  
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();		
		}
		
		//Images of the actual resulting item from the recipe:
		yoff += 4;
		GL11.glColor4f(1, 1, 1, 1);
		if(player.selectedRecipe >= 2 && player.getAllPossibleRecipes().length > 0) //Left slot, if applicable (result image)
		{
			x = xoff + 4;
			y = yoff;
			size = 12; //Top
			textures[player.getAllPossibleRecipes()[player.selectedRecipe - 2].getResult().getItemID()].bind();
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(player.getAllPossibleRecipes()[player.selectedRecipe - 2].getResult().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 17, ""+player.getAllPossibleRecipes()[player.selectedRecipe - 2].getResult().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		if(player.selectedRecipe >= 1 && player.getAllPossibleRecipes().length > 0) //Left-middle slot, if applicable (result image)
		{
			x = xoff + 28;
			y = yoff;
			size = 12;
			textures[player.getAllPossibleRecipes()[player.selectedRecipe - 1].getResult().getItemID()].bind();
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(player.getAllPossibleRecipes()[player.selectedRecipe - 1].getResult().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 17, ""+player.getAllPossibleRecipes()[player.selectedRecipe - 1].getResult().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		if(player.getAllPossibleRecipes().length != 0) //Currently selected recipe (result image)
		{
			x = xoff + 55;
			y = yoff - 2;
			size = 16;
			textures[player.getAllPossibleRecipes()[player.selectedRecipe].getResult().getItemID()].bind();
			t.startDrawingQuads();
			t.addVertexWithUV(x - 2, y + size, 0, 0, 1);
			t.addVertexWithUV(x - 2 + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x - 2 + size, y, 0, 1, 0);
			t.addVertexWithUV(x - 2, y, 0, 0, 0);
			t.draw();
			
			if(player.getAllPossibleRecipes()[player.selectedRecipe].getResult().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 3, y + 19, ""+player.getAllPossibleRecipes()[player.selectedRecipe].getResult().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
			for(int i = 0; i < player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe().length; i++) //Images of the ingredients for the selected recipe
			{
				double newSize = 12;
				float newX = x + ((size + 5) * i);
				float newY = y + size + 10;
				textures[player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe()[i].getItemID()].bind();
				t.startDrawingQuads();
				t.addVertexWithUV(newX, newY + newSize, 0, 0, 1);
				t.addVertexWithUV(newX + newSize, newY + newSize, 0, 1, 1);
				t.addVertexWithUV(newX + newSize, newY, 0, 1, 0);
				t.addVertexWithUV(newX, newY, 0, 0, 0);
				t.draw();

				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(newX - 2, newY + 17, new StringBuilder().append(player.getAllPossibleRecipes()[player.selectedRecipe].getRecipe()[i].getStackSize()).toString(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		if(player.selectedRecipe + 1 < player.getAllPossibleRecipes().length) //Mid-Right slot, if applicable (result image)
		{
			size = 12;
			x = xoff + 80;
			y = yoff;
			textures[player.getAllPossibleRecipes()[player.selectedRecipe + 1].getResult().getItemID()].bind();
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(player.getAllPossibleRecipes()[player.selectedRecipe + 1].getResult().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 17, ""+player.getAllPossibleRecipes()[player.selectedRecipe + 1].getResult().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		if(player.selectedRecipe + 2 < player.getAllPossibleRecipes().length) //Right slot, if applicable (result image)
		{
			size = 12;
			x = xoff + 104;
			y = yoff;
			t.startDrawingQuads();
			textures[player.getAllPossibleRecipes()[player.selectedRecipe + 2].getResult().getItemID()].bind();
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(player.getAllPossibleRecipes()[player.selectedRecipe + 2].getResult().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 17, ""+player.getAllPossibleRecipes()[player.selectedRecipe + 2].getResult().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}		
		}
	}	
}

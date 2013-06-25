package render;
import hardware.Keys;
import items.Item;
import items.ItemAmmo;
import items.ItemArmor;
import items.ItemArmorAccessory;
import items.ItemArmorBelt;
import items.ItemArmorBody;
import items.ItemArmorBoots;
import items.ItemArmorGloves;
import items.ItemArmorHelmet;
import items.ItemArmorPants;
import items.ItemGem;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import spells.Spell;
import statuseffects.StatusEffect;
import utils.ActionbarItem;
import utils.InventoryPlayer;
import utils.ItemStack;
import utils.MathHelper;
import utils.MetaDataHelper;
import utils.Recipe;
import world.World;
import blocks.Block;
import blocks.BlockChest;
import client.Settings;
import client.TerraeRasa;
import entities.EntityItemStack;
import entities.EntityPlayer;
import enums.EnumColor;
import enums.EnumItemQuality;

/**
 * Defines the class responsible for rendering the in-game user interface. This class 
 * defines a method to draw the user interface based on whether the player's inventory is
 * open, and a method to update the mouse.
 * <br><br>
 * Rendering is done based off the state of the player's inventory. If the player's 
 * inventory isn't open, the following are rendered:
 * <ul>
 *  <li>hearts 
 *  <li>mana crystals
 *  <li>The actionbar 
 * </ul>
 * Otherwise if the inventory is open, the following are rendered:
 * <ul>
 *  <li>hearts
 *  <li>mana crystals
 *  <li>the mainInventory
 *  <li>the garbage slot
 *  <li>the recipe scroller
 *  <li>and chest(s) if applicable
 * </ul>
 * The only public methods exposed are: 
 * <br>
<pre>
	{@link #render(World)}
	
	{@link #updateMouse(World)}
</pre>
 * 
 * <br>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */

public class RenderUI extends Render
{
	private TrueTypeFont boldTooltip = new TrueTypeFont(((new Font("times", Font.BOLD, 24)).deriveFont(16.0f)), true);
	private TrueTypeFont plainTooltip = new TrueTypeFont(((new Font("times", Font.PLAIN, 24)).deriveFont(16.0f)), true);
	private boolean isSocketWindowOpen;
	private boolean socketItemEquipped;
	private int socketItemIndex;
	private ItemStack socketedItem;
	
	//Variables for the item picked up by the mouse:
	private boolean shouldDropItem;
	private int mouseItemSize; //How big it is
	private ItemStack mouseItem; //What it is
	private int mouseXOffset; //How far it should be adjusted to avoid looking bad
	private int mouseYOffset;	
		
	/**
	 * Constructs a new instance of RenderUI. The constructor is only required to initialize the 'Save And Quit'
	 * button, and mouseItem information currently
	 */
	public RenderUI()
	{
		shouldDropItem = false;
		mouseItemSize = 0; 
		mouseItem = null; 
		mouseXOffset = 0; 
		mouseYOffset = 0;		
		//saveAndQuit = new GuiResizableTextUncentered("Save And Quit", 0.4f, 0.45f, 0, 0, ALIGN.H_ALIGN_RIGHT, ALIGN.V_ALIGN_TOP);
	}
	
	/**
	 * Renders the user interface. 
	 * If the player's inventory isn't open, Renders:
	 * <li>hearts 
	 * <li>mana crystals
	 * <li>The actionbar 
	 * <br><br>
	 * Otherwise if the inventory is open, Renders:
	 * <li>hearts
	 * <li>mana crystals
	 * <li>the mainInventory
	 * <li>the garbage slot
	 * <li>the recipe scroller
	 */
	public void render(World world, EntityPlayer player, Settings settings)
	{		
		GL11.glEnable(GL11.GL_BLEND);
		
		renderHeartsAndMana(world, player); //The hearts and mana	
		if(player.isInventoryOpen)
		{
			if(player.isViewingChest)
			{
				isSocketWindowOpen = false;
			}
			
			renderInventory(world, player); //The full inventory if it's open
			if(player.isViewingChest) //Chest(s) if they're being viewed
			{ 
				renderChest(world, player);
			}			
			if(!settings.menuOpen)
			{
				mouse(world, player);
				if(isSocketWindowOpen)
				{
					renderSocketsMenu(world, player);
				}
				attemptToRenderItemTooltip(world, player);
			}
		}	
		else
		{
			closeSocketWindow();
			renderActionBar(world, player);	//The actionbar if the inventory is closed
		}		
		renderStatusEffects(player);		
		renderMouseItem(); //The item held by the mouse, if there is one
		renderText(world, player); //Health and the 'Save And Quit' button		
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		GL11.glDisable(GL11.GL_BLEND);		
	}

	/**
	 * Closes the socket window if needed.
	 */
	public void closeSocketWindow()
	{
		isSocketWindowOpen = false;
	}
	
	/**
	 * Updates all mouse events on call. Including: chests, the mainInventory, the garbage, 
	 * and the recipe scroller 
	 */
	private void mouse(World world, EntityPlayer player)
	{
		mouseEventLeftClick(world, player);	
		mouseEventRightClick(world, player);
	}
	
	/**
	 * Renders the sockets menu for a given item.
	 * @param world
	 * @param player
	 */
	private void renderSocketsMenu(World world, EntityPlayer player)
	{
		int size = 20;
		int tooltipWidth = 8 * 20;
		int tooltipHeight = 5 * 20;
		int frameX = (int) (Display.getWidth() * 0.25) - (4 * size); 
		int frameY = (int) (Display.getHeight() * 0.5) - (4 * size) - 26; 
		
		ItemStack stack = socketedItem;
		EnumItemQuality quality = EnumItemQuality.COMMON;			
		String itemName = socketedItem.getRenderedName();
		String[] stats = { };        
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			quality = (Item.itemsList[stack.getItemID()]).itemQuality;
			stats = Item.itemsList[stack.getItemID()].getStats();
		}
		
		//% of total text size to render, in this case sacale to 1/2 size.
		float xScale = 0.5F;
		//Arbitrary padding to make things look nicer
		final int PADDING = 5;		
		
		//Find out how long does the tooltip actually has to be
		double requiredHeight = boldTooltip.getHeight(itemName) + 				
				(boldTooltip.getHeight(itemName) * 0.75 * stats.length) 
				+ 30;
		
		tooltipHeight = (int) requiredHeight;
		frameX += getCameraX();
		frameY += getCameraY();		
		frameY -= tooltipHeight;
		
		tooltipBackground.bind();			
		GL11.glColor4f(1, 1, 1, 0.8f); 
		t.startDrawingQuads();
		t.addVertexWithUV(frameX, frameY + tooltipHeight, 0, 0, 1);
		t.addVertexWithUV(frameX + tooltipWidth, frameY + tooltipHeight, 0, 1, 1);
		t.addVertexWithUV(frameX + tooltipWidth, frameY, 0, 1, 0);
		t.addVertexWithUV(frameX, frameY, 0, 0, 0);
		t.draw();
		
		GL11.glColor4d(quality.r, quality.g, quality.b, 1.0);
		
		//Title
		float yOffset = frameY + boldTooltip.getHeight(itemName);
		boldTooltip.drawString(frameX + PADDING, yOffset, itemName, xScale , -1, TrueTypeFont.ALIGN_LEFT); 
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		//Stats
		for(int i = 0; i < stats.length; i++)
		{
			boldTooltip.drawString(frameX + PADDING, 
					yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
					stats[i],
					xScale,
					-1, 
					TrueTypeFont.ALIGN_LEFT); 
		}			
		
		yOffset = yOffset + PADDING * (stats.length) + 
				(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
		
		int numberOfSockets = stack.getGemSockets().length;
		
		icons[8].bind(); //Gem Socket Icon
		GL11.glColor4f(1, 1, 1, 1); 
		for(int i = 0; i < numberOfSockets; i++)
		{
			size = 30;			
			double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
			double xoff = offsetByTotal + (i * (size + 7.5));
			int yoff = (int) yOffset;			
			t.startDrawingQuads();
			t.addVertexWithUV(frameX + xoff, yoff + size, 0, 0, 1);
			t.addVertexWithUV(frameX + xoff + size, yoff + size, 0, 1, 1);
			t.addVertexWithUV(frameX + xoff + size, yoff, 0, 1, 0);
			t.addVertexWithUV(frameX + xoff, yoff, 0, 0, 0);
			t.draw();
		}
		
		for(int i = 0; i < numberOfSockets; i++)
		{
			if(stack.getGemSockets()[i].getGem() != null)
			{
				size = 24;				
				double offsetByTotal = (numberOfSockets == 1) ? 68 : (numberOfSockets == 2) ? 50.5 : (numberOfSockets == 3) ? 33 : 13;
				double xoff = offsetByTotal + (i * (size + 6 + 7.5));
				int yoff = (int) yOffset + 3;
				textures[stack.getGemSockets()[i].getGem().id].bind();
				t.startDrawingQuads();
				t.addVertexWithUV(frameX + xoff, yoff + size, 0, 0, 1);
				t.addVertexWithUV(frameX + xoff + size, yoff + size, 0, 1, 1);
				t.addVertexWithUV(frameX + xoff + size, yoff, 0, 1, 0);
				t.addVertexWithUV(frameX + xoff, yoff, 0, 0, 0);
				t.draw();
			}
		}	
	}
	
	/**
	 * Does something appropriate if the user right clicks with the inventory open. The functionality of this is limited.
	 * @param world
	 * @param player
	 */
	private void mouseEventRightClick(World world, EntityPlayer player)
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
			boolean[] equipped = { false };
			ItemStack clickedItem = getClickedStack(world, player, x, y, equipped);
			socketItemEquipped = equipped[0];
			if(clickedItem != null && clickedItem.hasSockets())
			{					
				isSocketWindowOpen = true;
				player.clearViewedChest();
				socketedItem = clickedItem;
			}
			else
			{
				closeSocketWindow();
			}
		}
	}
		
	/**
	 * Renders the status effects afflicting or benefiting the player, including their remaining time in seconds.
	 * @param player the player in use currently
	 */
	private void renderStatusEffects(EntityPlayer player)
	{
		//Get the status effects
		List<StatusEffect> statusEffects = player.statusEffects;
		List<StatusEffect> goodEffects = new ArrayList<StatusEffect>();
		List<StatusEffect> badEffects = new ArrayList<StatusEffect>();
		
		//Split into positive and negative
		for(int i = 0; i < statusEffects.size(); i++)
		{
			if(statusEffects.get(i).isBeneficialEffect)
			{
				goodEffects.add(statusEffects.get(i));
			}
			else
			{
				badEffects.add(statusEffects.get(i));				
			}
		}
		
		//Ensure the icons render in the right place and on the screen (top-right)
		final int MAX_STATUS_PER_ROW = 8;
		int size = 16;
		int goodEffectHeight = 2;
		int badEffectHeight = 2 + (size + 2) * (1 + (goodEffects.size() / MAX_STATUS_PER_ROW));

		ICONS.bind();
		t.startDrawingQuads(); 
		t.setColorRGBA(255, 255, 255, 255);
		for(int i = 0; i < goodEffects.size(); i++) //Render good background icons
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = goodEffectHeight + (int) (getCameraY() + ((i / MAX_STATUS_PER_ROW) * (size + 3)));
			t.setColorRGBA(255, 255, 255, 255);
			double tx = (double)goodEffects.get(i).iconX / (double)ICONS_PER_ROW;
			double ty = (double)goodEffects.get(i).iconY / (double)ICONS_PER_COLUMN;
			double tw = tx + (double)ICONS_PER_ROW / (double)ICONS_SHEET_WIDTH;
			double th = ty + (double)ICONS_PER_COLUMN / (double)ICONS_SHEET_HEIGHT;			
			t.addVertexWithUV(x, y + size, 0, tx, th);
			t.addVertexWithUV(x + size, y + size, 0, tw, th);
			t.addVertexWithUV(x + size, y, 0, tw, ty);
			t.addVertexWithUV(x, y, 0, tx, ty);
		}
		
		for(int i = 0; i < badEffects.size(); i++) //Render bad background icons
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = badEffectHeight + (int) (getCameraY() + (i / MAX_STATUS_PER_ROW) * (size + 3));
			t.setColorRGBA(255, 255, 255, 255);
			double tx = (double)badEffects.get(i).iconX / ICONS_PER_ROW;
			double ty = (double)badEffects.get(i).iconY / ICONS_PER_COLUMN;
			double tw = tx + (double)ICONS_PER_ROW / ICONS_SHEET_WIDTH;
			double th = ty + (double)ICONS_PER_COLUMN / ICONS_SHEET_HEIGHT;			
			t.addVertexWithUV(x, y + size, 0, tx, th);
			t.addVertexWithUV(x + size, y + size, 0, tw, th);
			t.addVertexWithUV(x + size, y, 0, tw, ty);
			t.addVertexWithUV(x, y, 0, tx, ty);
		}
		t.draw();
		
		for(int i = 0; i < goodEffects.size(); i++) //Render good time remaining
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = goodEffectHeight + (int) (getCameraY() + ((i / MAX_STATUS_PER_ROW) * (size + 3)));
			
			GL11.glColor4f(0, 1, 0, 1);
			trueTypeFont.drawString(x - 1, 
					y + 17, 
					"" + (goodEffects.get(i).ticksLeft / 20), 
					0.25f, 
					-0.25f);
			GL11.glColor4f(1, 1, 1, 1);
		}
		
		for(int i = 0; i < badEffects.size(); i++) //Render bad time remaining
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = badEffectHeight + (int) (getCameraY() + (i / MAX_STATUS_PER_ROW) * (size + 3));
		
			GL11.glColor4f(0, 1, 0, 1);
			trueTypeFont.drawString(x - 1, 
					y + 17, 
					"" + (badEffects.get(i).ticksLeft / 20), 
					0.25f, 
					-0.25f);
			GL11.glColor4f(1, 1, 1, 1);
		}
		
	}
	
	/**
	 * Checks to see if the mouse is hovering over something that needs to have a tooltip rendered.
	 * Will return null if nothing appropriate is found.
	 * @return an appropriate ItemStack to render, or null if none is found
	 */
	private ItemStack getTooltipStack(World world, EntityPlayer player, int x, int y)
	{		
		//Inventory
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
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
				return player.inventory.getArmorInventoryStack(i);
			}			
		}
		
		size = 20;
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		//Garbage
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) 
		{
			return player.inventory.getTrashStack(0);
		}			
		
		//Middle Recipe Slot:
		size = 24;
		x1 = (int) (Display.getWidth() * 0.25f) - 13;
		y1 = 8; 
	
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.getAllPossibleRecipes().length > 0) //Middle
		{
			return player.getAllPossibleRecipes()[player.selectedRecipe].getResult();
		}
				
		if(player.isViewingChest)
		{
			//Get the initial block the player is viewing
			BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY);
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6), (int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6)); //metadata used by the block of size (x,y)
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
				chest = (BlockChest)(world.getBlock(player.viewedChestX - x1, player.viewedChestY - y1));
			}	
			
			int totalRows = chest.getInventorySize() / 5;
			for(int i = 0; i < chest.getInventorySize(); i++) //for each ItemStack in the chest
			{
				size = 20;
				x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
				y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
				
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					return chest.getItemStack(i);

				}		
			}
		}
		
		//Socketed gems		
		if(isSocketWindowOpen)
		{
			int tooltipHeight = 100;
			int frameX = (int) (Display.getWidth() * 0.25) - 80;  
			int frameY = (int) (Display.getHeight() * 0.5) - 106; 
			
			ItemStack stack = socketedItem;
			String itemName = socketedItem.getItemName();
			String[] stats = { };        
			if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
			{
				stats = Item.itemsList[stack.getItemID()].getStats();
			}
			
			final int PADDING = 5;					
			tooltipHeight = (int) (boldTooltip.getHeight(itemName) + (boldTooltip.getHeight(itemName) * 0.75 * stats.length) + 30);
			
			frameY -= tooltipHeight;			
			
			float yOffset = frameY + boldTooltip.getHeight(itemName) + PADDING * (stats.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
			
			int numberOfSockets = stack.getGemSockets().length;
			
			for(int i = 0; i < numberOfSockets; i++)
			{
				size = 30;
				double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
				double xOffset = frameX + offsetByTotal + (i * (size + 7.5));
				
				if(y > yOffset && y < yOffset + size && x > xOffset && x < xOffset + size)
				{
					if(stack.getGemSockets()[i].getGem() != null)
					{
						return new ItemStack(stack.getGemSockets()[i].getGem());
					}
				}
			}
		}
		
		return null;
	}	
	
	/**
	 * Checks to see if a mouse click is over something in one of the player's inventories.
	 * This will return null if nothing appropriate is found.
	 * @return the ItemStack the player is clicking on, or null if none is selected
	 */
	private ItemStack getClickedStack(World world, EntityPlayer player, int x, int y, boolean[] equipped)
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
			BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY);
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6), (int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6)); //metadata used by the block of size (x,y)
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
				chest = (BlockChest)(world.getBlock(player.viewedChestX - x1, player.viewedChestY - y1));
			}	
			
			int totalRows = chest.getInventorySize() / 5;
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
	
	/**
	 * Attempts to render a tooltip for the selected Item. Renders tooltips for the recipe at the center of 
	 * the slider, armour, anything in the inventory, and anything in a chest. A tooltip includes 
	 * name, stats, and extra information.
	 */
	private void attemptToRenderItemTooltip(World world, EntityPlayer player)
	{
		if(player.isInventoryOpen && mouseItem == null)
		{
			int x = MathHelper.getCorrectMouseXPosition();
			int y = MathHelper.getCorrectMouseYPosition();	

			ItemStack stack = getTooltipStack(world, player, x, y);
			if(stack == null)
			{
				return;
			}

			EnumItemQuality quality;			
			String itemName = stack.getRenderedName();

			String[] stats = { };
			String fulltooltip = ""; 
	        String[] setBonuses;
			Vector<String> bonusesVector = new Vector<String>();
	        String cooldown = "";
	        boolean[] activeSetBonuses = { };
	        
			if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
			{
				quality = (Item.itemsList[stack.getItemID()]).itemQuality;
				fulltooltip = Item.itemsList[stack.getItemID()].extraTooltipInformation;
				stats = Item.itemsList[stack.getItemID()].getStats();
				if(Item.itemsList[stack.getItemID()] instanceof ItemArmor)
				{
					String[] bonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getStringBonuses();
					for(String bonus : bonuses)
					{
						bonusesVector.add(bonus);
					}
					activeSetBonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType().getBonusesActivated(player);
				}
			}
			else if (stack.getItemID() < Item.itemIndex)
			{
				quality = EnumItemQuality.COMMON;
				fulltooltip = Block.blocksList[stack.getItemID()].extraTooltipInformation;
			}
			else 
			{
				quality = EnumItemQuality.COMMON;
				fulltooltip = Spell.spellList[stack.getItemID()].extraTooltipInformation;
			}
			
			String[] bonuses = stack.getStringBonuses();
			for(String bonus : bonuses)
			{
				bonusesVector.add(bonus);
			}
			
			setBonuses = new String[bonusesVector.size()];
			bonusesVector.copyInto(setBonuses);
			
			if(player.isOnCooldown(stack.getItemID()))
			{
				cooldown = "Remaining Cooldown: " + (player.getTicksLeftOnCooldown(stack.getItemID()) / 20);
			}
			
			//Variables to help determine where and how wide the frame/text will be, as well as text colour.
			int tooltipWidth = 160;
			int frameX = MathHelper.getCorrectMouseXPosition() - 25; 
			int frameY = MathHelper.getCorrectMouseYPosition() - 25;
						
			//% of total text size to render, in this case sacale to 1/2 size.
			float xScale = 0.5F;
			//Arbitrary padding to make things look nicer
			final int PADDING = 5;
			
			//Break the tooltip extra information into strings that don't exceed the tooltip's total width
			//And look terrible as a result. The only risk is the tooltip will be too long.			
			String[] words = fulltooltip.split(" ");
			List<String> renderLines = new ArrayList<String>(0);
			String line = "";
			int length = 0;			
			int spaceLength = (int) (xScale * plainTooltip.getWidth(" ") * 0.5F);
			
			if(fulltooltip != "")
			{
				for(int i = 0; i < words.length; i++)
				{
					int width = (int) (0.5F * xScale * plainTooltip.getWidth(words[i]));
					
					if(length + width + spaceLength < tooltipWidth)
					{
						length += width + spaceLength;
						line += words[i] + " ";					
					}
					else
					{
						renderLines.add(line);
						line = words[i] + " ";
						length = width + spaceLength;
					}				
				}
				renderLines.add(line);
			}
			
			//Break the set bonuses extra information into strings that don't exceed the tooltip's total width
			//And look terrible as a result.			
			List<String> setBonusesList = new ArrayList<String>(0);
			if(setBonuses.length > 0)
			{
				line = "Bonuses: " + setBonuses[0]; 
				setBonusesList.add(line);
				for(int i = 1; i < setBonuses.length; i++)
				{
					if(TerraeRasa.getOSName().startsWith("win"))
					{
						line = "                       " + setBonuses[i];
					}
					else
					{
						line = "                " + setBonuses[i];
					}
				
					setBonusesList.add(line);
				}
			}
			
			//If there's just a title, crop the tooltip
			if(setBonusesList.size() == 0 && renderLines.size() == 0 && stats.length == 0 && cooldown.equals(""))
			{
				tooltipWidth = 3 * PADDING + (int) (0.5F * xScale * boldTooltip.getWidth(itemName));
			}
			
			//Ensure the tooltip won't go off the right side of the screen
			if(frameX + tooltipWidth > Display.getWidth() * 0.5)
			{
				frameX = (int) (Display.getWidth() * 0.5 - tooltipWidth - 20);
			}
			//Ensure the tooltip doesnt go off the left side of the screen
			if(frameX < 0)
			{
				frameX = PADDING;
			}
			frameX += getCameraX();
			
			//Find out how long does the tooltip actually has to be
			double requiredHeight = (boldTooltip.getHeight(itemName)) + 
					PADDING * (setBonusesList.size() + stats.length) + 
					((plainTooltip.getHeight(itemName)) * 0.5f * (stats.length)) + 
					((boldTooltip.getHeight(itemName)) * 0.5f * (setBonusesList.size())) + 
					((plainTooltip.getHeight(itemName)) * 0.7f * (renderLines.size())) + 
					((!cooldown.equals("")) ? plainTooltip.getHeight(cooldown) * 0.5f + 5: 0) + 
					((stack.hasSockets()) ? 35 : 0);
			int tooltipHeight = (int) requiredHeight;
			//Make sure the tooltip doesnt go off the bottom
			if(frameY + tooltipHeight > Display.getHeight() * 0.5)
			{
				frameY = (int) (Display.getHeight() * 0.5 - tooltipHeight - 20);
			}
			//Ensure the tooltip doesnt go off the top of the screen
			if(frameY < 0)
			{
				frameY = PADDING;
			}
			frameY += getCameraY();
			
			//Render everything, in the order of:
			//Background, title, stats, and full description.			
			tooltipBackground.bind();			
			//Background
			t.startDrawingQuads();
			GL11.glColor4f(1, 1, 1, 0.8f); //the slots are partially transparent full colour
			t.addVertexWithUV(frameX, frameY + tooltipHeight, 0, 0, 1);
			t.addVertexWithUV(frameX + tooltipWidth, frameY + tooltipHeight, 0, 1, 1);
			t.addVertexWithUV(frameX + tooltipWidth, frameY, 0, 1, 0);
			t.addVertexWithUV(frameX, frameY, 0, 0, 0);
			t.draw();
			
			GL11.glColor4d(quality.r, quality.g, quality.b, 1.0);
			
			//Title
			float yOffset = frameY + boldTooltip.getHeight(itemName);
			boldTooltip.drawString(frameX + PADDING, yOffset, itemName, xScale , -1, TrueTypeFont.ALIGN_LEFT); 
			
			GL11.glColor4d(EnumColor.LIME_GREEN.COLOR[0], EnumColor.LIME_GREEN.COLOR[1], EnumColor.LIME_GREEN.COLOR[2], 1.0);
			
			//Stats
			for(int i = 0; i < stats.length; i++)
			{
				boldTooltip.drawString(frameX + PADDING, 
						yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						stats[i],
						xScale,
						-1, 
						TrueTypeFont.ALIGN_LEFT); 
			}			
			
			GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
			
			//Render the set bonuses
			yOffset = yOffset + PADDING * (stats.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
			
			for(int i = 0; i < setBonusesList.size(); i++)
			{
				if(i < activeSetBonuses.length && !activeSetBonuses[i])
				{
					GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 1.0);
				}
				else
				{
					GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
				}
				
				boldTooltip.drawString(frameX + PADDING, 
						yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						setBonusesList.get(i),
						xScale,
						-1, 
						TrueTypeFont.ALIGN_LEFT); 
			}
			
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
			//Render the text lines which are now broken up to fit in the tooltip
			//and adjust the yoffset so the text won't overlap
			yOffset = yOffset + PADDING * (setBonusesList.size()) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (setBonusesList.size()));
			
			for(int i = 0; i < renderLines.size(); i++)
			{
				plainTooltip.drawString(frameX + PADDING, 
						yOffset + PADDING*(i+ 1) + (((tooltipHeight) - (tooltipHeight - plainTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						renderLines.get(i),
						0.5F,
						-1,
						TrueTypeFont.ALIGN_LEFT); 			
			}
			
			//Render the cooldown remaining if it happens to be applicable.
			//(After adjusting to the proper position)
			yOffset = yOffset + PADDING * (renderLines.size()) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (renderLines.size()));
			
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			plainTooltip.drawString(frameX + PADDING, 
					yOffset + PADDING + (((tooltipHeight) - (tooltipHeight - plainTooltip.getHeight(cooldown))) * 0.5f), 
					cooldown,
					0.5F,
					-1,
					TrueTypeFont.ALIGN_LEFT); 			
		
			if(stack.hasSockets())
			{
				//Sockets 
				int numberOfSockets = stack.getGemSockets().length;
				icons[8].bind(); //Gem Socket Icon
				GL11.glColor4f(1, 1, 1, 1); 
				for(int i = 0; i < numberOfSockets; i++)
				{
					int size = 30;			
					double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
					double xoff = offsetByTotal + (i * (size + 7.5));
					int yoff = (int) yOffset;			
					t.startDrawingQuads();
					t.addVertexWithUV(frameX + xoff, yoff + size, 0, 0, 1);
					t.addVertexWithUV(frameX + xoff + size, yoff + size, 0, 1, 1);
					t.addVertexWithUV(frameX + xoff + size, yoff, 0, 1, 0);
					t.addVertexWithUV(frameX + xoff, yoff, 0, 0, 0);
					t.draw();
				}
				
				for(int i = 0; i < numberOfSockets; i++)
				{
					if(stack.getGemSockets()[i].getGem() != null)
					{
						int size = 24;				
						double offsetByTotal = (numberOfSockets == 1) ? 68 : (numberOfSockets == 2) ? 50.5 : (numberOfSockets == 3) ? 33 : 13;
						double xoff = offsetByTotal + (i * (size + 6 + 7.5));
						int yoff = (int) yOffset + 3;
						textures[stack.getGemSockets()[i].getGem().id].bind();
						t.startDrawingQuads();
						t.addVertexWithUV(frameX + xoff, yoff + size, 0, 0, 1);
						t.addVertexWithUV(frameX + xoff + size, yoff + size, 0, 1, 1);
						t.addVertexWithUV(frameX + xoff + size, yoff, 0, 1, 0);
						t.addVertexWithUV(frameX + xoff, yoff, 0, 0, 0);
						t.draw();
					}
				}
			}
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
	private void pickUpMouseItem(World world, EntityPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		
		if(whichInventory == 1) //Main Inventory
		{
			mouseItem = player.inventory.getMainInventoryStack(index);
			player.inventory.putItemStackInSlot(world, player, null, index);
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
			player.inventory.setQuiverStack(null, index);
		}			
	}
	
	/**
	 * Places an item into any of the standard inventories
	 * @param whichInventory the number value of the inventory the item is to be placed in. Main-1, Armor-2, Trash-3
	 * @param index slot of the selected inventory to place the item.
	 */
	private void placeItemIntoInventory(World world, EntityPlayer player, int whichInventory, int index)
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
	 * Drops the mouseItem into the world
	 */
	private void dropMouseItem(World world, EntityPlayer player)
	{
		if(mouseItem != null)
		{
			world.addItemStackToItemList(new EntityItemStack(player.x, player.y, mouseItem));
			mouseItem = null;
		}
	}
	
	/**
	 * Renders the item the mouse is holding, should there be one.
	 */
	private void renderMouseItem()
	{
		if(mouseItem != null)
		{
			int x = (Math.abs(getCameraX()) + MathHelper.getCorrectMouseXPosition()) - mouseXOffset; 
			int y = (Math.abs(getCameraY()) + MathHelper.getCorrectMouseYPosition()) - mouseYOffset;
			int newsize = mouseItemSize;
			
			if(mouseItem.getItemID() < 2048) //blocks are a slightly different size
			{
				x += 1;
				y += 1;
				newsize -= 2;
			}
			
			textures[mouseItem.getItemID()].bind();
			t.startDrawingQuads();
			t.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(mouseItem.getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 18, "" + mouseItem.getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
	}
	
	/**
	 * Handles Mouse Events for everything in the inventory.
	 */
	private void mouseEventLeftClick(World world, EntityPlayer player)
	{
		//If the mouse isnt down, there's really no reason to run the rest of this function
		if(!Mouse.isButtonDown(0)) 
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
			mouseClickShiftModifier(world, player, x, y);
		}
		else
		{
			mouseClickNoModifier(world, player, x, y);
		}		
	}
	
	/**
	 * Handles mouse input given that no modifier is applied. This will cause the default behaviour, which is to 
	 * pick things up using the mouse, or s.
	 * @param world the world in use
	 * @param player the player in use
	 */
	private void mouseClickNoModifier(World world, EntityPlayer player, int x, int y)
	{
		shouldDropItem = true;
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				if(player.inventory.getMainInventoryStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 1, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					placeItemIntoInventory(world, player, 1, i);
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
				if(player.inventory.getMainInventoryStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 4, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					placeItemIntoInventory(world, player, 4, i);
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
				if(player.inventory.getArmorInventoryStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up
				{
					pickUpMouseItem(world, player, 2, i, x - x1 - 2, y - y1 - 2, 16);
				}
				else if(mouseItem != null) //The mouse has something picked up
				{
					placeItemIntoInventory(world, player, 2, i);
				}
				player.onArmorChange();
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20) //Garbage
		{
			//System.out.println("Garbage");
			if(player.inventory.getTrashStack(0) != null && mouseItem == null)  //The mouse doesn't have something picked up
			{
				pickUpMouseItem(world, player, 3, 0, x - x1, y - y1, 16);
			}
			else if(mouseItem != null) //The mouse has something picked up
			{
				placeItemIntoInventory(world, player, 3, 0);
			}
		}			
		
		//Recipe Slots:
		int xoff = (int) (Display.getWidth() * 0.25f) - 62;
		int yoff = 10;
		size = 20;
			
		x1 = xoff;
		y1 = yoff; 
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.selectedRecipe >= 2) //Left
		{
			shouldDropItem = false;
			adjustSliderPosition(world, player, -1);
		}
		
		x1 = xoff + 24;
		y1 = yoff;
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.selectedRecipe >= 1) //Mid-Left
		{
		
			shouldDropItem = false;
			adjustSliderPosition(world, player, -1);
		}
	
		size = 24; 
		x1 = xoff + 49;
		y1 = yoff - 2; 
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Middle
		{
			
			shouldDropItem = false;
			craftRecipe(world, player, player.selectedRecipe, -2, -2, 16);
		}

		size = 20; 
		x1 = xoff + 76;
		y1 = yoff;
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Mid-right
		{
		
			shouldDropItem = false;
			adjustSliderPosition(world, player, 1);
		}
		
		x1 = xoff + 100;
		y1 = yoff;	
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Right
		{
			shouldDropItem = false;
			adjustSliderPosition(world, player, 1);
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
				
				ItemStack stack = socketedItem;
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
						stack.getGemSockets()[i].socket((ItemGem)Item.itemsList[mouseItem.getItemID()]);
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
			dropMouseItem(world, player);
		}
	}
	

	/**
	 * Handles mouse interaction with the inventory, and other stuff, given that the shift modifier is applied. This will
	 * change the behaviour of certain clicks and cause stuff to be moved around, not picked up, most of the time.
	 * @param world the world in use
	 * @param player the player in use
	 */
	private void mouseClickShiftModifier(World world, EntityPlayer player, int x, int y)
	{
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				//Ensure it's actually an Item
				if (player.inventory.getMainInventoryStack(i) != null && 
				    player.inventory.getMainInventoryStack(i).getItemID() < ActionbarItem.spellIndex)
				{
					Item selectedItem = (Item)(Item.itemsList[player.inventory.getMainInventoryStack(i).getItemID()]);
					if(selectedItem instanceof ItemArmor)
					{
						ItemStack stack = player.inventory.getMainInventoryStack(i);
						player.inventory.removeEntireStackFromInventory(world, player, i);
						stack = player.inventory.pickUpArmorItemStack(world, player, stack);
						player.inventory.putItemStackInSlot(world, player, stack, i);
					}
					else if(selectedItem instanceof ItemAmmo)
					{
						ItemStack stack = player.inventory.getMainInventoryStack(i);
						player.inventory.removeEntireStackFromInventory(world, player, i);
						stack = player.inventory.pickUpQuiverItemStack(world, player, stack);
						player.inventory.putItemStackInSlot(world, player, stack, i);
					}					
				}
			}		
		}
		
		for(int i = 0; i < player.inventory.getQuiverLength(); i++) //Quiver
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (-7 * size));
			int y1 = (int) ((Display.getHeight() * 0.5f) - (i * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.inventory.getQuiverStack(i) != null) 
			{
				//Try to place that stack in the inventory - leaving what's left in the quiver (if any)
				player.inventory.setQuiverStack(player.inventory.pickUpItemStack(world, 
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
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.inventory.getArmorInventoryStack(i) != null) 
			{
				player.inventory.setArmorInventoryStack(player, player.inventory.pickUpItemStack(world, 
							player, 
							player.inventory.getArmorInventoryStack(i)),
						player.inventory.getArmorInventoryStack(i),
						i);
				player.onArmorChange();
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - (6 * 20); 
		int y1 = (int)(Display.getHeight() * 0.5f) - (6 * 20);
		
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20 && player.inventory.getTrashStack(0) != null) //Garbage
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
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.selectedRecipe >= 2) //Left
		{
			adjustSliderPosition(world, player, -1);
		}
		
		x1 = xoff + 24;
		y1 = yoff;
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size && player.selectedRecipe >= 1) //Mid-Left
		{
		
			adjustSliderPosition(world, player, -1);
		}
	
		size = 24; 
		x1 = xoff + 49;
		y1 = yoff - 2; 
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Middle
		{
			
			craftRecipe(world, player, player.selectedRecipe, -2, -2, 16);
		}

		size = 20; 
		x1 = xoff + 76;
		y1 = yoff;
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Mid-right
		{
		
			adjustSliderPosition(world, player, 1);
		}
		
		x1 = xoff + 100;
		y1 = yoff;	
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Right
		{
			adjustSliderPosition(world, player, 1);
		}
		
		if(player.isViewingChest)
		{
			chestMouseEvents(world, player, x, y);
		}
	}
	
	
	/**
	 * Adjusts the currently selected recipe in the slider. Requires a function to prevent bounds errors.
	 */
	private void adjustSliderPosition(World world, EntityPlayer player, int adjustment)
	{
		player.selectedRecipe += adjustment;
		if(player.selectedRecipe < 0) 
		{
			player.selectedRecipe = 0; 
		}
		if(player.selectedRecipe >= player.getAllPossibleRecipes().length)
		{
			player.selectedRecipe = player.getAllPossibleRecipes().length - 1;
		}
	}
	
	
	/**
	 * Crafts the recipe in the selected slot of the recipe slider and sets the mouseItem 
	 * to that item. Removes items from the inventory and flags recipes for recalculation 
	 * as well
	 * @param index what recipe in the possible recipes list to craft
	 * @param xoff how far to offset the rendering of the cursor item (X)
	 * @param yoff how far to offset the rendering of the cursor item (Y)
	 * @param size how big the image is (16)
	 */
	private void craftRecipe(World world, EntityPlayer player, int index, int xoff, int yoff, int size)
	{
		if(player.getAllPossibleRecipes().length <= 0) 
		{
		 	return; //There are no recipes
		}
		
		Recipe whatToCraft = player.getAllPossibleRecipes()[index];
		
		if(mouseItem != null && mouseItem.getItemID() != whatToCraft.getResult().getItemID()) //check if it's possible to craft successfully
		{
			return;
		}
	
		if(mouseItem != null && mouseItem.getItemID() == whatToCraft.getResult().getItemID()) 
		{ //does the mouseItem have the same item being crafted already picked up?
			if(whatToCraft.getResult().getStackSize() + mouseItem.getStackSize() < mouseItem.getMaxStackSize()) //if there's room to pick it up
			{
				for(int i = 0; i < whatToCraft.getRecipe().length; i++) //remove the items from the inventory
				{
					player.inventory.removeItemsFromInventory(world, player, whatToCraft.getRecipe()[i]);
				}
				mouseItem.addToStack(whatToCraft.getResult().getStackSize()); //pick up the item
			}
		}
		else //the mouseitem is null, so pick up a new itemstack
		{
			for(int i = 0; i < whatToCraft.getRecipe().length; i++) //remove items from inventory
			{
				player.inventory.removeItemsFromInventory(world, player, whatToCraft.getRecipe()[i]);
			}
			
			mouseXOffset = xoff;
			mouseYOffset = yoff;
			mouseItemSize = size;
			mouseItem = new ItemStack(whatToCraft.getResult()); //THIS IS VERY IMPORTANT
		}
	}
	
	
	/**
	 * Draws the hearts and mana the player has, size adjusted for damaged life/mana
	 */
	private void renderHeartsAndMana(World world, EntityPlayer player)
	{		
		// --- Start Health bar
		//The background (black part)
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		int x1 = getCameraX() + 10;
		int y1 = getCameraY() + 10;		
		int newX = (int) (100);
		int newY = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
		t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
		t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
		t.addVertexWithUV(x1, y1, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		//"Foreground" of the texture (red part)
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 0, 0, 1);
		x1 = getCameraX() + 10;
		y1 = getCameraY() + 10;		
		newX = (int) (player.health / player.maxHealth * 100);
		newY = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
		t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
		t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
		t.addVertexWithUV(x1, y1, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// --- End Health bar
		
		// -- Start of Mana Bar
		if(player.maxMana > 0)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0, 0, 0, 1);
			x1 = getCameraX() + 10;
			y1 = getCameraY() + 25;		
			newX = (int) (100);
			newY = 11;		
			t.startDrawingQuads();
			t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
			t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
			t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
			t.addVertexWithUV(x1, y1, 0, 0, 0);
			t.draw();			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0, 0, 1, 1);
			x1 = getCameraX() + 10;
			y1 = getCameraY() + 25;		
			newX = (int) (player.mana / player.maxMana * 100);
			newY = 11;		
			t.startDrawingQuads();
			t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
			t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
			t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
			t.addVertexWithUV(x1, y1, 0, 0, 0);
			t.draw();			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		// --- End of Mana Bar
		
		
		//Special Energy
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		x1 = getCameraX() + 10;
		if(player.mana > 0)
			y1 = getCameraY() + 40;		
		else
			y1 = getCameraY() + 25;
		newX = (int) (100);
		newY = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
		t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
		t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
		t.addVertexWithUV(x1, y1, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(141F/255, 230F/255, 99F/255, 1);
		if(player.mana > 0)
			y1 = getCameraY() + 40;		
		else
			y1 = getCameraY() + 25;
		newX = (int) (player.specialEnergy / EntityPlayer.MAX_SPECIAL_ENERGY * 100);
		newY = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x1, y1 + newY, 0, 0, 1);
		t.addVertexWithUV(x1 + newX, y1 + newY, 0, 1, 1);
		t.addVertexWithUV(x1 + newX, y1, 0, 1, 0);
		t.addVertexWithUV(x1, y1, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
	}

	
	/**
	 * Renders the actionbar (only if the inventory is closed)
	 */
	private void renderActionBar(World world, EntityPlayer player) 
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
	private void renderInventory(World world, EntityPlayer player) 
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
	private void populateInventorySlots(World world, EntityPlayer player) 
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
	 * Renders the crafting 'wheel' that lists all possible recipes when near the appropriate furniture.
	 * 2 small images on either side that move it and one central image which can be crafted
	 */
	private void renderScrollableCraftingRecipeWheel(World world, EntityPlayer player)
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
	
	
	/**
	 * Renders all (constanty visible) text
	 */
	private void renderText(World world, EntityPlayer player)
	{
		if(player.isInventoryOpen)
		{
			GL11.glColor4f(1, 1, 1, 1);
			//Health:
			String health = new StringBuilder().append((int)player.health).append(" / ").append(player.maxHealth).toString();
			trueTypeFont.drawString((getCameraX() + 35), (getCameraY() + 22), health, 0.3f, -0.3f);
			
			//specialOffsetY is increased if the mana bar is rendered, because then the special bar is 
			//rendered further down. Otherwise, special energy is rendered directly under the healthbar
			int specialOffsetY = 37; 
			int offset = (player.maxMana < 100) ? 5 : 0;
			if(player.mana > 0) 
			{
				//Mana
				String mana = new StringBuilder().append((int)player.mana).append(" / ").append(player.maxMana).toString();
				trueTypeFont.drawString((getCameraX() + 35 + offset), (getCameraY() + 37), mana, 0.3f, -0.3f);
				specialOffsetY += 15;
			}
			
			//Special
			String special = new StringBuilder().append((int)(player.specialEnergy / EntityPlayer.MAX_SPECIAL_ENERGY * 100)).append("%").toString();
			trueTypeFont.drawString((getCameraX() + 35 + offset), (getCameraY() + specialOffsetY), special, 0.3f, -0.3f);
		}
		
		if(player.isInventoryOpen)
		{
			//Defense:
			int armorOffset = 80;
			int size = 20;
			if(armorOffset + (9 * (size + 1)) > Display.getHeight() * 0.5F)
			{
				armorOffset = (int) ((Display.getHeight() * 0.5F) - ((9 * (size + 1))));
			}
			String defense = new StringBuilder().append("Defense: " + (int)(player.defense)).toString();
			trueTypeFont.drawString(getCameraX() + ((Display.getWidth() * 0.5f) - 15), 
					getCameraY() + armorOffset - 5, 
					defense, 
					0.3f, 
					-0.3f, 
					TrueTypeFont.ALIGN_RIGHT);
		}
	}
	
	
	/**
	 * Picks up an item from the specified chest to the mouse's temporary 'itemstack'. 
	 * This function is by itself not very safe. Don't call it without ensuring that the 
	 * mouseItem ItemStack is null.
	 * @param chest the chest that is have an item taken from it
	 * @param index the slow of the chest that is being removed
	 * @param xOffset how far on the x-axis is the rendered mouse item adjusted
	 * @param yOffset how far on the y-axis is the rendered mouse item adjusted
	 * @param itemSize the size of the mouseItem being rendered
	 */
	private void pickUpMouseItemChest(BlockChest chest, int index, int xOffset, int yOffset, int itemSize)
	{
		shouldDropItem = false;
		mouseItemSize = itemSize;
		mouseXOffset = xOffset;
		mouseYOffset = yOffset;
		mouseItem = chest.getItemStack(index);
		chest.removeItemStack(index);
	}
	
	
	/**
	 * Handles chest mouse events, based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have
	 * @param x the x position of the mouse (not including getCameraX())
	 * @param y the y position of the mouse (not including getCameraY())
	 */
	private void chestMouseEvents(World world, EntityPlayer player, int x, int y)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY).clone();
		
		if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
		{
			//Get the metadata for the block's size
			int[][] metadata = MetaDataHelper.getMetaDataArray((int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6), (int)(world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6)); //metadata used by the block of size (x,y)
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
			chest = (BlockChest)(world.getBlock(player.viewedChestX - x1, player.viewedChestY - y1));
		}	
		
		int totalRows = chest.getInventorySize() / 5;
		for(int i = 0; i < chest.getInventorySize(); i++) //for each ItemStack in the chest
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % totalRows) - (totalRows / 2)) * (size))));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / totalRows)) * (size)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				if(chest.getItemStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up, so this is straightforward
				{
					pickUpMouseItemChest(chest, i, x - x1 - 2, y - y1 - 2, 16);
					shouldDropItem = false;
				}
				else if(mouseItem != null) //The mouse has something picked up, things need swapped
				{
					//Reference safe swap
					ItemStack stack = new ItemStack(mouseItem);
					mouseItem = chest.takeItemStack(i);
					chest.placeItemStack(stack, i);
					shouldDropItem = false;
				}
			}		
				
		}		
	}
	
	
	/**
	 * Renders the selected chest(s), based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have.
	 */
	private void renderChest(World world, EntityPlayer player)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY).clone();
		
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
			chest = (BlockChest)(world.getBlock(player.viewedChestX - x, player.viewedChestY - y));
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
}
package net.dimensia.src;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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
	public void render(World world, EntityLivingPlayer player)
	{		
		GL11.glEnable(GL11.GL_BLEND);
		
		renderHeartsAndMana(world, player); //The hearts and mana
	
		if(player.isInventoryOpen)
		{
			renderInventory(world, player); //The full inventory if it's open
			updateMouse(world, player); 
			if(player.isViewingChest) //Chest(s) if they're being viewed
			{
				renderChest(world, player);
			}
			
			attemptToRenderItemTooltip(world, player);
		}	
		else
		{
			renderActionBar(world, player);	//The actionbar if the inventory is closed
		}
		
		renderMouseItem(); //The item held by the mouse, if there is one
		renderText(world, player); //Health and the 'Save And Quit' button
		
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		GL11.glDisable(GL11.GL_BLEND);		
	}
	
	/**
	 * Updates all mouse events on call. Including: chests, the mainInventory, the garbage, 
	 * and the recipe scroller 
	 */
	public void updateMouse(World world, EntityLivingPlayer player)
	{
		mouseEventInventory(world, player);	
	}
	
	/**
	 * Checks to see if the mouse is hovering over something that needs to have a tooltip rendered.
	 * Will return null if nothing appropriate is found.
	 * @return an appropriate ItemStack to render, or null if none is found
	 */
	private ItemStack getTooltipStack(World world, EntityLivingPlayer player)
	{
		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();	
		
		//Inventory
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size + 3)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size + 3)) - (size + 22f));
			
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				return player.inventory.getMainInventoryStack(i);
			}		
		}
		
		//Armour and Accessories
		for(int i = 0; i < 8; i++) 
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.5f) - (size * 2.5f));
			int y1 = 80 + (i * (size + 1));
		
			if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
			{
				return player.inventory.getArmorInventoryStack(i);
			}			
		}
		
		int x1 = (int)(Display.getWidth() * 0.25f) - 138; 
		int y1 = (int)(Display.getHeight() * 0.5f) - 134;
		
		//Garbage
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20) 
		{
			return player.inventory.getTrashStack(0);
		}			
		
		//Middle Recipe Slot:
		int size = 24;
		x1 = (int) (Display.getWidth() * 0.25f) - 13;
		y1 = 8; 
	
		if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Middle
		{
			return player.getAllPossibleRecipes()[player.selectedRecipe].getResult();
		}
				
		if(player.isViewingChest)
		{
			//Get the initial block the player is viewing
			BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY).clone();
			
			if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
			{
				//Get the metadata for the block's size
				int[][] metadata = MetaDataHelper.getMetaDataArray(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6, world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6); //metadata used by the block of size (x,y)
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
			
			if(!chest.isAttached()) //Single chest (unattached)
			{
				for(int i = 0; i < 20; i++) //for each ItemStack in the chest
				{
					size = 20;
					x1 = (int) ((Display.getWidth() * 0.25f) + (((-2 + (i % 5)) * (size + 3))));
					y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
					
					if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
					{
						return chest.getItemStack(i);

					}		
				}
			}		
			else if(chest.isAttachedLeft()) //Chest with an attachment to the left
			{
				//Selected Chest:
				for(int i = 0; i < 20; i++)
				{			
					size = 20;				
					x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % 5)) * (size + 3))));
					y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
					
					if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
					{
						return chest.getItemStack(i);

					}	
				}
				
				//Chest to the left:			
				BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX - 2, player.viewedChestY));
				
				for(int i = 0; i < 20; i++)
				{
					size = 20;
					x1 = (int) ((Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
					y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
					
					if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
					{
						return chest2.getItemStack(i);

					}	
				}					
			}
			else if(chest.isAttachedRight()) //Chest with attachment to the right
			{
				//Selected Chest:
				for(int i = 0; i < 20; i++)
				{			
					size = 20;
					x1 = (int) ((Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
					y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
					
					if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
					{
						
						return chest.getItemStack(i);

						
					
					}	
				}
				
				//Chest to the right:			
				BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX + 2, player.viewedChestY));
				
				for(int i = 0; i < 20; i++)
				{
					size = 20;
					x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 5) * (size + 3))));
					y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
					
					if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
					{
						return chest2.getItemStack(i);

					}	
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
	private void attemptToRenderItemTooltip(World world, EntityLivingPlayer player)
	{
		if(player.isInventoryOpen && mouseItem == null)
		{
			ItemStack stack = getTooltipStack(world, player);
			if(stack == null)
			{
				return;
			}

			EnumItemQuality quality;			
			String itemName = stack.getItemName();

			String[] stats = { };
			String fulltooltip = ""; 
			//"A weak copper pick, which provides the ability to mine basic low level blocks. This is a long tooltip.";
	        String[] setBonuses = { };
			
			if(stack.getItemID() >= Item.shiftedIndex)
			{
				quality = (Item.itemsList[stack.getItemID()]).itemQuality;
				fulltooltip = Item.itemsList[stack.getItemID()].extraTooltipInformation;
				stats = Item.itemsList[stack.getItemID()].getStats();
				if(Item.itemsList[stack.getItemID()] instanceof ItemArmor)
				{
					setBonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getStringBonuses();
				}
			}
			else 
			{
				quality = EnumItemQuality.COMMON;
				fulltooltip = Block.blocksList[stack.getItemID()].extraTooltipInformation;
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
				line = "Set Bonuses: ";
				length = 0;			
				
				for(int i = 0; i < setBonuses.length; i++)
				{
					int width = (int) (0.5F * xScale * plainTooltip.getWidth(words[i]));
					
					if(length + width + spaceLength < tooltipWidth)
					{
						length += width + spaceLength;
						line += setBonuses[i] + " ";					
					}
					else
					{
						renderLines.add(line);
						line = setBonuses[i] + " ";
						length = width + spaceLength;
					}				
				}
				setBonusesList.add(line);
			}
			
			//If there's just a title, crop the tooltip
			if(setBonusesList.size() == 0 && renderLines.size() == 0 && stats.length == 0)
			{
				tooltipWidth = 3 * PADDING + (int) (0.5F * xScale * boldTooltip.getWidth(itemName));
			}
			
			//Ensure the tooltip won't go off the right side of the screen
			if(frameX + tooltipWidth > Display.getWidth() * 0.5)
			{
				frameX = (int) (Display.getWidth() * 0.5 - tooltipWidth);
			}
			frameX += getCameraX();
			
			//Find out how long does the tooltip actually has to be
			float requiredHeight = (boldTooltip.getHeight(itemName)) + 
					PADDING * (setBonusesList.size() + stats.length) + 
					((plainTooltip.getHeight(itemName)) * 0.5f * (stats.length)) + 
					((boldTooltip.getHeight(itemName)) * 0.5f * (setBonusesList.size()));
			int tooltipHeight = (int) requiredHeight;
			if(frameY + tooltipHeight > Display.getHeight() * 0.5)
			{
				frameY = (int) (Display.getHeight() * 0.5 - tooltipHeight);
			}
			frameY += getCameraY();
			
			//Render everything, in the order of:
			//Background, title, stats, and full description.			
			tooltipBackground.bind();			
			//Background
			t.startDrawingQuads();
	        t.setColorRGBA_F(1, 1, 1, 1);
			t.addVertexWithUV(frameX, frameY + tooltipHeight, 0, 0, 1);
			t.addVertexWithUV(frameX + tooltipWidth, frameY + tooltipHeight, 0, 1, 1);
			t.addVertexWithUV(frameX + tooltipWidth, frameY, 0, 1, 0);
			t.addVertexWithUV(frameX, frameY, 0, 0, 0);
			t.draw();
			
			GL11.glColor4f(quality.r, quality.g, quality.b, 1.0F);
			
			//Title
			//float xOffset = frameX + ((tooltipWidth) * 0.95f) * 0.5f;
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
			
			GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
			
			//Render the set bonuses
			yOffset = yOffset + PADDING * (stats.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
			
			for(int i = 0; i < setBonusesList.size(); i++)
			{
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
	private void pickUpMouseItem(World world, EntityLivingPlayer player, int whichInventory, int index, int xOffset, int yOffset, int itemSize)
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
			player.inventory.setArmorInventoryStack(null, index);
		}
		else if(whichInventory == 3) //Trash
		{
			mouseItem = player.inventory.getTrashStack(index);
			player.inventory.setTrashStack(null, index);
		}
	}
	
	/**
	 * Places an item into any of the standard inventories
	 * @param whichInventory the number value of the inventory the item is to be placed in. Main-1, Armor-2, Trash-3
	 * @param index slot of the selected inventory to place the item.
	 */
	private void placeItemIntoInventory(World world, EntityLivingPlayer player, int whichInventory, int index)
	{
		shouldDropItem = false;
		
		if(whichInventory == 1) //Main Inventory
		{
			if(player.inventory.getMainInventoryStack(index) == null) //There's nothing there, so the mouse doesnt have to pickup something
			{
				player.inventory.putItemStackInSlot(world, player, mouseItem, index);
				mouseItem = null;
			}
			else if(player.inventory.getMainInventoryStack(index).getItemID() == mouseItem.getItemID())
			{
				if(player.inventory.getMainInventoryStack(index).getStackSize() + mouseItem.getStackSize() <= player.inventory.getMainInventoryStack(index).getMaxStackSize())
				{
					player.inventory.combineItemStacksInSlot(world, player, mouseItem, index);
					mouseItem = null;
				}
				else
				{
					mouseItem.removeFromStack((player.inventory.getMainInventoryStack(index).getMaxStackSize() - player.inventory.getMainInventoryStack(index).getStackSize()));
					player.inventory.getMainInventoryStack(index).setStackSize(player.inventory.getMainInventoryStack(index).getMaxStackSize());
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
			Item item = Item.getItemByID(mouseItem.getItemID());			
			//Check if the item is actually valid for the selected slot:
			if(index == 0) //Helmet
			{
				if(!(item != null) || !(item instanceof ItemArmorHelmet))
				{
					return;
				}	
			}
			if(index == 1) //Body
			{
				if(!(item != null) || !(item instanceof ItemArmorBody))
				{
					return;
				}	
			}
			if(index == 2) //Greaves
			{
				if(!(item != null) || !(item instanceof ItemArmorGreaves))
				{
					return;
				}	
			}
			if(index > 2) //Accessory
			{
				if(!(item != null) || !(item instanceof ItemArmorAccessory))
				{
					return;
				}	
			}
			
			if(player.inventory.getArmorInventoryStack(index) == null) //There's nothing there, so the mouse doesnt have to pickup something
			{
				player.inventory.setArmorInventoryStack(mouseItem, index);
				mouseItem = null;
			}
			else //If there is an item there, swap that slot's item and the mouse's item.
			{
				ItemStack stack = player.inventory.getArmorInventoryStack(index);
				player.inventory.setArmorInventoryStack(mouseItem, index);
				mouseItem = stack;
			}
		}
		else if(whichInventory == 3) //Trash
		{
			//The mouse doesnt swap items in this instance, so just place the item there
			player.inventory.setTrashStack(mouseItem, index);
			mouseItem = null;
		}
		else //If something's added to no inventory, then obviously something's wrong.
		{
			throw new RuntimeException("Tried to place item into inventory " + whichInventory + " but failed");
		}
	}
		
	/**
	 * Drops the mouseItem into the world
	 */
	private void dropMouseItem(World world, EntityLivingPlayer player)
	{
		if(mouseItem != null)
		{
			world.addItemStackToItemList(new EntityLivingItemStack(player.x, player.y, mouseItem));
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
	private void mouseEventInventory(World world, EntityLivingPlayer player)
	{
		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();		
		shouldDropItem = true;
				
		//If the mouse isnt down, there's really no reason to run the rest of this function
		if(!Mouse.isButtonDown(0)) 
		{
			return;
		}
		
		try 
		{
			Mouse.destroy(); //Band-aid fix for mouse clicks
			Mouse.create();
		} 
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		for(int i = 0; i < player.inventory.getMainInventoryLength(); i++) //Inventory
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size + 3)));
			int y1 = (int) ((Display.getHeight() * 0.5f) - ((i / 12) * (size + 3)) - (size + 22f));
			
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
		
		for(int i = 0; i < 8; i++) //Armour and Accessories
		{
			int size = 20;
			int x1 = (int) ((Display.getWidth() * 0.5f) - (size * 2.5f));
			int y1 = 80 + (i * (size + 1));
		
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
		
		int x1 = (int)(Display.getWidth() * 0.25f) - 138; 
		int y1 = (int)(Display.getHeight() * 0.5f) - 134;
		
		if(x > x1 && x < x1 + 20 && y > y1 && y < y1 + 20) //Garbage
		{
			System.out.println("Garbage");
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
		int size = 20;
			
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
		
		//if the player didnt click something, drop their mouseitem
		if(shouldDropItem)
		{
			dropMouseItem(world, player);
		}
	}
	
	/**
	 * Adjusts the currently selected recipe in the slider. Requires a function to prevent bounds errors.
	 */
	private void adjustSliderPosition(World world, EntityLivingPlayer player, int adjustment)
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
	private void craftRecipe(World world, EntityLivingPlayer player, int index, int xoff, int yoff, int size)
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
	private void renderHeartsAndMana(World world, EntityLivingPlayer player)
	{		
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
		
		/*
		float heartsFull = (float)player.health / 20;	//How many hearts are full? (partial hearts are important too!)	
		float partialHeartValue = heartsFull - ((int)(player.health / 20));
		player_heart.bind();
		for(int i = 0; i < (player.maxHealth) / 20; i++) //Draw Heart images
		{	
			float newsize = 0;
			float x = 0;
			float y = 0;
			float size = 11;

			if((int)(heartsFull) > i) //Is the heart full?
			{
				newsize = 11; //Largest size
			}
			else
			{
				if(heartsFull > i) //Is the heart partially full? If so scale down the size proportunately
				{
					newsize = (7 + (partialHeartValue * 4)); //Scaled Size
					x += (size - newsize) / 2;
					y += (size - newsize) / 2;
				}
				else //The heart is empty if nothing else matches by this point
				{
					newsize = 7; //Smallest Size
					x += 2;
					y += 2;
				}
			}
			
			if(i < 10) //First Row of health
			{	
				x += (int) (getCameraX() + 3 + (i * (size + 1)));
				y += getCameraY() + 15;
			}
			else //Second row of health
			{
				x += (int) (getCameraX() + 3 + ((i - 10) * (size + 1)));
				y += (int) (getCameraY() + 15 + (size + 1));
			}         
		
			//Draw the heart
			GL11.glColor4f(1, 1, 1, 1); //Default colour for heart image
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();	
			
			//Blend darkness over hearts to show lost health
			if((int)(heartsFull) > i) //Full heart (full colour)
			{
				GL11.glColor4f(1, 1, 1, 1);
			}
			else
			{
				if(heartsFull > i) //Partial heart (partial colour)
				{
					GL11.glColor4f(0, 0, 0, 0.7f * MathHelper.inversedZeroToOneValue(heartsFull - i));
				}
				else //Empty heart (little colour)
				{
					GL11.glColor4f(0, 0, 0, 0.7f);
				}
			}
			
			//Draw the colour blended over the heart, to show lost health
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();	
		}		
				
		float manaFull = (float)player.mana / 20;	
		float partialMana = manaFull - ((int)(player.mana / 20));
		player_mana.bind();
		for(int i = 0; i < (player.maxMana) / 20; i++) //Draw Mana Stars
		{	
			float newsize = 0;
			float x = 0;
			float y = 0;
			int size = 11;

			if((int)(manaFull) > i) //Is the mana star full?
			{
				newsize = 11; //Largest size
			}
			else
			{
				if(manaFull > i) //Is the mana star partial?
				{
					newsize = (7 + (partialMana * 4));
					x += (size - newsize) / 2;
					y += (size - newsize) / 2;
				}
				else //The mana star is empty if nothing else matches by this point
				{
					newsize = 7; //Smallest Size
					x += 2;
					y += 2;
				}
			}
			
			x += getCameraX() + 3 + ((size + 1) * i );
			y += getCameraY() + 28;
			
			if(player.maxHealth > 200)
			{
				y += size;
			}
			
			//Draw the Mana star
			GL11.glColor4f(1, 1, 1, 1); 
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();	
			
			//Blend darkness over mana star to show lost mana
			if((int)(manaFull) > i) //Full mana star (full colour)
			{
				GL11.glColor4f(1, 1, 1, 1);
			}
			else
			{
				if(manaFull > i) //Partial mana star (partial colour)
				{
					GL11.glColor4f(0, 0, 0, 0.7f * MathHelper.inversedZeroToOneValue(manaFull - i));
				}
				else //Empty mana star (little colour)
				{
					GL11.glColor4f(0, 0, 0, 0.7f);
				}
			}
			
			//Draw the darkness blended over the mana heart image
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();	
		}
		GL11.glColor4f(1, 1, 1, 1); //Safety Colour Clear 	
		*/
	}

	/**
	 * Renders the actionbar (only if the inventory is closed)
	 */
	private void renderActionBar(World world, EntityLivingPlayer player) 
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
	private void renderInventory(World world, EntityLivingPlayer player) 
	{
		GL11.glColor4f(1, 1, 1, 0.6f);

		actionbarSlot.bind();
		t.startDrawingQuads();
		for(int i = 0; i < player.inventory.getMainInventory().length; i++) //Inventory Frames
		{
			int size = 20;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size + 3)));
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * (size + 3)) - (size + 22f));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();
		
		GL11.glColor4f(0.603f, 1.0f, 0.466f, 0.6f);
		t.startDrawingQuads();		
		for(int i = 0; i < 8; i++) //Armour and Accessories Frames
		{
			int size = 20;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - (size * 2.5f));
			int y = getCameraY() + 80 + (i * (size + 1));
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
		}
		t.draw();	
		
		t.startDrawingQuads(); //Garbage Slot Frame
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
		int size = 20;
		int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((-6 * (size + 3))));
		int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (4 * (size + 3)) - (size + 22f));
		t.addVertexWithUV(x, y + size, 0, 0, 1);
		t.addVertexWithUV(x + size, y + size, 0, 1, 1);
		t.addVertexWithUV(x + size, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		
		if(player.inventory.getTrashStack(0) == null) //Garbage Slot Image If nothing's there
		{
			player_garbage.bind(); 
			GL11.glColor4f(0.6f, 0.6f, 0.6f, 1);
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
		}
		
		populateInventorySlots(world, player); 
		renderScrollableCraftingRecipeWheel(world, player);
		
		//saveAndQuit.draw();
	}		

	/**
	 * Fills all the inventory frames rendered with items if the slot isnt null
	 */
	private void populateInventorySlots(World world, EntityLivingPlayer player) 
	{
		GL11.glColor4f(1, 1, 1, 1);
		
		for(int i = 0; i < 48; i++) //Main Inventory
		{
			if(player.inventory.getMainInventoryStack(i) == null) 
			{
				continue;
			}
			else
			{
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * (size + 7)) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * (size + 7)) - (size + 24f));
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
			}
		}	
		for(int i = 0; i < 8; i++) //Armor Inventory
		{
			if(player.inventory.getArmorInventoryStack(i) == null) 
			{	
				continue;			
			}
			else
			{
				textures[player.inventory.getArmorInventoryStack(i).getItemID()].bind();
				t.startDrawingQuads();
				int size = 16;
				int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((size + 4) * 2.5f) + 2);
				int y = getCameraY() + 82 + (i * (size + 5));
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
			int size = 16;
			int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((-6 * (size + 7)))) + 2;
			int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - (4 * (size + 7)) - (size + 26)) + 2;
			t.addVertexWithUV(x, y + size, 0, 0, 1);
			t.addVertexWithUV(x + size, y + size, 0, 1, 1);
			t.addVertexWithUV(x + size, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
		}
		
		//Stack Sizes:
		GL11.glColor4f(0, 1, 0, 1);
		for(int i = 0; i < 48; i++) //Main Inventory
		{
			if(player.inventory.getMainInventoryStack(i) == null || player.inventory.getMainInventoryStack(i).getMaxStackSize() == 1) 
			{
				continue;
			}
			else
			{
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 12) - 6) * 23) + 2);
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((i / 12) * 23) - 40);
				trueTypeFont.drawString(x - 2, y + 18, new StringBuilder().append(player.inventory.getMainInventoryStack(i).getStackSize()).toString(), 0.25f, -0.25f);
			}
		}	
		
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	/**
	 * Renders the crafting 'wheel' that lists all possible recipes when near the appropriate furniture.
	 * 2 small images on either side that move it and one central image which can be crafted
	 */
	private void renderScrollableCraftingRecipeWheel(World world, EntityLivingPlayer player)
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
				float newSize = 20;
				float newX = x + 2 + ((newSize + 2) * i);
				float newY = y + size + 2;
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
				float newSize = 12;
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
	private void renderText(World world, EntityLivingPlayer player)
	{
		if(player.isInventoryOpen)
		{
			GL11.glColor4f(1, 1, 1, 1);
			//Health:
			String health = new StringBuilder().append((int)player.health).append(" / ").append(player.maxHealth).toString();
			trueTypeFont.drawString((getCameraX() + 35), (getCameraY() + 22), health, 0.3f, -0.3f);
			
			int offset = (player.maxMana < 100) ? 5 : 0;
			
			String mana = new StringBuilder().append((int)player.mana).append(" / ").append(player.maxMana).toString();
			trueTypeFont.drawString((getCameraX() + 35 + offset), (getCameraY() + 37), mana, 0.3f, -0.3f);
		}
		
		if(player.isInventoryOpen)
		{
			//Defense:
			String defense = new StringBuilder().append("Defense: " + (int)(player.defense)).toString();
			trueTypeFont.drawString(getCameraX() + ((Display.getWidth() * 0.5f) - 15), getCameraY() + 75, defense, 0.3f, -0.3f, TrueTypeFont.ALIGN_RIGHT);
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
	private void chestMouseEvents(World world, EntityLivingPlayer player, int x, int y)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY).clone();
		
		if(chest.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
		{
			//Get the metadata for the block's size
			int[][] metadata = MetaDataHelper.getMetaDataArray(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6, world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6); //metadata used by the block of size (x,y)
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
		
		if(!chest.isAttached()) //Single chest (unattached)
		{
			for(int i = 0; i < 20; i++) //for each ItemStack in the chest
			{
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + (((-2 + (i % 5)) * (size + 3))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				
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
		else if(chest.isAttachedLeft()) //Chest with an attachment to the left
		{
			//Selected Chest:
			for(int i = 0; i < 20; i++)
			{			
				int size = 20;				
				int x1 = (int) ((Display.getWidth() * 0.25f) + ((((i % 5)) * (size + 3))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				
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
			
			//Chest to the left:			
			BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX - 2, player.viewedChestY));
			
			for(int i = 0; i < 20; i++)
			{
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					if(chest2.getItemStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up, so this is straightforward
					{
						pickUpMouseItemChest(chest2, i, x - x1 - 2, y - y1 - 2, 16);
						shouldDropItem = false;
					}
					else if(mouseItem != null) //The mouse has something picked up, things need swapped
					{
						//Reference safe swap
						ItemStack stack = new ItemStack(mouseItem);
						mouseItem = chest2.takeItemStack(i);
						chest2.placeItemStack(stack, i);
						shouldDropItem = false;
					}
				}	
			}					
		}
		else if(chest.isAttachedRight()) //Chest with attachment to the right
		{
			//Selected Chest:
			for(int i = 0; i < 20; i++)
			{			
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				
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
			
			//Chest to the right:			
			BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX + 2, player.viewedChestY));
			
			for(int i = 0; i < 20; i++)
			{
				int size = 20;
				int x1 = (int) ((Display.getWidth() * 0.25f) + (((i % 5) * (size + 3))));
				int y1 = (int) ((Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				
				if(x > x1 && x < x1 + size && y > y1 && y < y1 + size) //Is the click in bounds?
				{
					if(chest2.getItemStack(i) != null && mouseItem == null) //The mouse doesn't have something picked up, so this is straightforward
					{
						pickUpMouseItemChest(chest2, i, x - x1 - 2, y - y1 - 2, 16);
						shouldDropItem = false;
					}
					else if(mouseItem != null) //The mouse has something picked up, things need swapped
					{
						//Reference safe swap
						ItemStack stack = new ItemStack(mouseItem);
						mouseItem = chest2.takeItemStack(i);
						chest2.placeItemStack(stack, i);
						shouldDropItem = false;
					}
				}	
			}				
		}		
	}
	
	/**
	 * Renders the selected chest(s), based on the chest's attachment. This function is relatively long
	 * and tedious, due to there being multiple unique states a chest can have.
	 */
	private void renderChest(World world, EntityLivingPlayer player)
	{
		//Get the initial block the player is viewing
		BlockChest chest = (BlockChest)world.getBlock(player.viewedChestX, player.viewedChestY);
		
		if(chest.metaData != 1)
		{
			//Get the metadata for the block's size
			int[][] metadata = MetaDataHelper.getMetaDataArray(world.getBlock(player.viewedChestX, player.viewedChestY).blockWidth / 6, world.getBlock(player.viewedChestX, player.viewedChestY).blockHeight / 6); //metadata used by the block of size (x,y)
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
		
		if(!chest.isAttached()) //Single chest (unattached)
		{
			t.startDrawingQuads(); 
			GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
			for(int i = 0; i < 20; i++) //Draw all the background slots
			{			
				int size = 20;
				actionbarSlot.bind();
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((-2 + (i % 5)) * (size + 3))));
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
			}
			t.draw();
			
			GL11.glColor4f(1, 1, 1, 1);
			
			for(int i = 0; i < 20; i++) //Draw all the items, in the slots (with text)
			{
				if(chest.getItemStack(i) != null)
				{
					textures[chest.getItemStack(i).getItemID()].bind();
					int size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((-2 + (i % 5)) * (size + 7))) + 2);
					int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 7)) - (size + 22f) - 2);
	
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
		}
		else if(chest.isAttachedLeft())
		{
			t.startDrawingQuads(); 
			GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
			for(int i = 0; i < 20; i++) //Draw all the background slots
			{			
				int size = 20;
				actionbarSlot.bind();
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((((i % 5)) * (size + 3))));
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
			}
			t.draw();
			
			GL11.glColor4f(1, 1, 1, 1);
			
			for(int i = 0; i < 20; i++) //Draw all the items, in the slots (with text)
			{
				if(chest.getItemStack(i) != null)
				{
					textures[chest.getItemStack(i).getItemID()].bind();
					int size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((((i % 5)) * (size + 7))) + 2);
					int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 7)) - (size + 22f) - 2);
	
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
			
			//Chest to the left:
			BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX - 2, player.viewedChestY));
			
			for(int i = 0; i < 20; i++) //Draw all the background slots
			{
				t.startDrawingQuads(); 
				GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
				int size = 20;
				actionbarSlot.bind();
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			}		
			
			GL11.glColor4f(1, 1, 1, 1);
			
			for(int i = 0; i < 20; i++) //Draw all the items, in the slots (with text)
			{
				if(chest2.getItemStack(i) != null)
				{
					textures[chest2.getItemStack(i).getItemID()].bind();
					int size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((-5 + (i % 5)) * (size + 7)) + 2);
					int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 7)) - (size + 22f) - 2);
	
					t.startDrawingQuads();
					t.addVertexWithUV(x, y + size, 0, 0, 1);
					t.addVertexWithUV(x + size, y + size, 0, 1, 1);
					t.addVertexWithUV(x + size, y, 0, 1, 0);
					t.addVertexWithUV(x, y, 0, 0, 0);
					t.draw();
					
					if(chest2.getItemStack(i).getMaxStackSize() > 1) //If maxStackSize > 1, draw the stackSize
					{
						GL11.glColor4f(0, 1, 0, 1);
						trueTypeFont.drawString(x - 2, y + 18, new StringBuilder().append(chest2.getItemStack(i).getStackSize()).toString(), 0.25f, -0.25f);
						GL11.glColor4f(1, 1, 1, 1);
					}	
				}
			}
		}
		else if(chest.isAttachedRight()) //Chest and the chest to the right
		{
			t.startDrawingQuads(); 
			GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
			for(int i = 0; i < 20; i++) //Draw all the background slots
			{			
				int size = 20;
				actionbarSlot.bind();
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 3))));
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
			}
			t.draw();

			GL11.glColor4f(1, 1, 1, 1);
			
			for(int i = 0; i < 20; i++) //Draw all the items, in the slots (with text)
			{
				if(chest.getItemStack(i) != null)
				{				
					textures[chest.getItemStack(i).getItemID()].bind();
					int size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((-5 + (i % 5)) * (size + 7))) + 2);
					int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 7)) - (size + 22f) - 2);
					
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
			
			//Chest to the right:			
			BlockChest chest2 = (BlockChest)(world.getBlock(player.viewedChestX + 2, player.viewedChestY));
			
			for(int i = 0; i < 20; i++) //Draw all the background slots
			{
				t.startDrawingQuads(); 
				GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.6f);
				int size = 20;
				actionbarSlot.bind();
				int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + (((i % 5) * (size + 3))));
				int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 3)) - (size + 22f));
				t.addVertexWithUV(x, y + size, 0, 0, 1);
				t.addVertexWithUV(x + size, y + size, 0, 1, 1);
				t.addVertexWithUV(x + size, y, 0, 1, 0);
				t.addVertexWithUV(x, y, 0, 0, 0);
				t.draw();
			}		
			
			GL11.glColor4f(1, 1, 1, 1);
			
			for(int i = 0; i < 20; i++) //Draw all the items, in the slots (with text)
			{
				if(chest2.getItemStack(i) != null)
				{
					textures[chest2.getItemStack(i).getItemID()].bind();
					int size = 16;
					int x = (int) (getCameraX() + (Display.getWidth() * 0.25f) + ((i % 5) * (size + 7)) + 2);
					int y = (int) (getCameraY() + (Display.getHeight() * 0.5f) - ((4 + (i / 5)) * (size + 7)) - (size + 22f) - 2);
	
					t.startDrawingQuads();
					t.addVertexWithUV(x, y + size, 0, 0, 1);
					t.addVertexWithUV(x + size, y + size, 0, 1, 1);
					t.addVertexWithUV(x + size, y, 0, 1, 0);
					t.addVertexWithUV(x, y, 0, 0, 0);
					t.draw();
					
					if(chest2.getItemStack(i).getMaxStackSize() > 1) //If maxStackSize > 1, draw the stackSize
					{
						GL11.glColor4f(0, 1, 0, 1);
						trueTypeFont.drawString(x - 2, y + 18, new StringBuilder().append(chest2.getItemStack(i).getStackSize()).toString(), 0.25f, -0.25f);
						GL11.glColor4f(1, 1, 1, 1);
					}	
				}
			}
		}
		GL11.glColor4f(1, 1, 1, 1);
	}
}
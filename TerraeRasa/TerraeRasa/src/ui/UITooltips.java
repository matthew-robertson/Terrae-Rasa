package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import items.Item;
import items.ItemArmor;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import render.TrueTypeFont;
import spells.Spell;
import client.TerraeRasa;

import utils.ActionbarItem;
import utils.ItemStack;
import utils.MathHelper;
import utils.MetaDataHelper;
import world.World;
import blocks.Block;
import blocks.BlockChest;
import entities.EntityPlayer;
import enums.EnumColor;
import enums.EnumItemQuality;

public class UITooltips extends UIBase
{
	/**
	 * Checks to see if the mouse is hovering over something that needs to have a tooltip rendered.
	 * Will return null if nothing appropriate is found.
	 * @return an appropriate ItemStack to render, or null if none is found
	 */
	protected static ItemStack getTooltipStack(World world, EntityPlayer player, int x, int y)
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
			BlockChest chest = (BlockChest)world.getBlockGenerate(player.viewedChestX, player.viewedChestY);
			
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
				chest = (BlockChest)(world.getBlockGenerate(player.viewedChestX - x1, player.viewedChestY - y1));
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
	 * Attempts to render a tooltip for the selected Item. Renders tooltips for the recipe at the center of 
	 * the slider, armour, anything in the inventory, and anything in a chest. A tooltip includes 
	 * name, stats, and extra information.
	 */
	protected static void attemptToRenderItemTooltip(World world, EntityPlayer player)
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
	        String[] PassiveBonuses;
			Vector<String> bonusesVector = new Vector<String>();
	        String cooldown = "";
	        boolean[] activePassiveBonuses = { };
	        
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
					activePassiveBonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType().getBonusesActivated(player);
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
			
			PassiveBonuses = new String[bonusesVector.size()];
			bonusesVector.copyInto(PassiveBonuses);
			
			if(player.isOnCooldown(stack.getItemID()))
			{
				cooldown = "Remaining Cooldown: " + (player.getTicksLeftOnCooldown(stack.getItemID()) / 20);
			}
			
			//Variables to help determine where and how wide the frame/text will be, as well as text colour.
			int tooltipWidth = 185;
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
			List<String> PassiveBonusesList = new ArrayList<String>(0);
			if(PassiveBonuses.length > 0)
			{
				line = "Bonuses: " + PassiveBonuses[0]; 
				PassiveBonusesList.add(line);
				for(int i = 1; i < PassiveBonuses.length; i++)
				{
					if(TerraeRasa.getOSName().startsWith("win"))
					{
						line = "                       " + PassiveBonuses[i];
					}
					else
					{
						line = "                " + PassiveBonuses[i];
					}
				
					PassiveBonusesList.add(line);
				}
			}
			
			//If there's just a title, crop the tooltip
			if(PassiveBonusesList.size() == 0 && renderLines.size() == 0 && stats.length == 0 && cooldown.equals(""))
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
					PADDING * (PassiveBonusesList.size() + stats.length) + 
					((plainTooltip.getHeight(itemName)) * 0.5f * (stats.length)) + 
					((boldTooltip.getHeight(itemName)) * 0.5f * (PassiveBonusesList.size())) + 
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
			
			for(int i = 0; i < PassiveBonusesList.size(); i++)
			{
				if(i < activePassiveBonuses.length && !activePassiveBonuses[i])
				{
					GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 1.0);
				}
				else
				{
					GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
				}
				
				boldTooltip.drawString(frameX + PADDING, 
						yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						PassiveBonusesList.get(i),
						xScale,
						-1, 
						TrueTypeFont.ALIGN_LEFT); 
			}
			
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
			//Render the text lines which are now broken up to fit in the tooltip
			//and adjust the yoffset so the text won't overlap
			yOffset = yOffset + PADDING * (PassiveBonusesList.size()) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (PassiveBonusesList.size()));
			
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
}

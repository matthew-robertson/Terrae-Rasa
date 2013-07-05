package ui;

import items.Item;
import items.ItemArmor;

import java.awt.Font;
import java.util.Vector;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import passivebonuses.PassiveBonus;

import render.TrueTypeFont;
import spells.Spell;
import statuseffects.StatusEffect;
import utils.ActionbarItem;
import utils.ItemStack;
import utils.MathHelper;
import world.World;
import blocks.Block;
import client.TerraeRasa;
import entities.EntityPlayer;
import enums.EnumColor;
import enums.EnumItemQuality;

public class UITooltips extends UIBase
{
	//protected final static TrueTypeFont plainTooltip = new TrueTypeFont(((new Font("times", Font.PLAIN, 48)).deriveFont(48.0f)), true);
	protected final static TrueTypeFont tooltipFont = new TrueTypeFont(((new Font("times", Font.PLAIN, 36)).deriveFont(36.0f)), true);
	//plainTooltip.drawString(getCameraX() + 50, getCameraY() + 50, "ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0.16F, -0.16F);
	//plainTooltip.drawString(getCameraX() + 50, getCameraY() + 50, "ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0.22F, -0.22F);
	
	/**
	 * Checks to see if the mouse is hovering over something that needs to have a tooltip rendered.
	 * Will return null if nothing appropriate is found.
	 * @return an appropriate ItemStack to render, or null if none is found
	 */
	private static ItemStack getTooltipStack(World world, EntityPlayer player, int mouseX, int mouseY)
	{	
		int index = -1;
		//Inventory
		if((index = UIInventory.getMouseoverSlotInventory(player, mouseX, mouseY)) != -1)
		{
			return player.inventory.getMainInventoryStack(index);
		}		
		//Quiver
		if((index = UIInventory.getMouseoverSlotQuiver(player, mouseX, mouseY)) != -1)
		{
			return player.inventory.getQuiverStack(index);
		}		
		//Armour and Accessories
		if((index = UIInventory.getMouseoverSlotArmor(player, mouseX, mouseY)) != -1)
		{
			return player.inventory.getArmorInventoryStack(index);			
		}		
		//Garbage
		if((index = UIInventory.getMouseoverSlotTrash(player, mouseX, mouseY)) != -1)
		{
			return player.inventory.getTrashStack(index);
		}
		
		ItemStack stack = null;
		//Recipe slider stuff
		if((stack = UIInventory.getMouseoverRecipeSliderStack(player, mouseX, mouseY)) != null)
		{
			return stack;
		}		
		//Chest stuff
		if((stack = UIInventory.getMouseoverChestStack(world, player, mouseX, mouseY)) != null)
		{
			return stack;
		}		
		//Socketed gems
		if((stack = UISocketMenu.getMouseoverSocketedGems(mouseX, mouseY)) != null)
		{
			return stack;
		}		
		return null;
	}	
	
	/**
	 * Gets the quality of the ItemStack the mouse is hovering over. 
	 * @param stack the ItemStack being hovered over
	 * @return the quality of the ItemStack being hovered over
	 */
	private static EnumItemQuality getQuality(ItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			return (Item.itemsList[stack.getItemID()]).itemQuality;
		}
		return EnumItemQuality.COMMON;
	}

	/**
	 * Gets the fulltooltip of the ItemStack the mouse is hovering over. 
	 * @param stack the ItemStack being hovered over
	 * @return the fulltooltip of the ItemStack being hovered over
	 */
	private static String getFullTooltip(ItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			return Item.itemsList[stack.getItemID()].extraTooltipInformation;
		}
		else if (stack.getItemID() < Item.itemIndex)
		{
			return Block.blocksList[stack.getItemID()].extraTooltipInformation;
		}
		else 
		{
			return Spell.spellList[stack.getItemID()].extraTooltipInformation;
		}
	}
	
	/**
	 * Gets the stats of the ItemStack the mouse is hovering over. 
	 * @param stack the ItemStack being hovered over
	 * @return the stats of the ItemStack being hovered over
	 */
	private static String[] getStats(ItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			return Item.itemsList[stack.getItemID()].getStats();
		}
		return new String[] { };
	}
	
	/**
	 * Gets the passive bonuses of the ItemStack the mouse is hovering over. 
	 * @param stack the ItemStack being hovered over
	 * @return the passive bonuses of the ItemStack being hovered over
	 */
	private static String[] getBonuses(ItemStack stack)
	{
		Vector<String> bonusesVector = new Vector<String>();
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			String[] bonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getStringBonuses();
			for(String bonus : bonuses)
			{
				bonusesVector.add(bonus);
			}
		}
		
		String[] bonuses = stack.getStringBonuses();
		for(String bonus : bonuses)
		{
			bonusesVector.add(bonus);
		}
		
		String[] passiveBonuses = new String[bonusesVector.size()];
		bonusesVector.copyInto(passiveBonuses);
		return passiveBonuses;
	}

	/**
	 * Gets the active set bonuses of the ItemStack the mouse is currently hovering over. This must be a piece of armour
	 * @param player the player to whom this UI belongs
	 * @param stack the ItemStack being hovered over
	 * @return the active set bonuses of the ItemStack being hovered over
	 */
	private static boolean[] getActiveBonuses(EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			return ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType().getBonusesActivated(player);
		}		
		return new boolean[] { };
	}
	
	/**
	 * Gets the cooldown of the ItemStack the mouse is currently hovering over. This may not be applicable, in which case a blank string is yielded.
	 * @param player the player to whom this UI belongs
	 * @param stack the ItemStack being hovered over
	 * @return the cooldown of the ItemStack being hovered over if applicable
	 */
	private static String getCooldown(EntityPlayer player, ItemStack stack)
	{
		return (player.isOnCooldown(stack.getItemID())) ? "Remaining Cooldown: " + (player.getTicksLeftOnCooldown(stack.getItemID()) / 20) : "";
	}
	
	/**
	 * Splits up the fulltooltip of an item into something that fits in the tooltip's width.
	 * @param fulltooltip the fulltooltip to split up
	 * @param xScale the scale of the text in the tooltip
	 * @param tooltipWidth the width of the tooltip
	 * @return a String[] of the fulltooltip, split to fit within the given width
	 */
	private static String[] splitFulltooltip(String fulltooltip, float xScale, int tooltipWidth)
	{
		String[] words = fulltooltip.split(" ");
		Vector<String> renderLines = new Vector<String>();
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
		String[] temp = new String[renderLines.size()];
		renderLines.copyInto(temp);
		return temp;
	}
	
	/**
	 * Formats the passive bonuses string array to be more suitable for the tooltip.
	 * @param passiveBonuses the passive bonuses to format
	 * @return a better formatted passiveBonuses array
	 */
	private static String[] formatPassiveBonuses(String[] passiveBonuses)
	{
		Vector<String> passiveBonusesVector = new Vector<String>();
		if(passiveBonuses.length > 0)
		{
			String line = "Bonuses: " + passiveBonuses[0]; 
			passiveBonusesVector.add(line);
			for(int i = 1; i < passiveBonuses.length; i++)
			{
				if(TerraeRasa.getOSName().startsWith("win"))
				{
					line = "                       " + passiveBonuses[i];
				}
				else
				{
					line = "                " + passiveBonuses[i];
				}
			
				passiveBonusesVector.add(line);
			}
		}
		String[] temp = new String[passiveBonusesVector.size()];
		passiveBonusesVector.copyInto(temp);
		return temp;
	}
	
	private static String getTotalArmor(EntityPlayer player, ItemStack stack)
	{
		Vector<PassiveBonus> bonusesVector = new Vector<PassiveBonus>();
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			for(PassiveBonus bonus : ((ItemArmor)(Item.itemsList[stack.getItemID()])).getBonuses())
			{
				bonusesVector.add(bonus);
			}
		}
		
		for(PassiveBonus bonus : stack.getBonuses())
		{
			bonusesVector.add(bonus);
		}
		
		int totalArmor = 0; 
		
		//armor = "  " + ((ItemArmor)(Item.itemsList[stack.getItemID()])).getDefense();
		return "";
	}
	
	/**
	 * Attempts to render a tooltip for the selected Item. Renders tooltips for the recipe at the center of 
	 * the slider, armour, anything in the inventory, and anything in a chest. A tooltip includes 
	 * name, stats, and extra information.
	 */
	protected static void renderApplicableTooltip(World world, EntityPlayer player)
	{
		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();	
		
		ItemStack stack = getTooltipStack(world, player, x, y);
		if(stack != null)
		{
			renderItemStackTooltip(player, stack);
		}
		
		StatusEffect effect = UIStatusEffects.getMouseoverStatusEffect(x, y);
		if(effect != null)
		{
			renderStatusEffectTooltip(player, effect);
		}
	}
	
	private static void renderItemStackTooltip(EntityPlayer player, ItemStack stack)
	{
		if(player.isInventoryOpen && mouseItem == null)
		{
			EnumItemQuality quality = getQuality(stack);			
			String[] stats = getStats(stack);
			boolean[] activePassiveBonuses = getActiveBonuses(player, stack);
			String cooldown = getCooldown(player, stack);
	        String itemName = stack.getRenderedName();
			//Variables to help determine where and how wide the frame/text will be, as well as text colour.
			int tooltipWidth = 185;
			int frameX = MathHelper.getCorrectMouseXPosition() - 25; 
			int frameY = MathHelper.getCorrectMouseYPosition() - 25;
			//% of total text size to render, in this case sacale to 1/2 size.
			float xScale = 0.25F;
			//Arbitrary padding to make things look nicer
			final int PADDING = 5;
			//Break the tooltip extra information into strings that don't exceed the tooltip's total width
			String[] tooltipLines = splitFulltooltip(getFullTooltip(stack), xScale, tooltipWidth);
			//Break the set bonuses extra information into strings that don't exceed the tooltip's total width
			String[] passiveBonusLines = formatPassiveBonuses(getBonuses(stack));
			
			String armor = "";
			if(stack.getItemID() >= ActionbarItem.itemIndex && 
					stack.getItemID() < ActionbarItem.spellIndex && 
					Item.itemsList[stack.getItemID()] instanceof ItemArmor)
			{
				armor = getTotalArmor(player, stack);
			}
			
			
			//If there's just a title, crop the tooltip
			if(passiveBonusLines.length == 0 && tooltipLines.length == 0 && stats.length == 0 && cooldown.equals(""))
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
			double requiredHeight = (tooltipFont.getHeight(itemName)) + 
					PADDING * (passiveBonusLines.length + stats.length) + 
					((tooltipFont.getHeight(itemName)) * 0.5f * (stats.length)) + 
					((tooltipFont.getHeight(itemName)) * 0.5f * (passiveBonusLines.length)) + 
					((tooltipFont.getHeight(itemName)) * 0.7f * (tooltipLines.length)) + 
					((!cooldown.equals("")) ? tooltipFont.getHeight(cooldown) * 0.5f + 5: 0) + 
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
			tooltipFont.drawString(frameX + PADDING, yOffset, itemName, xScale * 1.5F, -xScale * 1.5F, TrueTypeFont.ALIGN_LEFT); 
			
			yOffset += 20;
			GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
			tooltipFont.drawString(frameX + PADDING, yOffset, armor, xScale * 2F, -xScale * 2F, TrueTypeFont.ALIGN_LEFT); 
			
			GL11.glColor4d(EnumColor.LIME_GREEN.COLOR[0], EnumColor.LIME_GREEN.COLOR[1], EnumColor.LIME_GREEN.COLOR[2], 1.0);
			//Stats
			for(int i = 0; i < stats.length; i++)
			{
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						stats[i],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}			
			
			GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
			
			//Render the set bonuses
			yOffset = yOffset + PADDING * (stats.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (stats.length));
			
			for(int i = 0; i < passiveBonusLines.length; i++)
			{
				if(i < activePassiveBonuses.length && !activePassiveBonuses[i])
				{
					GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 1.0);
				}
				else
				{
					GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
				}
				
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + PADDING*(1 + i) + (((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						passiveBonusLines[i],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}
			
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
			//Render the text lines which are now broken up to fit in the tooltip
			//and adjust the yoffset so the text won't overlap
			yOffset = yOffset + PADDING * (passiveBonusLines.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (passiveBonusLines.length));
			
			for(int i = 0; i < tooltipLines.length; i++)
			{
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + PADDING*(i+ 1) + (((tooltipHeight) - (tooltipHeight - plainTooltip.getHeight(itemName))) * 0.5f * (1+i)), 
						tooltipLines[i],
						xScale,
						-xScale,
						TrueTypeFont.ALIGN_LEFT); 			
			}
			
			//Render the cooldown remaining if it happens to be applicable.
			//(After adjusting to the proper position)
			yOffset = yOffset + PADDING * (tooltipLines.length) + 
					(((tooltipHeight) - (tooltipHeight - boldTooltip.getHeight(itemName))) * 0.5f * (tooltipLines.length));
			
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			tooltipFont.drawString(frameX + PADDING, 
					yOffset + PADDING + (((tooltipHeight) - (tooltipHeight - plainTooltip.getHeight(cooldown))) * 0.5f), 
					cooldown,
					xScale,
					-xScale,
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
						textures[stack.getGemSockets()[i].getGem().getItemID()].bind();
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
	
	private static void renderStatusEffectTooltip(EntityPlayer player, StatusEffect effect)
	{
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
		String[] tooltipLines = splitFulltooltip(effect.toString(), xScale, tooltipWidth);
		if(tooltipLines.length == 1)
		{
			tooltipWidth = 3 * PADDING + (int) (0.5F * xScale * boldTooltip.getWidth(effect.toString()));
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
		int tooltipHeight = (int) ((plainTooltip.getHeight("")) * 0.9 * (tooltipLines.length));

		//Make sure the tooltip doesnt go off the bottom
		if(frameY + tooltipHeight > Display.getHeight() * 0.5)
		{
			frameY = (int) (Display.getHeight() * 0.5 - tooltipHeight);
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
		
		GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
		for(int i = 0; i < tooltipLines.length; i++)
		{
			boldTooltip.drawString(frameX + PADDING, 
					frameY + boldTooltip.getHeight("") + ((tooltipHeight - boldTooltip.getHeight("")) * 0.5f * i), 
					tooltipLines[i],
					0.5F,
					-1,
					TrueTypeFont.ALIGN_LEFT); 			
		}
	}
}

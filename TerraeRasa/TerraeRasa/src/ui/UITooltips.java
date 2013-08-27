package ui;

import items.Item;
import items.ItemArmor;

import java.util.Vector;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import passivebonuses.DisplayablePassiveBonus;

import render.TrueTypeFont;
import spells.Spell;
import statuseffects.DisplayableStatusEffect;
import utils.ActionbarItem;
import utils.DisplayableItemStack;
import utils.MathHelper;
import world.World;
import blocks.Block;
import entities.EntityPlayer;
import enums.EnumColor;
import enums.EnumItemQuality;

public class UITooltips extends UIBase
{	
	/**
	 * Checks to see if the mouse is hovering over something that needs to have a tooltip rendered.
	 * Will return null if nothing appropriate is found.
	 * @return an appropriate DisplayableItemStack to render, or null if none is found
	 */
	private static DisplayableItemStack getTooltipStack(World world, EntityPlayer player, int mouseX, int mouseY)
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
		
		DisplayableItemStack stack = null;
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
		if((stack = UISocketMenu.getMouseoverSocketedGems(player, mouseX, mouseY)) != null)
		{
			return stack;
		}		
		return null;
	}	
	
	/**
	 * Gets the quality of the DisplayableItemStack the mouse is hovering over. 
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the quality of the DisplayableItemStack being hovered over
	 */
	private static EnumItemQuality getQuality(DisplayableItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			return (Item.itemsList[stack.getItemID()]).itemQuality;
		}
		return EnumItemQuality.COMMON;
	}

	/**
	 * Gets the fulltooltip of the DisplayableItemStack the mouse is hovering over. 
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the fulltooltip of the DisplayableItemStack being hovered over
	 */
	private static String getFullTooltip(DisplayableItemStack stack)
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
	 * Gets the stats of the DisplayableItemStack the mouse is hovering over. 
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the stats of the DisplayableItemStack being hovered over
	 */
	private static String[] getStats(DisplayableItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex)
		{
			return Item.itemsList[stack.getItemID()].getStats();
		}
		return new String[] { };
	}
	
	/**
	 * Gets the passive bonuses of the DisplayableItemStack the mouse is hovering over. 
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the passive bonuses of the DisplayableItemStack being hovered over
	 */
	private static String[] getBonuses(DisplayableItemStack stack)
	{
		Vector<String> bonusesVector = new Vector<String>();
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			String[] bonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getStringBonuses();
			for(String bonus : bonuses)
			{
//				if(!(bonus.contains("Defense")))
//				{
					bonusesVector.add(bonus);
//				}
			}
		}
		
		String[] bonuses = stack.getStringBonuses();
		for(String bonus : bonuses)
		{
//			if(!(bonus.contains("Defense")))
//			{
				bonusesVector.add(bonus);
//			}
		}
		
		String[] passiveBonuses = new String[bonusesVector.size()];
		bonusesVector.copyInto(passiveBonuses);
		return passiveBonuses;
	}

	/**
	 * Gets the active set bonuses of the DisplayableItemStack the mouse is currently hovering over. This must be a piece of armour
	 * @param player the player to whom this UI belongs
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the active set bonuses of the DisplayableItemStack being hovered over
	 */
	private static boolean[] getActiveBonuses(EntityPlayer player, DisplayableItemStack stack)
	{
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			return ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType().getBonusesActivated(player);
		}		
		return new boolean[] { };
	}
	
	/**
	 * Gets the cooldown of the DisplayableItemStack the mouse is currently hovering over. This may not be applicable, in which case a blank string is yielded.
	 * @param player the player to whom this UI belongs
	 * @param stack the DisplayableItemStack being hovered over
	 * @return the cooldown of the DisplayableItemStack being hovered over if applicable
	 */
	private static String getCooldown(EntityPlayer player, DisplayableItemStack stack)
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
		double length = 0;			
		tooltipWidth -= 15;
		double spaceLength = xScale * plainTooltip.getWidth(" ");
		if(fulltooltip != "")
		{
			for(int i = 0; i < words.length; i++)
			{
				double width = xScale * plainTooltip.getWidth(words[i]);
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
			for(int i = 0; i < passiveBonuses.length; i++)
			{
				passiveBonusesVector.add(passiveBonuses[i]);
			}
		}
		String[] temp = new String[passiveBonusesVector.size()];
		passiveBonusesVector.copyInto(temp);
		return temp;
	}
	
	private static String getTotalArmor(EntityPlayer player, DisplayableItemStack stack, EnumColor color)
	{
		int totalArmor = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getDefense(); 
		return (totalArmor > 0) ? "  " + totalArmor : "";
	}
	
	private static String[] getSetBonuses(DisplayableItemStack stack)
	{
		Vector<String> bonusesVector = new Vector<String>();
		if(stack.getItemID() >= Item.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
		{
			DisplayablePassiveBonus[] bonuses = ((ItemArmor)(Item.itemsList[stack.getItemID()])).getTierBonuses();
			bonusesVector.add(((ItemArmor)(Item.itemsList[stack.getItemID()])).getTierName());
			for(DisplayablePassiveBonus bonus : bonuses)
			{
				String setRequirement = "(" + bonus.getPiecesRequiredToActivate() + ") Set: ";
				bonusesVector.add(setRequirement);
				String bonusInfo = "    " + bonus.toString();
				bonusesVector.add(bonusInfo);
			}
		}
		
		String[] temp = new String[bonusesVector.size()];
		bonusesVector.copyInto(temp);
		return temp;
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
		
		DisplayableItemStack stack = getTooltipStack(world, player, x, y);
		if(stack != null)
		{
			renderDisplayableItemStackTooltip(player, stack);
		}
		
		DisplayableStatusEffect effect = UIStatusEffects.getMouseoverStatusEffect(x, y);
		if(effect != null)
		{
			renderStatusEffectTooltip(player, effect);
		}
	}
	
	private static void renderDisplayableItemStackTooltip(EntityPlayer player, DisplayableItemStack stack)
	{
		if(player.isInventoryOpen && player.getHeldItem() == null)
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
			String[] setBonuses = getSetBonuses(stack);
			String armor = "";
			EnumColor color = EnumColor.WHITE;
			if(stack.getItemID() >= ActionbarItem.itemIndex && stack.getItemID() < ActionbarItem.spellIndex && Item.itemsList[stack.getItemID()] instanceof ItemArmor)
			{
				armor = getTotalArmor(player, stack, color);
			}
			
			//If there's just a title, crop the tooltip
			if(passiveBonusLines.length == 0 && tooltipLines.length == 0 && stats.length == 0 && cooldown.equals(""))
			{
				tooltipWidth = 3 * PADDING + (int) (xScale * tooltipFont.getWidth(itemName));
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
			double requiredHeight = 10 + 
					(tooltipFont.getHeight("!") * xScale * 1.5) + 
					((!armor.equals("")) ? (tooltipFont.getHeight("!") * xScale * 2.2F) : 0) + 
					((tooltipFont.getHeight(itemName)) * xScale * (stats.length)) + 
					((tooltipFont.getHeight(itemName)) * xScale * (passiveBonusLines.length)) + 
					((setBonuses.length > 1) ? ((tooltipFont.getHeight(itemName)) * xScale * (setBonuses.length)) : 0) + 
					((tooltipFont.getHeight(itemName)) * xScale * (tooltipLines.length)) + 
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
			float yOffset = frameY + tooltipFont.getHeight(itemName) * xScale * 1.5F + PADDING;
			tooltipFont.drawString(frameX + PADDING, yOffset, itemName, xScale * 1.5F, -xScale * 1.5F, TrueTypeFont.ALIGN_LEFT); 
			
			//Armor value, if applicable
			if(!armor.equals(""))
			{
				yOffset += 24;
				GL11.glColor4d(color.COLOR[0], color.COLOR[1], color.COLOR[2], 1.0);
				tooltipFont.drawString(frameX + PADDING, yOffset, armor, xScale * 2.2F, -xScale * 2.2F, TrueTypeFont.ALIGN_LEFT); 
			}
			GL11.glColor4d(EnumColor.LIME_GREEN.COLOR[0], EnumColor.LIME_GREEN.COLOR[1], EnumColor.LIME_GREEN.COLOR[2], 1.0);
			//Stats
			for(int i = 0; i < stats.length; i++)
			{
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (1+i)), 
						stats[i],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}		
			GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
			
			//Render the set bonuses
			yOffset = yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (stats.length));
			for(int i = 0; i < passiveBonusLines.length; i++)
			{
				if(passiveBonusLines[i] == null)
				{
					//TODO null passive bonuses crashing the tooltip renderer
					System.out.println("null passive bonus -- fix req.");
					continue;
				}
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (1+i)), 
						passiveBonusLines[i],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}
			
			//The actual set bonuses
			yOffset = yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (passiveBonusLines.length));
			if(setBonuses.length > 1)
				yOffset += PADDING;
			//Title
			if(setBonuses.length > 0 && !setBonuses[0].equals("None"))
			{
				GL11.glColor4d(EnumColor.LIME_GREEN.COLOR[0], EnumColor.LIME_GREEN.COLOR[1], EnumColor.LIME_GREEN.COLOR[2], 1.0);
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale), 
						setBonuses[0],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}
			//Bonuses
			for(int i = 1; i < setBonuses.length; i+=2)
			{
				if(!activePassiveBonuses[((i - 1) / 2)])
				{
					GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 1.0);
				}
				else
				{
					GL11.glColor4d(EnumColor.WHITE.COLOR[0], EnumColor.WHITE.COLOR[1], EnumColor.WHITE.COLOR[2], 1.0);
				}

				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (1+i)), 
						setBonuses[i],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 

				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (2 + i)), 
						setBonuses[i + 1],
						xScale,
						-xScale, 
						TrueTypeFont.ALIGN_LEFT); 

			}
			
			//the full tooltip
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
			yOffset += (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * ((setBonuses.length > 1) ? setBonuses.length : 0));
			for(int i = 0; i < tooltipLines.length; i++)
			{
				tooltipFont.drawString(frameX + PADDING, 
						yOffset + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (1+i)), 
						tooltipLines[i],
						xScale,
						-xScale,
						TrueTypeFont.ALIGN_LEFT); 			
			}
			
			//Render the cooldown remaining if it happens to be applicable.
			//(After adjusting to the proper position)
			yOffset += PADDING * (tooltipLines.length) + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(itemName))) * xScale * (tooltipLines.length));
			
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			tooltipFont.drawString(frameX + PADDING, 
					yOffset + PADDING + (((tooltipHeight) - (tooltipHeight - tooltipFont.getHeight(cooldown))) * xScale), 
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
					double offsetByTotal = (tooltipWidth - (37.5 * numberOfSockets)) / 2; 
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
						double offsetByTotal = ((tooltipWidth - (37.5 * numberOfSockets)) / 2) + 3; 
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
	
	private static void renderStatusEffectTooltip(EntityPlayer player, DisplayableStatusEffect effect)
	{
		//Variables to help determine where and how wide the frame/text will be, as well as text colour.
		int tooltipWidth = 185;
		int frameX = MathHelper.getCorrectMouseXPosition() - 25; 
		int frameY = MathHelper.getCorrectMouseYPosition() - 25;
		//% of total text size to render, in this case sacale to 1/2 size.
		float xScale = 0.25F * 1.5F;
		//Arbitrary padding to make things look nicer
		final int PADDING = 5;
		
		//Break the tooltip extra information into strings that don't exceed the tooltip's total width
		//And look terrible as a result. The only risk is the tooltip will be too long.			
		String[] tooltipLines = splitFulltooltip(effect.toString(), xScale, tooltipWidth);
		if(tooltipLines.length == 1)
		{
			tooltipWidth = 4 * PADDING + (int) (0.25f * tooltipFont.getWidth(effect.toString()));
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
		int tooltipHeight = (int) ((tooltipFont.getHeight("")) * xScale * (tooltipLines.length));

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
			tooltipFont.drawString(frameX + PADDING, 
					frameY + tooltipFont.getHeight("") * xScale + ((tooltipHeight - tooltipFont.getHeight("")) * xScale * i), 
					tooltipLines[i],
					xScale,
					-xScale,
					TrueTypeFont.ALIGN_LEFT); 			
		}
	}
}

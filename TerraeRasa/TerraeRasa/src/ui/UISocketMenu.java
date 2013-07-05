package ui;

import items.Item;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import render.TrueTypeFont;
import utils.ActionbarItem;
import utils.ItemStack;
import world.World;
import entities.EntityPlayer;
import enums.EnumItemQuality;

public class UISocketMenu extends UIBase
{	
	/**
	 * Closes the socket window if needed.
	 */
	public static void closeSocketWindow()
	{
		isSocketWindowOpen = false;
	}

	/**
	 * Returns an applicable ItemStack if a gem that is being socketed is moused over, otherwise this will yield null.
	 * @return an applicable ItemStack if a gem in the socket UI is moused over, otherwise null
	 */
	protected static ItemStack getMouseoverSocketedGems(int mouseX, int mouseY)
	{
		if(isSocketWindowOpen)
		{
			int tooltipHeight = 100;
			int frameX = (int) (Display.getWidth() * 0.25) - 80;  
			int frameY = (int) (Display.getHeight() * 0.5) - 106; 
			
			ItemStack stack = socketedItem;
			String itemName = socketedItem.getRenderedName();
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
				int size = 30;
				double offsetByTotal = (numberOfSockets == 1) ? 65 : (numberOfSockets == 2) ? 47.5 : (numberOfSockets == 3) ? 30 : 10;
				double xOffset = frameX + offsetByTotal + (i * (size + 7.5));
				
				if(mouseY > yOffset && mouseY < yOffset + size && mouseX > xOffset && mouseX < xOffset + size)
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
	 * Renders the sockets menu for a given item.
	 * @param world
	 * @param player
	 */
	protected static void renderSocketsMenu(World world, EntityPlayer player)
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
}

package ui;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import render.TrueTypeFont;
import transmission.ClientUpdate;
import utils.MathHelper;
import world.World;
import client.Settings;
import entities.EntityPlayer;

/**
 * Refactor not entirely done
 * TODO: refactor more
 */
public class UI extends UIBase
{
	public static void render(ClientUpdate update, World world, EntityPlayer player, Settings settings)
	{		
		GL11.glEnable(GL11.GL_BLEND);
		UIStatusEffects.updateStatusEffects(player);
		UIStatBars.renderHeartsAndMana(world, player); //The hearts and mana	
		if(player.isInventoryOpen)
		{
			if(player.isViewingChest)
			{
				isSocketWindowOpen = false;
			}
			
			UIInventory.renderInventory(world, player); //The full inventory if it's open
			if(player.isViewingChest) //Chest(s) if they're being viewed
			{ 
				UIInventory.renderChest(world, player);
			}			
			if(!settings.menuOpen)
			{
				UIMouse.mouse(update, world, player);
				if(isSocketWindowOpen)
				{
					UISocketMenu.renderSocketsMenu(world, player);
				}
			}
		}	
		else
		{
			UISocketMenu.closeSocketWindow();
			UIInventory.renderActionBar(world, player);	//The actionbar if the inventory is closed
		}		
		UITooltips.renderApplicableTooltip(world, player);
		UIStatusEffects.renderStatusEffects(player);		
		renderMouseItem(player); //The item held by the mouse, if there is one
		renderText(world, player); //Health and the 'Save And Quit' button		
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		GL11.glDisable(GL11.GL_BLEND);		
	}

	/**
	 * Renders the item the mouse is holding, should there be one.
	 */
	protected static void renderMouseItem(EntityPlayer player)
	{
		if(player.getHeldItem() != null)
		{
			int x = (Math.abs(getCameraX()) + MathHelper.getCorrectMouseXPosition()) - mouseXOffset; 
			int y = (Math.abs(getCameraY()) + MathHelper.getCorrectMouseYPosition()) - mouseYOffset;
			int newsize = mouseItemSize;
			
			if(player.getHeldItem().getItemID() < 2048) //blocks are a slightly different size
			{
				x += 1;
				y += 1;
				newsize -= 2;
			}
			
			textures[player.getHeldItem().getItemID()].bind();
			t.startDrawingQuads();
			t.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
			t.addVertexWithUV(x, y + newsize, 0, 0, 1);
			t.addVertexWithUV(x + newsize, y + newsize, 0, 1, 1);
			t.addVertexWithUV(x + newsize, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();
			
			if(player.getHeldItem().getStackSize() > 1)
			{
				GL11.glColor4f(0, 1, 0, 1);
				trueTypeFont.drawString(x - 2, y + 18, "" + player.getHeldItem().getStackSize(), 0.25f, -0.25f);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
	}
	
	/**
	 * Renders all constantly visible text that's required for the inventory.
	 */
	protected static void renderText(World world, EntityPlayer player)
	{
		if(player.isInventoryOpen)
		{
			GL11.glColor4f(1, 1, 1, 1);
			//Health:
			String health = new StringBuilder().append((int)player.getHealth()).append(" / ").append(player.maxHealth).toString();
			trueTypeFont.drawString((getCameraX() + 35), (getCameraY() + 22), health, 0.3f, -0.3f);
			
			//specialOffsetY is increased if the mana bar is rendered, because then the special bar is 
			//rendered further down. Otherwise, special energy is rendered directly under the healthbar
			int specialOffsetY = 37; 
			int offset = (player.maxMana < 100) ? 5 : 0;
			if(player.maxMana > 0) 
			{
				//Mana
				String mana = new StringBuilder().append((int)player.mana).append(" / ").append(player.maxMana).toString();
				trueTypeFont.drawString((getCameraX() + 35 + offset), (getCameraY() + 37), mana, 0.3f, -0.3f);
				specialOffsetY += 15;
			}
			
			//Special
			String special = new StringBuilder().append((int)(player.specialEnergy / player.maxSpecialEnergy * 100)).append("%").toString();
			trueTypeFont.drawString((getCameraX() + 35 + offset), (getCameraY() + specialOffsetY), special, 0.3f, -0.3f);
		
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
	 * Crafts the recipe in the selected slot of the recipe slider and sets the mouseItem 
	 * to that item. Removes items from the inventory and flags recipes for recalculation 
	 * as well
	 * @param index what recipe in the possible recipes list to craft
	 * @param xoff how far to offset the rendering of the cursor item (X)
	 * @param yoff how far to offset the rendering of the cursor item (Y)
	 * @param size how big the image is (16)
	 */
	protected static void craftRecipe(ClientUpdate update, World world, EntityPlayer player, int index, int xoff, int yoff, int size)
	{
		if(player.getAllPossibleRecipes().length <= 0) 
		{
		 	return; //There are no recipes
		}
		String command = "/player " + player.entityID + " craft " + player.getAllPossibleRecipes()[index].getID();
		update.addCommand(command);
	}
		
	/**
	 * Adjusts the currently selected recipe in the slider. Requires a function to prevent bounds errors.
	 */
	protected static void adjustSliderPosition(World world, EntityPlayer player, int adjustment)
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
}

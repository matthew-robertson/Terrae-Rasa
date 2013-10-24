package client.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import statuseffects.StatusEffect;
import client.entities.EntityPlayer;

/**
 * UIStatusEffects handles everything relating to rendering and mouse events for status effects on the UI.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class UIStatusEffects extends UIBase
{
	private static final int MAX_STATUS_PER_ROW = 8;
	protected static List<StatusEffect> goodEffects = new ArrayList<StatusEffect>();
	protected static List<StatusEffect> badEffects = new ArrayList<StatusEffect>();

	/**
	 * Updates the status effect lists as is appropriate
	 * @param player the player to whom this UI belongs
	 */
	protected static void updateStatusEffects(EntityPlayer player)
	{
		//Get the status effects
		List<StatusEffect> statusEffects = player.statusEffects;
		goodEffects.clear();
		badEffects.clear();
		
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
	}

	/**
	 * Gets the status effect the mouse is currently hovering over, or null if there is none
	 * @param mouseX the mouse's X position in ortho units
	 * @param mouseY the mouse's Y position in ortho units
	 * @return the currently hovered over status effect, or null if there is none
	 */
	protected static StatusEffect getMouseoverStatusEffect(int mouseX, int mouseY)
	{
		int size = 16;
		int goodEffectHeight = 2;
		int badEffectHeight = 2 + (size + 2) * (1 + (goodEffects.size() / MAX_STATUS_PER_ROW));

		//Check if mousing over a 'good' status effect
		for(int i = 0; i < goodEffects.size(); i++) 
		{
			int x = (int) ((Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = goodEffectHeight + (int) (((i / MAX_STATUS_PER_ROW) * (size + 3)));
			if(mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size)
			{
				return goodEffects.get(i);
			}
		}
		//Check if mousing over a 'bad' status effect
		for(int i = 0; i < badEffects.size(); i++) 
		{
			int x = (int) (getCameraX() + (Display.getWidth() * 0.5f) - ((1 + (i % MAX_STATUS_PER_ROW)) * (size + 3)));
			int y = badEffectHeight + (int) (getCameraY() + (i / MAX_STATUS_PER_ROW) * (size + 3));
			if(mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size)
			{
				return badEffects.get(i);
			}
		}
		//Return nothing if there is no moused over status effect
		return null;
	}
	
	/**
	 * Renders the status effects afflicting or benefiting the player, including their remaining time in seconds.
	 * @param player the player to whom this UI belongs
	 */
	protected static void renderStatusEffects(EntityPlayer player)
	{
		//Ensure the icons render in the right place and on the screen (top-right)
		//The default size of an icon is 1 square, which is 16 pixels
		int defaultIconTextureSize = 16;
		
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
			double tx = (double)goodEffects.get(i).iconX / (defaultIconTextureSize * ICONS_SHEET_WIDTH);
			double ty = (double)goodEffects.get(i).iconY / (defaultIconTextureSize * ICONS_SHEET_HEIGHT);
			double tw = tx + (double)goodEffects.get(i).iconWidth / (defaultIconTextureSize * ICONS_SHEET_WIDTH);
			double th = ty + (double)goodEffects.get(i).iconHeight / (defaultIconTextureSize * ICONS_SHEET_HEIGHT);			
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
			double tx = (double)badEffects.get(i).iconX / (defaultIconTextureSize * ICONS_SHEET_WIDTH);
			double ty = (double)badEffects.get(i).iconY / (defaultIconTextureSize * ICONS_SHEET_HEIGHT);
			double tw = tx + (double)badEffects.get(i).iconWidth / (defaultIconTextureSize * ICONS_SHEET_WIDTH);
			double th = ty + (double)badEffects.get(i).iconHeight / (defaultIconTextureSize * ICONS_SHEET_HEIGHT);	
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
}

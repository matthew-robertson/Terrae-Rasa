package ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import statuseffects.StatusEffect;
import entities.EntityPlayer;

public class UIStatusEffects extends UIBase
{
	/**
	 * Renders the status effects afflicting or benefiting the player, including their remaining time in seconds.
	 * @param player the player in use currently
	 */
	protected static void renderStatusEffects(EntityPlayer player)
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
}

package ui;

import org.lwjgl.opengl.GL11;

import world.World;
import entities.EntityPlayer;

/**
 * UIStatBars handles drawing of the stat bars for the player. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class UIStatBars extends UIBase
{
	/**
	 * Draws the hearts, mana, and special energy bars for the player.
	 */
	protected static void renderHeartsAndMana(World world, EntityPlayer player)
	{		
		renderHealth(player);
		renderMana(player);
		renderSpecial(player);
	}
	
	/**
	 * Renders the healthbar for the player.
	 * @param player the player to whom this UI belongs
	 */
	private static void renderHealth(EntityPlayer player)
	{
		//The black background for the healthbar
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		int x = getCameraX() + 10;
		int y = getCameraY() + 10;		
		int width = 100;
		int height = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
		t.addVertexWithUV(x + width, y + height, 0, 1, 1);
		t.addVertexWithUV(x + width, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();			
		
		//"Foreground" of the texture (red part)
		GL11.glColor4f(1, 0, 0, 1);
		x = getCameraX() + 10;
		y = getCameraY() + 10;		
		width = (int) ((player.getHealth() / player.maxHealth) * 100);
		height = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
		t.addVertexWithUV(x + width, y + height, 0, 1, 1);
		t.addVertexWithUV(x + width, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	/**
	 * Renders the mana bar for the player.
	 * @param player the player to whom this UI belongs
	 */
	private static void renderMana(EntityPlayer player)
	{
		if(player.maxMana > 0)
		{
			//Black background
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0, 0, 0, 1);
			int x = getCameraX() + 10;
			int y = getCameraY() + 25;		
			int width = 100;
			int height = 11;		
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + height, 0, 0, 1);
			t.addVertexWithUV(x + width, y + height, 0, 1, 1);
			t.addVertexWithUV(x + width, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();			

			//Blue foreground
			GL11.glColor4f(0, 0, 1, 1);
			x = getCameraX() + 10;
			y = getCameraY() + 25;		
			width = (int) ((double)player.mana / player.maxMana * 100);
			height  = 11;		
			t.startDrawingQuads();
			t.addVertexWithUV(x, y + height, 0, 0, 1);
			t.addVertexWithUV(x + width, y + height, 0, 1, 1);
			t.addVertexWithUV(x + width, y, 0, 1, 0);
			t.addVertexWithUV(x, y, 0, 0, 0);
			t.draw();			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	
	/**
	 * Renders the special energy for the player.
	 * @param player the player to whom this UI belongs
	 */
	private static void renderSpecial(EntityPlayer player)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//The black background
		GL11.glColor4f(0, 0, 0, 1);
		int x = getCameraX() + 10;
		int y = getCameraY() + 25;
		if(player.maxMana > 0)
		{
			y = getCameraY() + 40;		
		}
		int width = 100;
		int height = 11;		
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
		t.addVertexWithUV(x + width, y + height, 0, 1, 1);
		t.addVertexWithUV(x + width, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();			

		//The lime-ish green foreground
		GL11.glColor4f(141F/255, 230F/255, 99F/255, 1);
		width = (int) (player.specialEnergy / player.maxSpecialEnergy * 100);
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
		t.addVertexWithUV(x + width, y + height, 0, 1, 1);
		t.addVertexWithUV(x + width, y, 0, 1, 0);
		t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();			
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}

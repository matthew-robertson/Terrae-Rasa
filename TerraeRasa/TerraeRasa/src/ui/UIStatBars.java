package ui;

import org.lwjgl.opengl.GL11;

import world.World;
import entities.EntityPlayer;

public class UIStatBars extends UIBase
{
	/**
	 * Draws the hearts, mana, and special energy bars for the player.
	 */
	protected static void renderHeartsAndMana(World world, EntityPlayer player)
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
		newX = (int) (player.getHealth() / player.maxHealth * 100);
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
}

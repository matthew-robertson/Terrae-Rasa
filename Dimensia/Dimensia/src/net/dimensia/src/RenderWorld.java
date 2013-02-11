package net.dimensia.src;

import org.lwjgl.opengl.GL11;

public class RenderWorld extends Render
{
	public void renderToolSwing(World world, EntityLivingPlayer player)
	{
		if(!player.hasSwungTool || (player.inventory.getMainInventoryStack(player.selectedSlot) == null) || !(Item.itemsList[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()] instanceof ItemTool))
		{
			return;
		}
		
		GL11.glTranslatef(player.x + 9, player.y + 9, 0);

		int size = 20;		
		int x = 0;
		int y = -size;
		int h = size;
		int w = size;        
		float angle = player.rotateAngle / 57.5f;
		
		double x1 = ((x) * Math.cos(angle)) - ((y+h) * Math.sin(angle));
		double y1 = ((x) * Math.sin(angle)) + ((y+h) * Math.cos(angle));	
		double x2 = ((x+w) * Math.cos(angle)) - ((y+h) * Math.sin(angle));
		double y2 = ((x+w) * Math.sin(angle)) + ((y+h) * Math.cos(angle));
		double x3 = ((x+w) * Math.cos(angle)) - ((y) * Math.sin(angle));
		double y3 = ((x+w) * Math.sin(angle)) + ((y) * Math.cos(angle));
		double x4 = ((x) * Math.cos(angle)) - ((y) * Math.sin(angle));
		double y4 = ((x) * Math.sin(angle)) + ((y) * Math.cos(angle));
		
		GL11.glColor4f(0, 0, 0, 1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        	GL11.glVertex3d(x1, y1, 0);
        	GL11.glVertex3d(x2, y2, 0);
        	GL11.glVertex3d(x3, y3, 0);
        	GL11.glVertex3d(x4, y4, 0);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 1, 1, 1);
		
        textures[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()].bind();        
        
        if(!player.isSwingingRight)
        {
        	GL11.glRotatef(player.rotateAngle, 0, 0, 1);
            t.startDrawingQuads();
	        t.addVertexWithUV(x, y+h, 0, 0, 1);
	        t.addVertexWithUV(x+w, y+h, 0, 1, 1);
	        t.addVertexWithUV(x+w, y, 0, 1, 0);
	        t.addVertexWithUV(x, y, 0, 0, 0);
	        t.draw();
        }
        else
        {
        	GL11.glRotatef(player.rotateAngle + 90.0f, 0, 0, 1);
            //GL11.glTranslatef(20, 0, 0);
        	GL11.glScalef(-1, 1, 1);
        	t.startDrawingQuads();
	        t.addVertexWithUV(x, y+h, 0, 0, 1);
	        t.addVertexWithUV(x+w, y+h, 0, 1, 1);
	        t.addVertexWithUV(x+w, y, 0, 1, 0);
	        t.addVertexWithUV(x, y, 0, 0, 0);
	        t.draw();
        }
        
        adjustCamera(world, player);
	}
}
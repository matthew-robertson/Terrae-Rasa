package render;

import items.Item;
import items.ItemTool;

import org.lwjgl.opengl.GL11;

import client.Dimensia;

import entities.EntityPlayer;

import utils.ActionbarItem;
import utils.MathHelper;
import utils.Point;
import utils.Vector2F;
import world.World;

public class RenderWorld extends Render
{
	public void renderToolSwing(World world, EntityPlayer player)
	{
		//The following conditions indicate that a tool swing should not be rendered:
		//(1)There is nothing being held; (2): there isnt a tool swing; (3): The player is holding a spell; 
		//(4): the player isnt holding a tool
		if(player.inventory.getMainInventoryStack(player.selectedSlot) == null ||
			!player.isSwingingTool() || 
			player.inventory.getMainInventoryStack(player.selectedSlot).getItemID() >= ActionbarItem.spellIndex || 
			!(Item.itemsList[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()] instanceof ItemTool))
		{
			return;
		}
		
		double const_ = 9;
		
		GL11.glTranslated(player.x + (double)const_, player.y + (double)const_, 0);
		
		ItemTool heldItem = ((ItemTool)(Item.itemsList[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()]));
		
		double size = heldItem.size;		
		double angle = player.getToolRotationAngle();
		
		double[] x_bounds = heldItem.xBounds;
		double[] y_bounds = heldItem.yBounds;
		
		Vector2F[] scaled_points = new Vector2F[x_bounds.length];
		Point[] points = { new Point(0, 0), new Point((int)size, 0), new Point((int)size, (int)-size), new Point(0, (int)-size) };
			
		
		for(int i = 0; i < scaled_points.length; i++)
		{
			scaled_points[i] = new Vector2F((float)(size * x_bounds[i]), (float)(size * ((float)y_bounds[i])) - (float)size );
		}
		
		double[] x_points = new double[scaled_points.length];
		double[] y_points = new double[scaled_points.length];
		
		for(int i = 0; i < scaled_points.length; i++)
		{
			x_points[i] =  (scaled_points[i].x * Math.cos(angle)) - 
					(scaled_points[i].y * Math.sin(angle));
			y_points[i] = (scaled_points[i].x * Math.sin(angle)) + 
					( scaled_points[i].y * Math.cos(angle));
		}
	
		if(Dimensia.initInDebugMode)
		{
			GL11.glColor4f(0, 0, 0, 1);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			for(int i = 0; i < x_points.length; i++)
			{
				GL11.glVertex3d(x_points[i], y_points[i], 0);
			}        
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(1, 1, 1, 1);
		}
		
        textures[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()].bind();        
        
        if(!player.getIsSwingingRight())
        {
        	GL11.glRotated(MathHelper.radianToDegree(player.getToolRotationAngle()), 0, 0, 1);
            t.startDrawingQuads();
	        t.addVertexWithUV(points[0].x, points[0].y, 0, 0, 1);
	        t.addVertexWithUV(points[1].x, points[1].y, 0, 1, 1);
	        t.addVertexWithUV(points[2].x, points[2].y, 0, 1, 0);
	        t.addVertexWithUV(points[3].x, points[3].y, 0, 0, 0);
	        t.draw();
        }
        else
        {
        	GL11.glRotated(MathHelper.radianToDegree(player.getToolRotationAngle()) + 90.0f, 0, 0, 1);
        	GL11.glScalef(-1, 1, 1);
        	t.startDrawingQuads();
        	t.addVertexWithUV(points[0].x, points[0].y, 0, 0, 1);
 	        t.addVertexWithUV(points[1].x, points[1].y, 0, 1, 1);
 	        t.addVertexWithUV(points[2].x, points[2].y, 0, 1, 0);
 	        t.addVertexWithUV(points[3].x, points[3].y, 0, 0, 0);
	        t.draw();
        }        
       
        adjustCameraToLastPosition();
        
       
	}
}
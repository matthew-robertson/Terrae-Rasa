package render;

import items.Item;
import items.ItemTool;

import org.lwjgl.opengl.GL11;

import utils.ActionbarItem;
import utils.MathHelper;
import utils.Position;
import utils.Vector2F;
import utils.WorldText;
import world.World;
import client.TerraeRasa;
import entities.DisplayableEntity;
import entities.EntityPlayer;

public class RenderEntities extends Render
{
	/**
	 * Renders the player entity
	 */
	public void renderPlayer(World world, EntityPlayer player)
	{
		GL11.glColor4f(1, 1, 1, 1);
		if(player.isFacingRight) //facing right (default)
		{
			double x = (int)player.x;
	        double y = (int)player.y;
			playerTexture.bind(); 
	        t.startDrawingQuads();
	        t.addVertexWithUV(x, y + 18, 0, 0, 1);
	        t.addVertexWithUV(x + 12, y + 18, 0, 1, 1);
	        t.addVertexWithUV(x + 12, y, 0, 1, 0);
	        t.addVertexWithUV(x, y, 0, 0, 0);
	        t.draw();  
		}
		else //flip player sprite and adjust it, so it appears the player turned left
		{
			GL11.glTranslatef((int)player.x, (int)player.y, 0);
			playerTexture.bind();
			GL11.glScalef(-1, 1, 1);
	        t.startDrawingQuads();
	        t.addVertexWithUV(-12, 18, 0, 0, 1);
	        t.addVertexWithUV(0, 18, 0, 1, 1);
	        t.addVertexWithUV(0, 0, 0, 1, 0);
	        t.addVertexWithUV(-12, 0, 0, 0, 0);
	        t.draw();  
	        adjustCameraToLastPosition();
		}
		
		renderToolSwing(world, player);
	}
	
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
		Position[] points = { new Position(0, 0), new Position((int)size, 0), new Position((int)size, (int)-size), new Position(0, (int)-size) };
			
		
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
	
		if(TerraeRasa.initInDebugMode)
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

	/**
	 * Renders all entities in world.entityList
	 */
	public void renderWorldEntityList(World world)
	{
		for(int i = 0; i < world.enemyList.size(); i++)
		{
			renderEntityNPCEnemy(world.enemyList.get(i));
		}
	}
	
	/**
	 * renders all npcs in world.npcList
	 */
	public void renderNPCs(World world){
		for(int i = 0; i < world.npcList.size(); i++){
			renderEntityNPC(world.npcList.get(i));
		}
	}

	/**
	 * Renders all entities in world.projectileList
	 */
	public void renderWorldProjectileList(World world)
	{
		for(int i = 0; i < world.projectileList.size(); i++)
		{
			renderEntityProjectile(world.projectileList.get(i));
		}
	}
	
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param enemy enemy to draw
	 */
	public void renderEntityNPCEnemy(DisplayableEntity enemy)
	{
		GL11.glColor4f(1, 1, 1, 1);
		double x = (int)enemy.x;
        double y = (int)enemy.y;
        double eh = enemy.getHeight();
        double ew = enemy.getWidth();
		double tx = (double)enemy.iconX * (32.0 / MONSTERS_SHEET_WIDTH);
		double ty = (double)enemy.iconY * (32.0 / MONSTERS_SHEET_HEIGHT); 
		double tw = (double)enemy.textureWidth / MONSTERS_SHEET_WIDTH;
		double th = (double)enemy.textureHeight / MONSTERS_SHEET_HEIGHT;
        
		monsterSheet.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + eh, 0, tx, ty + th);
        t.addVertexWithUV(x + ew, y + eh, 0, tx + tw, ty + th);
        t.addVertexWithUV(x + ew, y, 0, tx + tw, ty);
        t.addVertexWithUV(x, y, 0, tx, ty);
        t.draw();          
	}
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param enemy enemy to draw
	 */
	public void renderEntityNPC(DisplayableEntity npc)
	{
		GL11.glColor4f(1, 1, 1, 1);
		double x = (int)npc.x;
        double y = (int)npc.y;
        double eh = npc.getHeight();
        double ew = npc.getWidth();
        double tx = (double)npc.iconX * (32.0 / MONSTERS_SHEET_WIDTH);
		double ty = (double)npc.iconY * (32.0 / MONSTERS_SHEET_HEIGHT);  
		double tw = (double)npc.textureWidth / MONSTERS_SHEET_WIDTH;
		double th = (double)npc.textureHeight / MONSTERS_SHEET_HEIGHT;
        
		monsterSheet.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + eh, 0, tx, ty + th);
        t.addVertexWithUV(x + ew, y + eh, 0, tx + tw, ty + th);
        t.addVertexWithUV(x + ew, y, 0, tx + tw, ty);
        t.addVertexWithUV(x, y, 0, tx, ty);
        t.draw();        
	}
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param EntityProjectile to draw
	 */
	public void renderEntityProjectile(DisplayableEntity projectile)
	{
		GL11.glColor4f(1, 1, 1, 1);
		double x = (int)projectile.getX();
        double y = (int)projectile.getY();
        double ph = projectile.height;
		double pw = projectile.width;
		double tx = (double)(projectile.iconX * 16) / 256;
	    double ty = (double)(projectile.iconY * 16) / 256;
		double tw = tx + ((double)projectile.width  / (16 * 6));
		double th = ty + ((double)projectile.height / (16 * 6));
		PROJECTILES.bind();
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + ph, 0, tx, th);
        t.addVertexWithUV(x + pw, y + ph, 0, tw, th);
        t.addVertexWithUV(x + pw, y, 0, tw, ty);
        t.addVertexWithUV(x, y, 0, tx, ty);
        t.draw();        
	}	
	
	/**
	 * Renders all items in world.items
	 */
	public void renderItems(World world)
	{
		for(int i = 0; i < world.itemsList.size(); i++)
		{
			GL11.glColor4f(1, 1, 1, 1);
			double x = world.itemsList.get(i).x;
	        double y = world.itemsList.get(i).y;
			double w = world.itemsList.get(i).width;
			double h = world.itemsList.get(i).height;
			textures[world.itemsList.get(i).iconX].bind(); //TODO fix this really, really terrible thing that's going on right here
	        t.startDrawingQuads();
	        t.addVertexWithUV(x, y + h, 0, 0, 1);
	        t.addVertexWithUV(x + w, y + h, 0, 1, 1);
	        t.addVertexWithUV(x + w, y, 0, 1, 0);
	        t.addVertexWithUV(x, y, 0, 0, 0);
	        t.draw();     
		}
	}
	
	/**
	 * Renders all the temperary from the world object
	 */
	public void renderTemperaryText(World world)
	{
		for(int i = 0; i < world.temporaryText.size(); i++)
		{
			WorldText temp = (WorldText)(world.temporaryText.get(i));
			renderText(temp.message, temp.x, temp.y, temp.color.COLOR);		
		}
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void renderText(String message, int x, int y, double[] colours)
	{
		GL11.glColor4d(colours[0], colours[1], colours[2], colours[3]);
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
		
}
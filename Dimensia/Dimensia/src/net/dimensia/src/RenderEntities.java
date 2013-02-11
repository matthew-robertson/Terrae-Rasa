package net.dimensia.src;
import org.lwjgl.opengl.GL11;

public class RenderEntities extends Render
{
	/**
	 * Renders the player entity
	 */
	public void renderPlayer(World world, EntityLivingPlayer player)
	{
		GL11.glColor4f(1, 1, 1, 1);
		if(player.isFacingRight) //facing right (default)
		{
			float x = player.x;
	        float y = player.y;
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
			GL11.glTranslatef(player.x, player.y, 0);
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
	}

	/**
	 * Renders all entities in world.entityList
	 */
	public void renderWorldEntityList(World world)
	{
		for(int i = 0; i < world.entityList.size(); i++)
		{
			renderEntityLivingNPCEnemy(world.entityList.get(i).getTexture(), (EntityLivingNPCEnemy) world.entityList.get(i));
		}
	}
	
	/**
	 * renders all npcs in world.npcList
	 */
	public void renderNPCs(World world){
		for(int i = 0; i < world.npcList.size(); i++){
			renderEntityLivingNPC(world.npcList.get(i).getTexture(), (EntityLivingNPC) world.npcList.get(i));
		}
	}

	/**
	 * Renders all entities in world.projectileList
	 */
	public void renderWorldProjectileList(World world)
	{
		for(int i = 0; i < world.projectileList.size(); i++)
		{
			renderEntityProjectile((EntityProjectile) world.projectileList.get(i));
		}
	}
	
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param enemy enemy to draw
	 */
	public void renderEntityLivingNPCEnemy(Texture tex, EntityLivingNPCEnemy enemy)
	{
		GL11.glColor4f(1, 1, 1, 1);
		float x = enemy.x;
        float y = enemy.y;
        float eh = enemy.getHeight();
        float ew = enemy.getWidth();
				
        //System.out.println("[RenderEntities]" + enemy + " " + tex);
        
		tex.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + eh, 0, 0, 1);
        t.addVertexWithUV(x + ew, y + eh, 0, 1, 1);
        t.addVertexWithUV(x + ew, y, 0, 1, 0);
        t.addVertexWithUV(x, y, 0, 0, 0);
        t.draw();        
	}
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param enemy enemy to draw
	 */
	public void renderEntityLivingNPC(Texture tex, EntityLivingNPC npc)
	{
		GL11.glColor4f(1, 1, 1, 1);
		float x = npc.x;
        float y = npc.y;
        float eh = npc.getHeight();
        float ew = npc.getWidth();
				
		tex.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + eh, 0, 0, 1);
        t.addVertexWithUV(x + ew, y + eh, 0, 1, 1);
        t.addVertexWithUV(x + ew, y, 0, 1, 0);
        t.addVertexWithUV(x, y, 0, 0, 0);
        t.draw();        
	}
	
	/**
	 * Draws a single entity of specified type
	 * @param tex texture of entity being drawn <<<< likely being replaced
	 * @param EntityProjectile to draw
	 */
	public void renderEntityProjectile(EntityProjectile projectile)
	{
		GL11.glColor4f(1, 1, 1, 1);
		float x = projectile.getX();
        float y = projectile.getY();
        float ph = projectile.height;
		float pw = projectile.width;
		float tx = (float)((projectile.iconIndex / 16) * 16) / 256;
	    float ty = (float)((projectile.iconIndex % 16) * 16) / 256;
		float tw = tx + ((float)projectile.blockWidth  / 16);
		float th = ty + ((float)projectile.blockHeight / 16);
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
			float x = world.itemsList.get(i).x;
	        float y = world.itemsList.get(i).y;
			float w = world.itemsList.get(i).width;
			float h = world.itemsList.get(i).height;
			textures[world.itemsList.get(i).stack.getItemID()].bind(); 
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
			TemporaryTextStore temp = (TemporaryTextStore)(world.temporaryText.get(i));
			//char of h->healing; d->damage; w->white; g->grey; c->critical; b->mana(blue)
			if(temp.type == 'h') //healing
			{
				renderGreenText(temp.message, temp.x, temp.y);		
			}
			else if(temp.type == 'd') //damage
			{
				renderRedText(temp.message, temp.x, temp.y);		
			}
			else if(temp.type == 'w')//white
			{
				renderWhiteText(temp.message, temp.x, temp.y);
			}
			else if(temp.type == 'c') //Crit
			{
				renderCriticalText(temp.message, temp.x, temp.y);
			}
			else if(temp.type == 'b') //Mana(Blue)
			{
				renderBlueText(temp.message, temp.x, temp.y);
			}
			else 			
			{
				renderGreyText(temp.message, temp.x, temp.y);
			}
		}
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void renderGreenText(String message, int x, int y)
	{
		GL11.glColor4f(0, 1, 0, 1);
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
	
	public void renderWhiteText(String message, int x, int y)
	{
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
	
	public void renderGreyText(String message, int x, int y)
	{
		GL11.glColor4f(0.66666666f, 0.66666666f, 0.66666666f, 1);
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
	
	public void renderRedText(String message, int x, int y)
	{
		GL11.glColor4f(1, 0, 0, 1);		
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
	
	public void renderBlueText(String message, int x, int y)
	{
		GL11.glColor4f(0, 0, 1, 1);		
		trueTypeFont.drawString(x, y, message, 0.4f, -0.4f, TrueTypeFont.ALIGN_LEFT);
	}
	
	public void renderCriticalText(String message, int x, int y)
	{
		GL11.glColor4f(1, 0.5f, 0, 1);		
		trueTypeFont.drawString(x, y, message, 0.5f, -0.5f, TrueTypeFont.ALIGN_LEFT);
	}
}
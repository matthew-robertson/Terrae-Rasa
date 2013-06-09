package render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import client.Settings;

import entities.EntityLivingPlayer;

import world.World;

public class RenderWorldSky extends RenderWorld
{
	public void render(World world, EntityLivingPlayer player, Settings settings) 
	{	
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0,0,0,0);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);

		renderSkyBackgroundScene(); //Renders the background image thing		
		adjustCamera(world, player); //Adjusts the camera before rendering the world		
		renderBlocks.renderBackwall(world, player);
		renderBlocks.render(world, player);	
		renderEntities.renderWorldEntityList(world);
		renderEntities.renderWorldProjectileList(world);
		renderEntities.renderPlayer(world, player); 		
		renderEntities.renderItems(world);
		renderEntities.renderNPCs(world);
		renderEntities.renderTemperaryText(world);		
		renderToolSwing(world, player);
		renderParticles.render(world);
		//renderLighting(world); 

		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST); //cuts out blank junk in the background of text, etc	
	
		renderUI.render(world, player, settings);		
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);			
		GL11.glPopMatrix();
	
		try 
		{
			Display.swapBuffers();
		    Display.update(); //updates the display
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
}
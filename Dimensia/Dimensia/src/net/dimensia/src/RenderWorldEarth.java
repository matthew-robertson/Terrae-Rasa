package net.dimensia.src;

import org.lwjgl.opengl.GL11;

public class RenderWorldEarth extends RenderWorld
{	
	RenderLight renderLight = new RenderLight();
	public void render(World world, EntityLivingPlayer player) 
	{		
		GL11.glEnable(GL11.GL_BLEND);

	    renderSkyBackgroundScene(); //Renders the background image thing		
		adjustCamera(world, player); //Adjusts the camera before rendering the world		
		//renderBlocks.renderBackwall(world, player);
				
		renderBlocks.render(world, player);	
		renderEntities.renderWorldEntityList(world);
		renderEntities.renderWorldProjectileList(world);
		renderEntities.renderPlayer(world, player); 		
		renderEntities.renderItems(world);
		renderEntities.renderNPCs(world);
		renderEntities.renderTemperaryText(world);		
		renderToolSwing(world, player);
		renderParticles.render(world);
		renderLight.render(world, player);

		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST); //cuts out blank junk in the background of text, etc	
	
		renderUI.render(world, player);		
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);	
	}
}
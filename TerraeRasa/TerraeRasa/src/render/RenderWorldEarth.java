package render;


import org.lwjgl.opengl.GL11;

import ui.UI;
import world.World;
import client.Settings;
import entities.EntityPlayer;

public class RenderWorldEarth extends RenderWorld
{	
	RenderLight renderLight = new RenderLight();
	public void render(World world, EntityPlayer player, Settings settings) 
	{		
		GL11.glEnable(GL11.GL_BLEND);

	    renderSkyBackgroundScene(); //Renders the background image thing		
		adjustCamera(world, player); //Adjusts the camera before rendering the world		
		
		renderBlocks.renderBackwall(world, player);
		renderBlocks.render(world, player);	
		renderEntities.renderWorldEntityList(world);
		renderEntities.renderWorldProjectileList(world);
		for(EntityPlayer aPlayer : world.otherPlayers)
		{
			renderEntities.renderPlayer(world, aPlayer); 		
		}
		renderEntities.renderItems(world);
		renderEntities.renderNPCs(world);
		renderEntities.renderTemperaryText(world);		
		renderToolSwing(world, player);
		renderParticles.render(world);
		renderLight.render(world, player);

		//
		//
		//adjustCameraToLastPosition();
		//zombie_test_animation.render(getCameraX() + 50, getCameraY() + 50, 32/2, 48/2);
		//
		//
		
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST); //cuts out blank junk in the background of text, etc	
	
		UI.render(world, player, settings);		
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);	
	}
}
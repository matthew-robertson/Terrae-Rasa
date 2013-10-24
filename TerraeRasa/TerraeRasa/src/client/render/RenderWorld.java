package client.render;


import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import transmission.ClientUpdate;
import client.Settings;
import client.entities.EntityPlayer;
import client.ui.UI;
import client.world.WorldClientEarth;

public class RenderWorld extends Render
{	
	private static RenderLight renderLight = new RenderLight();
	private static RenderParticles renderParticles = new RenderParticles();
	private static RenderEntities renderEntities = new RenderEntities();
	private static RenderBlocks renderBlocks = new RenderBlocks();
	
	/**
	 * Renders a sky background image. This is a filler for now.
	 */
	private static void renderSkyBackgroundScene()
	{		
		background_1.bind();        
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(0, Display.getHeight() / 2, 0, 0, 1);
        t.addVertexWithUV(Display.getWidth() / 2, Display.getHeight() / 2, 0, 1, 1);
        t.addVertexWithUV(Display.getWidth() / 2, 0, 0, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();
	}
	
	public static void render(ClientUpdate update, WorldClientEarth world, List<EntityPlayer> otherPlayers, EntityPlayer player, Settings settings) 
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
		//renderToolSwing(world, player);
		renderParticles.render(world);
		renderLight.render(world, otherPlayers, player);

		//
		//
		//adjustCameraToLastPosition();
		//zombie_test_animation.render(getCameraX() + 50, getCameraY() + 50, 32/2, 48/2);
		//
		//
		
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST); //cuts out blank junk in the background of text, etc	
	
		UI.render(update, world, player, settings);		
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);	
	}
}
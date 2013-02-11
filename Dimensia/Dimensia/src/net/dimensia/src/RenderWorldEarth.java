package net.dimensia.src;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderWorldEarth extends RenderWorld
{	
	public void render(World world, EntityLivingPlayer player) 
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
	
		renderUI.render(world, player);		
		
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
	
	public void renderLighting(World world)
	{
		System.err.println("RenderWorld.renderLighting overriden");
		
		/*
		boolean isEmitingLight = false;
		
		//The proper lines are:
		
		if(world.player.inventory.getMainInventoryStack(world.player.selectedSlot) != null)
		{
			if((world.player.inventory.getMainInventoryStack(world.player.selectedSlot).getItemID() == Block.torch.blockID))
			{
				isEmitingLight = true;
			}	
		}
		
		//BUT it's changed so that lighting can render
		//This is what messes up the lighting when a null slot is selected.
		/*
		if(world.player.inventory.mainInventory[selectedSlot] == null || (world.player.inventory.mainInventory[selectedSlot].itemID == Block.torch.blockID))
		{
			return;
		}
		//**
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		int xm = 250;
		int ym = 1500; //50 1482
		
		int strength = 15;

		float[][] lightMap = LightingEngine.generateLightSource(world, (int)(world.player.x / 6), (int)(world.player.y / 6), strength, 1.0f);
		
		t.startDrawingQuads();
		
		final int xsize = (int)((float)(Display.getWidth() / 6) / 1.85);//70@800;
		final int ysize = (int)((float)(Display.getHeight() / 6) / 1.85);//53@600;
		float x = (world.player.x - (Display.getWidth() * 0.25f)) / 6;
		float y = (world.player.y - (Display.getHeight() * 0.208f)) / 6;
		if(y < 0) y = 0; //In Bounds Checking
		if(x < 0) x = 0;
		if(xsize + x > world.getWidth()) x = world.getWidth() - xsize - 1;
		if(ysize + y > world.getHeight()) y = world.getHeight() - ysize - 1;		
		
		int playerx = (int) (world.player.x / 6);
		int playery = (int) (world.player.y / 6);
		
		//System.err.println(playerx + " " + playery);
		for(int i = (int)x; i < (int)x + (int)xsize; i++) //Draw the tiles (X)
		{	
			for(int k = (int)y; k < (int)y + (int)ysize; k++) //(Y)
			{
				if(isEmitingLight && i > MathHelper.zeroOrGreater(playerx - strength - 1) && i < playerx + strength && k > playery - strength - 1 && k < playery + strength)
				{
					continue;
				}
				xm = i * 6;
				ym = k * 6;
				
				t.setColorRGBA_F(0, 0, 0, world.lightMap[i][k + 1]);
				t.addVertexWithUV(xm , ym + 6, 0, 0, 1);
				t.setColorRGBA_F(0, 0, 0, world.lightMap[i + 1][k + 1]);
				t.addVertexWithUV(xm + 6, ym + 6, 0, 1, 1);
				t.setColorRGBA_F(0, 0, 0, world.lightMap[i + 1][k]);
				t.addVertexWithUV(xm + 6, ym, 0, 1, 0);
				t.setColorRGBA_F(0, 0, 0, world.lightMap[i][k]);
				t.addVertexWithUV(xm, ym, 0, 0, 0);
			}
		}
		
		if(isEmitingLight) //Torch, handheld
		{
			for(int i = 0; i < strength * 2; i++) //Draw the tiles (X)
			{	
				for(int k = 0; k < strength * 2; k++) //(Y)
				{
					xm = (int) MathHelper.zeroOrGreater(MathHelper.multipleOfSix((int) ((i - strength) * 6 + (world.player.x / 6) * 6)));
					ym = (int) MathHelper.zeroOrGreater(MathHelper.multipleOfSix((int) ((k - strength) * 6 + (world.player.y / 6) * 6)));
					
					//System.err.println(">" + xm + " " + ym);
					//(int)world.player.x / 6, (int)world.player.y / 6
					
					t.setColorRGBA_F(0, 0, 0, lightMap[i][k + 1]);
					t.addVertexWithUV(xm , ym + 6, 0, 0, 1);
					t.setColorRGBA_F(0, 0, 0, lightMap[i + 1][k + 1]);
					t.addVertexWithUV(xm + 6, ym + 6, 0, 1, 1);
					t.setColorRGBA_F(0, 0, 0, lightMap[i + 1][k]);
					t.addVertexWithUV(xm + 6, ym, 0, 1, 0);
					t.setColorRGBA_F(0, 0, 0, lightMap[i][k]);
					t.addVertexWithUV(xm, ym, 0, 0, 0);
				}
			}
			
			//TODO: Bug where the torch doesnt render the bottom and rightmost columns at all.
		}
	
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		*/
	}
}
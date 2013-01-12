package net.dimensia.src;

import java.awt.Font;

import net.dimensia.client.Dimensia;

public class RunnableLoadResources extends Render implements Runnable
{
	public void run() 
	{
		if(true)throw new RuntimeException("Threading Fail");
		
		TrueTypeFont ttf = new TrueTypeFont(new Font("Comic Sans MS", Font.BOLD, 32), false);
		trueTypeFont  = ttf;
		loader = new TextureLoader();
		backgroundImages = new Texture[12];
		textures = loader.getItemTextureArray();
		
		//<menu textures>
		moons = loader.getMoonTextureArray();
		sun = loader.getTexture("Resources/terraria_sun.png");
		background_1 = loader.getTexture("Resources/terraria_background_2.png");		
		buttonTexture = loader.getTexture("Resources/background_dark_dirt.png");		
		//</menu textures>
		
		a_water = new Animation("Resources/water_animation.png",16,16, 100);

		goblin = loader.getTexture("Resources/goblin.png");
		zombie = loader.getTexture("Resources/zombie.png");
		slime = loader.getTexture("Resources/slime.png");
		
		snow_tex = loader.getTexture("Resources/snow_particle.png");
		ICONS = loader.getTexture("Resources/icons.png");
		TERRAIN = loader.getTexture("Resources/terrain.png");
		logo = loader.getTexture("Resources/terraria_logo.png");
		background2 = loader.getTexture("Resources/terraria_background_2.png");
		playerTexture = loader.getTexture("Resources/player_1.png");
		player_heart = loader.getTexture("Resources/player_heart.png");
		player_mana = loader.getTexture("Resources/player_mana.png");
		actionbarSlot = loader.getTexture("Resources/player_actionbar_slot.png");
		actionbarOutline = loader.getTexture("Resources/player_actionbar_outline.png");
		player_garbage = loader.getTexture("Resources/player_garbage.png");
		renderParticles = new RenderParticles();
		renderUI = new RenderUI();
		renderEntities = new RenderEntities();
		renderBlocks = new RenderBlocks();
		
		Dimensia.areResourcesLoaded = true;
	}
}

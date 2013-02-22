package net.dimensia.client;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.dimensia.src.Block;
import net.dimensia.src.EntityLivingPlayer;
import net.dimensia.src.EnumDifficulty;
import net.dimensia.src.ErrorUtils;
import net.dimensia.src.FileManager;
import net.dimensia.src.GuiMainMenu;
import net.dimensia.src.Item;
import net.dimensia.src.ItemStack;
import net.dimensia.src.Keys;
import net.dimensia.src.LightUtils;
import net.dimensia.src.MouseInput;
import net.dimensia.src.RenderGlobal;
import net.dimensia.src.RenderMenu;
import net.dimensia.src.Settings;
import net.dimensia.src.SoundManager;
import net.dimensia.src.World;
import net.dimensia.src.WorldHell;
import net.dimensia.src.WorldSky;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameEngine 
{
	public final static int RENDER_MODE_WORLD_EARTH = 1,
							   RENDER_MODE_WORLD_HELL = 2,
							   RENDER_MODE_WORLD_SKY  = 3;
	public int renderMode;
	public SoundManager soundManager;
	public GuiMainMenu mainMenu;
	public World world;
	public WorldHell worldHell;
	public WorldSky worldSky;
	public EntityLivingPlayer player;
	public Settings settings;
	public RenderMenu renderMenu;
	
	public GameEngine()
	{
		renderMode = RENDER_MODE_WORLD_EARTH;
		settings = new Settings();
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public void run()
	{
		try
		{
			//Variables for the gameloop cap (20 times / second)
	        final int TICKS_PER_SECOND = 20;
			final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
			final int MAX_FRAMESKIP = 5;
			long next_game_tick = System.currentTimeMillis();
			long start, end;
			int loops;
			
		    while(!Dimensia.done) //Main Game Loop
		    {
		    	start = System.currentTimeMillis();
		    			    	
		        loops = 0;
		        while(System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) //Update the game 20 times/second 
		        {
		        	if(settings.menuOpen /*&& Dimensia.isMainMenuOpen*/)
		        	{ 
		        	}
		        	else if(!Dimensia.isMainMenuOpen) //Handle game inputs if the main menu isnt open (aka the game is being played)
		        	{
		        		MouseInput.mouse(world, player);
		        		Keys.keyboard(world, player, settings);	            
		        		
		        		if(renderMode == RENDER_MODE_WORLD_EARTH) //Player is in the overworld ('earth')
		        		{
		        			world.onWorldTick(player);
		        		}
		        		else if(renderMode == RENDER_MODE_WORLD_HELL) //Player is in the hell dimension 
		        		{
		        			
		        		}
		        		else if(renderMode == RENDER_MODE_WORLD_SKY) //Player is in the sky dimension
		        		{
		        			
		        		}
		        	}
		        	
					if(Dimensia.needsResized || Dimensia.width < 640 || Dimensia.height < 400) //If the window needs resized, resize it
					{
						Dimensia.dimensia.resizeWindow();
						Dimensia.needsResized = false;
					}
		        	 
		        	next_game_tick += SKIP_TICKS;
		            loops++;
		        }
		        
		        //Make sure the game loop doesn't fall very far behind and have to accelerate the 
		        //game for an extended period of time
		        if(System.currentTimeMillis() - next_game_tick > 1000)
		        {
		        	next_game_tick = System.currentTimeMillis();
		        }
		        
		        Display.update();
		    	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glClearColor(0,0,0,0);
				GL11.glColor4f(1, 1, 1, 1);
				GL11.glPushMatrix();
		        
		        
		        if(Dimensia.isMainMenuOpen) //if the main menu is open, render that, otherwise render the game
		        {
		        	mainMenu.render();
			    }
		        else
		        {
		        	if(renderMode == RENDER_MODE_WORLD_EARTH)
		    		{
		        	 	RenderGlobal.render(world, player, renderMode); //Renders Everything on the screen for the game
		     		}
		    		else if(renderMode == RENDER_MODE_WORLD_HELL)
		    		{
		    		 	RenderGlobal.render(worldHell, player, renderMode); //Renders Everything on the screen for the game
		    		}
		    		else if(renderMode == RENDER_MODE_WORLD_SKY)
		    		{
		    		 	RenderGlobal.render(worldSky, player, renderMode); //Renders Everything on the screen for the game
		    		}
		        }
		        
		        if(settings.menuOpen)
		        {
		        	renderMenu.render(world, settings);
		        }
		        
		        GL11.glPopMatrix();
		        
		        Display.swapBuffers(); //allows the display to update when using VBO's, probably
		        Display.update(); //updates the display

				
		        
	        	end = System.currentTimeMillis();        	
	       //     System.out.println(end - start);
		    }     
		}
		catch(Exception e) //Fatal error catching
		{
			e.printStackTrace();			
			ErrorUtils errorUtils = new ErrorUtils();
			errorUtils.writeErrorToFile(e, true);			
		}
	}
	
	/**
	 * Starts a game from the main menu.
	 * @param world the world to play on.
	 * @param player the player to play on.
	 */
	public void startGame(World world, EntityLivingPlayer player)
	{
		this.world = world;
		this.player = player;
		world.addPlayerToWorld(player);
		Dimensia.isMainMenuOpen = false;
		mainMenu = null;
	}
	
	/**
	 * Misc. things required to initalize the game
	 */
	public void init()
	{
		mainMenu = new GuiMainMenu();
		renderMenu = new RenderMenu();
		
		if(Dimensia.initInDebugMode)
		{
			Dimensia.isMainMenuOpen = false;
			FileManager fileManager = new FileManager();
			world = fileManager.generateNewWorld("World", 1200, 800, EnumDifficulty.EASY);//EnumWorldSize.LARGE.getWidth(), EnumWorldSize.LARGE.getHeight());
			player = fileManager.generateAndSavePlayer("!!", EnumDifficulty.NORMAL);//new EntityLivingPlayer("Test player", EnumDifficulty.NORMAL);
			world.addPlayerToWorld(player);
			world.assessForAverageSky();
			LightUtils utils = new LightUtils();
			utils.applyAmbient(world);
			
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.stone, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.torch, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.chest, 100));
			//*
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.healthPotion1, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.healthPotion2, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaPotion1, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaPotion2, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaStar, 100));
			/**/
			
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.furnace, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.craftingTable, 6));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.plank, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldPickaxe));
		
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperIngot, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironIngot, 6));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperCoin, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 5));
			
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperOre, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.tinOre, 100));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldOre, 15));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.tinIngot, 100));
			
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldHelmet));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldHelmet));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldBody));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldGreaves));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeHelmet));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeBody));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeGreaves));

			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.rocketBoots));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.heartCrystal, 2));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaCrystal, 2));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.angelsSigil, 2));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ringOfVigor, 5));
			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.regenerationPotion2, 5));
			//world.player.registerStatusEffect(new StatusEffectDazed(500000, 1));
			
		}		
	}

	public World getWorld()
	{
		return world;
	}
	
	public void changeWorld(int newMode)
			throws IOException, ClassNotFoundException
	{
		String worldName = "";
		if(renderMode == RENDER_MODE_WORLD_EARTH)
		{
			worldName = world.getWorldName();
			world.saveRemainingWorld("Earth");
			world = null;
		}
		else if(renderMode == RENDER_MODE_WORLD_HELL)
		{
			worldName = worldHell.getWorldName();
			worldHell.saveRemainingWorld("Hell");
			worldHell = null;
		}
		else if(renderMode == RENDER_MODE_WORLD_SKY)
		{
			worldName = worldSky.getWorldName();
			worldSky.saveRemainingWorld("Sky");
			worldSky = null;
		}
		
		this.renderMode = newMode;
		FileManager manager = new FileManager();
		
		if(renderMode == RENDER_MODE_WORLD_EARTH)
		{
			manager.loadWorld("Earth", worldName);
		}
		else if(renderMode == RENDER_MODE_WORLD_HELL)
		{
			manager.loadWorld("Hell", worldName);
		}
		else if(renderMode == RENDER_MODE_WORLD_SKY)
		{
			manager.loadWorld("Sky", worldName);
		}
	}
	
	public void closeGameToMenu() 
			throws FileNotFoundException, IOException
	{
		FileManager manager = new FileManager();
		manager.savePlayer(player);
	
		if(renderMode == RENDER_MODE_WORLD_EARTH)
		{
			world.saveRemainingWorld("Earth");
		}
		else if(renderMode == RENDER_MODE_WORLD_HELL)
		{
			world.saveRemainingWorld("Hell");
		}
		else if(renderMode == RENDER_MODE_WORLD_SKY)
		{
			world.saveRemainingWorld("Sky");
		}
	}
}

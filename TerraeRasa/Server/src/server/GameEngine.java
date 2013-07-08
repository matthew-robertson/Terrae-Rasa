package server;

import java.io.FileNotFoundException;
import java.io.IOException;

import utils.ErrorUtils;

/**
 * <code>GameEngine</code> is the class responsible for running the main game loop, and other core features of multiple worlds.
 * <code>GameEngine</code> defines 4 important methods. 
 * <br><br>
 * The method {@link #startGame(World, EntityPlayer)} defines the method to actually start the game with the specified World object
 * and player. This will close the main menu and begin rendering based on the Chunk data. 
 * <br><br>
 * Most of the application's life cycle is spent in {@link #run()} method, which contains the main game loop. This handles
 * everything from the main menu to the game, and save menu. Before exiting this method, {@link #saveSettings()} will be called
 * to update any settings changed during runtime. 
 * <br><br>
 * Additional methods of interest: <br>
 * {@link #changeWorld(int)}, {@link #closeGameToMenu()}
 *   
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GameEngine 
{
//	/** The number of game ticks per second - this will always be 20 */
//	public static final int TICKS_PER_SECOND = 20;
//	private World world;
//	private EntityPlayer player;
//	public ChunkManager chunkManager;
//	private String universeName;
//	
//	/**
//	 * Creates a new instance of GameEngine. This includes setting the renderMode to RENDER_MODE_WORLD_EARTH
//	 * and loading the settings object from disk. If a settings object cannot be found a new one is created. 
//	 */
//	public GameEngine(String universeName)
//	{
//		this.universeName = universeName;
//	}
//	
//	public void run()
//	{
//		try
//		{
//			//Variables for the gameloop cap (20 times / second)
//	        
//			final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
//			final int MAX_FRAMESKIP = 5;
//			long next_game_tick = System.currentTimeMillis();
//			long start, end;
//			long fps = 0;
//			int loops;
//			start = System.currentTimeMillis();
//			
//		    while(!TerraeRasa.done) //Main Game Loop
//		    {
//		        loops = 0;
//		        while(System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) //Update the game 20 times/second 
//		        {
//		        	if(player != null && player.defeated)
//		        	{
//		        		hardcoreDeath();
//		        	}		        	
//		        	  		
//		        	world.onWorldTick(player);
//		        	
//		        	next_game_tick += SKIP_TICKS;
// 		            loops++;
//		        }
//		        
//		        //Make sure the game loop doesn't fall very far behind and have to accelerate the 
//		        //game for an extended period of time
//		        if(System.currentTimeMillis() - next_game_tick > 1000)
//		        {
//		        	next_game_tick = System.currentTimeMillis();
//		        }
//		        
//		        
//		   
//		        if(System.currentTimeMillis() - start >= 5000)
//		        {
//		        	start = System.currentTimeMillis();
//	        		end = System.currentTimeMillis();     
//	        		fps = 0;
//		    	}
//	        	//     System.out.println(end - start);
//		    }     
//		}
//		catch(Exception e) //Fatal error catching
//		{
//			e.printStackTrace();			
//			ErrorUtils errorUtils = new ErrorUtils();
//			errorUtils.writeErrorToFile(e, true);			
//		}
//		finally
//		{
//			TerraeRasa.done = true;
//			//issue some sort of cease and desist to the main thread and clients
//		}
//	}
//	
//	/**
//	 * Starts a game from the main menu.
//	 * @param world the world to play on.
//	 * @param player the player to play on.
//	 */
//	public void startGame(String universeName, World world, EntityPlayer player)
//	{
////		if(this.world != null)
////		{
////			throw new RuntimeException("World already exists!");
////		}
//		this.universeName = universeName;
//		this.world = world;
//		this.world.chunkManager = chunkManager;
//		this.world.chunkManager.setUniverseName(universeName);
//		this.world.initSoundEngine(soundEngine);
//		this.player = player;
//		world.addPlayerToWorld(player);
//		TerraeRasa.isMainMenuOpen = false;
//		mainMenu = null;
//	}
//		
//	/**
//	 * Initializes miscellaneous things within the game engine. This should be called after object creation so 
//	 * that the UI thread does not throw an exception while loading textures.
//	 */
//	public void init()
//	{
//		chunkManager = new ChunkManager();
//	}
//	
//	/**
//	 * Loads the Settings for the entire game upon starting the game.
//	 * @throws IOException Indicates the saving operation has failed
//	 * @throws ClassNotFoundException Indicates the Settings class does not exist with the correct version
//	 */
//	public void loadSettings() 
//			throws IOException, ClassNotFoundException
//	{
//		FileManager fileManager = new FileManager();
//		this.settings = fileManager.loadSettings();
//	}
//	
//	/**
//	 * Saves the Settings for the entire game to disk, this is called before exiting the run method.
//	 * @throws IOException Indicates the saving operation has failed
//	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
//	 */
//	public void saveSettings() 
//			throws FileNotFoundException, IOException
//	{
//		FileManager fileManager = new FileManager();
//		fileManager.saveSettings(settings);
//	}
//	
//	
//	
//	/**
//	 * Initiates a hardcore death - which deletes the player and exits to the main menu. For now.
//	 */
//	public void hardcoreDeath()
//	{
//		//Delete the player
//		FileManager manager = new FileManager();
//		manager.deletefile("/Player Saves/" + player.getName());
//		this.player = null;
//		//Save the world
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			world.saveRemainingWorld();
//		}
//		//Reset to the main menu
//		resetMainMenu();
//		TerraeRasa.isMainMenuOpen = true; //Sets menu open
//		System.out.println("Game Over.");
//	}
//	
//	public void setWorld(World world)
//	{
//		this.world = world;
//	}
//	
//	/**
//	 * Gets the world object currently in use by the game. This will be null if the main menu is open or something breaks.
//	 * @return the world object for the current game, which may be null if on the main menu
//	 */
//	public World getWorld()
//	{
//		return world;
//	}
//	
//	/**
//	 * Changes the loaded world to something else. For example, changing from Earth to Hell would use this. Calling
//	 * this method forces a save of the remaining World and then loads what's required for the new World. The value 
//	 * of the param newMode should correspond to the class variables in GameEngine such as RENDER_MODE_WORLD_EARTH or
//	 * RENDER_MODE_WORLD_HELL. Supplying an incorrect value will not load a new World.
//	 * @param newMode the final integer value for the new world (indicating what object to manipulate)
//	 * @throws IOException indicates a general failure to load the file, not relating to a version error
//	 * @throws ClassNotFoundException indicates the saved world version is incompatible
//	 */
//	public void changeWorld(int newMode)
//			throws IOException, ClassNotFoundException
//	{
//		String worldName = "";
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			world.saveRemainingWorld();
//			world = null;
//		}
//		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		{
//			worldHell.saveRemainingWorld();
//			worldHell = null;
//		}
//		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		{
//			worldName = worldSky.getWorldName();
//			worldSky.saveRemainingWorld();
//			worldSky = null;
//		}
//		
//		this.renderMode = newMode;
//		FileManager manager = new FileManager();
//		
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			manager.loadWorld("Earth", worldName);
//		}
//		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		{
//			manager.loadWorld("Hell", worldName);
//		}
//		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		{
//			manager.loadWorld("Sky", worldName);
//		}
//	}
//	
//	/**
//	 * Saves the remaining world that's loaded and the player to their respective save locations before
//	 * exiting to the main menu.
//	 * @throws FileNotFoundException indicates a failure to find the save location of the player or world
//	 * @throws IOException indicates a general failure to save, not relating to the file
//	 */
//	public void closeGameToMenu() 
//			throws FileNotFoundException, IOException
//	{
//		if(!player.defeated)
//		{
//			FileManager manager = new FileManager();
//			manager.savePlayer(player);
//		}
//		
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			world.saveRemainingWorld();
//		}
//		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		{
//			world.saveRemainingWorld();
//		}
//		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		{
//			world.saveRemainingWorld();
//		}
//		
//		TerraeRasa.isMainMenuOpen = true;
//		resetMainMenu();
//	}
//	
//	/**
//	 * Creates a new GuiMainMenu and assigns it to mainMenu.
//	 */
//	public void resetMainMenu()
//	{
//		mainMenu = new MainMenu(settings);
//	}
}
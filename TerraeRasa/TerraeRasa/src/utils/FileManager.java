package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import savable.SaveManager;
import world.World;
import world.WorldGenEarth;
import world.WorldGenHell;
import world.WorldGenSky;
import world.WorldHell;
import world.WorldSky;
import client.Settings;
import client.TerraeRasa;
import entities.EntityPlayer;
import enums.EnumPlayerDifficulty;
import enums.EnumWorldDifficulty;
import enums.EnumWorldSize;


public class FileManager
{		
	private final String BASE_PATH;
	
	public FileManager()
	{
		BASE_PATH = TerraeRasa.getBasePath();
	}
	
	/**
	 * Gets a new world, with specified settings, and saves it to file before returning it.
	 * @param name the name of the world to create
	 * @param worldSize the Enum used to indicate the world's size
	 * @param difficulty the Enum used to indicate the world's difficulty
	 * @return the newly generated world or in case of failure, null
	 */
	public World generateAndSaveWorld(String name, EnumWorldSize worldSize, EnumWorldDifficulty difficulty)
	{
		World world = generateNewWorld(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		verifyDirectoriesExist();
		new File(TerraeRasa.getBasePath() + "/World Saves/" + name).mkdir();
		new File(TerraeRasa.getBasePath() + "/World Saves/" + name + "/Earth").mkdir();
		world.chunkManager = TerraeRasa.terraeRasa.gameEngine.chunkManager;
		world.chunkManager.setUniverseName(name);
		world.saveRemainingWorld();	
		return world;
	}
	
	/**
	 * Gets a new WorldHell, with specified settings, and saves it to file before returning it.
	 * @param name the name of the WorldHell to create
	 * @param worldSize the Enum used to indicate the WorldHell's size
	 * @param difficulty the Enum used to indicate the WorldHell's difficulty
	 * @return the newly generated WorldHell or in case of failure, null
	 */
	public WorldHell generateAndSaveWorldHell(String name, EnumWorldSize worldSize, EnumWorldDifficulty difficulty)
	{
		WorldHell worldHell = generateNewWorldHell(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		worldHell.saveRemainingWorld();
		return worldHell;
	}
	
	/**
	 * Gets a new WorldSky, with specified settings, and saves it to file before returning it.
	 * @param name the name of the WorldSky to create
	 * @param worldSize the Enum used to indicate the WorldSky's size
	 * @param difficulty the Enum used to indicate the WorldSky's difficulty
	 * @return the newly generated WorldSky or in case of failure, null
	 */
	public WorldSky generateAndSaveWorldSky(String name, EnumWorldSize worldSize, EnumWorldDifficulty difficulty)
	{
		WorldSky worldSky = generateNewWorldSky(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		worldSky.saveRemainingWorld();
		return worldSky;
	}
	
	/**
	 * Loads the specified world from the ~/World Saves/ Directory
	 * @param name the name of the world to load
	 * @return the world loaded
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the World class does not exist with the correct version
	 */	
	public World loadWorld(String dir, String universeName) 
			throws IOException, ClassNotFoundException
	{
		World world = new World();
		world.loadAndApplyWorldData(BASE_PATH, universeName, dir);
		return world;
	}
		
	/**
	 * Creates a new world, complete with worldgen, and ready to use
	 * @param name the world's name
	 * @param w the width of the world
	 * @param h the height of the world
	 * @param difficulty the difficulty setting of the world (easy, medium, hardcore)
	 * @return the newly created world
	 */
	public World generateNewWorld(String name, int w, int h, EnumWorldDifficulty difficulty)
	{
		World world = new WorldGenEarth().generate(new World(name, w, h, difficulty), 0, w, 0, h);
	//LightingEngine.applySunlight(world);
		return world;
	}
	
	/**
	 * Creates a new WorldHell, complete with worldgen, and ready to use
	 * @param name the WorldHell's name
	 * @param w the width of the WorldHell
	 * @param h the height of the WorldHell
	 * @param difficulty the difficulty setting of the WorldHell (easy, medium, hardcore)
	 * @return the newly created WorldHell
	 */
	public WorldHell generateNewWorldHell(String name, int w, int h, EnumWorldDifficulty difficulty)
	{
		WorldHell worldHell = (WorldHell) new WorldGenHell().generate(new WorldHell(name, w, h, difficulty), 0, w, 0, h);
	//	LightingEngine.applySunlight(worldHell);
		return worldHell;
	}
	
	/**
	 * Creates a new WorldSky, complete with worldgen, and ready to use
	 * @param name the WorldSky's name
	 * @param w the width of the WorldSky
	 * @param h the height of the WorldSky
	 * @param difficulty the difficulty setting of the WorldSky (easy, medium, hardcore)
	 * @return the newly created WorldSky
	 */
	public WorldSky generateNewWorldSky(String name, int w, int h, EnumWorldDifficulty difficulty)
	{
		WorldSky world = (WorldSky) new WorldGenSky().generate(new World(name, w, h, difficulty), 0, w, 0, h);
	//	LightingEngine.applySunlight(world);
		return world;
	}
	
	/**
	 * Creates a new player and saves it to file immediately before returning that new player
	 * @param name Name of the player being created
	 * @param difficulty The difficulty settings (EnumDifficulty) of the player being created
	 * @return The new player created, or in the case of a failure - null;
	 */
	public EntityPlayer generateAndSavePlayer(String name, EnumPlayerDifficulty difficulty)
	{
		try 
		{
			EntityPlayer player = generateNewEntityPlayer(name, difficulty);
			savePlayer(player);
			return player;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Generates a new EntityPlayer with filler x, y positions
	 * @param name the name of the player to be generated.
	 * @param difficulty the difficulty setting (EnumDifficulty) of the player to be generated
	 * @return a newly generated EntityPlayer with given attributes 
	 */
	private EntityPlayer generateNewEntityPlayer(String name, EnumPlayerDifficulty difficulty)
	{
		return new EntityPlayer(name, difficulty);
	}
	
	/**
	 * Saves the specified player object in compressed GZIP format to the ~/Player Saves/ Directory
	 * @param player The player to be saved
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	public void savePlayer(EntityPlayer player) 
			throws FileNotFoundException, IOException
	{
		verifyDirectoriesExist();

		SaveManager manager = new SaveManager();
		manager.saveFile("/Player Saves/" + player.getName() + ".xml", player);
	}
	
	/**
	 * Ensures that the directories not involving LWJGL at least exist. In other words the following are created:
	 * <li>The base folder
	 * <li>The 'bin' folder
	 * <li>The 'World Saves' folder
	 * <li>The 'Resources' folder
	 * <li>The 'Player Saves' folder
	 */
	private void verifyDirectoriesExist()
	{
		new File(BASE_PATH);
		new File(BASE_PATH + "/bin").mkdir();
    	new File(BASE_PATH + "/World Saves").mkdir();
        new File(BASE_PATH + "/Resources").mkdir();
        new File(BASE_PATH + "/Player Saves").mkdir();
	}
	
	
	
	/**
	 * Saves the player and world to disk after quitting the game. This currently cheats a bit and gets the world using
	 * "TerraeRasa.terraeRasa.world" as the location. This is not advised, but currently siffices.
	 */
	public void saveAndQuitGame()
	{
		try
		{
			TerraeRasa.terraeRasa.gameEngine.closeGameToMenu();
			System.out.println("Save and quit successful");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Loads the specified player from the ~/Player Saves/ Directory
	 * @param name the name of the player to load
	 * @return the EntityPlayer loaded
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the EntityPlayer class does not exist with the correct version
	 */
	public EntityPlayer loadPlayer(String name) 
			throws IOException, ClassNotFoundException
	{
		String fileName = "/Player Saves/" + name + ".xml";
		EntityPlayer player = (EntityPlayer)new SaveManager().loadFile(fileName);
		player.reconstructPlayerFromFile();
		return player;
	}
	
	/**
	 * Deletes the specified .dat file in the base-folder or deeper
	 * @param fileName the path of the .dat file to be deleted
	 * @return success of the operation
	 */
	public boolean deletefile(String fileName)
	{
		File file = new File(BASE_PATH + fileName + ".xml");
		boolean success = file.delete();
		if (!success)
		{
			System.err.println("Failed to delete file: " + file);
			return false;
		}
		else
		{
			System.out.println("Successfully deleted: " + file);
			return true;
		}
	}
	
	/**
	 * Attempts to delete the specified world save. This is done through recursion, as all sub-folders must be deleted as well.
	 * Generally, the call-stack generated from this should never exceed 3-4 method calls in depth.
	 * @param worldName
	 * @return
	 */
	public boolean deleteWorldSave(String worldName)
	{
		File dir = new File(BASE_PATH + worldName);
		
		//Try to delete the directory, and indicate whether the contents deleted successfully or not.
		if(deleteDirectory(dir)) 
		{
			System.out.println("World Deletion Succeeded: " + worldName);
		}
		else
		{
			System.out.println("World Deletion Failed: " + worldName);
		}
        
		return dir.delete();
	}
	
	/**
	 * Implements a recursive method to delete a folder, and all folders inside that folder. Another method call of deleteDirectory(...)
	 * is added to the stack if an item inside the deleted folder, is a directory.
	 * @param dir the directory and its contents to delete
	 * @return whether or not the operation succeeded. Returns true unless a sub-File or sub-directory fails to delete.
	 */
	private boolean deleteDirectory(File dir)
	{
		if (dir.isDirectory()) //Safety check
		{
            String[] children = dir.list();
			//For each File in the folder...
            for (int i = 0; i < children.length; i++)
            {
            	File newDir = new File(dir, children[i]);
                if(newDir.isDirectory()) //Check if it's a directory
                {
                	deleteDirectory(newDir); //If so, call deleteDirectory(...) again to delete all the contents of that folder
                }
                
                //Then try to delete the File/Directory
            	boolean success = newDir.delete();
                if (!success)
                {
                    return false;
                }
            }
	    } 
		return true;
	}
	
	/**
	 * Get the base path used to save files, etc on the user's computer. This path is created based off
	 * the user's Operating System when an instance of FileManager is created. 
	 * @return BASE_PATH field
	 */
	public String getBasePath()
	{
		return BASE_PATH;
	}
	
	/**
	 * Saves the specified Settings object in compressed GZIP format to the ~/ Directory
	 * @param settings the Settings object for the entire game
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	public void saveSettings(Settings settings) 
			throws FileNotFoundException, IOException
	{
		verifyDirectoriesExist();
		new SaveManager().saveFile("/settings.xml", settings);
	}	

	/**
	 * Loads the Settings object from the ~/ Directory
	 * @param name the name of the player to load
	 * @return the Settings for the entire game
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the Settings class does not exist with the correct version
	 */
	public Settings loadSettings() 
			throws IOException, ClassNotFoundException
	{
		return (Settings)new SaveManager().loadFile("/settings.xml");
	}
}

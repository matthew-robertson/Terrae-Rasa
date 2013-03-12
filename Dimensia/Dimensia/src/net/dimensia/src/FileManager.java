package net.dimensia.src;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.dimensia.client.Dimensia;

public class FileManager
{		
	private final String BASE_PATH;
	
	public FileManager()
	{
		BASE_PATH = Dimensia.getBasePath();
	}
	
	/**
	 * Gets a new world, with specified settings, and saves it to file before returning it.
	 * @param name the name of the world to create
	 * @param worldSize the Enum used to indicate the world's size
	 * @param difficulty the Enum used to indicate the world's difficulty
	 * @return the newly generated world or in case of failure, null
	 */
	public World generateAndSaveWorld(String name, EnumWorldSize worldSize, EnumDifficulty difficulty)
	{
		World world = generateNewWorld(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		world.saveRemainingWorld("Earth");
		return world;
	}
	
	/**
	 * Gets a new WorldHell, with specified settings, and saves it to file before returning it.
	 * @param name the name of the WorldHell to create
	 * @param worldSize the Enum used to indicate the WorldHell's size
	 * @param difficulty the Enum used to indicate the WorldHell's difficulty
	 * @return the newly generated WorldHell or in case of failure, null
	 */
	public WorldHell generateAndSaveWorldHell(String name, EnumWorldSize worldSize, EnumDifficulty difficulty)
	{
		WorldHell worldHell = generateNewWorldHell(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		worldHell.saveRemainingWorld("Hell");
		return worldHell;
	}
	
	/**
	 * Gets a new WorldSky, with specified settings, and saves it to file before returning it.
	 * @param name the name of the WorldSky to create
	 * @param worldSize the Enum used to indicate the WorldSky's size
	 * @param difficulty the Enum used to indicate the WorldSky's difficulty
	 * @return the newly generated WorldSky or in case of failure, null
	 */
	public WorldSky generateAndSaveWorldSky(String name, EnumWorldSize worldSize, EnumDifficulty difficulty)
	{
		WorldSky worldSky = generateNewWorldSky(name, worldSize.getWidth(), worldSize.getHeight(), difficulty);
		worldSky.saveRemainingWorld("Sky");
		return worldSky;
	}
	
	/**
	 * Loads the specified world from the ~/World Saves/ Directory
	 * @param name the name of the world to load
	 * @return the world loaded
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the World class does not exist with the correct version
	 */	
	public World loadWorld(String dir, String name) 
			throws IOException, ClassNotFoundException
	{
		World world = new World();
		world.loadAndApplyWorldData(BASE_PATH, name, dir);
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
	public World generateNewWorld(String name, int w, int h, EnumDifficulty difficulty)
	{
		World world = new WorldGenEarth().generate(new World(name, w, h, difficulty));
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
	public WorldHell generateNewWorldHell(String name, int w, int h, EnumDifficulty difficulty)
	{
		WorldHell worldHell = (WorldHell) new WorldGenHell().generate(new WorldHell(name, w, h, difficulty));
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
	public WorldSky generateNewWorldSky(String name, int w, int h, EnumDifficulty difficulty)
	{
		WorldSky world = (WorldSky) new WorldGenSky().generate(new World(name, w, h, difficulty));
	//	LightingEngine.applySunlight(world);
		return world;
	}
	
	/**
	 * Creates a new player and saves it to file immediately before returning that new player
	 * @param name Name of the player being created
	 * @param difficulty The difficulty settings (EnumDifficulty) of the player being created
	 * @return The new player created, or in the case of a failure - null;
	 */
	public EntityLivingPlayer generateAndSavePlayer(String name, EnumDifficulty difficulty)
	{
		try 
		{
			EntityLivingPlayer player = generateNewEntityLivingPlayer(name, difficulty);
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
	 * Generates a new EntityLivingPlayer with filler x, y positions
	 * @param name the name of the player to be generated.
	 * @param difficulty the difficulty setting (EnumDifficulty) of the player to be generated
	 * @return a newly generated EntityLivingPlayer with given attributes 
	 */
	private EntityLivingPlayer generateNewEntityLivingPlayer(String name, EnumDifficulty difficulty)
	{
		return new EntityLivingPlayer(name, difficulty);
	}
	
	/**
	 * Saves the specified player object in compressed GZIP format to the ~/Player Saves/ Directory
	 * @param player The player to be saved
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	public void savePlayer(EntityLivingPlayer player) 
			throws FileNotFoundException, IOException
	{
		verifyDirectoriesExist();
		String fileName = (BASE_PATH + "/Player Saves/" + player.getName() + ".dat");		
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(fileName)));//Open an output stream
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();//Convert world to byte[]
		ObjectOutputStream s = new ObjectOutputStream(bos); //open the OOS, used to save serialized objects to file
		
		s.writeObject(player); //write the byte[] to the OOS
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length);//Actually save it to file
		System.out.println("Player Saved to: " + fileName + " With Initial Size: " + data.length);
		
		//Cleanup: 
		s.close();
		bos.close();
		fileWriter.close();   		
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
	 * Gets how many worlds exist in the ~/World Saves/ Directory. Returns a maximum of 5, because there should never be more currently
	 * @return upto 5, for the number of worlds in the directory, or in the case of a failure 0
	 */
	public int getTotalWorlds()
	{
		try 
		{
			File dir = new File(BASE_PATH + "/World Saves/");
			String[] children = dir.list(); //Get names of all the files
			Vector<String> vector = new Vector<String>();
			
			if (children.length == 0) //if there're no files, return 0
			{
				return 0;
			}
			else
			{
			    for (int i = 0; i < children.length; i++) //For each file name found
			    {
			        String filename = children[i];
			    	vector.add(filename);
			    }
			}
			
			return vector.size(); 
		}
		catch(Exception e) //Failure
		{
			return 0;
		}
	}
	
	/**
	 * Gets how many players exist in the ~/Player Saves/ Directory. Returns a maximum of 5, because there should never be more currently
	 * @return upto 5 for the number of players in the directory, or in the case of a failure 0
	 */
	public int getTotalPlayers()
	{
		try
		{
			File dir = new File(BASE_PATH + "/Player Saves/");
			String[] children = dir.list();//Get names of all the files
			Vector<String> vector = new Vector<String>();
			
			if (children.length == 0) //if there're no files, return 0
			{
				return 0;
			}
			else
			{
			    for (int i = 0; i < children.length; i++)  //for each entry, check if valid
			    {
			        String filename = children[i];
			        if(filename.endsWith(".dat") && filename.length() > 4) //Ensure the filetype is valid and not ".dat"
			        {
			        	vector.add(filename);
			        }
			    }
			}
			
			return vector.size();			
		}
		catch(Exception e) //Failure
		{
			return 0;
		}
	}
	
	/**
	 * Saves the player and world to disk after quitting the game. This currently cheats a bit and gets the world using
	 * "Dimensia.Dimensia.world" as the location. This is not advised, but currently siffices.
	 */
	public void saveAndQuitGame()
	{
		try
		{
			Dimensia.dimensia.gameEngine.closeGameToMenu();
			Dimensia.resetMainMenu();
			Dimensia.isMainMenuOpen = true; //Sets menu open
			System.out.println("Save and quit successful");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets upto the first 5 player.dat names in the ~/Player Saves/ Directory. There should not be more than 5 currently
	 * @return upto the first 5 names for players, or in the case of a failure 0
	 */
	public String[] getPlayerFileNames()
	{
		try 
		{
			File dir = new File(BASE_PATH + "/Player Saves/");
			String[] children = dir.list(); //get the file names
			String[] players = new String[5];
			int index = 0;
			
			if (children.length == 0) //return nothing if there was nothing
			{
				return new String[] { };
			}
			else
			{
			    for (int i = 0; i < children.length; i++) //For each name
			    {
			        String fileName = children[i];
			        if(fileName.endsWith(".dat") && fileName.length() > 4 && index < 5) //Ensure the file is a valid .dat, has enough length, and that there're less than 5 players
			        {
			        	players[index] = (fileName.substring(0, fileName.length() - 4)); //Trim .dat ending
			        	index++;
			        }
			    }
			}
			
			String temp[] = new String[index]; 
			for(int i = 0; i < temp.length; i++) //Copy the strings into a new array with no excess
			{
				temp[i] = players[i];
			}
			
			return temp;
		}
		catch(Exception e) //Failure
		{
			return new String[] { };
		}
	}
	
	/**
	 * Gets upto the first 5 world save dats in the ~/World Saves/ Directory. There should not be more than 5 currently
	 * @return upto the first 5 names for worlds, or in the case of a failure 0
	 */
	public String[] getWorldFileNames()
	{
		try
		{
			File dir = new File(BASE_PATH + "/World Saves/");
			String[] children = dir.list(); //get the file names
			String[] worlds = new String[5];
			int index = 0;
			
			if (children.length == 0) //return nothing if there was nothing
			{
				return new String[] { };
			}
			else
			{
			    for (int i = 0; i < children.length; i++) //For each name
			    {
		        	worlds[index] = children[i]; 
		        	index++;
			    }
			}
			
			String temp[] = new String[index];
			
			for(int i = 0; i < temp.length; i++) //Copy the strings into a new array with no excess
			{
				temp[i] = worlds[i];
			}
			
			return temp;			
		}
		catch(Exception e) //Failure
		{
			return new String[] { };
		}
	}

	/**
	 * Loads the specified player from the ~/Player Saves/ Directory
	 * @param name the name of the player to load
	 * @return the EntityLivingPlayer loaded
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the EntityLivingPlayer class does not exist with the correct version
	 */
	public EntityLivingPlayer loadPlayer(String name) 
			throws IOException, ClassNotFoundException
	{
		String fileName = BASE_PATH + "/Player Saves/" + name + ".dat";
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))); //Open an input stream
		EntityLivingPlayer player = (EntityLivingPlayer)ois.readObject(); //Load the object
	    System.out.println("Player loaded from: " + fileName);
		ois.close();
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
		File file = new File(BASE_PATH + fileName + ".dat");
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
		String fileName = (BASE_PATH + "/settings.dat");		
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(fileName)));//Open an output stream
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();//Convert world to byte[]
		ObjectOutputStream s = new ObjectOutputStream(bos); //open the OOS, used to save serialized objects to file
		
		s.writeObject(settings); //write the byte[] to the OOS
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length);//Actually save it to file
		System.out.println("Settings Saved to: " + fileName + " With Initial Size: " + data.length);
		
		//Cleanup: 
		s.close();
		bos.close();
		fileWriter.close();   		
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
		String fileName = BASE_PATH + "/settings.dat";
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))); //Open an input stream
		Settings settings = (Settings)ois.readObject(); //Load the object
	    System.out.println("Settings loaded from: " + fileName);
		ois.close();
		return settings;
	}
}

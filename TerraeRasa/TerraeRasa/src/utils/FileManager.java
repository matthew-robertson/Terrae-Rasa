package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import savable.SaveManager;
import client.Settings;
import client.TerraeRasa;
import enums.EnumPlayerDifficulty;


public class FileManager
{		
	private final String BASE_PATH;
	private SaveManager manager;
	
	public FileManager()
	{
		BASE_PATH = TerraeRasa.getBasePath();
		manager = new SaveManager();
	}

	/**
	 * Creates a new player and saves it to file immediately before returning that new player
	 * @param name Name of the player being created
	 * @param difficulty The difficulty settings (EnumDifficulty) of the player being created
	 * @return The new player created, or in the case of a failure - null;
	 */
	public void generateAndSavePlayer(String name, EnumPlayerDifficulty difficulty)
	{
		try {
			String newPlayer = generateNewEntityPlayer(name, difficulty);
			savePlayer(name, newPlayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a new EntityPlayer with filler x, y positions
	 * @param name the name of the player to be generated.
	 * @param difficulty the difficulty setting (EnumDifficulty) of the player to be generated
	 * @return a newly generated EntityPlayer with given attributes 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	private String generateNewEntityPlayer(String name, EnumPlayerDifficulty difficulty) 
	{
		return "type=newplayer;name=" + name + ";difficulty=" + difficulty.toString() + ";";
	}
	
	/**
	 * Saves the specified player object in compressed GZIP format to the ~/Player Saves/ Directory
	 * @param player The player to be saved
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	public void savePlayer(String name, String xml) 
			throws FileNotFoundException, IOException
	{
		verifyDirectoriesExist();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TerraeRasa.getBasePath() + "/Player Saves/" + name + ".xml")));
		writer.write(xml);
		writer.close();
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
		try {
			TerraeRasa.terraeRasa.gameEngine.requestClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the specified player from the ~/Player Saves/ Directory
	 * @param name the name of the player to load
	 * @return the EntityPlayer loaded
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the EntityPlayer class does not exist with the correct version
	 * @throws URISyntaxException 
	 */
	public String loadPlayer(String name) 
			throws IOException, ClassNotFoundException, URISyntaxException
	{
		String fileName = "/Player Saves/" + name + ".xml";
		String xml = new SaveManager().getFileXML(fileName, false);
		return xml;
	}
	
	/**
	 * Deletes the specified .xml file in the base-folder or deeper
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
			System.out.println("World Deletion Succeeded: " + worldName);
		else
			System.out.println("World Deletion Failed: " + worldName);
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

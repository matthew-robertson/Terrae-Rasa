package io;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

import world.World;
import world.WorldGenEarth;

import enums.EnumDifficulty;
import enums.EnumWorldSize;


/**
 * @deprecated 
 * This will be fixed later. Depreciated because it doesnt apply needed things to the world. Use fileManager.generateNewWorld(...)
 * @author Alec
 *
 */
public class CallableGenerateWorld implements Callable<World>
{
	public CallableGenerateWorld(String basepath, String worldName, EnumWorldSize worldSize, EnumDifficulty difficulty)
	{
		BASE_PATH = basepath;
		WORLD_NAME = worldName;
		WORLD_SIZE = worldSize;
		WORLD_DIFFICULTY = difficulty;
	}
	
	public World call() throws Exception 
	{
		try
		{
			World world = generateNewWorld(WORLD_NAME, WORLD_SIZE.getWidth(), WORLD_SIZE.getHeight(), WORLD_DIFFICULTY);
			saveWorld(world);
			return world;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Creates a new world, complete with worldgen, and ready to use
	 * @param name the world's name
	 * @param w the width of the world
	 * @param h the height of the world
	 * @param difficulty the difficulty setting of the world (easy, medium, hardcore)
	 * @return the newly created world
	 */
	public World generateNewWorld(String universeName, int w, int h, EnumDifficulty difficulty)
	{
		return new WorldGenEarth().generate(new World(universeName, w, h, difficulty));
	}
	
	/**
	 * Saves the specified world object in compressed GZIP format to the ~/World Saves/ Directory
	 * @param world the world to be saved 
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	private void saveWorld(World world) throws FileNotFoundException, IOException
	{
		verifyDirectoriesExist();
		
		String fileName = (BASE_PATH + "/World Saves/" + world.getWorldName() + ".dat"); 
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(fileName))); //Open an output stream
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(); //Convert world to byte[]
		ObjectOutputStream s = new ObjectOutputStream(bos); //open the OOS, used to save serialized objects to file
		
		s.writeObject(world); //write the byte[] to the OOS
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length); //Actually save it to file
		System.out.println("World Saved to: " + fileName + " With Initial Size: " + data.length);
		
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
		new File(BASE_PATH + "/Bin").mkdir();
    	new File(BASE_PATH + "/World Saves").mkdir();
        new File(BASE_PATH + "/Resources").mkdir();
        new File(BASE_PATH + "/Player Saves").mkdir();
	}
	
	private final EnumWorldSize WORLD_SIZE;
	private final EnumDifficulty WORLD_DIFFICULTY;
	private final String BASE_PATH;
	private final String WORLD_NAME;
}

package io;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import blocks.Block;
import blocks.BlockChest;
import blocks.MinimalBlock;

import savable.SavableBlock;
import savable.SavableChunk;
import savable.SavableWorld;
import savable.SaveManager;
import utils.ItemStack;
import world.World;
import client.TerraeRasa;


/**
 * World's chunk hashtable keys are in the following form: x,y
 *
 * NOTE: all x and y values used are for the chunk grid, not the blocks grid, or whatever(IE use
 * 1, not the chunk width or height...)
 */
public class ChunkManager 
{
	private final List<Integer> loadRequests;
	private final ExecutorService threadPool;
	private final ArrayList<Future<Chunk>> scheduledLoadOperations;
	private final ArrayList<Future<Boolean>> scheduledSaveOperations;
	private final String BASE_PATH;
	private String universeName;
	private final List<Integer> chunkLock;

	/**
	 * Constructs a new instance of ChunkManager, bound to the specific world. 4 Threads are created to help perform timely chunk operations. Additionally,
	 * a BASE_PATH variable is assigned, leading to the TerraeRasa operating system for the specified OS. Additional directories are created if the required
	 * directories for the world/chunk save are missing.
	 * @param worldName the name of the world, to which this object belongs
	 * @param worldName the name of the 'dimension' within the project (Ex. 'Earth')
	 */
	public ChunkManager()
	{
		threadPool = Executors.newFixedThreadPool(16);
		scheduledLoadOperations = new ArrayList<Future<Chunk>>(4);
		scheduledSaveOperations = new ArrayList<Future<Boolean>>(4);
		this.universeName = "";
		loadRequests = new ArrayList<Integer>(8);
		chunkLock = new ArrayList<Integer>(16);
		BASE_PATH = TerraeRasa.getBasePath();
		
		//Create missing folders
		//Folder for the world ('/worldName')
		if(!new File(BASE_PATH + "/World Saves/" + universeName).exists()) 
		{
			new File(BASE_PATH + "/World Saves/" + universeName).mkdir();
			System.out.println("[ChunkManager] created directory @" + BASE_PATH + "/World Saves/" + universeName);
		}
	}
	
	private void verifyFolderExists(String subdir)
	{
		if(!new File(BASE_PATH + "/World Saves/" + universeName + "/" + subdir).exists()) 
		{
			new File(BASE_PATH + "/World Saves/" + universeName + "/" + subdir).mkdir();
		}
	}
	
	/**
	 * Issues a request to the threadpool to load a chunk into the world chunk map. Requests are not waited for here, instead 
	 * they can be queried for later using {@link #addAllLoadedChunks(World, ConcurrentHashMap)}. There is another version of this
	 * method, generally for spawning, that waits for chunks to load - {@link #addAllLoadedChunks_Wait(World, ConcurrentHashMap)}.
	 * This is however not advised as it takes several seconds to load all the required chunks in most cases.
	 * @param directory the subdirectory to request the chunk
	 * @param world the universal world object
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 * @param x the x position of the chunk in the chunk grid
	 * @param y the y position of the chunk in the chunk grid
	 * @return whether the chunk could be requested
	 */
	public boolean requestChunk(String directory, World world, ConcurrentHashMap<String, Chunk> chunks, int x)
	{
		//Check if the chunk is being requested in an invalid (out of bounds) position
		if(x < 0 || x >= (world.getWidth() / Chunk.getChunkWidth()) || chunkLocked(x))
		{
			return false;
		}
		
		String key = ""+x;
		//Check if the chunk is already loaded
		for(int i = 0; i < loadRequests.size(); i++)
		{
			if(loadRequests.get(i).toString().equals(key)) 
			{
				return false;
			}
		}
		
		//Load the chunk, if it doesnt exist (if this fails, there's a bug. This is a final safety.)
		if(chunks.get(key) == null)
		{
			lockChunk(x);
			verifyFolderExists(directory);
			loadRequests.add(x);
			submitLoadOperation(directory, x);
			return true;
		}
		return false;
	}
	
	/**
	 * Issues a request to the threadpool to save the specified chunk. Save requests are generally made, and then nothing else is 
	 * ever done involving them. They are performed when there is a free thread able to deal with the request.
	 * @param directory the subdirectory to save the chunk
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 * @param x the x position of the chunk in the chunk grid
	 * @param y the y position of the chunk in the chunk grid
	 * @return whether or not the save request succeeded
	 */
	public boolean saveChunk(String directory, ConcurrentHashMap<String, Chunk> chunks, int x)
	{
		if(chunkLocked(x))
		{
			return false;
		}

		verifyFolderExists(directory);
		
		String key = ""+x;
		Chunk chunk = chunks.get(key);
		chunks.remove(key);
		
		//Make sure the chunk isnt null before saving
		if(chunk == null)
		{
			return false;
		}
		
		lockChunk(x);
		
		submitSaveOperation(chunk, directory, x);
		return true;
	}
	
	/**
	 * Gets whether I/O is being performed on that chunk, preventing its use.
	 * @param x the x index of the chunk in the chunk grid
	 * @return true if the chunk is locked, otherwise false
	 */
	private boolean chunkLocked(int x)
	{
		for(int i = 0; i < chunkLock.size(); i++)
		{
			if(chunkLock.get(i) == x)
			{
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * Locks the chunk, preventing further I/O
	 * @param x the x index of the chunk in the chunk grid
	 */
	private void lockChunk(int x)
	{
		chunkLock.add(x);
	}
	
	/**
	 * Unlocks the specified chunk, allowing further I/O
	 * @param x the x index of the chunk in the chunk grid
	 */
	public void unlockChunk(int x)
	{
		for(int i = 0; i < chunkLock.size(); i++)
		{
			if(chunkLock.get(i) == x)
			{
				chunkLock.remove(i);
			}
		}
	}
		
	private void submitSaveOperation(Chunk chunk, String dir, int x)
	{
		Future<Boolean> event = threadPool.submit(new CallableSaveChunk(this,
				chunk, x, "/World Saves/" + universeName + "/" + dir,
				universeName));
		scheduledSaveOperations.add(event);
	}
	
	private void submitLoadOperation(String dir, int x)	
	{
		Future<Chunk> event = threadPool.submit(new CallableLoadChunk(this, 
				x, "/World Saves/" + universeName + "/" + dir, 
				universeName));
		scheduledLoadOperations.add(event);
	}
	
	/**
	 * Determines whether any of the chunk request operations have finished.
	 * @return whether a chunk request has been completed
	 */
	public boolean isAnyLoadOperationDone()
	{
		for(int i = 0; i < scheduledLoadOperations.size(); i++)
		{
			if(scheduledLoadOperations.get(i).isDone()) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds all the chunks requested and fully loaded to the world chunk map. Operations still queued or in progress are not effected
	 * and will continue normally. Anything added to the chunk map is removed from the queues in chunkManager and can no longer be
	 * accessed from within chunkManager, and instead must be accessed in the world object.
	 * @param world the universal world object
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 */
	public void addAllLoadedChunks(World world, ConcurrentHashMap<String, Chunk> chunks)
	{
		for(int i = 0; i < scheduledLoadOperations.size(); i++)
		{
			if(scheduledLoadOperations.get(i).isDone())
			{
				Chunk chunk = null;
				try 
				{
					chunk = scheduledLoadOperations.get(i).get();
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				} 
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				chunk.setFlaggedForLightingUpdate(true);
				
				String key = ""+chunk.getX();
				chunks.put(key, chunk);
				world.chunksLoaded.put(""+chunk.getX(), true);
				
				for(int j = 0; j < loadRequests.size(); j++)
				{
					if(loadRequests.get(j).toString().equals(key))
					{
						loadRequests.remove(j);
					}
				}
				
				scheduledLoadOperations.remove(i);
			}
		}
	}
	
	/**
	 * Saves all non-chunk data from a world into the worlddata.dat file.
	 * @param world the world from which to save data
	 */
	public void saveWorldData(World world)
	{
		try
		{
			SavableWorld savable = new SavableWorld();
			savable.width = world.getWidth();
			savable.height = world.getHeight();
			savable.chunkWidth = world.getChunkWidth();
			savable.chunkHeight = 1;
			savable.averageSkyHeight = world.getAverageSkyHeight();
			savable.generatedHeightMap = world.getGeneratedHeightMap();
			savable.worldTime = world.getWorldTime();
			savable.worldName = world.getWorldName();
			savable.totalBiomes = world.getTotalBiomes();
			savable.difficulty = world.getDifficulty();

			SaveManager manager = new SaveManager();
			manager.saveFile("/World Saves/" + universeName + "/" + world.getWorldName() + "/worlddata.xml", savable);
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * Adds all the chunks requested and fully loaded to the world chunk map. Operations still queued or in progress are not effected
	 * and will continue normally. Anything added to the chunk map is removed from the queues in chunkManager and can no longer be
	 * accessed from within chunkManager, and instead must be accessed in the world object. This version of the method forces the thread to 
	 * wait until all load operations are complete.
	 * @param world the universal world object
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 */
	public void addAllLoadedChunks_Wait(World world, ConcurrentHashMap<String, Chunk> chunks)
	{
		while(scheduledLoadOperations.size() > 0)
		{
			Chunk chunk = null;
			try 
			{
				//Get the value of the callable, or wait if it's not done.
				chunk = scheduledLoadOperations.get(0).get();	
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			catch (ExecutionException e) 
			{
				e.printStackTrace();
			}
			
			//Add the chunk to the world
			String key = ""+chunk.getX();
			chunks.put(key, chunk);
			world.chunksLoaded.put(""+chunk.getX(), true);
			
			//Remove the loadRequests data corresponding to the chunk
			for(int j = 0; j < loadRequests.size(); j++)
			{
				if(loadRequests.get(j).toString().equals(key))
				{
					loadRequests.remove(j);
				}
			}
			scheduledLoadOperations.remove(0);
		}
	}
	
	public void setUniverseName(String name)
	{
		this.universeName = name;
	}
	
	public boolean saveChunkAndLockThread(String directory, ConcurrentHashMap<String, Chunk> chunks, int x) 
	{
		if(chunkLocked(x))
		{
			return false;
		}

		verifyFolderExists(directory);
		
		String key = ""+x;
		Chunk chunk = chunks.get(key);
		chunks.remove(key);
		
		//Make sure the chunk isnt null before saving
		if(chunk == null)
		{
			return false;
		}
		
		lockChunk(x);
		
		SavableChunk savable = new SavableChunk();
		savable.biomeID = chunk.getBiome().getBiomeID();
		savable.light = chunk.getLight();
		savable.diffuseLight = chunk.diffuseLight;
		savable.ambientLight = chunk.ambientLight;
		savable.backWalls = convertToSavable(chunk.backWalls);
		savable.blocks = convertToSavable(chunk.blocks);
		savable.x = chunk.getX();
		savable.wasChanged = chunk.getChanged();
		savable.height = chunk.getHeight();
		savable.lightUpdated = true;
		savable.flaggedForLightingUpdate = true;
			
		String basepath = "/World Saves/" + universeName + "/" + directory;
		
		SaveManager smanager = new SaveManager();
		try {
			smanager.saveCompressedFile(basepath + "/" + x + ".trc", savable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Chunk Saved to: " + basepath + "/" + x + ".trc");
		unlockChunk(chunk.getX());
		
		
		return true;
	}
	
	private SavableBlock[][] convertToSavable(MinimalBlock[][] blocks)
	{
		SavableBlock[][] savables = new SavableBlock[blocks.length][blocks[0].length];
		for(int i = 0; i < savables.length; i++)
		{
			for(int k = 0; k < savables[0].length; k++)
			{
				SavableBlock sblock = new SavableBlock();
				sblock.bitMap = blocks[i][k].bitMap;
				sblock.id = blocks[i][k].id;
				sblock.metaData = blocks[i][k].metaData;
				sblock.mainInventory = blocks[i][k].mainInventory;
						//(blocks[i][k] instanceof BlockChest) ? ((BlockChest)(blocks[i][k])).getMainInventory() : new ItemStack[0];
				savables[i][k] = sblock;			
			}
		}
		return savables;
	}
}

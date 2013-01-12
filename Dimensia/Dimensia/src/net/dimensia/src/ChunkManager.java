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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * World's chunk hashtable keys are in the following form: x,y
 *
 * NOTE: all x and y values used are for the chunk grid, not the blocks grid, or whatever(IE use
 * 1, not 300/600...)
 */

public class ChunkManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * RequestedChunkData defines a private data structure to store data for a chunk load request. Currently contains:
	 * <li>int x
	 * <li>int y
	 */
	private class RequestedChunkData
	{
		public RequestedChunkData(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		private int x;
		private int y;
	}
	
	/**
	 * RequestedChunkData defines a private data structure to store data for a chunk save request. Currently contains:
	 * <li>int x
	 * <li>int y
	 * <li>Chunk chunk
	 */
	private class SavingChunkData
	{
		public SavingChunkData(Chunk chunk, int x, int y)
		{
			this.chunk = chunk;
			this.x = x;
			this.y = y;
		}
		
		public Chunk getChunk()
		{
			return chunk;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		private Chunk chunk;
		private int x;
		private int y;
	}
	
	/**
	 * Constructs a new instance of ChunkManager, bound to the specific world. 4 Threads are created to help perform timely chunk operations. Additionally,
	 * a BASE_PATH variable is assigned, leading to the Dimensia operating system for the specified OS. Additional directories are created if the required
	 * directories for the world/chunk save are missing.
	 * @param worldName the name of the world, to which this object belongs
	 */
	public ChunkManager(String worldName)
	{
		threadPool = Executors.newFixedThreadPool(4);
		scheduledLoadOperations = new ArrayList<Future<Chunk>>(4);
		scheduledSaveOperations = new ArrayList<Future<Boolean>>(4);
		this.worldName = worldName;
		loadRequests = new ArrayList<Position>(8);
		
		//Assign the BASE_PATH
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("win"))
		{
			BASE_PATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/Dimensia").toString();
		}
		else if(osName.contains("mac"))
		{
			BASE_PATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application Support/Dimensia").toString();
		}
		else
		{
			throw new RuntimeException("Invalid OS");
		}
		
		//Create missing folders
		if(!new File(BASE_PATH + "/World Saves/" + worldName).exists()) //Folder for the world ('/worldName')
		{
			new File(BASE_PATH + "/World Saves/" + worldName).mkdir();
			System.out.println("[ChunkManager] created directory @" + BASE_PATH + "/World Saves/" + worldName);
		}
		if(!new File(BASE_PATH + "/World Saves/" + worldName + "/Earth").exists()) //Sub-Folder ('/worldName/Earth') for the chunks
		{
			new File(BASE_PATH + "/World Saves/" + worldName + "/Earth").mkdir();
			System.out.println("[ChunkManager] created directory @" + BASE_PATH + "/World Saves/" + worldName + "/Earth");
		}
	}
	
	/**
	 * Issues a request to the threadpool to load a chunk into the world chunk map. Requests are not waited for here, instead 
	 * they can be queried for later using {@link #addAllLoadedChunks(World, ConcurrentHashMap)}. There is another version of this
	 * method, generally for spawning, that waits for chunks to load - {@link #addAllLoadedChunks_Wait(World, ConcurrentHashMap)}.
	 * This is however not advised as it takes several seconds to load all the required chunks in most cases.
	 * @param world the universal world object
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 * @param x the x position of the chunk in the chunk grid
	 * @param y the y position of the chunk in the chunk grid
	 * @return whether the chunk could be requested
	 */
	public boolean requestChunk(World world, ConcurrentHashMap<String, Chunk> chunks, int x, int y)
	{
		//Check if the chunk is being requested in an invalid (out of bounds) position
		if(x < 0 || x >= (world.getWidth() / Chunk.getChunkWidth()) || y < 0 || y >= (world.getHeight()) / Chunk.getChunkHeight())
		{
			return false;
		}
		
		String key = x + "," + y;
		//Check if the chunk is already loaded
		for(int i = 0; i < loadRequests.size(); i++)
		{
			if(loadRequests.get(i).toString().equals(key)) 
			{
				//System.out.println("Chunk @" + key + " already loaded");
				return false;
			}
		}
		
		//Load the chunk, if it doesnt exist (if this fails, there's a bug. This is a final safety.)
		if(chunks.get(key) == null)
		{
			//System.out.println("Load @" + key);
			loadRequests.add(new Position(x, y));
			processChunkRequest(x, y);
			return true;
		}
		else
		{
			return false;
			//throw new RuntimeException("Illegal chunk request@ " + x + " " + y);
		}
	}
	
	/**
	 * Issues a request to the threadpool to save the specified chunk. Save requests are generally made, and then nothing else is 
	 * ever done involving them. They are performed when there is a free thread able to deal with the request.
	 * @param chunks the ConcurrentHashMap<String, Chunk>, chunks, from world which contains all chunks
	 * @param x the x position of the chunk in the chunk grid
	 * @param y the y position of the chunk in the chunk grid
	 * @return whether or not the save request succeeded
	 */
	public boolean saveChunk(ConcurrentHashMap<String, Chunk> chunks, int x, int y)
	{
		String key = x + "," + y;
		Chunk chunk = chunks.get(key);
		chunks.remove(key);
		
		//Make sure the chunk isnt null before saving
		if(chunk == null)
		{
			return false;
			//throw new RuntimeException("Tried to save chunk that doesnt exist @" + x + " " + y);
		}
		
		processChunkSave(chunk, x, y);
		return true;
	}
	
	private void processChunkSave(Chunk chunk, int x, int y)
	{
		submitSaveOperation(new SavingChunkData(chunk, x, y));
	}
	
	private void processChunkRequest(int x, int y)
	{
		submitLoadOperation(new RequestedChunkData(x, y));
	}
	
	private void submitSaveOperation(SavingChunkData data)
	{
		Future<Boolean> event = threadPool.submit(new CallableSaveChunk(data.getChunk(), data.getX(), data.getY(), BASE_PATH, worldName));
		scheduledSaveOperations.add(event);
	}
	
	private void submitLoadOperation(RequestedChunkData data)	
	{
		Future<Chunk> event = threadPool.submit(new CallableLoadChunk(data.getX(), data.getY(), BASE_PATH, worldName));
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
				String key = chunk.getX() + "," + chunk.getY();
				chunks.put(key, chunk);
				world.loadedChunks_B[chunk.getX()][chunk.getY()] = true;
				
				for(int j = 0; j < loadRequests.size(); j++)
				{
					if(loadRequests.get(i).toString().equals(key))
					{
						loadRequests.remove(i);
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
			GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(BASE_PATH + "/World Saves/" + worldName + "/worlddata.dat"))); //Open an output stream
		    ByteArrayOutputStream bos = new ByteArrayOutputStream(); //Open a stream to turn objects into byte[]
			ObjectOutputStream s = new ObjectOutputStream(bos); //open the OOS, used to save serialized objects to file
			
			/**
			Variables are saved in the following order:
				worldName
				width
				height
				chunkWidth
				chunkHeight
				averageSkyHeight
				generatedHeightMap
				worldTime
				totalTimes
				difficulty
				biomes
				biomesByColumn
			**/

			s.writeObject(world.getWorldName());
			s.writeObject(world.getWidth());
			s.writeObject(world.getHeight());
			s.writeObject(world.getChunkWidth());
			s.writeObject(world.getChunkHeight());
			s.writeObject(world.getAverageSkyHeight());
			s.writeObject(world.getGeneratedHeightMap());
			s.writeObject(world.getWorldTime());
			s.writeObject(world.getTotalBiomes());
			s.writeObject(world.getDifficulty());
			s.writeObject(world.biomes);
			s.writeObject(world.biomesByColumn);
			s.writeObject(world.itemsList);
			byte data[] = bos.toByteArray();
			fileWriter.write(data, 0, data.length); //Actually save it to file
			
			//Cleanup: 
			s.close();
			bos.close();
			fileWriter.close();      
			System.out.println("Saved World Data File");
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
			String key = chunk.getX() + "," + chunk.getY();
			chunks.put(key, chunk);
			world.loadedChunks_B[chunk.getX()][chunk.getY()] = true;
			
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
	
	private final List<Position> loadRequests;
	private final ExecutorService threadPool;
	private final ArrayList<Future<Chunk>> scheduledLoadOperations;
	private final ArrayList<Future<Boolean>> scheduledSaveOperations;
	private final String BASE_PATH;
	private final String worldName;
}

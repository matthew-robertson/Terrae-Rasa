package io;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import transmission.SuperCompressedChunk;
import world.World;


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
	private final ArrayList<Future<Chunk>> scheduledExpandOperations;

	/**
	 * Constructs a new instance of ChunkManager, bound to the specific world. 4 Threads are created to help perform timely chunk operations. Additionally,
	 * a BASE_PATH variable is assigned, leading to the TerraeRasa operating system for the specified OS. Additional directories are created if the required
	 * directories for the world/chunk save are missing.
	 * @param worldName the name of the world, to which this object belongs
	 * @param worldName the name of the 'dimension' within the project (Ex. 'Earth')
	 */
	public ChunkManager()
	{
		threadPool = Executors.newFixedThreadPool(3);
		scheduledExpandOperations = new ArrayList<Future<Chunk>>(3);
		loadRequests = new ArrayList<Integer>(8);
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
	public boolean expandChunk(SuperCompressedChunk compressedChunk)
	{
		//Make sure the chunk isnt null 
		if(compressedChunk == null)
		{
			return false;
		}
		
		submitExpandOperation(compressedChunk);
		return true;
	}
		
	private void submitExpandOperation(SuperCompressedChunk compressedChunk)
	{
		Future<Chunk> event = threadPool.submit(new CallableReconstructChunk(compressedChunk));
		scheduledExpandOperations.add(event);
	}
	
	/**
	 * Determines whether any of the chunk request operations have finished.
	 * @return whether a chunk request has been completed
	 */
	public boolean isAnyLoadOperationDone()
	{
		for(int i = 0; i < scheduledExpandOperations.size(); i++)
		{
			if(scheduledExpandOperations.get(i).isDone()) 
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
		for(int i = 0; i < scheduledExpandOperations.size(); i++)
		{
			if(scheduledExpandOperations.get(i).isDone())
			{
				Chunk chunk = null;
				try {
					chunk = scheduledExpandOperations.get(i).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				chunk.setRequiresAmbientLightingUpdate(true);
				
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
				
				scheduledExpandOperations.remove(i);
			}
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
		while(scheduledExpandOperations.size() > 0)
		{
			Chunk chunk = null;
			try {
				//Get the value of the callable, or wait if it's not done.
				chunk = scheduledExpandOperations.get(0).get();	
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
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
			scheduledExpandOperations.remove(0);
		}
	}
}

package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transmission.ChunkExpander;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import world.World;
import client.EngineLock;
import client.GameEngine;
import entities.EntityPlayer;

public class ClientConnectionThread extends Thread
{
	private Socket socket;
	private boolean done = false;
	private EngineLock engineLock;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	
	public ClientConnectionThread(Socket socket, EngineLock lock, ObjectOutputStream os, ObjectInputStream is)
	{
		setDaemon(true);
		this.socket = socket;
		this.engineLock = lock;
		this.os = os;
		this.is = is;
	}
	
	public void run()
	{
		requestBasicData();
		
		final int SKIP_TICKS = 1000 / GameEngine.TICKS_PER_SECOND;
		final int MAX_FRAMESKIP = 5;
		long next_game_tick = System.currentTimeMillis();
		int loops;
		
		while(!done)
		{
		    loops = 0;
	        while(System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) //Update the game 20 times/second 
	        {
	        	//The update cycle things
	        	
	        	//Write hardware IO and wait for a response in the form of a server object
	        	
	        	
		        next_game_tick += SKIP_TICKS;
			    loops++;
	        }
			
	        //Make sure the game loop doesn't fall very far behind and have to accelerate the 
	        //game for an extended period of time
	        if(System.currentTimeMillis() - next_game_tick > 1000)
	        {
	        	next_game_tick = System.currentTimeMillis();
	        }
	        
		}
	}
	
	private void requestBasicData()
	{
		try {
			os.writeUTF("/sendplayer");
			//This will fail
			os.writeObject(EntityPlayer.compress(engineLock.getPlayer()));
			os.flush();
			
			os.writeUTF("/requestinitChunks");
			
			os.flush();
			
			SuperCompressedChunk[] scc = (SuperCompressedChunk[])(is.readObject());
			Chunk[] chunks = new Chunk[scc.length];
			for(int i = 0; i < chunks.length; i++)
			{
				chunks[i] = ChunkExpander.expandChunk(scc[i]);
			}
			
			os.writeUTF("/initialgamedata");
			os.flush();
			
			WorldData data = (WorldData)is.readObject();
			World world = new World(data, chunks);
			engineLock.setWorld(world);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}

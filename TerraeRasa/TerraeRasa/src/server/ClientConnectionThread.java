package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

import savable.SaveManager;
import transmission.ChunkExpander;
import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.GZIPHelper;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import world.World;
import client.EngineLock;
import client.GameEngine;
import client.TerraeRasa;

public class ClientConnectionThread extends Thread
{
	private Socket socket;
	private boolean done = false;
	private EngineLock engineLock;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private GZIPHelper gzipHelper;
	
	public ClientConnectionThread(Socket socket, EngineLock lock, ObjectOutputStream os, ObjectInputStream is)
	{
		setDaemon(true);
		this.socket = socket;
		this.engineLock = lock;
		this.os = os;
		this.is = is;
		gzipHelper = new GZIPHelper();
	}
	
	public void run()
	{
		try {
			try {
				requestBasicData();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
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
		        	CompressedClientUpdate[] input = engineLock.yieldClientUpdates();        		
					os.writeObject(gzipHelper.compress(input));
		        	os.flush();
		        	
		        	CompressedServerUpdate[] updates = (CompressedServerUpdate[])(gzipHelper.expand((byte[])is.readObject()));
		        	for(CompressedServerUpdate update : updates)
		        	{
		        		engineLock.addUpdate(update);
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
		        
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			
			TerraeRasa.isMainMenuOpen = true;
			TerraeRasa.terraeRasa.gameEngine.resetMainMenu();
			
		}		
	}
	
	private void requestBasicData()
			throws URISyntaxException
	{
		try {
			os.writeUTF("/sendplayer");
			String savableXML = new SaveManager().getFileXML(TerraeRasa.getBasePath() + "/Player Saves/" + engineLock.getActivePlayerName() + ".xml", false);
//			byte[] bytes = gzipHelper.compress(savableXML);
			os.writeUTF(savableXML);			
			os.flush();
			
			int id = is.readInt();
			engineLock.setActivePlayerID(id);
			
			os.writeUTF("/requestinitChunks");
			os.flush();
			
			SuperCompressedChunk[] scc = (SuperCompressedChunk[])(gzipHelper.expand((byte[])is.readObject()));
			Chunk[] chunks = new Chunk[scc.length];
			for(int i = 0; i < chunks.length; i++)
			{
				chunks[i] = ChunkExpander.expandChunk(scc[i]);
			}
			
			os.writeUTF("/initialgamedata");
			os.flush();
			
			WorldData data = (WorldData)(gzipHelper.expand((byte[])is.readObject()));
			World world = new World(data, chunks);
			engineLock.setWorld(world);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}

	public void end()
	{
		done = true;
	}
}

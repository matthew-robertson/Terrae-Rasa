package client;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;

import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.GZIPHelper;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import blocks.ChunkClient;
import client.utils.SaveHelper;
import client.world.WorldClientEarth;
import entry.MPGameLoop;
import entry.SPGameEngine;
import entry.TerraeRasa;

public class ClientConnectionThread extends Thread
{
	private Socket socket;
	private volatile boolean done = false;
	private EngineLock engineLock;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private GZIPHelper gzipHelper;
	private static int id = 0;
	
	public ClientConnectionThread(Socket socket, EngineLock lock, ObjectOutputStream os, ObjectInputStream is)
	{
		setName("Client_Connection_Thread"+id++);
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
			
			final int SKIP_TICKS = 1000 / MPGameLoop.TICKS_PER_SECOND;
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

		        	byte loopTotal = is.readByte();
		        	for(int i = 0; i < loopTotal; i++)
		        	{
			        	CompressedServerUpdate[] updates = (CompressedServerUpdate[])(gzipHelper.expand((byte[])is.readObject()));
			        	for(CompressedServerUpdate update : updates)
			        	{
			        		engineLock.addUpdate(update);
			        	}
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
		} catch (SocketException e){
			System.out.println("Clientside Socket connection terminated.");
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
			
			System.out.println("Closing the client connection thread");
			SPGameEngine.terraeRasa.gameEngine.resetMainMenu();
			SPGameEngine.isMainMenuOpen = true;
		}		
	}
	
	private void requestBasicData()
			throws URISyntaxException
	{
		try {
			os.writeUTF("/sendplayer");
			String savableXML = new SaveHelper().getFileXML(TerraeRasa.getBasePath() + "/Player Saves/" + engineLock.getActivePlayerName() + ".xml", false);
//			byte[] bytes = gzipHelper.compress(savableXML);
			os.writeUTF(savableXML);			
			os.flush();
			
			int id = is.readInt();
			engineLock.setActivePlayerID(id);
			
			os.writeUTF("/requestinitChunks");
			os.flush();
			
			SuperCompressedChunk[] scc = (SuperCompressedChunk[])(gzipHelper.expand((byte[])is.readObject()));
			for(int i = 0; i < scc.length; i++)
			{
				engineLock.expandChunk(scc[i]);
			}
			
			os.writeUTF("/initialgamedata");
			os.flush();
			
			WorldData data = (WorldData)(gzipHelper.expand((byte[])is.readObject()));
			WorldClientEarth world = new WorldClientEarth(data, new ChunkClient[] { });
			engineLock.setWorld(world);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}

	public void kill() throws IOException
	{
		socket.close();
		done = true;
		interrupt();
	}
}

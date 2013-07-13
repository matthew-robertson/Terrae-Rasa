package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transmission.ChunkCompressor;
import transmission.CompressedPlayer;
import transmission.CompressedServerUpdate;
import transmission.GZIPHelper;
import transmission.ServerUpdate;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import entities.EntityPlayer;
import enums.EnumHardwareInput;

public class ServerConnectionThread extends Thread
{
	private Socket socket;
	private WorldLock worldLock;
	private boolean open;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private GZIPHelper gzipHelper;
	
	public ServerConnectionThread(WorldLock lock, Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		this.socket = socket;
		this.worldLock = lock;
		setDaemon(true);
		open = true;
		this.is = is;
		this.os = os;
		gzipHelper = new GZIPHelper();
	}
	
	public void registerWorldUpdate(ServerUpdate update)
	{
		worldLock.addUpdate(update);
	}
	
	public void run()
	{	
		handleInitialData();
		
		while(open)
		{
			try {
				String message = is.readUTF();
				if(message.equals("/clientinput"))
				{
					EnumHardwareInput[] clientInput = (EnumHardwareInput[])(gzipHelper.expand((byte[])is.readObject()));
					worldLock.registerPlayerInput(clientInput);
				}
				//TODO: Maybe? this may or may not be reckless.
				
				CompressedServerUpdate[] updates = worldLock.yieldServerUpdates();
	        	os.writeObject(gzipHelper.compress(updates));
	        	os.flush();
	        	
//	        	os.writeObject(gzipHelper.compress(EntityPlayer.compress(worldLock.getRelevantPlayer())));
//	        	os.flush();
	        	//Update cycle done.
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			}
			
			
			
			
			
		}
		
		
		
		try {
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void handleInitialData()
	{
		try {
			String message = is.readUTF();
			if(message.equals("/sendplayer"))
			{
				EntityPlayer player = EntityPlayer.expand((CompressedPlayer)(gzipHelper.expand((byte[])is.readObject())));
				worldLock.addPlayerToWorld(player);
			}
			
			message = is.readUTF();
			if(message.equals("/requestinitChunks"))
			{
				
				Chunk[] chunks = worldLock.getInitialChunks();
				SuperCompressedChunk[] scc = new SuperCompressedChunk[chunks.length];
				for(int i = 0; i < scc.length; i++)
				{
					scc[i] = ChunkCompressor.compressChunk(chunks[i]);
				}				
				os.writeObject(gzipHelper.compress(scc));
				os.flush();
			}
						
			message = is.readUTF();
			if(message.equals("/initialgamedata"))
			{
				WorldData data = worldLock.getWorldData();
				os.writeObject(gzipHelper.compress(data));
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

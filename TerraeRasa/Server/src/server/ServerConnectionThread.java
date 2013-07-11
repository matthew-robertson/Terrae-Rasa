package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transmission.ChunkCompressor;
import transmission.CompressedPlayer;
import transmission.SuperCompressedChunk;
import transmission.WorldData;
import entities.EntityPlayer;

public class ServerConnectionThread extends Thread
{
	private Socket socket;
	private WorldLock worldLock;
	private boolean open;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	
	public ServerConnectionThread(WorldLock lock, Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		this.socket = socket;
		this.worldLock = lock;
		setDaemon(true);
		open = true;
		this.is = is;
		this.os = os;
	}
	
	public void run()
	{	
		try {
			
			String message = is.readUTF();
			if(message.equals("/sendplayer"))
			{
				EntityPlayer player = EntityPlayer.expand((CompressedPlayer)is.readObject());
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
				os.writeObject(scc);
				os.flush();
			}
						
			message = is.readUTF();
			if(message.equals("/initialgamedata"))
			{
				WorldData data = worldLock.getWorldData();
				os.writeObject(data);
				os.flush();
			}
		
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		while(open)
		{
			System.out.println("Here");
			try {
				Thread.sleep(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//do stuff
		}
		
		
		
		try {
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}

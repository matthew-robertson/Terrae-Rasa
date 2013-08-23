package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import savable.SavablePlayer;
import savable.SaveManager;
import transmission.ChunkCompressor;
import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.GZIPHelper;
import transmission.ServerUpdate;
import transmission.SuperCompressedChunk;
import transmission.UpdateWithObject;
import transmission.WorldData;
import entities.EntityPlayer;

public class ServerConnectionThread extends Thread
{
	private Socket socket;
	private WorldLock worldLock;
	private volatile boolean open;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private GZIPHelper gzipHelper;
	private final int connectionID;
	private int associatedPlayerID;
	private volatile boolean sendPlayerAndClose;
	
	public ServerConnectionThread(WorldLock lock, Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		this.socket = socket;
		this.worldLock = lock;
		setDaemon(true);
		open = false;
		sendPlayerAndClose = false;
		this.is = is;
		this.os = os;
		gzipHelper = new GZIPHelper();
		connectionID = ServerSettings.getConnectionID();
	}
	
	public void registerWorldUpdate(ServerUpdate update)
	{
		if(open)
		{
			worldLock.addUpdate(update);
		}
	}
	
	public void run()
	{	
		try {
			handleInitialData();
			while(open)
			{
				CompressedClientUpdate[] clientUpdate = (CompressedClientUpdate[])(gzipHelper.expand((byte[])is.readObject()));
				worldLock.registerPlayerUpdate(clientUpdate);
				//TODO: Maybe? this may or may not be reckless.
				
				if(sendPlayerAndClose)
				{
					CompressedServerUpdate closingUpdate = new CompressedServerUpdate();
					UpdateWithObject playerUpdate = new UpdateWithObject();
					playerUpdate.command = "/recievesavable " + worldLock.getRelevantPlayer().entityID;
					playerUpdate.object = worldLock.getRelevantPlayer().getSavableXML();
					closingUpdate.objectUpdates = new UpdateWithObject[1];
					closingUpdate.objectUpdates[0] = playerUpdate;
					CompressedServerUpdate[] updates = { closingUpdate };
					os.writeObject(gzipHelper.compress(updates));
		        	os.flush();
		        	open = false;
				}
				else
				{
					CompressedServerUpdate[] updates = worldLock.yieldServerUpdates();
					os.writeObject(gzipHelper.compress(updates));
		        	os.flush();
				}
			}			
		} catch (IOException e) {
			System.err.println("Fatal error to connection thread with ID " + connectionID + " caused by: ");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Fatal error to connection thread with ID " + connectionID + " caused by: ");
			e.printStackTrace();
		} catch (Exception e) { //This is likely an IOException, but in the event it isnt, account for everything
			System.err.println("Fatal error to connection thread with ID " + connectionID + " caused by: ");
			e.printStackTrace();
		} finally {
			TerraeRasa.requestClientConnectionClosed(this, worldLock.getRelevantPlayer());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInitialData()
	{
		try {
			//Recieve the player and generate an ID for them
			String message = is.readUTF();
			int playerID = ServerSettings.getEntityID();
			this.associatedPlayerID = playerID;
			EntityPlayer player = null;
			if(message.equals("/sendplayer"))
			{
				String savableXML = is.readUTF();
				//TODO this is dangerous (the world access)
				player = new EntityPlayer(TerraeRasa.terraeRasa.gameEngine.getWorld(), (SavablePlayer)new SaveManager().xmlToObject(savableXML));
				player.setEntityID(playerID);
				player.verifyName();
				worldLock.addPlayerToWorld(player);

			}
			
			//Tell the client what their player's ID is
			os.writeInt(playerID);
			os.flush();
			
			//Decide what chunks need to be sent
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
						
			//Send world data to the client
			message = is.readUTF();
			if(message.equals("/initialgamedata"))
			{
				WorldData data = worldLock.getWorldData();
				data.otherplayers = worldLock.requestOtherPlayers();
				os.writeObject(gzipHelper.compress(data));
				os.flush();
			}
			
			//Add the player to the world and mark this thread as being active.
			if(player != null)
			{
			}
			open = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets whether or not this connection thread to a client is open. If it is open then server/client update cycle is allowed.
	 * @return whether this connection thread is open, allowing updates
	 */
	public boolean getOpen()
	{
		return open;
	}
	
	/**
	 * Requests that this thread is closed. This will send an updated version of the player back to the client to save and perform any applicable cleanup.
	 */
	public void close()
	{
		this.sendPlayerAndClose = true;
	}
	
	/**
	 * Gives the ID of the player associated to this connection.
	 * @return the ID of the player associated with this connection.
	 */
	public final int getAssociatedPlayerID()
	{
		return associatedPlayerID;
	}
}

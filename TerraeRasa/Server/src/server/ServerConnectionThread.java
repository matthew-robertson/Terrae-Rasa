package server;

import io.Chunk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;

import savable.SavablePlayer;
import savable.SaveManager;
import transmission.ChunkCompressor;
import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.ConnectionFilter;
import transmission.GZIPHelper;
import transmission.ServerUpdate;
import transmission.SuperCompressedChunk;
import transmission.UpdateWithObject;
import transmission.WorldData;
import entities.EntityPlayer;
import enums.EnumPlayerDifficulty;

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
	private ConnectionFilter filter;
	private List<Future<byte[]>> deferredCompressions = new ArrayList<Future<byte[]>>();
	private long sentData;
	
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
		filter = new ConnectionFilter();
		sentData = 0;
	}
	
	public void registerWorldUpdate(ServerUpdate update)
	{
		if(open)
		{
			if(update.deferCompression)
			{
				deferCompression(new CompressedServerUpdate[]{ filter.filterOutgoing(update, worldLock.getRelevantPlayer()) });
			}
			else
			{
				worldLock.addUpdate(update);
			}
		}
	}
	

	public void run()
	{	
		try {
			handleInitialData();
			while(open)
			{
				CompressedClientUpdate[] clientUpdate = (CompressedClientUpdate[])(gzipHelper.expand((byte[])is.readObject()));
				worldLock.registerPlayerUpdate(new ConnectionFilter().filterIn(clientUpdate, associatedPlayerID));
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
					os.writeByte(1);
					byte[] result = gzipHelper.compress(updates);
					sentData += result.length;
					os.writeObject(result);
		        	os.flush();
		        	open = false;
				}
				else
				{
					Vector<Future<byte[]>> doneOperations = finishedCompressionOperations();
					int totalUpdates = 1 + doneOperations.size();

					os.writeByte(totalUpdates);
					
					//"i = 0" version of this might be another way to state it -- it's for the 100% chance of server updates.
					ServerUpdate[] updates = worldLock.yieldServerUpdates();
					CompressedServerUpdate[] compressedUpdates = new CompressedServerUpdate[updates.length];
					for(int i = 0; i < updates.length; i++)
					{
						compressedUpdates[i] = filter.filterOutgoing(updates[i], worldLock.getRelevantPlayer());
					}			
					byte[] result = gzipHelper.compress(compressedUpdates);
					sentData += result.length + 1;
		        	os.writeObject(result);
		        	os.flush();
					
					for(int i = 1; i < totalUpdates; i++)
					{
						byte[] compressed = doneOperations.get(i - 1).get();
						sentData += compressed.length;
						os.writeObject(compressed);
			        	os.flush();
					}
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
			System.out.println("This thread " + connectionID + " sent a total of " + sentData + " bytes of data.");
		}
	}
	
	private void deferCompression(Object object)
	{
		Future<byte[]> future = HeavyLoadCompressor.scheduleRequest(object);
		deferredCompressions.add(future);
	}
	
	private Vector<Future<byte[]>> finishedCompressionOperations()
	{
		Vector<Future<byte[]>> done = new Vector<Future<byte[]>>();
		for(int i = 0; i < deferredCompressions.size(); i++)
		{
			if(deferredCompressions.get(i).isDone()) 
			{
				done.add(deferredCompressions.get(i));
				deferredCompressions.remove(i);
			}
		}
		return done;
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
				//A new player is an XML file with just the following string of text in it:
				//"type=newplayer;name=NAME;difficulty=DIFFICULTY;"
				if(savableXML.startsWith("type="))
				{
					String[] split = savableXML.split(";");
					String name = split[1].split("=")[1];
					EnumPlayerDifficulty difficulty = EnumPlayerDifficulty.getDifficulty(split[2].split("=")[1]);
					System.out.println(split[1].split("=")[1] + " " + split[2].split("=")[1]);
					player = new EntityPlayer(name, difficulty, getIP());
				}
				else
				{
					//TODO this is dangerous (the world access)
					player = new EntityPlayer(TerraeRasa.terraeRasa.gameEngine.getWorld(), (SavablePlayer)new SaveManager().xmlToObject(savableXML), getIP());
				}
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
	public int getAssociatedPlayerID()
	{
		return associatedPlayerID;
	}
	
	public String getIP()
	{
		return (socket.getInetAddress().toString()).substring(1);
	}
}

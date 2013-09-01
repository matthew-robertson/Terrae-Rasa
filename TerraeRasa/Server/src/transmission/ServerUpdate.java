package transmission;

import io.Chunk;

import java.util.Vector;

public class ServerUpdate
{
	private final Object commandLock = new Object();
	private final Object entityUpdateLock = new Object();
	private final Object positionUpdateLock = new Object();
	private final Object blockUpdateLock = new Object();
	private final Object statUpdateLock = new Object();
	private final Object chunkUpdateLock = new Object();
	private final Object objectUpdateLock = new Object();
	private Vector<String> commands; 
	private Vector<EntityUpdate> entityUpdates;
	private Vector<PositionUpdate> positionUpdates;
	private Vector<BlockUpdate> blockUpdates;
	private Vector<StatUpdate> statUpdates;
	private Vector<Chunk> chunkUpdates;
	private Vector<UpdateWithObject> objectUpdates;
	
	public ServerUpdate()
	{
		this.commands = new Vector<String>();
		this.entityUpdates = new Vector<EntityUpdate>();
		this.positionUpdates = new Vector<PositionUpdate>();
		this.blockUpdates = new Vector<BlockUpdate>();
		this.statUpdates = new Vector<StatUpdate>();
		this.chunkUpdates = new Vector<Chunk>();
		this.objectUpdates = new Vector<UpdateWithObject>();
	}
	
	public void addValue(String val)
	{
		synchronized(commandLock)
		{
			this.commands.add(val);
		}
	}
	
	public String[] getValues()
	{
		synchronized(commandLock)
		{
			String[] vals = new String[commands.size()];
			commands.copyInto(vals);
			return vals;
		}
	}
	
	public void addEntityUpdate(EntityUpdate update)
	{
		synchronized(entityUpdateLock)
		{
			entityUpdates.add(update);
		}
	}
	
	public EntityUpdate[] getEntityUpdates()
	{
		synchronized(entityUpdateLock)
		{
			EntityUpdate[] updates = new EntityUpdate[entityUpdates.size()];
			entityUpdates.copyInto(updates);
			return updates;
		}
	}
	
	public void addPositionUpdate(PositionUpdate update)
	{
		synchronized(positionUpdateLock)
		{
			positionUpdates.add(update);
		}
	}
	
	public PositionUpdate[] getPositionUpdates()
	{
		synchronized(positionUpdateLock)
		{
			PositionUpdate[] updates = new PositionUpdate[positionUpdates.size()];
			positionUpdates.copyInto(updates);
			return updates;
		}
	}
	
	public void addBlockUpdate(BlockUpdate update)
	{
		synchronized(blockUpdateLock)
		{
			this.blockUpdates.add(update);
		}
	}
	
	public BlockUpdate[] getBlockUpdates()
	{
		synchronized(blockUpdateLock)
		{
			BlockUpdate[] vals = new BlockUpdate[blockUpdates.size()];
			blockUpdates.copyInto(vals);
			return vals;
		}
	}
	
	public void addStatUpdate(StatUpdate update)
	{
		synchronized(statUpdateLock)
		{
			statUpdates.add(update);
		}
	}
	
	public StatUpdate[] getStatUpdates()
	{
		synchronized(statUpdateLock)
		{
			StatUpdate[] updates = new StatUpdate[statUpdates.size()];
			statUpdates.copyInto(updates);
			return updates;
		}
	}
	
	public void addChunkUpdate(Chunk chunk)
	{
		synchronized(chunkUpdateLock)
		{
			chunkUpdates.add(chunk);
		}
	}
	
	public Chunk[] getChunks()
	{
		synchronized(chunkUpdateLock)
		{
			Chunk[] updates = new Chunk[chunkUpdates.size()];
			chunkUpdates.copyInto(updates);
			return updates;
		}
	}
	
	public void addObjectUpdate(UpdateWithObject update)
	{
		synchronized(objectUpdateLock)
		{
			objectUpdates.add(update);
		}
	}
	
	public UpdateWithObject[] getObjectUpdates()
	{
		synchronized(objectUpdateLock)
		{
			UpdateWithObject[] updates = new UpdateWithObject[objectUpdates.size()];
			objectUpdates.copyInto(updates);
			return updates;
		}
	}
}

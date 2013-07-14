package transmission;

import java.util.Vector;

public class ServerUpdate
{
	private Vector<String> values; 
	private Vector<EntityUpdate> entityUpdates;
	private Vector<PositionUpdate> positionUpdates;
	private Vector<BlockUpdate> blockUpdates;
	
	public ServerUpdate()
	{
		this.values = new Vector<String>();
		this.entityUpdates = new Vector<EntityUpdate>();
		this.positionUpdates = new Vector<PositionUpdate>();
		this.blockUpdates = new Vector<BlockUpdate>();
	}
	
	public synchronized void addValue(String val)
	{
		this.values.add(val);
	}
	
	public synchronized String[] getValues()
	{
		String[] vals = new String[values.size()];
		values.copyInto(vals);
		return vals;
	}
	
	public synchronized void addEntityUpdate(EntityUpdate update)
	{
		entityUpdates.add(update);
	}
	
	public synchronized EntityUpdate[] getUpdates()
	{
		EntityUpdate[] updates = new EntityUpdate[entityUpdates.size()];
		entityUpdates.copyInto(updates);
		return updates;
	}
	
	public synchronized void addPositionUpdate(PositionUpdate update)
	{
		positionUpdates.add(update);
	}
	
	public synchronized PositionUpdate[] getPositionUpdates()
	{
		PositionUpdate[] updates = new PositionUpdate[positionUpdates.size()];
		positionUpdates.copyInto(updates);
		return updates;
	}
	
	public synchronized void addBlockUpdate(BlockUpdate update)
	{
		this.blockUpdates.add(update);
	}
	
	public synchronized BlockUpdate[] getBlockUpdates()
	{
		BlockUpdate[] vals = new BlockUpdate[blockUpdates.size()];
		blockUpdates.copyInto(vals);
		return vals;
	}
}

package transmission;

import java.util.Vector;

public class ServerUpdate
{
	private Vector<String> values; 
	private Vector<EntityUpdate> entityUpdates;
	private Vector<PositionUpdate> positionUpdates;
	
	public ServerUpdate()
	{
		this.values = new Vector<String>();
		this.entityUpdates = new Vector<EntityUpdate>();
		this.positionUpdates = new Vector<PositionUpdate>();
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
}

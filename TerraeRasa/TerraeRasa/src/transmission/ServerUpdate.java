package transmission;

import java.util.Vector;

public class ServerUpdate
{
	public Vector<String> values; 
	public Vector<EntityUpdate> entityUpdates;
	public Vector<PositionUpdate> positionUpdates;
	
	public ServerUpdate()
	{
		this.values = new Vector<String>();
	}
	
	public void addValue(String val)
	{
		this.values.add(val);
	}
	
	public String[] getValues()
	{
		String[] vals = new String[values.size()];
		values.copyInto(vals);
		return vals;
	}
	
	public void addEntityUpdate(EntityUpdate update)
	{
		entityUpdates.add(update);
	}
	
	public EntityUpdate[] getUpdates()
	{
		EntityUpdate[] updates = new EntityUpdate[entityUpdates.size()];
		entityUpdates.copyInto(updates);
		return updates;
	}
	
	public void addPositionUpdate(PositionUpdate update)
	{
		positionUpdates.add(update);
	}
	
	public PositionUpdate[] getPositionUpdates()
	{
		PositionUpdate[] updates = new PositionUpdate[positionUpdates.size()];
		positionUpdates.copyInto(updates);
		return updates;
	}
}

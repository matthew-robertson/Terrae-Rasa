package transmission;

import java.io.Serializable;

public class PositionUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public double x;
	public double y;
	public int entityID;
	
	public PositionUpdate(int entityID, double x, double y)
	{
		this.entityID = entityID;
		this.x = x;
		this.y = y;
	}
}

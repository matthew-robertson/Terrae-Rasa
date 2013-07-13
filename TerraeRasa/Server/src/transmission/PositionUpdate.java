package transmission;

import java.io.Serializable;

public class PositionUpdate 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public double x;
	public double y;
	
	public PositionUpdate(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}

package net.dimensia.src;

/**
 * Position implements a simple class that stores a point in 2D space. It is essentially a Vector2D, storing an (x,y) point. The only notable addition
 * is the overriding of {@link #toString()} to return a point as "x,y".
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Position 
{
	/**
	 * Constructs a new Position, a point in 2D space.
	 * @param x the x value of the point
	 * @param y the y value of the point
	 */
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * Gets the point as a string in the form of "x,y" (there are no brackets). This is the same form used as the keys for the world chunk map.
	 * @return the x,y value of the Position as a String
	 */
	public String toString()
	{
		return x + "," + y;
	}
	
	public int x;
	public int y;
}

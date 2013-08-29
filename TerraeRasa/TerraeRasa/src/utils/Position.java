package utils;

import java.io.Serializable;

/**
 * Position implements a simple class that stores a point in 2D space. It is essentially a Vector2D, storing an (x,y) point, 
 * though the given x and y values must be integers. There are two notable additions:
 * <ul>
 *  <li>{@link #toString()} will return a value of "x,y" for a point</li>
 *  <li></li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Position 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	
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
	
	/**
	 * Gets the x value of this point.
	 * @return the x value of this point
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Gets the y value of this point.
	 * @return the y value of this point
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Sets the x value of this point.
	 * @param x the new x value for this point
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 * Sets the y value for this point.
	 * @param y the new y value for this point
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * Determines if a given (x, y) set equals this position.
	 * @param x the x value to compare to this position's x value
	 * @param y the y value to compare to this position's y value
	 * @return true if the given (x, y) set equals this position; otherwise false
	 */
	public boolean equals(int x, int y)
	{
		return (this.x == x && this.y == y);
	}

	/**
	 * Determines if a given position set equals this position.
	 * @param x the x value to compare to this position's x value
	 * @param y the y value to compare to this position's y value
	 * @return true if the given position set equals this position; otherwise false
	 */
	public boolean equals(Position position)
	{
		return (this.x == position.x && this.y == position.y);
	}

	/**
	 * Gets the point as a string in the form of "x,y" (there are no brackets). This is the same form used as the keys for the world chunk map.
	 * @return the x,y value of the Position as a String
	 */
	public String toString()
	{
		return x + "," + y;
	}
}

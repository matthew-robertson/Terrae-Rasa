package utils;

import java.io.Serializable;

/**
 * Cooldown is a data structure to store an ability or potion cooldown of some sort. An ID and remaining time are stored.
 * An id should be a value with some connection to the item, such as the item id. Calling {@link #update()} will decrease the
 * ticksRemaining by 1, and {@link #isExpired()} returns a boolean representing whether or not the cooldown should still apply.
 * True means that it should not and the cooldown is finished.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Cooldown 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int id;
	public int ticksLeft;
	
	/**
	 * Creates a new cooldown with the specified ID and length of the cooldown in ticks
	 * @param id the id of this cooldown
	 * @param ticksDuration
	 */
	public Cooldown(int id, int ticksDuration)
	{
		this.id = id;
		this.ticksLeft = ticksDuration;
	}
	
	/**
	 * Gets whether or not the cooldown is expired. If the remaining time is 0 or negative then it is expired.
	 * @return true if ticksLeft is <= 0, otherwise false
	 */
	public boolean isExpired()
	{
		return ticksLeft <= 0;
	}
	
	/**
	 * Updates the cooldown, reducing the ticksLeft by 1
	 */
	public void update()
	{
		ticksLeft--;
	}
}

package utils;

import java.io.Serializable;

/**
 * A GemSocket is a small helper class to hold an ItemGem. Socketting a new gem will remove
 * the old one.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class GemSocket
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private DisplayableItemStack gem;
	
	/**
	 * Sockets the given ItemGem, replacing whatever gem is currently held.
	 * @param gem the ItemGem to socket
	 */
	public void socket(DisplayableItemStack gem)
	{
		this.gem = gem;
	}	
	
	/**
	 * Gets the ItemGem stored in this GemSocket. This will be null if none is stored in it.
	 * @return the ItemGem stored in this socket; or null if there is none.
	 */
	public DisplayableItemStack getGem()
	{
		return gem;
	}
}

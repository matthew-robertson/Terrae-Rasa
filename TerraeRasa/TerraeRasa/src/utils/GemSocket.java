package utils;

import items.ItemGem;

/**
 * A GemSocket is a small helper class to hold an ItemGem.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.1
 * @since       1.0
 */
public class GemSocket
{
	private ItemGem gem;
	
	/**
	 * Sockets the given ItemGem, replacing whatever gem is currently held.
	 * @param gem the ItemGem to socket
	 */
	public void socket(ItemGem gem)
	{
		this.gem = gem;
	}	
	
	/**
	 * Gets the ItemGem stored in this GemSocket. This will be null if none is stored in it.
	 * @return the ItemGem stored in this socket; or null if there is none.
	 */
	public ItemGem getGem()
	{
		return gem;
	}
}

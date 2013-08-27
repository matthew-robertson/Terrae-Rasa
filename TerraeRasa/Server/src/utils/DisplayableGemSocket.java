package utils;

import java.io.Serializable;

public class DisplayableGemSocket
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DisplayableItemStack gem;
	
	public DisplayableGemSocket()
	{
		gem = null;
	}
	
	public DisplayableGemSocket(GemSocket socket)
	{
		if(socket.getGem() != null)
		{
			this.gem = new DisplayableItemStack(socket.getGem());
		}
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

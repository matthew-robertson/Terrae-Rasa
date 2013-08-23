package savable;

import java.io.Serializable;

import utils.DisplayableItemStack;

public class SavableBlock 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DisplayableItemStack[] mainInventory;
	public int id;
	public int metaData;
	public int bitMap;
	
}

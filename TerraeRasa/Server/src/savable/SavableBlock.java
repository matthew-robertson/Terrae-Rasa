package savable;

import java.io.Serializable;

import utils.ItemStack;

public class SavableBlock 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ItemStack[] mainInventory;
	public int id;
	public int metaData;
	public int bitMap;
	
}

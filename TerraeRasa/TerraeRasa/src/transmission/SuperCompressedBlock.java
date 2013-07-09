package transmission;

import java.io.Serializable;

import utils.ItemStack;


public class SuperCompressedBlock 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ItemStack[] mainInventory;
	public short id;
	public short metaData;
	public short bitMap;
}

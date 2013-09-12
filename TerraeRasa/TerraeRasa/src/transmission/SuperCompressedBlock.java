package transmission;

import java.io.Serializable;

import utils.DisplayableItemStack;


public class SuperCompressedBlock 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DisplayableItemStack[] mainInventory;
	public short id;
	public byte metaData;
	public byte bitMap;
}

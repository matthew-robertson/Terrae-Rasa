package transmission;

import java.io.Serializable;

import utils.ItemStack;

public class CompressedInventory 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ItemStack[] mainInventory;
	public ItemStack[] armorInventory;
	public ItemStack[] quiver;
}

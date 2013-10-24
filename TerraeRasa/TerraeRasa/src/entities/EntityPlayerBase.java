package entities;

import items.ItemArmor;

import java.util.Vector;

import server.entities.EntityLiving;
import utils.ItemStack;

public abstract class EntityPlayerBase extends EntityLiving
{
	private static final long serialVersionUID = 1L;
	public Vector<String> changedInventorySlots = new Vector<String>();

	public abstract void onArmorChange();

	public abstract void onInventoryChange();

	public abstract void removeSingleArmorItem(ItemArmor itemArmor, ItemStack oldStack, int index);

	public abstract void applySingleArmorItem(ItemArmor itemArmor, ItemStack newStack, int index);
}

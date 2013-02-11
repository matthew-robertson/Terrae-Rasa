package net.dimensia.src;

import java.util.Vector;

public class ItemRanged extends Item
{
	protected Vector<ItemStack> ammo;
	
	protected ItemRanged(int i, int d) {
		super(i);
		maxStackSize = 1;
		damage = d;
		if (ammo == null){
			ammo = new Vector<ItemStack>();
		}
	}	
	
	public ItemRanged setAmmo(Item item){
		ammo.add(new ItemStack(item));
		return this;
	}
	
	public ItemRanged setdamage(int d){
		damage = d;
		return this;
	}
	
	public ItemStack[] getAmmoAsArray(){
		ItemStack[] ammunition = new ItemStack[ammo.size()];
		ammo.copyInto(ammunition);
		return ammunition;
	}	
	
	public int getDamage(){
		return damage;
	}
}

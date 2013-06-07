package items;


import java.util.Vector;

import utils.ItemStack;


public class ItemRanged extends Item
{
	protected Vector<ItemStack> ammo;
	protected int cooldownTicks;
	
	protected ItemRanged(int i, int d) {
		super(i);
		maxStackSize = 1;
		damage = d;
		cooldownTicks = 5;
		if (ammo == null){
			ammo = new Vector<ItemStack>();
		}
	}	
	
	public ItemRanged setCooldownTicks(int i){
		cooldownTicks = i;
		return this;
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
	
	public int getCooldownTicks(){
		return cooldownTicks;
	}
	
	public int getDamage(){
		return damage;
	}
}

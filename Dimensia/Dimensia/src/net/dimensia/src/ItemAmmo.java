package net.dimensia.src;

public class ItemAmmo extends Item
{
	protected ItemAmmo(int i)
	{
		super(i);
		maxStackSize = 250;
	}
	
	public ItemAmmo setProjectile(EntityProjectile projectile){
		this.projectile = projectile;
		return this;
	}
}

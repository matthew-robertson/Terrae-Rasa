package items;

import server.entities.EntityProjectile;

public class ItemAmmo extends Item
{
	protected EntityProjectile projectile;
	
	protected ItemAmmo(int i)
	{
		super(i);
		maxStackSize = 250;
	}
	
	public ItemAmmo setProjectile(EntityProjectile projectile){
		this.projectile = projectile;
		this.damage = projectile.damage;
		return this;
	}
	
	public EntityProjectile getProjectile(){
		return projectile;
	}
	
}

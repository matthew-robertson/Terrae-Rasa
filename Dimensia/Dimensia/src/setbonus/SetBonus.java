package setbonus;

import entities.EntityLivingPlayer;

/**
 * Set bonus - a static bonus to a stat
 *
 */
public abstract class SetBonus 
{
	protected float power;
	
	protected SetBonus(float power)
	{
		this.power = power;
	}
	
	public abstract void apply(EntityLivingPlayer player);
	
	public abstract void remove(EntityLivingPlayer player);
	
}

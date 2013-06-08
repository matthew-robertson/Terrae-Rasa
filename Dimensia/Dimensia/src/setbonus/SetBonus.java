package setbonus;

import entities.EntityLivingPlayer;

/**
 * Set bonus - a static bonus to a stat
 *
 */
public abstract class SetBonus 
{
	protected float power;
	protected int piecesRequiredToActivate;
	
	protected SetBonus(float power)
	{
		this.power = power;
		this.piecesRequiredToActivate = 1;
	}
	
	protected SetBonus(float power, int piecesRequiredToActivate)
	{
		this.power = power;
		this.piecesRequiredToActivate = piecesRequiredToActivate;
	}
	
	public int getPiecesRequiredToActivate()
	{
		return piecesRequiredToActivate;
	}
	
	public SetBonus setPiecesRequiredToActivate(int pieces)
	{
		this.piecesRequiredToActivate = pieces;
		return this;
	}
	
	public abstract void apply(EntityLivingPlayer player);
	
	public abstract void remove(EntityLivingPlayer player);
	
}

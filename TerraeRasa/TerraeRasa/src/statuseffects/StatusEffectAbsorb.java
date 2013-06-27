package statuseffects;

import entities.EntityLiving;
import world.World;

/**
 * <code>StatusEffectAbsorb extends StatusEffect</code> 
 * A StatusEffectAbsorb is a beneficial effect that can be applied to an EntityLiving that will absorb
 * damage, upto the point where it "breaks" (can absorb no more damage). 
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class StatusEffectAbsorb extends StatusEffect
{
	private static final long serialVersionUID = 1L;
	private int startAbsorbAmount;
	private int remainingAbsorb;
	
	/**
	 * Creates a new StatusEffectAbsorb. 
	 * @param durationSeconds
	 * @param tier the tier of the absorb 
	 * @param absorb the amount this StatusEffect will absorb, in total damage
	 */
	public StatusEffectAbsorb(double durationSeconds, int tier, int absorb) 
	{
		super(durationSeconds, tier, absorb, 1);
		this.iconX = 12;
		this.iconY = 2;
		this.startAbsorbAmount = absorb;
		this.remainingAbsorb = absorb;
		this.reapplicationSkipsRemovalEffect = true;
	}
	
	/**
	 * Gets the remaining damage that can be absorbed before breaking this absorb-shield
	 * @return the amount of damage that can still be absorbed
	 */
	public int getRemainingAbsorb()
	{
		return remainingAbsorb;
	}
	
	/**
	 * Deals damage to an absorb, partially or completely using it. A value of 0 being returned indicates that the absorb
	 * was fully used, but not necessarily that the damage was entirely absorbed.
	 * @param damageDone the damage to deal to the absorb
	 * @return the damage left over from the absorb after damaging it, a value of 0 indicates the absorb is fully used
	 */
	public int absorbDamage(double damageDone)
	{
		if(damageDone <= remainingAbsorb)
		{
			remainingAbsorb -= damageDone;
			return 0;
		}
		else
		{
			remainingAbsorb -= damageDone;
			return Math.abs(remainingAbsorb);
		}
	}
	
	/**
	 * Gets whether or not the absorb has been broken. A broken absorb shield is one that has been 
	 * fully used, having 0 or less remaining absorbable damage.
	 * @return
	 */
	public boolean isBroken()
	{
		return remainingAbsorb <= 0;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
	
		if(isBroken())
		{
			ticksLeft = 0;
		}		
	}
	
	/**
	 * Restores the absorb to the initial(starting) amount
	 */
	public void refresh()
	{
		remainingAbsorb = startAbsorbAmount;
	}
	
	public String toString()
	{
		return "Absorbs " + (int)remainingAbsorb + " damage";
	}	
}

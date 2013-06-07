package statuseffects;

import java.io.Serializable;

import entities.EntityLiving;
import world.World;

public class StatusEffect implements Serializable
{
	private static final long serialVersionUID = 1L;
	public boolean reapplicationSkipsRemovalEffect;
	public int ticksLeft;
	public int tier;
	/** If this effect benefits the entity, then this is true. */
	public boolean isBeneficialEffect;
	public int iconX;
	public int iconY;
	public boolean stacksIndependantly;
	public float power;
	public int ticksBetweenEffect;
	
	/**
	 * Creates a new status effect of no type. This class should be extended in order to make use of its functionality.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffect(float durationSeconds, int tier, float power, int ticksBetweenEffect)
	{
		reapplicationSkipsRemovalEffect = false;
		ticksLeft = (int) (durationSeconds * 20);
		this.tier = tier;
		this.isBeneficialEffect = true;
		iconX = 15;
		iconY = 0;
		stacksIndependantly = true;
		this.ticksBetweenEffect = ticksBetweenEffect;
		this.power = power;
	}
	
	public StatusEffect(StatusEffect effect)
	{
		this.reapplicationSkipsRemovalEffect =  effect.reapplicationSkipsRemovalEffect;
		this.ticksLeft = effect.ticksLeft;
		this.tier = effect.tier;
		this.power = effect.power;
		this.ticksBetweenEffect = effect.ticksBetweenEffect;
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
	}
	
	public boolean isExpired()
	{
		return ticksLeft <= 0;
	}
	
	public void setIconPosition(int x, int y)
	{
		this.iconX = x;
		this.iconY = y;
	}
	
	public String toString()
	{
		return "Status_Effect_Base(None)";
	}
}

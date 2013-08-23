package statuseffects;

import world.World;
import entities.EntityLiving;

/**
 * A StatusEffect is any short term beneficial or negative effect. They can be applied to an EntityLiving or any subclass
 * thereof. A StatusEffect will persist until the remaining time expires. 
 * <br><br>
 * When applied a StatusEffect will call {@link #applyInitialEffect(EntityLiving)}, and when removed
 * {@link #removeInitialEffect(EntityLiving)} will be called. Each game tick the effect is active 
 * {@link #applyPeriodicBonus(World, EntityLiving)} will be called. None of these methods must do anything
 * and don't in StatusEffect, but any subclass may override them to implement custom functionality.
 * The apply and remove methods should directly counter each other to ensure that residual effects are
 * not remaining.
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffect 
		 implements IStatusEffect
{
	public boolean reapplicationSkipsRemovalEffect;
	public int ticksLeft;
	public int tier;
	/** If this effect benefits the entity, then this is true. */
	public boolean isBeneficialEffect;
	public int iconX;
	public int iconY;
	public boolean stacksIndependantly;
	public double power;
	public int ticksBetweenEffect;
	public long id;
	
	/**
	 * Creates a new status effect of no type. This class should be extended in order to make use of its functionality.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public StatusEffect(double durationSeconds, int tier, double power, int ticksBetweenEffect)
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
		this.id = System.nanoTime();
	}
	
	public StatusEffect(StatusEffect effect)
	{
		this.reapplicationSkipsRemovalEffect =  effect.reapplicationSkipsRemovalEffect;
		this.ticksLeft = effect.ticksLeft;
		this.tier = effect.tier;
		this.power = effect.power;
		this.ticksBetweenEffect = effect.ticksBetweenEffect;
		this.id = effect.id;
		this.isBeneficialEffect = effect.isBeneficialEffect;
		this.iconX = effect.iconX;
		this.iconY = effect.iconY;
		this.stacksIndependantly = effect.stacksIndependantly;
	}
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{	
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
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
	
	public long getID()
	{
		return id;
	}
	
	public String toString()
	{
		return "Status_Effect_Base(None)";
	}
}

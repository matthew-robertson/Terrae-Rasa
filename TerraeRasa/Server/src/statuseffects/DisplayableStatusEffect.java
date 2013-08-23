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
public class DisplayableStatusEffect 
{
	public int ticksLeft;
	/** If this effect benefits the entity, then this is true. */
	public boolean isBeneficialEffect;
	public int iconX;
	public int iconY;
//	public int xSizeOrtho;
//	public int ySizeOrtho;
	public long id;
	public String description;
	
	/**
	 * Creates a new status effect of no type. This class should be extended in order to make use of its functionality.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	public DisplayableStatusEffect(double durationSeconds, int tier, double power, int ticksBetweenEffect, String description)
	{
		ticksLeft = (int) (durationSeconds * 20);
		this.isBeneficialEffect = true;
		iconX = 15;
		iconY = 0;
		this.id = System.nanoTime();
		this.description = description;
	}
	
	public DisplayableStatusEffect(StatusEffect effect)
	{
		this.ticksLeft = effect.ticksLeft;
		this.isBeneficialEffect = effect.isBeneficialEffect;
		this.iconX = effect.iconX;
		this.iconY = effect.iconY;
		this.id = effect.id;
		this.description = effect.toString();
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
		return description;
	}
	
	public String getDescription()
	{
		return description;
	}
}

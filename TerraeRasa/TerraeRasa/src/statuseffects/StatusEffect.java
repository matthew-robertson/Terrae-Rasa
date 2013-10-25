package statuseffects;

import java.io.Serializable;

import server.entities.EntityLiving;
import world.World;

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
		 implements IStatusEffect, Serializable
{
	private static final long serialVersionUID = 1L;
	public boolean reapplicationSkipsRemovalEffect;
	public int tier;
	public boolean stacksIndependantly;
	public double power;
	public int ticksBetweenEffect;

	/** The remaining time of this effect in game ticks. */
	public int ticksLeft;
	/** If this effect benefits the entity somehow, then this is true. */
	public boolean isBeneficialEffect;
	/** The X position of the StatusEffect icon, measured in squares of pixel size 16 on the sprite sheet. IE 
	 * position 1 is from pixels 16 to 32 on the X-axis of the spritesheet. */
	public short iconX; 
	/** The y position of the StatusEffect icon, measured in squares of pixel size 16 on the sprite sheet. IE 
	 * position 3 is from pixels 48 to 64 on the Y-axis of the spritesheet. */
	public short iconY;
	/** The width of this StatusEffect icon in squares of size 16. Size 1 for example is 16 pixels in the x direction. */
	public short iconWidth; 
	/** The height of this StatusEffect icon in squares of size 16. Size 2 for example is 32 pixels in the y direction. */
	public short iconHeight; 
	/** The unique ID number for this StatusEffect, allowing it to be distinguished from other effects. */
	public long id;
	
	/**
	 * Creates a new status effect of no type. This class should be extended in order to make use of its functionality.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	protected StatusEffect(double durationSeconds, int tier, double power, int ticksBetweenEffect)
	{
		reapplicationSkipsRemovalEffect = false;
		ticksLeft = (int) (durationSeconds * 20);
		this.tier = tier;
		this.isBeneficialEffect = true;
		iconX = 15;
		iconY = 0;
		iconWidth = 16;
		iconHeight = 16;
		stacksIndependantly = true;
		this.ticksBetweenEffect = ticksBetweenEffect;
		this.power = power;
		this.id = System.nanoTime();
	}
	
	/**
	 * Provides a deep copy of all the values in this StatusEffect base class using a manually implemented copy constructor.
	 * @param effect the StatusEffect to copy
	 */
	public StatusEffect(StatusEffect effect)
	{
		this.reapplicationSkipsRemovalEffect =  effect.reapplicationSkipsRemovalEffect;
		this.ticksLeft = effect.ticksLeft;
		this.tier = effect.tier;
		this.power = effect.power;
		this.ticksBetweenEffect = effect.ticksBetweenEffect;
		this.id = effect.id;
		this.iconWidth = effect.iconWidth;
		this.iconHeight = effect.iconHeight;
		this.isBeneficialEffect = effect.isBeneficialEffect;
		this.iconX = effect.iconX;
		this.iconY = effect.iconY;
		this.stacksIndependantly = effect.stacksIndependantly;
	}
	
	public void applyInitialEffect(World world, EntityLiving entity)
	{	
	}
	
	public void removeInitialEffect(World world, EntityLiving entity) {
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
	}
	
	public boolean isExpired()
	{
		return ticksLeft <= 0;
	}
	
	/**
	 * Sets the (x,y) icon positions of this StatusEffect. Each unit in the x or y directions here will correspond
	 * to a 16 pixel movement on the sprite sheet. Ex. (5,7) as an icon position means this StatusEffect's icon
	 * will start at (5 * 16, 7 * 16) = (80, 112) on the spritesheet. 
	 * @param x the new x position for this StatusEffect's icon
	 * @param y the new y position for this StatusEffect's icons
	 */
	public void setIconPosition(short x, short y)
	{
		this.iconX = x;
		this.iconY = y;
	}
	
	public long getID()
	{
		return id;
	}
	
	/**
	 * Provides a description of this StatusEffect that will update alongside the StatusEffect is applicable.
	 */
	public String toString()
	{
		return "Status_Effect_Base(None)";
	}
}

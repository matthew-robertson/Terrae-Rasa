package statuseffects;

import java.io.Serializable;

/**
 * A DisplayableStatusEffect contains the data to display a StatusEffect in the client. It is a conversion of 
 * a status effect to a data structure that is simpler to transmit over a socket.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DisplayableStatusEffect 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** The remaining time of this effect in game ticks. */
	public int ticksLeft;
	/** If this effect benefits the entity, then this is true. */
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
	/** A description of this DisplayableStatusEffect which can be rendered or output. */
	public String description;
	
	/**
	 * Creates a new status effect of no type. This class should be extended in order to make use of its functionality.
	 * @param durationSeconds the duration of the effect in seconds
	 * @param tier the tier of the effect, which may serve as a tie-breaker if effects cannot stack
	 * @param power the strength of the effect
	 * @param ticksBetweenEffect the number of game ticks between the periodic effect being applied, if applicable
	 */
	protected DisplayableStatusEffect(double durationSeconds, int tier, double power, int ticksBetweenEffect, String description)
	{
		ticksLeft = (int) (durationSeconds * 20);
		this.isBeneficialEffect = true;
		iconX = 15;
		iconY = 0;
		iconWidth = 16;
		iconHeight = 16;
		this.id = System.nanoTime();
		this.description = description;
	}
	
	protected DisplayableStatusEffect(StatusEffect effect)
	{
		this.ticksLeft = effect.ticksLeft;
		this.isBeneficialEffect = effect.isBeneficialEffect;
		this.iconX = effect.iconX;
		this.iconY = effect.iconY;
		this.iconWidth = effect.iconWidth;
		this.iconHeight = effect.iconHeight;
		this.id = effect.id;
		this.description = effect.toString();
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

package auras;

import entities.EntityPlayer;

/**
 * Auras are event listeners for the player. They react to certain events and will do something if appropriate. Aura.java implements 
 * all the methods required by IAura but they do not do anything. To provide customized event functionality, the different 
 * event methods should be overriden. Version 1.0 of Aura includes the following event methods:
 * <ul>
 * <li>{@link #onDamageDone(EntityPlayer)}</li>
 * <li>{@link #onDamageTaken(EntityPlayer)}</li>
 * <li>{@link #onDeath(EntityPlayer)}</li>
 * <li>{@link #onHeal(EntityPlayer)}</li>
 * <li>{@link #onPercentageHealth(EntityPlayer)}</li>
 * <li>{@link #onStatusEffectGained(EntityPlayer)}</li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DisplayableAura  
{
	/** A number unique to this aura, used to distinguish it from other auras it. */
	private final long id;
	protected String description;

	protected DisplayableAura(int maxCooldown, double activationChance, String description)
	{
		this.id = System.nanoTime();
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the id of this Aura - a unique value to this Aura.
	 * @return a value unique to this Aura
	 */
	public final long getID()
	{
		return id;
	}
	
	/**
	 * Overrides Object.toString() to accurately describe this aura in text form.
	 */
	public String toString()
	{
		return description;
	}
}

package auras;

import java.io.Serializable;
import java.util.Random;

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
public abstract class Aura 
		implements IAura, Serializable
{
	private static final long serialVersionUID = 1L;
	protected final Random random;
	/** The maximum cooldown of the Aura in game ticks. This will be the cooldown triggered when the aura actives. A value of zero or one indicates no cooldown*/
	protected int maxCooldown;
	/** The remaining time before this aura can reactivate, in game ticks. */
	protected int remainingCooldown;
	/** The chance for this Aura to activate, where 0F indicates never and 1F indicates always. */
	protected double activationChance;
	/** A number unique to this aura, used to distinguish it from other auras it. */
	private final long id;
	
	protected Aura()
	{
		this.random = new Random();
		this.remainingCooldown = 0;
		this.activationChance = 1F;
		this.maxCooldown = 1;
		this.id = System.nanoTime();
	}

	protected Aura(int maxCooldown, double activationChance)
	{
		this.random = new Random();
		this.remainingCooldown = 0;
		this.activationChance = activationChance;
		this.maxCooldown = maxCooldown;
		this.id = System.nanoTime();
	}
	
	public void update(EntityPlayer player)
	{
		remainingCooldown--;
	}

	public void onDamageDone(EntityPlayer player)
	{
	}

	public void onDamageTaken(EntityPlayer player)
	{
		onPercentageHealth(player);
	}
	
	public void onHeal(EntityPlayer player)
	{
		onPercentageHealth(player);
	}
	
	public void onPercentageHealth(EntityPlayer player)
	{
	}

	public void onDeath(EntityPlayer player)
	{
	}
	
	/**
	 * Returns the id of this Aura - a unique value to this Aura.
	 * @return a value unique to this Aura
	 */
	public final long getID()
	{
		return id;
	}
}

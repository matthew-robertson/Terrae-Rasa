package auras;

import entities.EntityLivingPlayer;

/**
 * IAura defines the different event listeners an Aura can implement. These must be implemented by the base class of all Auras - Aura.
 * Version 1.0 defines the following methods:
 * <ul>
 * <li>{@link #onDamageDone(EntityLivingPlayer)}</li>
 * <li>{@link #onDamageTaken(EntityLivingPlayer)}</li>
 * <li>{@link #onDeath(EntityLivingPlayer)}</li>
 * <li>{@link #onHeal(EntityLivingPlayer)}</li>
 * <li>{@link #onPercentageHealth(EntityLivingPlayer)}</li>
 * <li>{@link #onStatusEffectGained(EntityLivingPlayer)}</li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IAura 
{
	public abstract void onDamageDone(EntityLivingPlayer player); 

	public abstract void onDamageTaken(EntityLivingPlayer player); 

	public abstract void onHeal(EntityLivingPlayer player);

	public abstract void onPercentageHealth(EntityLivingPlayer player); 

	public abstract void onDeath(EntityLivingPlayer player); 
}

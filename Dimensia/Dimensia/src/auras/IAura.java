package auras;

import entities.EntityPlayer;

/**
 * IAura defines the different event listeners an Aura can implement. These must be implemented by the base class of all Auras - Aura.
 * Version 1.0 defines the following methods:
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
public interface IAura 
{
	public abstract void onDamageDone(EntityPlayer player); 

	public abstract void onDamageTaken(EntityPlayer player); 

	public abstract void onHeal(EntityPlayer player);

	public abstract void onPercentageHealth(EntityPlayer player); 

	public abstract void onDeath(EntityPlayer player); 
}

package auras;

import world.World;
import entities.EntityPlayer;

/**
 * IAura defines the different event listeners an Aura can implement. These must be implemented by the base class of all Auras - Aura.
 * Version 1.0 defines the following methods:
 * <ul>
 * <li>{@link #onDamageDone(World, EntityPlayer)}</li>
 * <li>{@link #onDamageTaken(World, EntityPlayer)}</li>
 * <li>{@link #onDeath(World, EntityPlayer)}</li>
 * <li>{@link #onHeal(World, EntityPlayer)}</li>
 * <li>{@link #onPercentageHealth(World, EntityPlayer)}</li>
 * <li>{@link #onStatusEffectGained(World, EntityPlayer)}</li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IAura 
{
	public abstract void onDamageDone(World world, EntityPlayer player); 

	public abstract void onDamageTaken(World world, EntityPlayer player); 

	public abstract void onHeal(World world, EntityPlayer player);

	public abstract void onPercentageHealth(World world, EntityPlayer player); 

	public abstract void onDeath(World world, EntityPlayer player); 
}

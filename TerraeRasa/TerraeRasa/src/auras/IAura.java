package auras;

import server.entities.EntityPlayer;
import utils.Damage;
import world.World;

/**
 * IAura defines the different event listeners an Aura can implement. These must be implemented by the base class of all Auras - Aura.
 * Version 1.0 defines the following methods:
 * <ul>
 *  <li>{@link #onDamageDone(World, EntityPlayer)}</li>
 *  <li>{@link #onDamageTaken(World, EntityPlayer)}</li>
 *  <li>{@link #onHeal(World, EntityPlayer)}</li>
 *  <li>{@link #onPercentageHealth(World, EntityPlayer)} </li>
 *  <li>{@link #onDeath(World, EntityPlayer)}</li>
 *  <li>{@link #onTick(World, EntityPlayer)} </li>
 *  <li>{@link #onManaRestored(World, EntityPlayer)} </li>
 *  <li>{@link #onManaSpend(World, EntityPlayer)} </li>
 *  <li>{@link #onPercentageMana(World, EntityPlayer)} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IAura 
{
	/**
	 * This event activates when the player deals damage.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 * @param damage the damage hit to be inflicted by the player
	 */
	public abstract void onDamageDone(World world, EntityPlayer player, Damage damage); 

	/**
	 * This event activates when the player takes damage.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 * @param damage the damage hit to be inflicted to the player
	 */
	public abstract void onDamageTaken(World world, EntityPlayer player, Damage damage); 

	/**
	 * This event activates when the player is healed.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onHeal(World world, EntityPlayer player);

	/**
	 * This event activates when the player's health is updated, and reaches a certain level.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onPercentageHealth(World world, EntityPlayer player); 

	/**
	 * This event activates when the player is killed.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onDeath(World world, EntityPlayer player); 
	
	/**
	 * This event activates on each game tick.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onTick(World world, EntityPlayer player);
	
	/**
	 * This event activates when the player's mana is restored.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onManaRestored(World world, EntityPlayer player);

	/**
	 * This event activates when the player spends mana.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onManaSpend(World world, EntityPlayer player);

	/**
	 * This event activates when the player's mana reaches a certain level.
	 * @param world the world the player is currently in
	 * @param player the player to whom this aura belongs
	 */
	public abstract void onPercentageMana(World world, EntityPlayer player);
}

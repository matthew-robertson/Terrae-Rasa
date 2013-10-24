package statuseffects;

import server.entities.EntityLiving;
import world.World;

/**
 * IStatusEffect defines the different methods that a StatusEffect can implement in order to perform certain actions.
 * The following methods must be defined either by the baseclass StatusEffect or any extension thereof:
 * <ul>
 *  <li> {@link #applyInitialEffect(World, EntityLiving)} </li>
 *  <li> {@link #removeInitialEffect(World, EntityLiving)} </li>
 *  <li> {@link #applyPeriodicBonus(World, EntityLiving)} </li>
 *  <li> {@link #isExpired()} </li>
 *  <li> {@link #getID()} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IStatusEffect 
{
	/**
	 * Applies the initial effect of this StatusEffect. This should be completely countered by {@link #removeInitialEffect(World, EntityLiving)}
	 * or the StatusEffect will leave a residual effect after being removed.
	 * @param world the world the player is currently in
	 * @param entity the living entity to whom this effect belongs
	 */
	public void applyInitialEffect(World world, EntityLiving entity);
	
	/**
	 * Removes the initial effect of this StatusEffect. This should completely counter {@link #applyInitialEffect(World, EntityLiving)}
	 * or the StatusEffect will leave residual effects behind.
	 * @param world the world the player is currently in
	 * @param entity the living entity to whom this effect belongs
	 */
	public void removeInitialEffect(World world, EntityLiving entity);
	
	/**
	 * Applies some sort of effect for this StatusEffect on each game tick, if applicable.
	 * @param world the world the player is currently in
	 * @param entity the living entity to whom this effect belongs
	 */
	public void applyPeriodicBonus(World world, EntityLiving entity);
	
	/**
	 * Determines whether or not a StatusEffect is expired. If it is expired, it should be removed from the entity.
	 * @return true if the effect is expired (and should be removed), otherwise false
	 */
	public boolean isExpired();
	
	/**
	 * Gets a unique ID for this particular instance of StatusEffect.
	 * @return a unique ID for this particular instance of StatusEffect, which no other instance will share
	 */
	public long getID();
}

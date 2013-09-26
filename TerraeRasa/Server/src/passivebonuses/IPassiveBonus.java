package passivebonuses;

import entities.EntityPlayer;

/**
 * IPassiveBonus defines the methods a PassiveBonus must implement. Version 1.0 includes definitions for the following methods:
 * <ul>
 *  <li> {@link #apply(EntityPlayer)} </li>
 *  <li> {@link #remove(EntityPlayer)} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IPassiveBonus 
{	
	/**
	 * Applies the initial effect of the passive bonus to the given EntityPlayer. This should be a direct counter to the effects of
	 * {@link #remove(EntityPlayer)}.
	 * @param player the player to whom this status effect is being applied
	 */
	public abstract void apply(EntityPlayer player);

	/**
	 * Removes the initial effect of the passive bonus to the given EntityPlayer. This should be a direct counter to the effects of 
	 * {@link #apply(EntityPlayer)}.
	 * @param player the player to whom this status effect is being removed
	 */
	public abstract void remove(EntityPlayer player);
}

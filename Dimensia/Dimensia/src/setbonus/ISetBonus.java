package setbonus;

import entities.EntityLivingPlayer;

/**
 * ISetBonus defines the methods a SetBonus must implement. Version 1.0 includes definitions for the following methods:
 * <ul>
 * <li> {@link #apply(EntityLivingPlayer)} </li>
 * <li> {@link #remove(EntityLivingPlayer)} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface ISetBonus 
{	
	public abstract void apply(EntityLivingPlayer player);
	
	public abstract void remove(EntityLivingPlayer player);
}

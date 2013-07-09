package passivebonuses;

import entities.EntityPlayer;

/**
 * IPassiveBonus defines the methods a PassiveBonus must implement. Version 1.0 includes definitions for the following methods:
 * <ul>
 * <li> {@link #apply(EntityPlayer)} </li>
 * <li> {@link #remove(EntityPlayer)} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IPassiveBonus 
{	
	public abstract void apply(EntityPlayer player);
	
	public abstract void remove(EntityPlayer player);
}

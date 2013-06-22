package setbonus;

import entities.EntityPlayer;

/**
 * ISetBonus defines the methods a SetBonus must implement. Version 1.0 includes definitions for the following methods:
 * <ul>
 * <li> {@link #apply(EntityPlayer)} </li>
 * <li> {@link #remove(EntityPlayer)} </li>
 * </ul>
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface ISetBonus 
{	
	public abstract void apply(EntityPlayer player);
	
	public abstract void remove(EntityPlayer player);
}

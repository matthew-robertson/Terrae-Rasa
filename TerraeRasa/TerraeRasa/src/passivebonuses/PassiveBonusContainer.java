package passivebonuses;

import java.io.Serializable;
import java.util.Vector;

import entities.EntityPlayer;

/**
 * A set bonus container holds an array of set bonuses for further use. This is useful when 
 * reversing a set bonus, for example. Set Bonuses cannot be added or removed after being 
 * added, but can be copied to a new set bonus container. This restriction is to prevent
 * corruption or wierd side effects.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusContainer 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private PassiveBonus[] bonuses;
	
	/**
	 * Constructs a new PassiveBonusContainer with the specified bonuses.
	 * @param bonuses the bonuses for this container
	 */
	public PassiveBonusContainer(PassiveBonus[] bonuses)
	{
		this.bonuses = bonuses;
	}
	
	/**
	 * Constructs a new PassiveBonusContainer with the specified bonuses. This version of the constructor 
	 * accepts a Vector instead of an Array.
	 * @param allPassiveBonuses a Vector of PassiveBonuses that will be stored in the PassiveBonusContainer
	 */
	public PassiveBonusContainer(Vector<PassiveBonus> allPassiveBonuses) 
	{
		this.bonuses = new PassiveBonus[allPassiveBonuses.size()];
		allPassiveBonuses.copyInto(this.bonuses);
	}

	/**
	 * Gets the set bonus at the given index. This will throw an exception if out of bounds.
	 * @param index the index of the set bonus to get
	 * @return the set bonus at the given index
	 */
	public PassiveBonus getAtIndex(int index)
	{
		return bonuses[index];
	}
	
	/**
	 * Gets a list with all the set bonuses.
	 * @return all the set bonuses in this container
	 */
	public PassiveBonus[] getAll()
	{
		return bonuses;
	}
	
	/**
	 * Gets the total number of set bonuses.
	 * @return the total number of set bonuses
	 */
	public int length()
	{
		return bonuses.length;
	}
	
	/**
	 * Applies all the PassiveBonuses in the PassiveBonusContainer to the player
	 * @param player the player to apply the PassiveBonuses to
	 */
	public void applyAll(EntityPlayer player)
	{
		for(PassiveBonus bonus : bonuses)
		{
			bonus.apply(player);
		}
	}
	
	/**
	 * Removes all the PassiveBonuses in the PassiveBonusContainer from the player
	 * @param player the player to remove the PassiveBonuses from
	 */
	public void removeAll(EntityPlayer player)
	{
		for(PassiveBonus bonus : bonuses)
		{
			bonus.remove(player);
		}
	}
}

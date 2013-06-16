package setbonus;

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
public class SetBonusContainer 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private SetBonus[] bonuses;
	
	/**
	 * Constructs a new SetBonusContainer with the specified bonuses.
	 * @param bonuses the bonuses for this container
	 */
	public SetBonusContainer(SetBonus[] bonuses)
	{
		this.bonuses = bonuses;
	}
	
	/**
	 * Constructs a new SetBonusContainer with the specified bonuses. This version of the constructor 
	 * accepts a Vector instead of an Array.
	 * @param allSetBonuses a Vector of SetBonuses that will be stored in the SetBonusContainer
	 */
	public SetBonusContainer(Vector<SetBonus> allSetBonuses) 
	{
		this.bonuses = new SetBonus[allSetBonuses.size()];
		allSetBonuses.copyInto(this.bonuses);
	}

	/**
	 * Gets the set bonus at the given index. This will throw an exception if out of bounds.
	 * @param index the index of the set bonus to get
	 * @return the set bonus at the given index
	 */
	public SetBonus getAtIndex(int index)
	{
		return bonuses[index];
	}
	
	/**
	 * Gets a list with all the set bonuses.
	 * @return all the set bonuses in this container
	 */
	public SetBonus[] getAll()
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
	 * Applies all the SetBonuses in the SetBonusContainer to the player
	 * @param player the player to apply the SetBonuses to
	 */
	public void applyAll(EntityPlayer player)
	{
		for(SetBonus bonus : bonuses)
		{
			bonus.apply(player);
		}
	}
	
	/**
	 * Removes all the SetBonuses in the SetBonusContainer from the player
	 * @param player the player to remove the SetBonuses from
	 */
	public void removeAll(EntityPlayer player)
	{
		for(SetBonus bonus : bonuses)
		{
			bonus.remove(player);
		}
	}
}

package setbonus;

import net.dimensia.src.EnumSetBonuses;
import entities.EntityLivingPlayer;

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
{
	private EnumSetBonuses[] bonuses;
	
	/**
	 * Constructs a new SetBonusContainer with the specified bonuses.
	 * @param bonuses the bonuses for this container
	 */
	public SetBonusContainer(EnumSetBonuses[] bonuses)
	{
		this.bonuses = bonuses;
	}
	
	/**
	 * Gets the set bonus at the given index. This will throw an exception if out of bounds.
	 * @param index the index of the set bonus to get
	 * @return the set bonus at the given index
	 */
	public EnumSetBonuses getAtIndex(int index)
	{
		return bonuses[index];
	}
	
	/**
	 * Gets a list with all the set bonuses.
	 * @return all the set bonuses in this container
	 */
	public EnumSetBonuses[] getAll()
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
	
	public void apply(EntityLivingPlayer player)
	{
		
	}
	
	public void remove(EntityLivingPlayer player)
	{
		
	}
	
}

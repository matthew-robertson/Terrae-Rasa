package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusDexterityModifier extends PassiveBonus to provide changes to a player's dexterity modifier. This allows an item to increase 
 * the player's dexterity by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusDexterityModifier extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusDexterityModifier with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusDexterityModifier(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.dexterityModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.dexterityModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Dexterity";
	}
}

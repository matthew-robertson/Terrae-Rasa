package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusSpecialRegeneration extends PassiveBonus to provide changes to a player's [passive] specialRegenerationModifier. This allows an item to increase 
 * the player's special regeneration modifier by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusSpecialRegeneration extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusSpecialRegeneration with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusSpecialRegeneration(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.specialRegenerationModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.specialRegenerationModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Passive Special Regeneration";
	}
}

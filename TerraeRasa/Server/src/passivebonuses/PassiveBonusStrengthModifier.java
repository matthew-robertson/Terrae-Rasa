package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusStrengthModifier extends PassiveBonus to provide changes to a player's strength modifier. This allows an item to increase 
 * the player's strength by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusStrengthModifier extends PassiveBonus
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PassiveBonusStrengthModifier(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.strengthModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.strengthModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Strength";
	}
}

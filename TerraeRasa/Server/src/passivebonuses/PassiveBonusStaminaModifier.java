package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusStaminaModifier extends PassiveBonus to provide changes to a player's stamina modifier. This allows an item to increase 
 * the player's stamina by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusStaminaModifier extends PassiveBonus
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PassiveBonusStaminaModifier(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.staminaModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.staminaModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Stamina";
	}
}

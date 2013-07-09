package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusPickupRangeModifier extends PassiveBonus to provide changes to a player's pickup range. This allows an item to increase 
 * the player's pickup range by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusPickupRangeModifier extends PassiveBonus
{
	public PassiveBonusPickupRangeModifier(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.pickupRangeModifier *= (1 + power);
	}

	public void remove(EntityPlayer player) 
	{
		player.pickupRangeModifier /= (1 + power);
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Pickup Range";
	}
}

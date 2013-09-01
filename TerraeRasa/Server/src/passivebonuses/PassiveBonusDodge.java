package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusDodge extends PassiveBonus to increase the player's dodge chance by a given amount. Dodge is increased based on 
 * power from 0-1F. 1F gives a 100% chance to dodge, scaling down appropriately to 0% at 0F.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusDodge extends PassiveBonus
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PassiveBonusDodge(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.dodgeChance += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.dodgeChance -= power;
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Dodge Chance";
	}
}

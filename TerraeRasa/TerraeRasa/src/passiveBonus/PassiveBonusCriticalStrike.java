package passiveBonus;

import entities.EntityPlayer;

/**
 * PassiveBonusCriticalStrike extends PassiveBonus to increase the player's critical chance by a given amount. Critical Strike Chance
 * is increased based on power from 0-1F. 1F gives a 100% chance to critically strike, scaling down appropriately to 0% at 0F.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusCriticalStrike extends PassiveBonus
{

	public PassiveBonusCriticalStrike(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.criticalStrikeChance += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.criticalStrikeChance -= power;
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Critical Chance";
	}
}

package passivebonuses;

import server.entities.EntityPlayer;

/**
 * PassiveBonusAttackSpeed power extends PassiveBonus to increase the player's attack speed by a given amount. Attack Speed
 * is increased based on power from 0-1F. 1F gives a 100% increase to attack speed. Note: A 100% increase in attack
 * speed may not indicate instant attacks due to diminishing returns.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusAttackSpeed extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusAttackSpeed with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusAttackSpeed(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		//Is this the right stack method?
		player.attackSpeedModifier += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.attackSpeedModifier -= power;
	}
	
	public String toString()
	{
		return "+" + (int)(power * 100) + "% Attack Speed";
	}
}

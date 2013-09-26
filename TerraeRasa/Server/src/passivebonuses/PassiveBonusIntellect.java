package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusIntellect extends PassiveBonus to increase the player's intellect by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Intellect (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusIntellect extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusIntellect with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusIntellect(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.intellect += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.intellect -= power;
	}

	public String toString()
	{
		return "+" + (int)power + " Intellect";
	}
}

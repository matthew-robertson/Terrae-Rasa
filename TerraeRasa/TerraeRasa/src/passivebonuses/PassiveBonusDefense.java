package passivebonuses;

import server.entities.EntityPlayer;

/**
 * PassiveBonusDefense extends PassiveBonus to increase the player's defense by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Defense (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusDefense extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusDefemse with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusDefense(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.defense += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.defense -= power;
	}
	
	public String toString()
	{
		return "+" + (int)power + " Defense";
	}
}

package passivebonuses;

import server.entities.EntityPlayer;
/**
 * PassiveBonusStamina extends PassiveBonus to increase the player's stamina by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 stamina (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusStamina extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusStamina with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusStamina(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.stamina += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.stamina -= power;
	}

	public String toString()
	{
		return "+" + (int)power + " Stamina";
	}
}

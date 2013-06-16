package setbonus;

import entities.EntityPlayer;

/**
 * SetBonusStrength extends SetBonus to increase the player's strength by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Strength (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusStrength extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusStrength(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.strength += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.strength -= power;
	}

	public String toString()
	{
		return "+" + (int)power + " Strength";
	}
}

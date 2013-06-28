package setbonus;

import entities.EntityPlayer;

/**
 * SetBonusIntellect extends SetBonus to increase the player's intellect by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Intellect (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusIntellect extends SetBonus
{

	public SetBonusIntellect(double power) 
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

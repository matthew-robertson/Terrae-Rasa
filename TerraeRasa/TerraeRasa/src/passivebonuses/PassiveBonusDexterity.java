package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonusDexterity extends PassiveBonus to increase the player's dexterity by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Dexterity (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusDexterity extends PassiveBonus
{

	public PassiveBonusDexterity(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.dexterity += power;
	}

	public void remove(EntityPlayer player) 
	{
		player.dexterity -= power;
	}
	
	public String toString()
	{
		return "+" + (int)power + " Dexterity";
	}
}

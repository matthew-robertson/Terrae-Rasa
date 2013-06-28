package setbonus;

import entities.EntityPlayer;

/**
 * SetBonusSpeed extends SetBonus to provide changes to a player's movement speed. This allows an item to increase the player's
 * movement speed by a given % of power, between 0-1F (1F is 100% increase).  
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusSpeed extends SetBonus
{

	public SetBonusSpeed(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.setMovementSpeedModifier(player.getMovementSpeedModifier()
				* (1 + power));
	}

	public void remove(EntityPlayer player) 
	{
		player.setMovementSpeedModifier(player.getMovementSpeedModifier()
				/ (1 + power));
	}

	public String toString()
	{
		return "+" + (int)(power * 100) + "% Movement Speed";
	}
}

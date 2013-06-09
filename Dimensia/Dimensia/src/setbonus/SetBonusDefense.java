package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDefense extends SetBonus to increase the player's defense by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Defense (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDefense extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDefense(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.defense += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.defense -= power;
	}
	
	public String toString()
	{
		return "+" + (int)power + " Defense";
	}
}

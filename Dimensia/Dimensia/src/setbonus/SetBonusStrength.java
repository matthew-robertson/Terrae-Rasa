package setbonus;

import entities.EntityLivingPlayer;

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

	protected SetBonusStrength(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.strength += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.strength -= power;
	}

}

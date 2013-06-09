package setbonus;

import entities.EntityLivingPlayer;

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
	private static final long serialVersionUID = 1L;

	public SetBonusIntellect(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.intellect += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.intellect -= power;
	}

}

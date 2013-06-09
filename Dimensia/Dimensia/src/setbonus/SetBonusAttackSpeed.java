package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusAttackSpeed power extends SetBonus to increase the player's attack speed by a given amount. Attack Speed
 * is increased based on power from 0-1F. 1F gives a 100% increase to attack speed. Note: A 100% increase in attack
 * speed may not indicate instant attacks due to diminishing returns.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusAttackSpeed extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusAttackSpeed(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		//Is this the right stack method?
		player.attackSpeedModifier += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.attackSpeedModifier -= power;
	}
}

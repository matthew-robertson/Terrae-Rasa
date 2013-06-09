package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusDexterity extends SetBonus to increase the player's dexterity by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Dexterity (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusDexterity extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusDexterity(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.dexterity += power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.dexterity -= power;
	}

}

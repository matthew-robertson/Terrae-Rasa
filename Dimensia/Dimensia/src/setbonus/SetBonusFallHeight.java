package setbonus;

import entities.EntityLivingPlayer;

/**
 * SetBonusFallHeight extends SetBonus to increase the player's distance fallen safely. Each point of power allows the player
 * to fall 1 more block safely. IE 1 power = 1 more block fallen safely
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusFallHeight extends SetBonus
{
	private static final long serialVersionUID = 1L;

	public SetBonusFallHeight(double power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.maxHeightFallenSafely += 6 * power;
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.maxHeightFallenSafely -= 6 * power; 
	}

	public String toString()
	{
		return "+" + (int)power + " Fall Height";
	}
}

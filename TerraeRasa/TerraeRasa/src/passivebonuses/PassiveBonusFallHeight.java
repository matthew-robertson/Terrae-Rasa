package passivebonuses;

import server.entities.EntityPlayer;
import blocks.Block;

/**
 * PassiveBonusFallHeight extends PassiveBonus to increase the player's distance fallen safely. Each point of power allows the player
 * to fall 1 more block safely. IE 1 power = 1 more block fallen safely
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusFallHeight extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusFallHeight with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusFallHeight(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.setMaxHeightFallenSafely(player.getMaxHeightFallenSafely() +(Block.BLOCK_HEIGHT * power));
	}

	public void remove(EntityPlayer player) 
	{
		player.setMaxHeightFallenSafely(player.getMaxHeightFallenSafely() - (Block.BLOCK_HEIGHT * power)); 
	}

	public String toString()
	{
		return "+" + (int)power + " Fall Height";
	}
}

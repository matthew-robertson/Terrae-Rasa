package passivebonuses;

import blocks.Block;
import entities.EntityPlayer;

/**
 * PassiveBonusJumpHeight extends PassiveBonus to increase the player's jump height by a given amount. This is a fixed amount, indicated
 * by the power value. 1 Power = +1 Block of jump height (etc) 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusJumpHeight extends PassiveBonus
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PassiveBonusJumpHeight with given power.
	 * @param power the strength of this PassiveBonus
	 */
	public PassiveBonusJumpHeight(double power) 
	{
		super(power);
	}

	public void apply(EntityPlayer player) 
	{
		player.setUpwardJumpHeight(player.getUpwardJumpHeight() + (Block.BLOCK_HEIGHT * power));
	}

	public void remove(EntityPlayer player) 
	{
		player.setUpwardJumpHeight(player.getUpwardJumpHeight() - (Block.BLOCK_HEIGHT * power));
	}

	public String toString()
	{
		return "+" + (int)power + " Jump Height";
	}
}

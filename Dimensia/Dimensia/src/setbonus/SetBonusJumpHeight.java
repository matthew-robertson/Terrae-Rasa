package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusJumpHeight extends SetBonus
{
	protected SetBonusJumpHeight(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.setUpwardJumpHeight(player.getUpwardJumpHeight() + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.setUpwardJumpHeight(player.getUpwardJumpHeight() - power);
	}
}

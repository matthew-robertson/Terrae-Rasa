package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusAttackSpeed extends SetBonus
{
	protected SetBonusAttackSpeed(float power) 
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

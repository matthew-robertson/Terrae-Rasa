package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusDamageAll extends SetBonus
{
	protected SetBonusDamageAll(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.allDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.allDamageModifier /= (1 + power);
	}

}

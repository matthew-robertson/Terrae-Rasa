package setbonus;

import entities.EntityLivingPlayer;

public class SetBonusDamageRanged extends SetBonus
{
	protected SetBonusDamageRanged(float power) 
	{
		super(power);
	}

	public void apply(EntityLivingPlayer player) 
	{
		player.rangeDamageModifier *= (1 + power);
	}

	public void remove(EntityLivingPlayer player) 
	{
		player.rangeDamageModifier /= (1 + power);
	}

}

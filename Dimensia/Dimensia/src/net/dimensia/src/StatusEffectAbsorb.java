package net.dimensia.src;

public class StatusEffectAbsorb extends StatusEffect
{
	private int startAbsorbAmount;
	private int remainingAbsorb;
	
	/**
	 * 
	 * @param durationSeconds
	 * @param tier
	 * @param absorb the amount this statuseffect will absorb, in total damage
	 */
	public StatusEffectAbsorb(float durationSeconds, int tier, int absorb) 
	{
		super(durationSeconds, tier);
		this.iconX = 12;
		this.iconY = 2;
		this.startAbsorbAmount = absorb;
		this.remainingAbsorb = absorb;
		this.reapplicationSkipsRemovalEffect = true;
	}
	
	public int getRemainingAbsorb()
	{
		return remainingAbsorb;
	}
	
	/**
	 * -- return, the damage left over from the absorb. 0 = full absorb. doc-comment nyi
	 * @param damageDone
	 * @return
	 */
	public int absorbDamage(float damageDone)
	{
		if(damageDone <= remainingAbsorb)
		{
			remainingAbsorb -= damageDone;
			return 0;
		}
		else
		{
			remainingAbsorb -= damageDone;
			return Math.abs(remainingAbsorb);
		}
	}

	public boolean isBroken()
	{
		return remainingAbsorb <= 0;
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
	
		if(isBroken())
		{
			ticksLeft = 0;
		}		
	}
}

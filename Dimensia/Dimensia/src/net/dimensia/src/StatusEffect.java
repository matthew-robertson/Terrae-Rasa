package net.dimensia.src;

public class StatusEffect 
{
	public boolean reapplicationSkipsRemovalEffect;
	public int ticksLeft;
	public int tier;
	/** If this effect benefits the entity, then this is true. */
	public boolean isBeneficialEffect;
	public int iconX;
	public int iconY;
	
	public StatusEffect(float durationSeconds, int tier)
	{
		reapplicationSkipsRemovalEffect = false;
		ticksLeft = (int) (durationSeconds * 20);
		this.tier = tier;
		this.isBeneficialEffect = true;
		iconX = 15;
		iconY = 0;
	}
	
	public StatusEffect(StatusEffect effect)
	{
		this.reapplicationSkipsRemovalEffect =  effect.reapplicationSkipsRemovalEffect;
		this.ticksLeft = effect.ticksLeft;
		this.tier = effect.tier;
	}
	
	public void applyInitialEffect(EntityLiving entity)
	{	
	}
	
	public void removeInitialEffect(EntityLiving entity)
	{	
	}
	
	public void applyPeriodicBonus(World world, EntityLiving entity)
	{
		ticksLeft--;
	}
	
	public boolean isExpired()
	{
		return ticksLeft <= 0;
	}
	
	public void setIconPosition(int x, int y)
	{
		this.iconX = x;
		this.iconY = y;
	}
	
	public String toString()
	{
		return "Status_Effect_Base(None)";
	}
}

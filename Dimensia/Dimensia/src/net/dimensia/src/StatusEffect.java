package net.dimensia.src;

public class StatusEffect 
{
	public StatusEffect(float durationSeconds, int tier)
	{
		reapplicationSkipsRemovalEffect = false;
		ticksLeft = (int) (durationSeconds * 20);
		this.tier = tier;
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
	
	public String toString()
	{
		return "Status_Effect_Base(None)";
	}
	
	public boolean reapplicationSkipsRemovalEffect;
	public int ticksLeft;
	public int tier;
}

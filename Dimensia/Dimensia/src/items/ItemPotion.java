package items;

public class ItemPotion extends Item
{
	protected int tier;
	protected int durationSeconds;
	protected int ticksBetweenEffect;
	protected float power;
	
	public ItemPotion(int i, int durationSeconds, int tier, float power, int ticksBetweenEffect)
	{
		super(i);
		this.durationSeconds = durationSeconds;
		this.tier = tier;
		this.power = power;
		this.ticksBetweenEffect = ticksBetweenEffect;
	}
	
	public int getTier()
	{
		return tier;
	}
	
	public int getDurationSeconds()
	{
		return durationSeconds;
	}
}

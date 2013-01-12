package net.dimensia.src;

public class ItemPotion extends Item
{
	public ItemPotion(int i, int durationSeconds, int tier)
	{
		super(i);
		this.durationSeconds = durationSeconds;
		this.tier = tier;
	}
	
	public int getTier()
	{
		return tier;
	}
	
	public int getDurationSeconds()
	{
		return durationSeconds;
	}
	
	protected int tier;
	protected int durationSeconds;
}

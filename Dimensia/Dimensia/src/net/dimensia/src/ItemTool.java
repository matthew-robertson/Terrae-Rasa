package net.dimensia.src;

public class ItemTool extends Item
{
	/** Damage the weapon does */
	public int damageDone; 	
	
	protected ItemTool(int i)
	{
		super(i);
		damageDone = 1;
		maxStackSize = 1;
	}
		
	protected ItemTool setDamageDone(int i)
	{
		damageDone = i;
		return this;
	}
	
	public int getDamageDone()
	{
		return damageDone;
	}
	
	/**
	 * Gets the stats of the given item. ItemTools should have at least a damage value.
	 * @return an array of this ItemTool's stats, which should be at least of size 1
	 */
	public String[] getStats()
	{
		return new String[] { "Damage: "+damageDone };
	}
}

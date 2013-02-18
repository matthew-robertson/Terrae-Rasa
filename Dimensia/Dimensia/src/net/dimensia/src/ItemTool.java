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
}

package net.dimensia.src;

/**
 * EnumArmor defines the armour values given by a specific grade of equipment. These values are available for any piece of the Armour
 * set (the helmet, chestpiece, and greaves). Defense values are generally: Chestpiece > helmet >= greaves.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumArmor 
{
	NOTHING(0, 0, 0),
	COPPER(1, 2, 1),
	BRONZE(1, 3, 2),
	IRON(2, 4, 2),
	SILVER(3, 4, 2),
	GOLD(4, 5, 3);

	public int helmDefense;
	public int bodyDefense;
	public int greavesDefense;
	
	EnumArmor(int helmDef, int bodyDef, int greavesDef)
	{
		this.helmDefense = helmDef;
		this.bodyDefense = bodyDef;
		this.greavesDefense = greavesDef;
	}
	
	public int getHelmetDefense()
	{
		return helmDefense;
	}
	
	public int getBodyDefense()
	{
		return bodyDefense;
	}
	
	public int getGreavesDefense()
	{
		return greavesDefense;
	}
}

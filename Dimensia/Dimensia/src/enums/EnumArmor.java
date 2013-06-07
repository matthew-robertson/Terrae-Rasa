package enums;

/**
 * EnumArmor defines the armour values given by a specific grade of equipment. These values are 
 * available for any piece of the Armour set (the helmet, chestpiece, and pants, boots, belts, and gloves). 
 * Defense values are generally: Chestpiece > helmet >= pants > boots >= gloves >= belt.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public enum EnumArmor 
{
	NOTHING(0, 0, 0, 0, 0, 0),
	COPPER(2, 3, 2, 1, 1, 1),
	BRONZE(2, 4, 3, 1, 1, 1),
	IRON(3, 5, 4, 2, 2, 2),
	SILVER(4, 6, 5, 3, 3, 3),
	GOLD(5, 7, 5, 4, 4, 4);

	private final int helmDefense;
	private final int bodyDefense;
	private final int pantsDefense;
	private final int bootsDefense;
	private final int glovesDefense;
	private final int beltDefense;
	
	EnumArmor(int helmDef, int bodyDef, int pantsDef, int bootsDefense, int glovesDefense, int beltDefense)
	{
		this.helmDefense = helmDef;
		this.bodyDefense = bodyDef;
		this.pantsDefense = pantsDef;
		this.bootsDefense = bootsDefense;
		this.glovesDefense = glovesDefense;
		this.beltDefense = beltDefense;
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
		return pantsDefense;
	}
	
	public int getBootsDefense()
	{
		return bootsDefense;
	}
	
	public int getGlovesDefense()
	{
		return glovesDefense;
	}
	
	public int getBeltDefense()
	{
		return beltDefense;
	}
}

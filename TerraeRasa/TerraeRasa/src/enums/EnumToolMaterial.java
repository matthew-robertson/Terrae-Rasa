package enums;

/**
 * Tool Material 
 * @author alec
 *
 */
public enum EnumToolMaterial 
{
	/**
	 * Stone hardness is 50.0f;
	 * There are some comments describing how long it takes to break a piece of stone with
	 * the specified tool strength. Ex. Copper takes 50 game ticks (2.5 sec)
	 */
	
	FIST		(0.0f, 0, 2.5f, "Fist"),
	COPPER		(1.1f, 1, 39.0f, "Copper"), //50
	BRONZE		(1.5f, 2, 39.0f, "Bronze"),
	IRON		(2.25f, 3, 45.0f, "Iron"),
	SILVER		(3.0f, 4, 45.0f, "Silver"),
	GOLD		(3.8f, 5, 45.0f, "Gold"), //13 (round down)
	TIER6H1		(4.5f, 6, 45.0f, ""),
	TIER7H2		(5.3f, 7, 45.0f, ""),
	TIER8H3		(6.1f, 8, 45.0f, ""), //8 (round down)
	TIER9Hell1	(6.9f, 9, 45.0f, ""),
	TIER10Hell2	(7.9f , 10, 45.0f, ""),
	TIER11Hell3	(8.9f , 11, 45.0f, ""), //6 (round up)
	TIER12		(10.0f , 12, 45.0f, ""), //5 
	GOD			(100.0f , 12, 45.0f, ""); //This is intended for testing 
	
	public double strength;
	public int toolTier;
	public double distance;
	public String materialName;
	
	EnumToolMaterial(double s, int t, double d, String name)
	{
		strength = s;
		toolTier = t;
		materialName = name;
		distance = d;
	}

	public double getStrength()
	{
		return strength;
	}
	
	public int getToolTier()
	{
		return toolTier;
	}

	public String getMaterialName()
	{
		return materialName;
	}
	
	public double getDistance()
	{
		return distance;
	}
}

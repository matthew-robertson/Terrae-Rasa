package enums;

/**
 * Defines the different tiers of tool materials. Each tool material has a: strength,
 * reach in ortho units, tier, and name. Tier simply indicates how strong something is
 * at a glance, while name is a renderable or displayable name for the tier. Reach is how 
 * far away in terms of the (x,y) coordinate system something can be broken IE 42 is a 
 * 7 block breaking distance. 
 * <br> <br>
 * Mine time is determined in part by the strength of the tool. 
 * floor_one(Block_Hardness / Tool_Material_Strength) is the time required to mine a block
 * in game ticks.
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public enum EnumToolMaterial 
{
	FIST		(0.0f, 0, 2.5f, "Fist"),
	COPPER		(1.1f, 1, 39.0f, "Copper"), 
	BRONZE		(1.5f, 2, 39.0f, "Bronze"),
	IRON		(2.25f, 3, 45.0f, "Iron"),
	SILVER		(3.0f, 4, 45.0f, "Silver"),
	GOLD		(3.8f, 5, 45.0f, "Gold"), 
	TIER6H1		(4.5f, 6, 45.0f, ""),
	TIER7H2		(5.3f, 7, 45.0f, ""),
	TIER8H3		(6.1f, 8, 45.0f, ""), 
	TIER9Hell1	(6.9f, 9, 45.0f, ""),
	TIER10Hell2	(7.9f , 10, 45.0f, ""),
	TIER11Hell3	(8.9f , 11, 45.0f, ""), 
	TIER12		(10.0f , 12, 45.0f, ""), 
	GOD			(100.0f , 12, 45.0f, ""); //This is intended for testing 
	
	/** This tool's strength - a higher number breaks blocks faster. */
	private final double strength;
	/** An integer representation of this tool material's relative power. */
	private final int toolTier;
	/** The reach of a tool, in the (x,y) coordinate system, 6 of such units equals 1 block's size. */
	private final double reachOrtho;
	/** A displayable name for this material. */
	private final String materialName;
	
	/**
	 * Creates a new EnumToolMaterial with given parameters.
	 * @param strength a double indicating how potent this tier is at breaking blocks
	 * @param toolTier an integer representation of this tier
	 * @param reachOrtho the reach of the tool tier in (x,y) coordinate system
	 * @param materialName a displayable name for this material
	 */
	EnumToolMaterial(double strength, int toolTier, double reachOrtho, String materialName)
	{
		this.strength = strength;
		this.toolTier = toolTier;
		this.materialName = materialName;
		this.reachOrtho = reachOrtho;
	}

	/**
	 * Gets this tool material's strength, an indication of how able it is to break blocks. A higher value
	 * makes something more able to break blocks.
	 * @return this tool material's strength
	 */
	public double getStrength()
	{
		return strength;
	}
	
	/**
	 * Gets this tool's tier, an integer representing it.
	 * @return this tool's tier
	 */
	public int getToolTier()
	{
		return toolTier;
	}

	/**
	 * Gets this tool material's name - a displayable string for I/O purposes.
	 * @return this tool material's name.
	 */
	public String getMaterialName()
	{
		return materialName;
	}
	
	/**
	 * Gets this tool material's reach in ortho units - a unit in the game corresponding directly to 
	 * the game's (x,y) coordinate system. 1 block is 6 ortho units.
	 * @return this tool material's reach, in ortho units
	 */
	public double getReachOrtho()
	{
		return reachOrtho;
	}
}

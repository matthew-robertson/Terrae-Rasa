package affix;

/**
 * Holds the data to recreate an affix - it's ID and power[].
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AffixData 
{
	/** Corresponds to some extension's of Affix's unique ID number. */
	private int affixID;
	/** The powers needed to recreate an affix. Form varies by extension.*/
	private double[] power;
	
	/**
	 * Creates a new, blank AffixData.
	 */
	public AffixData()
	{
		this.setAffixID(0);
		this.setPower(new double[0]);
	}
	
	/**
	 * Creates an AffixData already populated with the required data.
	 * @param affixID the unique ID of an Affix's extension, used to identify it
	 * @param power the power values need to recreate an affix
	 */
	public AffixData(int affixID, double[] power)
	{
		this.setAffixID(affixID);
		this.setPower(power);
	}

	/**
	 * Gets this AffixData's ID number.
	 * @return this AffixData's ID number
	 */
	public int getAffixID() 
	{
		return affixID;
	}

	/**
	 * Sets this AffixData's ID number.
	 * @param affixID the value to set this AffixData's ID number to
	 */
	public void setAffixID(int affixID) 
	{
		this.affixID = affixID;
	}
	
	/**
	 * Gets this AffixData's power[].
	 * @return this AffixData's power[]
	 */
	public double[] getPower() 
	{
		return power;
	}

	/**
	 * Sets this AffixData's power[]
	 * @param power the double[] used to recreate an affix later
	 */
	public void setPower(double[] power) 
	{
		this.power = power;
	}
}

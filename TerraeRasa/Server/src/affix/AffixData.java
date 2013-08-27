package affix;

/**
 * Holds the data to recreate an affix
 * @author alec
 *
 */
public class AffixData 
{
	public int affixID;
	public double[] power;
	
	public AffixData()
	{
		this.affixID = 0;
		this.power = new double[0];
	}
	
	public AffixData(int affixID, double[] power)
	{
		this.affixID = affixID;
		this.power = power;
	}
}

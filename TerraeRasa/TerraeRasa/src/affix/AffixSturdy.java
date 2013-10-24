package affix;

import auras.Aura;
import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDefense;

/**
 * AffixSturdy extends Affix to give a defense bonus ranging from +2 to +10 defense.
 * The power[] is as follows: [defense_value{passive_bonus}]
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0 
 */
public class AffixSturdy extends Affix
{
	private static final long serialVersionUID = 1L;
	/** The maximum allowed defense. */
	private final int max = 10;
	/** The minimum allowed defense. */
	private final int min = 2;
	
	/**
	 * Constructs a new Sturdy Affix (this will not roll new powers)
	 */
	public AffixSturdy() 
	{
		super("Sturdy", getAffixID(), true);
	}
	
	/**
	 * Gets a unique ID for this affix.
	 * @return this affix's unique ID
	 */
	public final static int getAffixID()
	{
		return 1;
	}

	/**
	 * The power[] for this method should be in the form: [defense_value{passive_bonus}]
	 */
	public void verifyPowers(double[] powers)
	{
		if(powers == null || powers.length == 0)
		{
			powers = new double[] { this.min };
			return;
		}
		if(powers[0] > max)
		{
			powers[0] = max;
		}
		if(powers[0] < min)
		{
			powers[0] = min;
		}
	}
	
	/**
	 * The power[] for this method should be in the form: [defense_value{passive_bonus}]
	 */
	public double[] rollPowers() 
	{
		return new double[] { rng.nextInt(max - min) + min };
	}

	/**
	 * The power[] for this method should be in the form: [defense_value{passive_bonus}]
	 */
	public PassiveBonus[] getPassives(double[] powers) 
	{
		return new PassiveBonus[] { new PassiveBonusDefense(powers[0]) };
	}

	/**
	 * The power[] for this method should be in the form: [defense_value{passive_bonus}]
	 */
	public Aura[] getAuras(double[] powers) 
	{
		return new Aura[] { };
	}
}
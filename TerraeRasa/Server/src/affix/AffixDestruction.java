package affix;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDamageAll;
import auras.Aura;

/**
 * AffixDestruction extends Affix to give a damage bonus ranging from +2% to +6% to a piece of armour.
 * The power[] is as follows: [damage_bonus_value{passive_bonus}]
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0 
 */
public class AffixDestruction extends Affix
{
	private static final long serialVersionUID = 1L;
	/** The maximum damage bonus percent */
	private final double max = 0.06;
	/** The minimum damage bonus percent */
	private final double min = 0.02;
	
	/**
	 * Constructs a new AffixDestruction (this does not roll new power values).
	 */
	public AffixDestruction()
	{
		super("of destruction", getAffixID(), false);
	}

	/**
	 * Gets a unique ID for this affix.
	 * @return this affix's unique ID
	 */
	public static int getAffixID()
	{
		return 3;
	}
	
	/**
	 * The power[] for this method should be in the form: [damage_bonus_value{passive_bonus}]
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
	 * The power[] for this method should be in the form: [damage_bonus_value{passive_bonus}]
	 */
	public double[] rollPowers() 
	{		
		return new double[] {  rng.nextDouble() * (max - min) + min };
	}

	/**
	 * The power[] for this method should be in the form: [damage_bonus_value{passive_bonus}]
	 */
	public PassiveBonus[] getPassives(double[] powers) 
	{
		return new PassiveBonus[] { new PassiveBonusDamageAll(powers[0]) };
	}

	/**
	 * The power[] for this method should be in the form: [damage_bonus_value{passive_bonus}]
	 */
	public Aura[] getAuras(double[] powers) 
	{
		return new Aura[] { };
	}
}
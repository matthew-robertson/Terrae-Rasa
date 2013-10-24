package affix;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusAttackSpeed;
import passivebonuses.PassiveBonusStrength;
import auras.Aura;

/**
 * AffixFrenzied extends Affix to give an item +2 to +10 Strength and +2% to +16% attack speed.
 * The power[] is as follows:
 * [strength_value{passive_bonus}, attack_speed_value{passive_bonus}]
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0 
 */
public class AffixFrenzied extends Affix
{
	private static final long serialVersionUID = 1L;
	/** The maximum possible strength benefit */
	private final int maxStr = 10;
	/** The minimum possible strength benefit */
	private final int minStr = 2;
	/** The maximum possible attack speed benefit. */
	private final double maxSpd = 0.16;
	/** The minimum possible attack speed benefit. */
	private final double minSpd = 0.02;
	
	/**
	 * Constructs a new Frenzied Affix (this will not roll new powers).
	 */
	public AffixFrenzied()
	{
		super("Frenzied", getAffixID(), true);
	}
	
	/**
	 * Gets a unique ID for this affix.
	 * @return this affix's unique ID
	 */
	public static int getAffixID()
	{
		return 2;
	}

	/**
	 * The power[] for this method should be in the form: [strength_value{passive_bonus}, attack_speed_value{passive_bonus}]
	 */
	public void verifyPowers(double[] powers)
	{
		if(powers == null || powers.length < 2)
		{
			powers = new double[] { this.minStr, this.minSpd};
			return;
		}
		//Check strength
		if(powers[0] > maxStr)
		{
			powers[0] = maxStr;
		}
		if(powers[0] < minStr)
		{
			powers[0] = minStr;
		}
		//Check attack speed
		if(powers[0] > maxSpd)
		{
			powers[0] = maxSpd;
		}
		if(powers[0] < minSpd)
		{
			powers[0] = minSpd;
		}
	}
	
	/**
	 * The power[] for this method should be in the form: [strength_value{passive_bonus}, attack_speed_value{passive_bonus}]
	 */
	public double[] rollPowers() 
	{
		return new double[] { (int) (rng.nextDouble() * (maxStr - minStr) + minStr), rng.nextDouble() * (maxSpd - minSpd) + minSpd};
	}

	/**
	 * The power[] for this method should be in the form: [strength_value{passive_bonus}, attack_speed_value{passive_bonus}]
	 */
	public PassiveBonus[] getPassives(double[] powers) 
	{
		return new PassiveBonus[] { new PassiveBonusStrength(powers[0]), new PassiveBonusAttackSpeed(powers[1])};
	}

	/**
	 * The power[] for this method should be in the form: [strength_value{passive_bonus}, attack_speed_value{passive_bonus}]
	 */
	public Aura[] getAuras(double[] powers) 
	{
		return new Aura[] { };
	}

}
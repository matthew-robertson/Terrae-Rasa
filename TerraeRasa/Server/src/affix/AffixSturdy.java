package affix;

import auras.Aura;
import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDefense;

public class AffixSturdy extends Affix{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int max = 10;
	private int min = 2;
	
	public AffixSturdy() {
		super("Sturdy", getAffixID(), true);
		
	}
	
	public final static int getAffixID()
	{
		return 1;
	}

	//[defense_value{passive}]
	public void verifyPowers(double[] powers)
	{
		if(powers[0] > max)
		{
			powers[0] = max;
		}
		if(powers[0] < min)
		{
			powers[0] = min;
		}
	}
	
	//[defense_value{passive}]
	public double[] rollPowers() {
		return new double[] { rng.nextInt(max - min) + min };
	}

	//param powers - [defense_value{passive}]
	public PassiveBonus[] getPassives(double[] powers) {
		return new PassiveBonus[] { new PassiveBonusDefense(powers[0]) };
	}

	//param powers [defense_value{passive}]
	public Aura[] getAuras(double[] powers) {
		return new Aura[] { };
	}
	
}
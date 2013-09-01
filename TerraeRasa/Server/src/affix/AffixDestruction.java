package affix;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDamageAll;
import auras.Aura;

public class AffixDestruction extends Affix{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double max = 0.06;
	private double min = 0.02;
	
	public AffixDestruction(){
		super("of destruction", getAffixID(), false);
		
	}

	public static int getAffixID()
	{
		return 3;
	}
	
	//param powers [damage_bonus_value{passive}]
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
	
	//param powers [damage_bonus_value{passive}]
	public double[] rollPowers() {		
		return new double[] {  rng.nextDouble() * (max - min) + min };
	}

	//param powers [damage_bonus_value{passive}]
	public PassiveBonus[] getPassives(double[] powers) {
		return new PassiveBonus[] { new PassiveBonusDamageAll(powers[0]) };
	}

	//param powers [damage_bonus_value{passive}]
	public Aura[] getAuras(double[] powers) {
		return new Aura[] { };
	}
}
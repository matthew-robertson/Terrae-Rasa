package affix;

import auras.Aura;
import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDefense;

public class AffixSturdy extends Affix{
	
	private int max = 10;
	private int min = 2;
	
	public AffixSturdy() {
		super("Sturdy", 1, true);
		
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